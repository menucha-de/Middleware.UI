package havis.net.ui.middleware.client.cc;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

import havis.middleware.ale.service.cc.CCCmdSpec;
import havis.middleware.ale.service.cc.CCCmdSpec.OpSpecs;
import havis.middleware.ale.service.cc.CCOpDataSpec;
import havis.middleware.ale.service.cc.CCOpSpec;
import havis.middleware.ale.service.cc.CCParameterListEntry;
import havis.middleware.ale.service.cc.CCSpec;
import havis.middleware.ale.service.cc.CCSpec.CmdSpecs;
import havis.middleware.ale.service.mc.MCCommandCycleSpec;
import havis.net.rest.middleware.cc.CCAsync;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.shared.spec.SpecItemListActivity;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;

public class CommandCycleListActivity extends SpecItemListActivity<MCCommandCycleSpec> {
	
	@Inject
	PlaceController placeController;
	
	@Inject
	CommonStorage storage;
	
	@Inject
	public CommandCycleListActivity(CCAsync service) {
		super(service.getSpecs(), ListType.CC);
	}
	
	@Override
	public void onRun(final String id) {
		MCCommandCycleSpec mcSpec = (MCCommandCycleSpec) findMCMcSpec(id);
		if (mcSpec != null) {
			List<CCParameterListEntry> parameters = new ArrayList<>();
			CCSpec spec = mcSpec.getSpec();
			if (spec != null) {
				CmdSpecs cmdSpecs = spec.getCmdSpecs();
				if (cmdSpecs != null) {
					for (CCCmdSpec cs : cmdSpecs.getCmdSpec()) {
						OpSpecs opSpecs = cs.getOpSpecs();
						if (opSpecs != null) {
							for (CCOpSpec os : opSpecs.getOpSpec()) {
								CCOpDataSpec cods = os.getDataSpec();
								if (cods != null) {
									if (cods.getSpecType().equals("PARAMETER")) {
										CCParameterListEntry entry = new CCParameterListEntry();
										entry.setName(cods.getData());
										parameters.add(entry);
									}
								}
							}
						}
					}
				}
			}
			storage.setParameters(parameters);
		}
		placeController.goTo(new EditorPlace(ListType.CC, EditorType.CCREP, id, false, "0"));
	}
}