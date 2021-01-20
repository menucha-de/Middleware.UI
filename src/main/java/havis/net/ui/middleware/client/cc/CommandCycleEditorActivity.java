package havis.net.ui.middleware.client.cc;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.inject.Inject;

import havis.middleware.ale.service.mc.MCCommandCycleSpec;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.cc.CCCmdSpecEditorPlace;
import havis.net.ui.middleware.client.shared.MCCycleSpecBaseActivity;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.CCSpecStorage;

public class CommandCycleEditorActivity extends MCCycleSpecBaseActivity<MCCommandCycleSpec, CommandCycleEditor> {

	interface Driver extends SimpleBeanEditorDriver<MCCommandCycleSpec, CommandCycleEditor> {
	}

	private CommandCycleEditor editor;

	@Inject
	public CommandCycleEditorActivity(Driver driver, CCSpecStorage storage, CommandCycleEditor editor) {
		super(driver, ConstantsResource.INSTANCE.commandCycle(), storage, editor, ListType.CC);
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
		editor.commandSpecsListWidget.addNewItemHandler(new NewItemEvent.Handler() {

			@Override
			public void onNewItem(NewItemEvent event) {
				flushChanges();
				goTo(new CCCmdSpecEditorPlace(getPlace(), true, -1, "0"));
			}
		});

		editor.commandSpecsListWidget.setOpenItemHandler(new OpenItemEvent.Handler() {

			@Override
			public void onOpenItem(OpenItemEvent event) {
				flushChanges();
				int index = event.getIndex();
				goTo(new CCCmdSpecEditorPlace(getPlace(), false, index, "0"));
			}
		});
	}

	@Override
	public void initializeEditorComponents() {
		super.initializeEditorComponents();
		initializeReportsEditor();
	}
}
