package havis.net.ui.middleware.client.ec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.inject.Inject;

import havis.middleware.ale.service.mc.MCEventCycleSpec;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.ec.ECReportSpecEditorPlace;
import havis.net.ui.middleware.client.shared.MCCycleSpecBaseActivity;
import havis.net.ui.middleware.client.shared.event.ItemsLoadedEvent;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.shared.storage.ECSpecStorage;
import havis.net.ui.middleware.client.shared.storage.TMSpecStorage;
import havis.net.ui.middleware.client.tm.data.Fieldname;
import havis.net.ui.middleware.client.utils.Utils;

public class EventCycleEditorActivity extends MCCycleSpecBaseActivity<MCEventCycleSpec, EventCycleEditor> {

	private ConstantsResource res = ConstantsResource.INSTANCE;

	@Inject
	private CommonStorage commonStorage;

	@Inject
	private TMSpecStorage tms;

	interface Driver extends SimpleBeanEditorDriver<MCEventCycleSpec, EventCycleEditor> {
	}

	private EventCycleEditor editor;

	@Inject
	public EventCycleEditorActivity(Driver driver, ECSpecStorage storage, EventCycleEditor editor) {
		super(driver, ConstantsResource.INSTANCE.ecTitleEventCycle(), storage, editor, ListType.EC);
		this.editor = editor;
	}
	
	/**
	 * checkForNewTM - hint: call it before driver.edit(mcEventCycleSpec);
	 */
	private void checkForNewTM() {
		String newTMFieldName = tms.getNewFieldName();
		if (!Utils.isNullOrEmpty(newTMFieldName)) {
			getMCSpec().getSpec().getExtension().getPrimaryKeyFields().getPrimaryKeyField().add(newTMFieldName);
			tms.setNewFieldName(null);
		}
	}

	private void checkForNewLR() {
		String newLRSpecName = commonStorage.getNewLRSpecName();
		if (!Utils.isNullOrEmpty(newLRSpecName)) {
			getMCSpec().getSpec().getLogicalReaders().getLogicalReader().add(newLRSpecName);
			commonStorage.setNewLRSpecName(null);
		}
	}

	/**
	 * Adds addNewItemHandler() and sets setOpenItemHandler() for
	 * editor.reportSpecsEdit
	 * 
	 * @param place,
	 *            the current EditorPlace
	 */
	private void initializeReportsEditor() {
		editor.reportSpecsListWidget.addNewItemHandler(new NewItemEvent.Handler() {

			@Override
			public void onNewItem(NewItemEvent event) {
				flushChanges();
				goTo(new ECReportSpecEditorPlace(getPlace(), true, -1, "0"));
			}
		});

		editor.reportSpecsListWidget.setOpenItemHandler(new OpenItemEvent.Handler() {

			@Override
			public void onOpenItem(OpenItemEvent event) {
				flushChanges();
				int index = event.getIndex();
				goTo(new ECReportSpecEditorPlace(getPlace(), false, index, "0"));
			}
		});
	}

	private void initializePrimaryKeysEditor() {
		ItemsLoadedEvent.register(getEventBus(), editor.primaryKeysList, new ItemsLoadedEvent.Handler() {

			@Override
			public void onItemsLoaded(ItemsLoadedEvent event) {
				Map<String, List<String>> values = new HashMap<String, List<String>>();
				values.putAll(Fieldname.asPreDefGroup());
				values.putAll(tms.getFieldNames());
				editor.primaryKeysList.setAcceptableValues(values);
			}

			@Override
			public void onFailure(ItemsLoadedEvent event, String message) {
				showErrorMessage(message);
			}

		});
		tms.loadList(editor.primaryKeysList);

		editor.primaryKeysList.setNewItemHandler(new NewItemEvent.Handler() {

			@Override
			public void onNewItem(NewItemEvent event) {
				flushChanges();
				updateCache();
				tms.setNewSpecName(null);
				tms.setNewFieldName(res.newEntry());
				goTo(new EditorPlace(ListType.EC, EditorType.TM, Utils.getNewId(), false, "0"));
			}
		});
	}

	@Override
	protected void initializeEditorComponents() {
		super.initializeEditorComponents();
		initializePrimaryKeysEditor();
		initializeReportsEditor();
	}
	
	@Override
	protected void prepareEditor() {
		checkForNewLR();
		checkForNewTM();
		super.prepareEditor();
	}

}
