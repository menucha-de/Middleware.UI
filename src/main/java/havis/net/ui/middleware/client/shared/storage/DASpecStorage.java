package havis.net.ui.middleware.client.shared.storage;

import havis.middleware.ale.service.cc.AssocTableEntryList;
import havis.middleware.ale.service.cc.AssocTableEntryList.Entries;
import havis.middleware.ale.service.cc.AssocTableSpec;
import havis.middleware.ale.service.mc.MCAssociationSpec;
import havis.net.rest.middleware.cc.CCAsync;
import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;
import havis.net.ui.middleware.client.shared.DataSourceNamesCallback;
import havis.net.ui.middleware.client.shared.event.ItemsLoadedEvent;
import havis.net.ui.middleware.client.shared.event.MessageEvent;
import havis.net.ui.middleware.client.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class DASpecStorage extends BaseSpecStorage<MCAssociationSpec> {

	interface Codec extends JsonEncoderDecoder<MCAssociationSpec> {}

	@Inject
	public DASpecStorage(CCAsync service) {
		super((Codec) GWT.create(Codec.class));
		setSpecs(service.getAssociations());
	}

	@Override
	protected Object getInitializedSpec(CommonEditorPlace place, MCAssociationSpec spec) {
		return initializeASSpec(place, spec);
	}

	private MCAssociationSpec initializeASSpec(CommonEditorPlace place, MCAssociationSpec mcAssociationSpec) {
		boolean isDirty = false;
		boolean isNull = (mcAssociationSpec == null);

		if (isNull) {
			mcAssociationSpec = new MCAssociationSpec();
			mcAssociationSpec.setEnable(false);
			mcAssociationSpec.setId(null);
			mcAssociationSpec.setCreationDate(new Date());
		}
		AssocTableSpec spec = mcAssociationSpec.getSpec();
		if (spec == null) {
			spec = new AssocTableSpec();
			spec.setCreationDate(new Date());
			mcAssociationSpec.setSpec(spec);
		}
		AssocTableEntryList list = mcAssociationSpec.getEntries();
		if (list == null) {
			list = new AssocTableEntryList();
			isDirty = true;
		}

		Entries entries = list.getEntries();
		if (entries == null) {
			entries = new Entries();
			entries.getEntry();
			list.setEntries(entries);
			isDirty = true;
		}
		if (isDirty) {
			mcAssociationSpec.setEntries(list);
		}

		if (isNull) {
			setSpec(place.getSpecId(), mcAssociationSpec);
		}

		return mcAssociationSpec;
	}

	@Override
	public void cleanSpec(MCAssociationSpec spec) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeEmptyLists(CommonEditorPlace place, Object spec) {
		MCAssociationSpec as = (MCAssociationSpec) spec;
		if (as.getEntries() == null || as.getEntries().getEntries() == null
				|| as.getEntries().getEntries().getEntry().isEmpty()) {
			as.setEntries(null);
		}
		if (as.getSpec() == null || Utils.isNullOrEmpty(as.getSpec().getDatatype())
				|| Utils.isNullOrEmpty(as.getSpec().getFormat())) {
			as.setSpec(null);
		}

	}
	
	/**
	 * Gets the user defined association names
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
		Collection<MCAssociationSpec> assocSpecs = getList();
		List<String> names = new ArrayList<String>();
		if(assocSpecs != null){
			for(MCAssociationSpec spec : assocSpecs){
				names.add(spec.getName());
			}
		}
		return names;
	}
}
