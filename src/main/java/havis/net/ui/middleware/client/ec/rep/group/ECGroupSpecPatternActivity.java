package havis.net.ui.middleware.client.ec.rep.group;

import havis.middleware.ale.service.ECFieldSpec;
import havis.middleware.ale.service.ec.ECGroupSpec;
import havis.middleware.ale.service.tm.TMFixedFieldSpec;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.ec.ECReportSpecEditorPlace;
import havis.net.ui.middleware.client.place.ec.GroupPatternItemEditorPlace;
import havis.net.ui.middleware.client.shared.BaseActivity;
import havis.net.ui.middleware.client.shared.EditorDialogView;
import havis.net.ui.middleware.client.shared.LoadingSpinner;
import havis.net.ui.middleware.client.shared.event.ItemsLoadedEvent;
import havis.net.ui.middleware.client.shared.event.SpecLoadedEvent;
import havis.net.ui.middleware.client.shared.pattern.PatternItemEditor;
import havis.net.ui.middleware.client.shared.report.model.PatternModel;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.ECSpecStorage;
import havis.net.ui.middleware.client.shared.storage.TMSpecStorage;
import havis.net.ui.middleware.client.tm.data.DataType;
import havis.net.ui.middleware.client.tm.data.Fieldname;
import havis.net.ui.middleware.client.tm.data.Format;
import havis.net.ui.middleware.client.utils.Utils;

