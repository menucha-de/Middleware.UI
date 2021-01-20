package havis.net.ui.middleware.client.ec;

import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

import havis.middleware.ale.service.mc.MCEventCycleSpec;
import havis.net.rest.middleware.ec.ECAsync;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.shared.spec.SpecItemListActivity;

public class EventCycleListActivity extends SpecItemListActivity<MCEventCycleSpec> {
	
	@Inject
	PlaceController placeController;
	
	@Inject
	public EventCycleListActivity(ECAsync service) {
		super(service.getSpecs(), ListType.EC);
	}

	@Override
	public void onRun(final String id) {
		placeController.goTo(new EditorPlace(ListType.EC, EditorType.ECREP, id, false, "0"));
	}		

}
