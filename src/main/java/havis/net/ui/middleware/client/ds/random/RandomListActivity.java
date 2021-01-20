package havis.net.ui.middleware.client.ds.random;

import com.google.inject.Inject;

import havis.middleware.ale.service.mc.MCRandomSpec;
import havis.net.rest.middleware.cc.CCAsync;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.shared.spec.SpecItemListActivity;

public class RandomListActivity extends SpecItemListActivity<MCRandomSpec> {
	@Inject
	public RandomListActivity(CCAsync service) {
		super(service.getRandoms(), ListType.DSRN);
	}
}