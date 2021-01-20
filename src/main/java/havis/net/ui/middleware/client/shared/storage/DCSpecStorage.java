package havis.net.ui.middleware.client.shared.storage;

import havis.middleware.ale.service.cc.EPCCacheSpec;
import havis.middleware.ale.service.cc.EPCPatternList;
import havis.middleware.ale.service.cc.EPCPatternList.Patterns;
import havis.middleware.ale.service.mc.MCCacheSpec;
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

public class DCSpecStorage extends BaseSpecStorage<MCCacheSpec> {

	interface Codec extends JsonEncoderDecoder<MCCacheSpec> {}

	@Inject
	public DCSpecStorage(CCAsync service) {
		super((Codec) GWT.create(Codec.class));
		setSpecs(service.getCaches());
	}

	@Override
	protected Object getInitializedSpec(CommonEditorPlace place, MCCacheSpec spec) {
		return initializeCASpec(place, (MCCacheSpec) spec);
	}

	private MCCacheSpec initializeCASpec(CommonEditorPlace place, MCCacheSpec mcCacheSpec) {

		boolean isNull = (mcCacheSpec == null);

		if (isNull) {
			mcCacheSpec = new MCCacheSpec();
			mcCacheSpec.setEnable(false);
			mcCacheSpec.setId(null);
		}
		EPCCacheSpec spec = mcCacheSpec.getSpec();
		if (spec == null) {
			spec = new EPCCacheSpec();
			mcCacheSpec.setSpec(spec);
		}
		EPCPatternList list = mcCacheSpec.getPatterns();
		if (list == null) {
			list = new EPCPatternList();

			Patterns pats = new Patterns();
			pats.getPattern();
			// pats.getPattern().add("new:epc:new:new:*.*.*");

			list.setPatterns(pats);
			mcCacheSpec.setPatterns(list);
		}

		if (isNull) {
			setSpec(place.getSpecId(), mcCacheSpec);
		}

		return mcCacheSpec;
	}

	@Override
	public void cleanSpec(MCCacheSpec spec) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Gets the user defined cache names
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
		Collection<MCCacheSpec> cacheSpecs = getList();
		List<String> names = new ArrayList<String>();
		if(cacheSpecs != null){
			for(MCCacheSpec spec : cacheSpecs){
				names.add(spec.getName());
			}
		}
		return names;
	}

}
