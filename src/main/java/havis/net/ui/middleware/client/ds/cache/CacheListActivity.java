package havis.net.ui.middleware.client.ds.cache;

import com.google.inject.Inject;

import havis.middleware.ale.service.mc.MCCacheSpec;
import havis.net.rest.middleware.cc.CCAsync;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.shared.spec.SpecItemListActivity;

public class CacheListActivity extends SpecItemListActivity<MCCacheSpec> {
	@Inject
	public CacheListActivity(CCAsync service) {
		super(service.getCaches(), ListType.DSCA);
	}
}