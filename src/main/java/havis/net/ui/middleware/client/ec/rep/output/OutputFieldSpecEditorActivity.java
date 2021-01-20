package havis.net.ui.middleware.client.ec.rep.output;

import havis.middleware.ale.service.ECFieldSpec;
import havis.middleware.ale.service.ec.ECReportOutputFieldSpec;
import havis.middleware.ale.service.ec.ECReportOutputFieldSpecExtension;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.ec.ECReportSpecEditorPlace;
import havis.net.ui.middleware.client.place.ec.OutputFieldItemEditorPlace;
import havis.net.ui.middleware.client.shared.BaseActivity;
import havis.net.ui.middleware.client.shared.EditorDialogView;
import havis.net.ui.middleware.client.shared.LoadingSpinner;
import havis.net.ui.middleware.client.shared.TMFieldNamesCallback;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.SpecLoadedEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.shared.storage.ECSpecStorage;
import havis.net.ui.middleware.client.shared.storage.TMSpecStorage;
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

public class OutputFieldSpecEditorActivity extends BaseActivity implements EditorDialogView.Presenter{

	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	@Inject
	private CommonStorage commonStorage;
	
	@Inject
	private TMSpecStorage tms;
	
	@Inject
	private ECSpecStorage storage;
	
	@Inject
	private EditorDialogView view;

	@Inject
	private PlaceController placeController;
	
	@Inject
	private Driver driver;
	
	private OutputFieldItemEditorPlace place;

	interface Driver extends SimpleBeanEditorDriver<ECReportOutputFieldSpec, OutputFieldSpecEditor> {
	}
	
	@Inject
	OutputFieldSpecEditor editor;
	
	private int currentIndex = -1;
	
	private List<ECReportOutputFieldSpec> ecReportOutputFields;
	
	private ECReportOutputFieldSpec outFieldSpec;
	
	private PopupPanel overlay;
	
	@Override
	public void bind() {
		view.setPresenter(this);
	}
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		setEventBus(eventBus);
		panel.setWidget(view.asWidget());
		bind();
		overlay = new PopupPanel(false, true);
		Utils.blockUi(overlay, 300);
		
		place = (OutputFieldItemEditorPlace) placeController.getWhere();
			
		view.setEditorTitle(res.field());
		view.setEnableButton(null);
		view.setEnabledAcceptButton(false);
		view.setEditor(new LoadingSpinner());
		
		driver.initialize(editor);
		
		editor.fieldSpecEditor.addNewItemHandler(new NewItemEvent.Handler() {
			
			@Override
			public void onNewItem(NewItemEvent event) {
				commonStorage.setNewTMSpecName(event.getName());
				commonStorage.setNewTMFieldName(res.newEntry());
				placeController.goTo(new EditorPlace(ListType.EC, EditorType.TM, Utils.getNewId(), false, "0"));
			}
		});
		
		tms.fetchFieldNames(getEventBus(), new TMFieldNamesCallback() {
			@Override
			public void onGotTMFieldNames(Map<String, List<String>> map) {
				editor.fieldSpecEditor.setTMFields(tms.getFixedFields(), tms.getVariableFields());				
			}
		});
		
		loadSpec();
	}

	private void loadSpec() {
		SpecLoadedEvent.register(getEventBus(), this, new SpecLoadedEvent.Handler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSpecLoaded(SpecLoadedEvent event) {
				
				ecReportOutputFields = (List<ECReportOutputFieldSpec>) event.getSpec();
				
				if(ecReportOutputFields == null){
					showErrorMessage("MCEventCycleSpec: spec.reportSpec.output is NULL!");
					return;
				}
				
				if(place.isNew() && Utils.isNullOrEmpty(commonStorage.getNewTMFieldName())){
					outFieldSpec = new ECReportOutputFieldSpec();
					ecReportOutputFields.add(outFieldSpec);
					currentIndex = ecReportOutputFields.size()-1;
					place.setNew(currentIndex);
				}
				else{
					currentIndex = place.getPathAsInt(2);
					outFieldSpec = ecReportOutputFields.get(currentIndex);
				}
				
				checkForNewTM();
				
				if(outFieldSpec.getFieldspec() == null){
					outFieldSpec.setFieldspec(new ECFieldSpec());
				}
				if(outFieldSpec.getExtension() == null){
					outFieldSpec.setExtension(new ECReportOutputFieldSpecExtension());
				}
					
				driver.edit(outFieldSpec);
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
	

	@Override
	public void onClose() {
		if((place.isNew() || place.isFormerNew()) && ecReportOutputFields != null && ecReportOutputFields.size() > currentIndex){
			overlay.show();
			ecReportOutputFields.remove(currentIndex);
			place.updatePathId(""+(currentIndex-1));
			overlay.hide();
		}
		storage.removeEmptyLists(place, ecReportOutputFields);
		placeController.goTo(new ECReportSpecEditorPlace(ListType.EC , EditorType.EC, place.getPathList(), place.getOpenWidgetIdList()));
	}

	@Override
	public void onAccept() {
		overlay.show();
		driver.flush();
		
		if (driver.hasErrors()) {
			StringBuilder builder = new StringBuilder();
			for (EditorError e : driver.getErrors()) {
				builder.append(e.getMessage());
				builder.append('\n');
			}
			overlay.hide();
			showErrorMessage(builder.toString());
			return;
		} 
		storage.removeEmptyLists(place, ecReportOutputFields);
		overlay.hide();
		placeController.goTo(new ECReportSpecEditorPlace(ListType.EC , EditorType.EC, place.getPathList(), place.getOpenWidgetIdList()));
	}
	
	/** 
	 * checkForNewTM()
	 * if getNewTMFieldName() contains a name then it comes back from an new TM creation
	 * and the newly created names will be  inserted to the new POJO. 
	 */
	private void checkForNewTM() {
		
		String newTMFieldName = commonStorage.getNewTMFieldName();
		if (!Utils.isNullOrEmpty(newTMFieldName)) {
			
			if(outFieldSpec.getFieldspec() == null){
				outFieldSpec.setFieldspec(new ECFieldSpec());
			}
			
			String specName = commonStorage.getNewTMSpecName();
			if(!Utils.isNullOrEmpty(specName)){
				outFieldSpec.setName(specName);
			}
			commonStorage.setNewTMSpecName(null);
			
			outFieldSpec.getFieldspec().setFieldname(newTMFieldName);
			outFieldSpec.getFieldspec().setDatatype(null);
			outFieldSpec.getFieldspec().setFormat(null);
			
			commonStorage.setNewTMFieldName(null);
		}
	}
	
}
