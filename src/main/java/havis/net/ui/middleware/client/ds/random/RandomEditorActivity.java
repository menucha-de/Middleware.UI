package havis.net.ui.middleware.client.ds.random;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.inject.Inject;

import havis.middleware.ale.service.mc.MCRandomSpec;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.shared.MCSpecBaseActivity;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.DRSpecStorage;

public class RandomEditorActivity extends MCSpecBaseActivity<MCRandomSpec, RandomEditor> {

	interface Driver extends SimpleBeanEditorDriver<MCRandomSpec, RandomEditor> {
	}

	@Inject
	public RandomEditorActivity(Driver driver, DRSpecStorage storage, RandomEditor editor) {
		super(driver, ConstantsResource.INSTANCE.random(), storage, editor, ListType.DSRN);
	}

	@Override
	public void initializeEditorComponents() {
		// nothing to do ...
	}

}
