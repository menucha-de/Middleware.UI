package havis.net.ui.middleware.client.shared.trigger;

import havis.middleware.ale.service.mc.MCSpec;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.TriggerListType;
import havis.net.ui.middleware.client.place.TriggerPlace;
import havis.net.ui.middleware.client.place.pc.PcReportSpecItemEditorPlace;
import havis.net.ui.middleware.client.shared.BaseActivity;
import havis.net.ui.middleware.client.shared.EditorDialogView;
import havis.net.ui.middleware.client.shared.LoadingSpinner;
import havis.net.ui.middleware.client.shared.event.ItemsLoadedEvent;
import havis.net.ui.middleware.client.shared.event.SpecLoadedEvent;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.shared.storage.LRSpecStorage;

import java.util.List;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class TriggerEditorActivity extends BaseActivity implements EditorDialogView.Presenter{

	@Inject
	private EditorDialogView view;

	@Inject
	private CommonStorage commonStorage;
	
	@Inject
	private LRSpecStorage lrs;

	@Inject
	private TriggerEditor editor;

	@Inject
	private PlaceController placeController;
	
	private TriggerPlace place;
	
	private PopupPanel overlay;

	@Inject
	private Driver driver;
	interface Driver extends SimpleBeanEditorDriver<String, TriggerEditor> {}

	private int currentIndex = -1;
	
	private List<String> triggerList;
	
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		setEventBus(eventBus);
		panel.setWidget(view.asWidget());
		bind();
		
		view.setEditorTitle("Trigger");
		view.setEnableButton(null);
		view.setEnabledAcceptButton(false);
		view.setEditor(new LoadingSpinner());

		place = (TriggerPlace) placeController.getWhere();
		
		int lastIdx = place.getPathSize()-1;
		currentIndex = place.getPathAsInt(lastIdx);
		
		driver.initialize(editor);

		SpecLoadedEvent.register(eventBus, this, new SpecLoadedEvent.Handler() {

			@Override
			public void onSpecLoaded(SpecLoadedEvent event) {
				
				triggerList = commonStorage.getTriggerList(place, (MCSpec)event.getSpec());
				
				if(triggerList == null){
					showErrorMessage("triggerList is NULL!");
					return;
				}
				
				if(place.isNew()){
					triggerList.add("");
					currentIndex = triggerList.size()-1;
					place.setNew(currentIndex);
				}

				driver.initialize(editor);
				
				if(currentIndex < 0) return;
				driver.edit(triggerList.get(currentIndex));

				view.setEnabledAcceptButton(true);
				view.setEditor(editor);
			}

			@Override
			public void onFailure(SpecLoadedEvent event, String message) {
				view.setEditor(null);
				showErrorMessage(message);
			}
		});

		ItemsLoadedEvent.register(eventBus, this, new ItemsLoadedEvent.Handler() {

			@Override
			public void onItemsLoaded(ItemsLoadedEvent event) {
				editor.portTriggerEditor.reader.setItems(lrs.getNames());
			}

			@Override
			public void onFailure(ItemsLoadedEvent event, String message) {
				showErrorMessage(message);
			}
		});
		
		commonStorage.loadSpec(place, this);
		lrs.loadList(this);
	}
	

	@Override
	public void bind() {
		view.setPresenter(this);
	}

	@Override
	public void onClose() {
		
		if((place.isNew() || place.isFormerNew()) && triggerList != null && triggerList.size() > currentIndex){
			overlay = new PopupPanel(false, true);
			overlay.show();
			triggerList.remove(currentIndex);
			place.updatePathId(""+(currentIndex-1));
			
		}
		commonStorage.resetEmptyListObjects(place, triggerList);
		goBack();
	}

	@Override
	public void onAccept() {
		
		driver.flush();
		overlay = new PopupPanel(false, true);
		overlay.show();
		
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
		
		triggerList.set(currentIndex, editor.urn.getValue());
		
		goBack();
	}
	
	private void goBack(){
		if(overlay != null)overlay.hide();
		
		if(place.getTriggerListType() == TriggerListType.REPORT){
			placeController.goTo(new PcReportSpecItemEditorPlace(place.getListType(),EditorType.valueOf(place.getListType().toString()),
					place.getPathList(), place.getOpenWidgetIdList()));
		}
		else{
			placeController.goTo(new EditorPlace(place.getListType(),EditorType.valueOf(place.getListType().toString()),
					place.getPathList(), place.getOpenWidgetIdList()));
		}
	}

}
