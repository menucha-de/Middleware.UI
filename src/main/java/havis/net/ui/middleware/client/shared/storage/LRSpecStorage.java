package havis.net.ui.middleware.client.shared.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;

import havis.middleware.ale.service.mc.MCLogicalReaderSpec;
import havis.net.rest.middleware.lr.LRAsync;
import havis.net.ui.middleware.client.utils.Utils;

public class LRSpecStorage extends BaseSpecStorage<MCLogicalReaderSpec> {

	interface Codec extends JsonEncoderDecoder<MCLogicalReaderSpec> {}

	@Inject
	public LRSpecStorage(LRAsync service) {
		super((Codec) GWT.create(Codec.class));
		setSpecs(service.getSpecs());
	}
	
	/**
	 * Returns the list of Logical Reader Names. The method
	 * {@link #loadList(Object)} has to be called first.
	 * 
	 * @return list of Logical Reader Names
	 */
	public List<String> getNames() {
		Collection<MCLogicalReaderSpec> lrSpecs = getList();
		List<String> values = new ArrayList<String>();
		if (lrSpecs != null) {
			for (MCLogicalReaderSpec spec : lrSpecs) {
				if (!Utils.isNullOrEmpty(spec.getId()))
					values.add(spec.getName());
			}
		}
		return values;
	}

	@Override
	public void cleanSpec(MCLogicalReaderSpec spec) {
	}
}
