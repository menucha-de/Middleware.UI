package havis.net.ui.middleware.client.pc;

import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

import havis.middleware.ale.service.mc.MCPortCycleSpec;
import havis.net.rest.middleware.pc.PCAsync;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.shared.spec.SpecItemListActivity;

public class PortCycleListActivity extends SpecItemListActivity<MCPortCycleSpec> {
	@Inject
	PlaceController placeController;
	
	@Inject
	public PortCycleListActivity(PCAsync service) {
		super(service.getSpecs(), ListType.PC);
	}
	
	@Override
	public void onRun(final String id) {
		placeController.goTo(new EditorPlace(ListType.PC, EditorType.PCREP, id, false, "0"));
	}
}