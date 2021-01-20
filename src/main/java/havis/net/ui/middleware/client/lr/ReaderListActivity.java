package havis.net.ui.middleware.client.lr;

import com.google.inject.Inject;

import havis.middleware.ale.service.mc.MCLogicalReaderSpec;
import havis.net.rest.middleware.lr.LRAsync;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.shared.spec.SpecItemListActivity;

public class ReaderListActivity extends SpecItemListActivity<MCLogicalReaderSpec> {
	@Inject
	public ReaderListActivity(LRAsync service) {
		super(service.getSpecs(), ListType.LR);
	}
}
