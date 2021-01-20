package havis.net.ui.middleware.client.shared.storage;

import havis.middleware.ale.service.cc.RNGSpec;
import havis.middleware.ale.service.mc.MCRandomSpec;
import havis.net.rest.middleware.cc.CCAsync;
import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;
import havis.net.ui.middleware.client.shared.DataSourceNamesCallback;
import havis.net.ui.middleware.client.shared.event.ItemsLoadedEvent;
import havis.net.ui.middleware.client.shared.event.MessageEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class DRSpecStorage extends BaseSpecStorage<MCRandomSpec> {

	interface Codec extends JsonEncoderDecoder<MCRandomSpec> {}

	@Inject
	public DRSpecStorage(CCAsync service) {
		super((Codec) GWT.create(Codec.class));
		setSpecs(service.getRandoms());
	}

	@Override
	protected Object getInitializedSpec(CommonEditorPlace place, MCRandomSpec spec) {
		return initializeRNSpec(place, (MCRandomSpec) spec);
	}

	private MCRandomSpec initializeRNSpec(CommonEditorPlace place, MCRandomSpec mcRandomSpec) {
		boolean isNull = (mcRandomSpec == null);

		if (isNull) {
			mcRandomSpec = new MCRandomSpec();
			mcRandomSpec.setEnable(false);
			mcRandomSpec.setId(null);
		}
		RNGSpec spec = mcRandomSpec.getSpec();
		if (spec == null) {
			spec = new RNGSpec();
			mcRandomSpec.setSpec(spec);
		}

		if (isNull) {
			setSpec(place.getSpecId(), mcRandomSpec);
		}

		return mcRandomSpec;
	}

	@Override
	public void cleanSpec(MCRandomSpec spec) {
	}
	
	/**
	 * Gets the user defined random names
	 */
	public void fetchDataSourceNames(final EventBus eventBus, final DataSourceNamesCallback callback ){
		ItemsLoadedEvent.register(eventBus, this, new ItemsLoadedEvent.Handler() {
			@Override
			public void onItemsLoaded(ItemsLoadedEvent event) {
				List<String> list = getDataSourceNames();
				callback.onGotDataSourceNames(list);
			}
			@Override
			public void onFailure(ItemsLoadedEvent event, String message) {
				if(eventBus != null)
					eventBus.fireEventFromSource(new MessageEvent(MessageEvent.MessageType.ERROR, message), this);
			}
		});
		loadList(this, false);
	}

	private List<String> getDataSourceNames(){
		Collection<MCRandomSpec> randomSpecs = getList();
		List<String> names = new ArrayList<String>();
		if(randomSpecs != null){
			for(MCRandomSpec spec : randomSpecs){
				names.add(spec.getName());
			}
		}
		return names;
	}

}
