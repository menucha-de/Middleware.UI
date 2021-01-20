package havis.net.ui.middleware.client.ec.rep;

import havis.middleware.ale.service.ECFieldSpec;
import havis.middleware.ale.service.ec.ECFilterSpec;
import havis.middleware.ale.service.ec.ECGroupSpec;
import havis.middleware.ale.service.ec.ECGroupSpecExtension;
import havis.middleware.ale.service.ec.ECReportOutputSpec;
import havis.middleware.ale.service.ec.ECReportOutputSpecExtension;
import havis.middleware.ale.service.ec.ECReportOutputSpecExtension.FieldList;
import havis.middleware.ale.service.ec.ECReportSetSpec;
import havis.middleware.ale.service.ec.ECReportSpec;
import havis.middleware.ale.service.ec.ECReportSpecExtension;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.ec.ECReportSpecEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterItemEditorPlace;
import havis.net.ui.middleware.client.place.ec.GroupPatternItemEditorPlace;
import havis.net.ui.middleware.client.place.ec.OutputFieldItemEditorPlace;
import havis.net.ui.middleware.client.shared.BaseActivity;
import havis.net.ui.middleware.client.shared.EditorDialogView;
import havis.net.ui.middleware.client.shared.LoadingSpinner;
import havis.net.ui.middleware.client.shared.TMFieldNamesCallback;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.event.SpecLoadedEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.shared.storage.ECSpecStorage;
import havis.net.ui.middleware.client.shared.storage.TMSpecStorage;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.SectionExpandedCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class ECReportSpecEditorActivity extends BaseActivity implements EditorDialogView.Presenter{
	
	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	@Inject
	CommonStorage commonStorage;
	
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

	interface Driver extends SimpleBeanEditorDriver<ECReportSpec, ECReportSpecEditor> {
	}
	
	@Inject
	ECReportSpecEditor editor;
	
	private List<ECReportSpec> ecReportSpecs;
	
	private ECReportSpec ecReportSpec;
	
	private int currentIndex = -1;
		
	private ECReportSpecEditorPlace place;
	
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
		
		place = (ECReportSpecEditorPlace) placeController.getWhere();
		overlay = new PopupPanel(false, true);
		Utils.blockUi(overlay, 300);
		
			
		view.setEditorTitle(res.report());
		view.setEnableButton(null);
		view.setEnabledAcceptButton(false);
		view.setEditor(new LoadingSpinner());
		
		driver.initialize(editor);
		
		initializeListWidgetHandlers();
		
		initializeOpenWidgetHandling();
		
		editor.configSecGroup.groupFieldEditor.ecFieldSpecEditor.addNewItemHandler(new NewItemEvent.Handler() {
			
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
				editor.configSecGroup.setTMFields(tms.getFixedFields(), tms.getVariableFields());				
			}
		});
		
		SpecLoadedEvent.register(eventBus, this, new SpecLoadedEvent.Handler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSpecLoaded(SpecLoadedEvent event) {
				
				ecReportSpecs = (List<ECReportSpec>) event.getSpec();
				
				if(ecReportSpecs == null) {
					showErrorMessage("MCEventCycleSpec: spec.reportSpecs is NULL!");
					return;
				}
				currentIndex = place.getPathAsInt(1);
				
				
				if(place.isNew() || currentIndex >= ecReportSpecs.size()){
					ecReportSpec = new ECReportSpec();
					ecReportSpec.setReportName("");
					ecReportSpecs.add(ecReportSpec);
					currentIndex = ecReportSpecs.size()-1;
					place.setNew(currentIndex);
				}
				else{	
					ecReportSpec = ecReportSpecs.get(currentIndex);
				}
				checkForNewTM();
				
				if(ecReportSpec.getReportSet() == null){
					ecReportSpec.setReportSet(new ECReportSetSpec());
				}
				if(ecReportSpec.getExtension() == null){
					ecReportSpec.setExtension(new ECReportSpecExtension());
				}
				if(ecReportSpec.getFilterSpec() == null){
					ecReportSpec.setFilterSpec(new ECFilterSpec());
				}
				if(ecReportSpec.getOutput() == null){
					ecReportSpec.setOutput(new ECReportOutputSpec());
				}
				if(ecReportSpec.getOutput().getExtension() == null){
					ecReportSpec.getOutput().setExtension(new ECReportOutputSpecExtension());
				}
				if(ecReportSpec.getOutput().getExtension().getFieldList() == null){
					ecReportSpec.getOutput().getExtension().setFieldList(new FieldList());
				}
				if(ecReportSpec.getGroupSpec() == null){
					ecReportSpec.setGroupSpec(new ECGroupSpec());
				}
				if(ecReportSpec.getGroupSpec().getExtension() == null){
					ecReportSpec.getGroupSpec().setExtension(new ECGroupSpecExtension());
				}
				if(ecReportSpec.getGroupSpec().getExtension().getFieldspec() == null){
					ecReportSpec.getGroupSpec().getExtension().setFieldspec(new ECFieldSpec());
				}
				
				driver.edit(ecReportSpec);
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
	
	
	
	private void initializeListWidgetHandlers() {
		editor.configSecOutput.configSecOutputFields.addNewItemHandler(new NewItemEvent.Handler() {

			@Override
			public void onNewItem(NewItemEvent event) {
				driver.flush();
				updatePathId();
				placeController.goTo(new OutputFieldItemEditorPlace(place, true, -1, "0")); //goTo...OutputFieldItemEditorActivity()
			}
		});
		editor.configSecOutput.configSecOutputFields.setOpenItemHandler(new OpenItemEvent.Handler() {

			@Override
			public void onOpenItem(OpenItemEvent event) {
				driver.flush();
				updatePathId();
				placeController.goTo(new OutputFieldItemEditorPlace(place, false, event.getIndex(), "0"));
			}
		});
		
		editor.configSecGroup.getGroupPatternListWidget().addNewItemHandler(new NewItemEvent.Handler() {
			@Override
			public void onNewItem(NewItemEvent event) {
				driver.flush();
				updatePathId();
				placeController.goTo(new GroupPatternItemEditorPlace(place, true, -1, "0")); //goTo...PatternItemEditorActivity()
			}
		});
		editor.configSecGroup.getGroupPatternListWidget().setOpenItemHandler(new OpenItemEvent.Handler() {
			@Override
			public void onOpenItem(OpenItemEvent event) {
				driver.flush();
				updatePathId();
				placeController.goTo(new GroupPatternItemEditorPlace(place, false, event.getIndex(), "0"));
			}
		});
		
		editor.configSecFilter.addNewItemHandler(new NewItemEvent.Handler() {
			@Override
			public void onNewItem(NewItemEvent event) {
				driver.flush();
				updatePathId();
				placeController.goTo(new FilterItemEditorPlace(place, true , -1, "0")); //goTo...FilterItemEditorActivity()
			}
		});
		editor.configSecFilter.setOpenItemHandler(new OpenItemEvent.Handler() {
			@Override
			public void onOpenItem(OpenItemEvent event) {
				driver.flush();
				updatePathId();
				placeController.goTo(new FilterItemEditorPlace(place, false, event.getIndex(), selectOpenWidgetId(event.getColumn()))); //goTo...FilterItemEditorActivity()
			}
		});
	}
	
	
	/**
	 * update the path 
	 * and mark it as 'FormerNew' if necessary
	 */
	private void updatePathId(){
		if(place.isNew()){
			place.setFormerNew(currentIndex);
		}
	}
	
	@Override
	public void onClose() {
		if((place.isNew() || place.isFormerNew())  && ecReportSpecs != null && ecReportSpecs.size() > currentIndex){
			overlay.show();
			ecReportSpecs.remove(currentIndex);
			place.updatePathId("NEW"+(currentIndex-1));
			overlay.hide();
		}
		storage.removeEmptyLists(place, ecReportSpecs);
		goBack();
	}

	@Override
	public void onAccept() {
		overlay.show();
		driver.flush();
		
		if (driver.hasErrors()) {
			StringBuilder builder = new StringBuilder();
			for(EditorError e : driver.getErrors()) {
				builder.append(e.getMessage());
				builder.append('\n');
			}
			showErrorMessage(builder.toString());
			overlay.hide();
			return;
		}
		
		//Check for reportName doublets
		List<String> nameList = new ArrayList<String>();
		for(ECReportSpec spec : ecReportSpecs){
			if(!nameList.contains(spec.getReportName())){
				nameList.add(spec.getReportName());
			}
			else{
				showErrorMessage(res.errorAlreadyExists("report", spec.getReportName()));
				overlay.hide();
				return;
			}
		}
		
		storage.removeEmptyLists(place, ecReportSpecs);
		overlay.hide();
		goBack();
	}
	
	private void goBack(){
		placeController.goTo(new EditorPlace(place.getListType(),place.getEditorType(),
					place.getPathList(), place.getOpenWidgetIdList()));
		//History.back();
	}
	
	/**
	 * Selects the appropriate openWidgetId
	 * @param column
	 * @return openWidgetId
	 */
	private String selectOpenWidgetId(int column){
		String openWidgetId = "0";
		switch(column){
			case 1:
				openWidgetId = "1";
				break;
			case 2:
				openWidgetId = "2";
				break;
			default:
				break;
		}
		return openWidgetId;
	}
	
	/**
	 * OpenWidgetHandling stores information about opened/closed config sections utilizing the place history mechanism
	 */
	private void initializeOpenWidgetHandling(){
		//opens the specified section
		editor.getConfigSections().setOpen(place.getOpenWidgetIdAsArr(1));
		
		//adds the ExpandedHandler using the specified place level 
		editor.getConfigSections().setSectionExpandedLevel(new SectionExpandedCallback<String>() {
			@Override
			public void onExpandedChanged(String response) {
				place.updateOpenWidgetId(response);
			}
		});
	}
	
	/** 
	 * checkForNewTM()
	 * if getNewTMFieldName() contains a name then it comes back from an new TM creation
	 * and the newly created names will be  inserted to the new POJO. 
	 */
	private void checkForNewTM() {
		
		String newTMFieldName = commonStorage.getNewTMFieldName();
		if (!Utils.isNullOrEmpty(newTMFieldName)) {
			
			if(ecReportSpec.getGroupSpec().getExtension().getFieldspec() == null){
				ecReportSpec.getGroupSpec().getExtension().setFieldspec(new ECFieldSpec());
			}
			
			//commonStorage.getNewTMSpecName(); - not used
			commonStorage.setNewTMSpecName(null);
			
			ecReportSpec.getGroupSpec().getExtension().getFieldspec().setFieldname(newTMFieldName);
			ecReportSpec.getGroupSpec().getExtension().getFieldspec().setDatatype(null);
			ecReportSpec.getGroupSpec().getExtension().getFieldspec().setFormat(null);
			
			commonStorage.setNewTMFieldName(null);
		}
	}
}
