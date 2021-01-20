package havis.net.ui.middleware.client.shared;

import havis.net.ui.shared.client.ConfigurationSections;

public interface MCCycleSpecEditor<T> extends MCSpecEditor<T> {
	HasTriggerHandlers boundarySpec();
	
	@Path("spec.logicalReaders.logicalReader")
	CommonListEditor logicalReadersList();
	
	ConfigurationSections getConfigSections();
}
