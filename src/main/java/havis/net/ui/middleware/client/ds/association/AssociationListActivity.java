package havis.net.ui.middleware.client.ds.association;

import com.google.inject.Inject;

import havis.middleware.ale.service.mc.MCAssociationSpec;
import havis.net.rest.middleware.cc.CCAsync;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.shared.spec.SpecItemListActivity;

public class AssociationListActivity extends SpecItemListActivity<MCAssociationSpec> {
	@Inject
	public AssociationListActivity(CCAsync service) {
		super(service.getAssociations(), ListType.DSAS);
	}
}