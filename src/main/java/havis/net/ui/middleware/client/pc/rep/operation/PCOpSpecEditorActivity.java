package havis.net.ui.middleware.client.pc.rep.operation;

import havis.middleware.ale.service.pc.PCOpSpec;
import havis.middleware.ale.service.pc.PCOpSpecExtension;
import havis.middleware.ale.service.pc.PCPortSpec;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.pc.OpSpecEditorPlace;
import havis.net.ui.middleware.client.place.pc.PcReportSpecItemEditorPlace;
import havis.net.ui.middleware.client.shared.BaseActivity;
import havis.net.ui.middleware.client.shared.EditorDialogView;
import havis.net.ui.middleware.client.shared.LoadingSpinner;
import havis.net.ui.middleware.client.shared.event.ItemsLoadedEvent;
import havis.net.ui.middleware.client.shared.event.SpecLoadedEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.LRSpecStorage;
import havis.net.ui.middleware.client.shared.storage.PCSpecStorage;
import havis.net.ui.middleware.client.shared.storage.TMSpecStorage;
import havis.net.ui.middleware.client.shared.trigger.model.PortTrigger;
import havis.net.ui.middleware.client.utils.Utils;

import java.util.List;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class PCOpSpecEditorActivity extends BaseActivity implements EditorDialogView.Presenter{
	
	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	@Inject
	PCSpecStorage storage;
	
	@Inject
	LRSpecStorage lrs;
	
	@Inject
	TMSpecStorage tms;
	
	@Inject
	EditorDialogView view;

	@Inject
	PlaceController placeController;
	
	private OpSpecEditorPlace place;
	
	@Inject
	private Driver driver;

	interface Driver extends SimpleBeanEditorDriver<PCOpSpec, PCOpSpecEditor> {
	}
	
	@Inject
	PCOpSpecEditor editor;
	
	private List<PCOpSpec> pcOpSpecs;
	
	private PCOpSpec pcOpSpec;
	
	private int currentIndex = -1;
		
	private PopupPanel overlay;
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		overlay = new PopupPanel(false, true);
		Utils.blockUi(overlay, 300);
		
		setEventBus(eventBus);
		panel.setWidget(view.asWidget());
		bind();
		
		place = (OpSpecEditorPlace) placeController.getWhere();
			
		view.setEditorTitle(res.portOperation());
		view.setEnableButton(null);
		view.setEnabledAcceptButton(false);
		view.setEditor(new LoadingSpinner());
		driver.initialize(editor);
		
		fetchLRNames();
		
		SpecLoadedEvent.register(eventBus, this, new SpecLoadedEvent.Handler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSpecLoaded(SpecLoadedEvent event) {
				
				pcOpSpecs = (List<PCOpSpec>) event.getSpec();
				
				if(pcOpSpecs == null){
					showErrorMessage("MCPortCycleSpec: opSpecs.opSpec is NULL!");
					return;
				}
				
				if(place.isNew()){
					pcOpSpec = new PCOpSpec();
					pcOpSpecs.add(pcOpSpec);
					currentIndex = pcOpSpecs.size()-1;
					place.setNew(currentIndex);
				}
				else{
					currentIndex = place.getPathAsInt(2);
					pcOpSpec = pcOpSpecs.get(currentIndex); 
				}
				
				if(pcOpSpec.getPortSpec() == null){
					PCPortSpec portSpec = new PCPortSpec();
					portSpec.setId(PortTrigger.PIN_ANY);
					pcOpSpec.setPortSpec(portSpec);
				}
				if(pcOpSpec.getDuration() == null){
					pcOpSpec.setDuration(Utils.getTime());
				}
				if(pcOpSpec.getExtension() == null){
					pcOpSpec.setExtension(new PCOpSpecExtension());
				}
				
				
				driver.edit(pcOpSpec);
				view.setEnabledAcceptButton(true);
				view.setEditor(editor);
			}

			@Override
			public void onFailure(SpecLoadedEvent event, String message) {
				view.setEditor(null);
				showErrorMessage(message);
			}
		});
		
		storage.loadSpec(place, this); //triggers SpecLoadedEvent...
	}
	
	
	@Override
	public void bind() {
		view.setPresenter(this);
	}

	@Override
	public void onClose() {
		if(place.isNew() && pcOpSpecs != null && pcOpSpecs.size() > currentIndex){
			// Lock UI
			//PopupPanel overlay = new PopupPanel(false, true);
			overlay.show();
			if(pcOpSpecs != null)
				pcOpSpecs.remove(currentIndex);
			place.updatePathId(""+(currentIndex-1));
			
			overlay.hide();
		}
		storage.removeEmptyLists(place, pcOpSpecs);
		placeController.goTo(new PcReportSpecItemEditorPlace(ListType.PC , EditorType.PC, place.getPathList(), place.getOpenWidgetIdList()));
	}

	@Override
	public void onAccept() {
		// Lock UI
		//overlay.show();
		
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
		
		if (!Utils.isTimeSet(pcOpSpec.getDuration())) {
			pcOpSpec.setDuration(null);
		}
		
		storage.removeEmptyLists(place, pcOpSpecs);
		overlay.hide();
		placeController.goTo(new PcReportSpecItemEditorPlace(ListType.PC , EditorType.PC, place.getPathList(), place.getOpenWidgetIdList()));
	}
	
	/**
	 * Gets the local reader names
	 */
	private void fetchLRNames(){
		ItemsLoadedEvent.register(getEventBus(), this, new ItemsLoadedEvent.Handler() {
			@Override
			public void onItemsLoaded(ItemsLoadedEvent event) {
				List<String> lst = lrs.getNames();
				editor.setReaderItems(lst);
			}
			@Override
			public void onFailure(ItemsLoadedEvent event, String message) {
				showErrorMessage(message);
			}
		});
		tms.loadList(this);
	}


	
}
