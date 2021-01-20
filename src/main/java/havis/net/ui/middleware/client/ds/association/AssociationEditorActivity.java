package havis.net.ui.middleware.client.ds.association;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.inject.Inject;

import havis.middleware.ale.service.mc.MCAssociationSpec;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.ds.AssociationEntriesEditorPlace;
import havis.net.ui.middleware.client.shared.MCSpecBaseActivity;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.DASpecStorage;
import havis.net.ui.shared.client.SectionExpandedCallback;

public class AssociationEditorActivity extends MCSpecBaseActivity<MCAssociationSpec, AssociationEditor> {

	interface Driver extends SimpleBeanEditorDriver<MCAssociationSpec, AssociationEditor> {
	}

	private AssociationEditor editor;

	@Inject
	public AssociationEditorActivity(Driver driver, DASpecStorage storage, AssociationEditor editor) {
		super(driver, ConstantsResource.INSTANCE.association(), storage, editor, ListType.DSAS);
		this.editor = editor;
	}

	/**
	 * New and open handler for the 'Entries' table
	 */
	private void initializeListWidgetHandlers() {

		editor.configSecEntries.addNewItemHandler(new NewItemEvent.Handler() {
			@Override
			public void onNewItem(NewItemEvent event) {
				flushChanges();
				goTo(new AssociationEntriesEditorPlace(getPlace(), true, 0, "0"));
			}
		});

		editor.configSecEntries.setOpenItemHandler(new OpenItemEvent.Handler() {

			@Override
			public void onOpenItem(OpenItemEvent event) {
				flushChanges();
				goTo(new AssociationEntriesEditorPlace(getPlace(), false, event.getIndex(), "0"));
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

	@Override
	public void initializeEditorComponents() {
		initializeListWidgetHandlers();
		initializeOpenWidgetHandling();
	}
}
