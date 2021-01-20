package havis.net.ui.middleware.client.shared.pattern;

import havis.middleware.ale.service.ECFieldSpec;
import havis.middleware.ale.service.ECFilterListMember;
import havis.middleware.ale.service.ECFilterListMember.PatList;
import havis.middleware.ale.service.tm.TMFixedFieldSpec;
import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterItemEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterPatternItemEditorPlace;
import havis.net.ui.middleware.client.shared.BaseActivity;
import havis.net.ui.middleware.client.shared.EditorDialogView;
import havis.net.ui.middleware.client.shared.LoadingSpinner;
import havis.net.ui.middleware.client.shared.event.ItemsLoadedEvent;
import havis.net.ui.middleware.client.shared.event.SpecLoadedEvent;
import havis.net.ui.middleware.client.shared.report.model.PatternModel;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
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

public class FilterPatternItemEditorActivity extends BaseActivity implements EditorDialogView.Presenter{

	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	@Inject
	private CommonStorage commonStorage;
	
	@Inject
	private TMSpecStorage tms;
	
	@Inject
	private EditorDialogView view;

	@Inject
	private PlaceController placeController;
	
	@Inject
	private Driver driver;
	
	private FilterPatternItemEditorPlace place;
	
	private Map<String, List<TMFixedFieldSpec>> tmFieldObjects;
	
	
	interface Driver extends SimpleBeanEditorDriver<PatternModel,  PatternItemEditor> {
	}
	
	@Inject
	PatternItemEditor editor;
	
	private int currentIndex = -1;
	
	private List<String> currentPatternList;
	
	private PatternModel filterPatternSpec;
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		setEventBus(eventBus);
		panel.setWidget(view.asWidget());
		bind();
		
		place = (FilterPatternItemEditorPlace) placeController.getWhere();
		
		view.setEditorTitle(res.filterPattern());
		view.setEnableButton(null);
		view.setEnabledAcceptButton(false);
		view.setEditor(new LoadingSpinner());
		
		fetchTMFieldObjects();
		
		driver.initialize(editor);
		
		SpecLoadedEvent.register(eventBus, this, new SpecLoadedEvent.Handler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSpecLoaded(SpecLoadedEvent event) {
				
				List<ECFilterListMember> filterListMembers = (List<ECFilterListMember>)event.getSpec();
				
				currentPatternList = initializePatList(place, filterListMembers);
				if(currentPatternList == null) {
					showErrorMessage("PCReportSpec: filterSpec.filterList.filter.patList.pat is NULL!");
					return;
				}
				
				ECFieldSpec fieldSpec = filterListMembers.get(place.getPathAsInt(2)).getFieldspec();
				
				if(place.isNew()){					
					currentIndex = currentPatternList.size();
					place.setNew(currentIndex);
					
					String dataType = fieldSpec.getDatatype();
					String format = fieldSpec.getFormat();										
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
					
					//"new:epc:new:new:*.*.*"
					filterPatternSpec = new PatternModel(composeNewUrn(format));	
					currentPatternList.add(filterPatternSpec.toString());
				}
				else{
					currentIndex = place.getPathAsInt(3);
					filterPatternSpec = new PatternModel(currentPatternList.get(currentIndex));
					
					if(Utils.isNullOrEmpty(filterPatternSpec.getScheme())){
						TMFixedFieldSpec ffSpec = getTMFixedFieldSpecOfName(fieldSpec.getFieldname());
						if(ffSpec != null){
							if(!Utils.isNullOrEmpty(ffSpec.getDefaultFormat())){
								String format = ffSpec.getDefaultFormat();
								filterPatternSpec.setEpc(format);
							}
							if(!Utils.isNullOrEmpty(ffSpec.getDefaultDatatype())){
								String datatype = ffSpec.getDefaultDatatype();
								filterPatternSpec.setId(datatype);
							}
						}
					}
				}
				
				driver.edit(filterPatternSpec);
				view.setEnabledAcceptButton(true);
				view.setEditor(editor);
			}

			@Override
			public void onFailure(SpecLoadedEvent event, String message) {
				view.setEditor(null);
				showErrorMessage(message);
			}
		});

		commonStorage.loadSpec(place, this);
	}
	
	/**
	 * Get TMFixedFieldSpec of name
	 * @param name
	 * @return spec
	 */
	private TMFixedFieldSpec getTMFixedFieldSpecOfName(String name){
		if(Utils.isNullOrEmpty(name) || tmFieldObjects == null) return null;
		
		for(List<TMFixedFieldSpec> specs : tmFieldObjects.values()){
			for(TMFixedFieldSpec spec : specs){
				if(spec.getFieldname().startsWith(name)){
					return spec;
				}
			}
		}
		return null;
	}
	
	private List<String> initializePatList(CommonEditorPlace place, List<ECFilterListMember> filterListMembers){
		if(filterListMembers == null || filterListMembers.isEmpty() )return null;
		
		if(filterListMembers.get(place.getPathAsInt(2)).getPatList() == null){
			filterListMembers.get(place.getPathAsInt(2)).setPatList(new PatList());
		}
		
		List<String> pl = filterListMembers.get(place.getPathAsInt(2)).getPatList().getPat();
		return pl;
	}
	
	/**
	 * Creates an urn which indicates a new record
	 * @param epc
	 * @return composed urn
	 */
	
	private String composeNewUrn(String epc){
		String[] tmp = epc.split("-");
		if (tmp.length > 1) {
			if(tmp[0].startsWith("epc") && tmp[1].startsWith("pure")){
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
		if(place.isNew() && currentPatternList != null && currentPatternList.size() > currentIndex){
			PopupPanel overlay = new PopupPanel(false, true);
			overlay.show();
			currentPatternList.remove(currentIndex);
			place.updatePathId(""+(currentIndex-1));
			overlay.hide();
		}
		
		placeController.goTo(new FilterItemEditorPlace(place.getListType() , place.getEditorType(), place.getPathList(), place.getOpenWidgetIdList()));
	}

	@Override
	public void onAccept() {
		PopupPanel overlay = new PopupPanel(false, true);
		overlay.show();
		
		driver.flush();
		
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
		
		currentPatternList.set(currentIndex, filterPatternSpec.toUri());
		overlay.hide();
		placeController.goTo(new FilterItemEditorPlace(place.getListType() , place.getEditorType(), place.getPathList(), place.getOpenWidgetIdList()));
	}
	
	/**
	 * Gets the user defined field TM names
	 */
	private void fetchTMFieldObjects(){
		
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
