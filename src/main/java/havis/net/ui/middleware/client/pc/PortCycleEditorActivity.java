package havis.net.ui.middleware.client.pc;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.inject.Inject;

import havis.middleware.ale.service.mc.MCPortCycleSpec;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.pc.PcReportSpecItemEditorPlace;
import havis.net.ui.middleware.client.shared.EditorDialogView;
import havis.net.ui.middleware.client.shared.MCCycleSpecBaseActivity;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.shared.storage.PCSpecStorage;
import havis.net.ui.middleware.client.utils.Utils;

public class PortCycleEditorActivity extends MCCycleSpecBaseActivity<MCPortCycleSpec, PortCycleEditor>
		implements EditorDialogView.Presenter {

	@Inject
	private CommonStorage commonStorage;

	interface Driver extends SimpleBeanEditorDriver<MCPortCycleSpec, PortCycleEditor> {
	}

	private PortCycleEditor editor;

	@Inject
	public PortCycleEditorActivity(Driver driver, PCSpecStorage storage, PortCycleEditor editor) {
		super(driver, ConstantsResource.INSTANCE.pcTitlePortCycle(), storage, editor, ListType.PC);
		this.editor = editor;
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
				updateCache();
				goTo(new PcReportSpecItemEditorPlace(getPlace(), true, 0, "0"));
			}
		});

		editor.reportSpecsListWidget.setOpenItemHandler(new OpenItemEvent.Handler() {

			@Override
			public void onOpenItem(OpenItemEvent event) {
				flushChanges();
				updateCache();
				int index = event.getIndex();
				goTo(new PcReportSpecItemEditorPlace(getPlace(), false, index, "0"));
			}
		});
	}
	private void checkForNewLR() {
		String newLRSpecName = commonStorage.getNewLRSpecName();
		if (!Utils.isNullOrEmpty(newLRSpecName)) {
			getMCSpec().getSpec().getLogicalReaders().getLogicalReader().add(newLRSpecName);
			commonStorage.setNewLRSpecName(null);
		}
	}

	@Override
	protected void initializeEditorComponents() {
		super.initializeEditorComponents();
		initializeReportsEditor();
	}

	@Override
	protected void prepareEditor() {
		checkForNewLR();
		super.prepareEditor();
	}

}
