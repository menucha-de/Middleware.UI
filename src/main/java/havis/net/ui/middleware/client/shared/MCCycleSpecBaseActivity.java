package havis.net.ui.middleware.client.shared;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.inject.Inject;

import havis.middleware.ale.service.mc.MCSpec;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.TriggerListType;
import havis.net.ui.middleware.client.place.TriggerPlace;
import havis.net.ui.middleware.client.shared.event.ItemsLoadedEvent;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.shared.storage.CycleSpecStorage;
import havis.net.ui.middleware.client.shared.storage.LRSpecStorage;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.SectionExpandedCallback;

public class MCCycleSpecBaseActivity<M extends MCSpec, E extends MCCycleSpecEditor<? super M>>
		extends MCSpecBaseActivity<M, E> {

	private E editor;
	private ListType listType;

	@Inject
	private CommonStorage commonStorage;

	@Inject
	private LRSpecStorage lrs;

	public MCCycleSpecBaseActivity(SimpleBeanEditorDriver<M, E> driver, String editorTitle, CycleSpecStorage<M> storage,
			E editor, ListType listType) {
		super(driver, editorTitle, storage, editor, listType);
		this.listType = listType;
		this.editor = editor;
	}

	@Override
	protected void initializeEditorComponents() {
		initializeBoundariesEditor();
		initializeReaderEditor();
		initializeOpenWidgetHandling();
	}

	private class TriggerHandler implements NewItemEvent.Handler, OpenItemEvent.Handler {
		private TriggerListType listType;

		public TriggerHandler(TriggerListType listType) {
			this.listType = listType;
		}

		@Override
		public void onNewItem(NewItemEvent event) {
			flushChanges();
			goTo(new TriggerPlace(getPlace(), listType, true, -1, "0"));
		}

		@Override
		public void onOpenItem(OpenItemEvent event) {
			flushChanges();
			goTo(new TriggerPlace(getPlace(), listType, false, event.getIndex(), "0"));
		}

	}

	private void initializeReaderEditor() {
		ItemsLoadedEvent.register(getEventBus(), editor.logicalReadersList(), new ItemsLoadedEvent.Handler() {

			@Override
			public void onItemsLoaded(ItemsLoadedEvent event) {
				editor.logicalReadersList().setAcceptableValues(lrs.getNames());
			}

			@Override
			public void onFailure(ItemsLoadedEvent event, String message) {
				showErrorMessage(message);
			}
		});
		lrs.loadList(editor.logicalReadersList());

		editor.logicalReadersList().setNewItemHandler(new NewItemEvent.Handler() {

			@Override
			public void onNewItem(NewItemEvent event) {
				flushChanges();
				updateCache();
				commonStorage.setNewLRSpecName("");
				goTo(new EditorPlace(listType, EditorType.LR, Utils.getNewId(), true, "0"));
			}
		});
	}

	/**
	 * OpenWidgetHandling stores information about opened/closed config sections
	 * utilizing the place history mechanism
	 */
	private void initializeOpenWidgetHandling() {
		// opens the specified section
		editor.getConfigSections().setOpen(getPlace().getOpenWidgetIdAsArr(0));

		// adds the ExpandedHandler using the specified place level
		editor.getConfigSections().setSectionExpandedLevel(new SectionExpandedCallback<String>() {

			@Override
			public void onExpandedChanged(String response) {
				getPlace().updateOpenWidgetId(response);
			}
		});
	}

	private void initializeBoundariesEditor() {
		TriggerHandler startTriggerHandler = new TriggerHandler(TriggerListType.START);
		editor.boundarySpec().addNewStartTriggerHandler(startTriggerHandler);
		editor.boundarySpec().setOpenStartTriggerHandler(startTriggerHandler);

		TriggerHandler stopTriggerHandler = new TriggerHandler(TriggerListType.STOP);
		editor.boundarySpec().addNewStopTriggerHandler(stopTriggerHandler);
		editor.boundarySpec().setOpenStopTriggerHandler(stopTriggerHandler);
	}

}
