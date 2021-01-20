package havis.net.ui.middleware.client.ds.cache;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.inject.Inject;

import havis.middleware.ale.service.mc.MCCacheSpec;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.ds.CachePatternEditorPlace;
import havis.net.ui.middleware.client.shared.MCSpecBaseActivity;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.DCSpecStorage;
import havis.net.ui.shared.client.SectionExpandedCallback;

public class CacheEditorActivity extends MCSpecBaseActivity<MCCacheSpec, CacheEditor> {

	interface Driver extends SimpleBeanEditorDriver<MCCacheSpec, CacheEditor> {
	}

	private CacheEditor editor;

	@Inject
	public CacheEditorActivity(Driver driver, DCSpecStorage storage, CacheEditor editor) {
		super(driver, ConstantsResource.INSTANCE.cache(), storage, editor, ListType.DSCA);
		this.editor = editor;
	}

	/**
	 * New and open handler for the 'Pattern' table
	 */
	private void initializeListWidgetHandlers() {

		editor.patternListEditor.addNewItemHandler(new NewItemEvent.Handler() {
			@Override
			public void onNewItem(NewItemEvent event) {
				flushChanges();
				goTo(new CachePatternEditorPlace(getPlace(), true, 0, "0"));
			}
		});

		editor.patternListEditor.setOpenItemHandler(new OpenItemEvent.Handler() {

			@Override
			public void onOpenItem(OpenItemEvent event) {
				flushChanges();
				// goTo...DsCachePatternItemEditorActivity()
				goTo(new CachePatternEditorPlace(getPlace(), false, event.getIndex(), "0"));
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
