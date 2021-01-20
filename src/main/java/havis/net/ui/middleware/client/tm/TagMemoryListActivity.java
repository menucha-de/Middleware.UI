package havis.net.ui.middleware.client.tm;

import com.google.inject.Inject;

import havis.middleware.ale.service.mc.MCTagMemorySpec;
import havis.net.rest.middleware.tm.TMAsync;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.shared.spec.SpecItemListActivity;

public class TagMemoryListActivity extends SpecItemListActivity<MCTagMemorySpec> {
	@Inject
	public TagMemoryListActivity(TMAsync service) {
		super(service.getSpecs(), ListType.TM);
	}
}