import java.util.List;
import java.util.Map;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class ECGroupSpecPatternActivity extends BaseActivity implements EditorDialogView.Presenter {

	private ConstantsResource res = ConstantsResource.INSTANCE;

	@Inject
	ECSpecStorage storage;

	@Inject
	TMSpecStorage tms;

	@Inject
	EditorDialogView view;

	@Inject
	PlaceController placeController;

	@Inject
	private Driver driver;

	private GroupPatternItemEditorPlace place;

	interface Driver extends SimpleBeanEditorDriver<PatternModel, PatternItemEditor> {
	}

	@Inject
	PatternItemEditor editor;

	private int currentIndex = -1;

	private List<String> currentPatternList;

	private PatternModel groupPatternSpec;

	private ECGroupSpec ecGroupSpec;

	private Map<String, List<TMFixedFieldSpec>> tmFieldObjects;

	private PopupPanel overlay;

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {

		setEventBus(eventBus);
		panel.setWidget(view.asWidget());
		bind();
		
		overlay = new PopupPanel(false, true);
		Utils.blockUi(overlay, 300);

		place = (GroupPatternItemEditorPlace) placeController.getWhere();

		view.setEditorTitle(res.groupPattern());
		view.setEnableButton(null);
		view.setEnabledAcceptButton(false);
		view.setEditor(new LoadingSpinner());

		fetchTMFieldObjects();

		driver.initialize(editor);

		SpecLoadedEvent.register(eventBus, this, new SpecLoadedEvent.Handler() {

			@Override
			public void onSpecLoaded(SpecLoadedEvent event) {

				ecGroupSpec = (ECGroupSpec) event.getSpec();

				currentPatternList = ecGroupSpec.getPattern();
				
				ECFieldSpec fieldSpec = ecGroupSpec.getExtension().getFieldspec();

				groupPatternSpec = null;
				if (place.isNew()) {
					currentIndex = currentPatternList.size();
					place.setNew(currentIndex);
					
					String format = fieldSpec.getFormat();
					String dataType = fieldSpec.getDatatype();
					if(Utils.isNullOrEmpty(dataType)){
						if(Fieldname.getFieldname(fieldSpec.getFieldname()) != null){
							view.setEditor(null);
							showErrorMessage(ConstantsResource.INSTANCE.errorNoXSpecified(ConstantsResource.INSTANCE.dataType()));
							return;		
						} else {
							TMFixedFieldSpec ffSpec = getTMFixedFieldSpecOfName(fieldSpec.getFieldname());
							if(ffSpec == null){
								view.setEditor(null);
								showErrorMessage(ConstantsResource.INSTANCE.errorIsInvalid(ConstantsResource.INSTANCE.field()));
								return;
							} else {
								dataType = ffSpec.getDefaultDatatype();
								format = ffSpec.getDefaultFormat();
							}
						}			
					}
					if(Utils.isNullOrEmpty(format)){
						view.setEditor(null);
						showErrorMessage(ConstantsResource.INSTANCE.errorNoXSpecified(ConstantsResource.INSTANCE.format()));						
						return;
					}
					DataType d;			
					if((d = DataType.getDataType(dataType)) == null || !d.hasPatternSyntax(Format.getFormat(format))){											
						view.setEditor(null);
						showErrorMessage(ConstantsResource.INSTANCE.errorNoXSpecified(ConstantsResource.INSTANCE.patternSyntax()));
						return;
					}					
					groupPatternSpec = new PatternModel(composeNewUrn(format));
					currentPatternList.add(groupPatternSpec.toString());
				} else {
					currentIndex = place.getPathAsInt(2);
					groupPatternSpec = new PatternModel(currentPatternList.get(currentIndex));

					if (Utils.isNullOrEmpty(groupPatternSpec.getScheme())) {
						TMFixedFieldSpec ffSpec = getTMFixedFieldSpecOfName(fieldSpec.getFieldname());
						if (ffSpec != null) {
							if (!Utils.isNullOrEmpty(ffSpec.getDefaultFormat())) {
								String format = ffSpec.getDefaultFormat();
								groupPatternSpec.setEpc(format);
							}
							if (!Utils.isNullOrEmpty(ffSpec.getDefaultDatatype())) {
								String datatype = ffSpec.getDefaultDatatype();
								groupPatternSpec.setId(datatype);
							}
						}
					}
				}

				driver.edit(groupPatternSpec);
				view.setEnabledAcceptButton(true);
				view.setEditor(editor);
			}

			@Override
			public void onFailure(SpecLoadedEvent event, String message) {
				view.setEditor(null);
				showErrorMessage(message);
			}
		});
		storage.loadSpec(place, this);
	}

	/**
	 * Gets TMFixedFieldSpec of given Name
	 * 
	 * @param name
	 * @return the spec
	 */
	private TMFixedFieldSpec getTMFixedFieldSpecOfName(String name) {
		if (Utils.isNullOrEmpty(name) || tmFieldObjects == null)
			return null;

		for (List<TMFixedFieldSpec> specs : tmFieldObjects.values()) {
			for (TMFixedFieldSpec spec : specs) {
				if (spec.getFieldname().startsWith(name)) {
					return spec;
				}
			}
		}
		return null;
	}

	/**
	 * Creates an urn which indicates a new record
	 * 
	 * @param epc
	 * @return the urn
	 */

	private String composeNewUrn(String epc) {
		String[] tmp = epc.split("-");
		if (tmp.length > 1) {
			if (tmp[0].startsWith("epc") && tmp[1].startsWith("pure")) {
				return ("new:" + epc + ":idpat:new:*.*.*.*");
			}
		}
		return ("new:" + epc + ":pat:new:*.*.*.*");
	}

	@Override
	public void bind() {
		view.setPresenter(this);
	}

	@Override
	public void onClose() {
		if (place.isNew() && currentPatternList != null && currentPatternList.size() > currentIndex) {
			// Roll back
			currentPatternList.remove(currentIndex);
		}
		placeController.goTo(new ECReportSpecEditorPlace(ListType.EC, EditorType.EC, place.getPathList(),
				place.getOpenWidgetIdList()));
	}

	@Override
	public void onAccept() {
		driver.flush();
		overlay.show();

		if (driver.hasErrors()) {
			StringBuilder builder = new StringBuilder();
			for (EditorError e : driver.getErrors()) {
				builder.append(e.getMessage());
				builder.append('\n');
			}
			showErrorMessage(builder.toString());
			overlay.hide();
			return;
		}
		currentPatternList.set(currentIndex, groupPatternSpec.toUri());
		storage.removeEmptyLists(place, currentPatternList);
		overlay.hide();
		placeController.goTo(new ECReportSpecEditorPlace(ListType.EC, EditorType.EC, place.getPathList(),
				place.getOpenWidgetIdList()));
	}

	/**
	 * Gets the user defined field TM names
	 */
	private void fetchTMFieldObjects() {

		ItemsLoadedEvent.register(getEventBus(), this, new ItemsLoadedEvent.Handler() {

			@Override
			public void onItemsLoaded(ItemsLoadedEvent event) {

				tmFieldObjects = tms.getFixedFields();
			}

			@Override
			public void onFailure(ItemsLoadedEvent event, String message) {
				showErrorMessage(message);
			}

		});
		tms.loadList(this);
	}
}
