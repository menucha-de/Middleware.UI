package havis.net.ui.middleware.client.shared.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import havis.middleware.ale.service.mc.MCTagMemorySpec;
import havis.middleware.ale.service.tm.TMFixedFieldListSpec;
import havis.middleware.ale.service.tm.TMFixedFieldSpec;
import havis.middleware.ale.service.tm.TMSpec;
import havis.middleware.ale.service.tm.TMVariableFieldListSpec;
import havis.middleware.ale.service.tm.TMVariableFieldSpec;
import havis.net.rest.middleware.tm.TMAsync;
import havis.net.ui.middleware.client.shared.TMFieldNamesCallback;
import havis.net.ui.middleware.client.shared.event.ItemsLoadedEvent;
import havis.net.ui.middleware.client.shared.event.MessageEvent;
import havis.net.ui.middleware.client.utils.Utils;

public class TMSpecStorage extends BaseSpecStorage<MCTagMemorySpec> {

	interface Codec extends JsonEncoderDecoder<MCTagMemorySpec> {}
	
	private String newFieldName;
	private String newSpecName;
	
	@Inject
	public TMSpecStorage(TMAsync service) {
		super((Codec) GWT.create(Codec.class));
		setSpecs(service.getSpecs());
	}

	/**
	 * Returns the list of TMFieldNames grouped by Tag Memory. The method
	 * {@link #fetchFieldNames(EventBus, TMFieldNamesCallback)} has to be called first.
	 * 
	 * @return TMFieldNames
	 */
	public Map<String, List<String>> getFieldNames() {
		Collection<MCTagMemorySpec> tmSpecs = getList();
		Map<String, List<String>> values = new HashMap<String, List<String>>();
		if (tmSpecs != null) {
			for (MCTagMemorySpec spec : tmSpecs) {
				if (!Utils.isNullOrEmpty(spec.getId())) {
					if (spec.getSpec() != null) {
						TMSpec tmSpec = spec.getSpec();
						values.put(spec.getName(), new ArrayList<String>());
						if (tmSpec instanceof TMFixedFieldListSpec) {
							for (TMFixedFieldSpec f : ((TMFixedFieldListSpec) tmSpec).getFixedFields().getFixedField())
								values.get(spec.getName()).add(f.getFieldname());
						} else if (tmSpec instanceof TMVariableFieldListSpec) {
							for (TMVariableFieldSpec f : ((TMVariableFieldListSpec) tmSpec).getVariableFields()
									.getVariableField())
								values.get(spec.getName()).add(f.getFieldname());
						}
					}
				}
			}
		}
		return values;
	}

	/**
	 * Returns the list of fixed TMFields grouped by Tag Memory. The method
	 * {@link #fetchFieldNames(EventBus, TMFieldNamesCallback)} has to be called first.
	 *
	 * @return fixed TMFields
	 */
	public Map<String, List<TMFixedFieldSpec>> getFixedFields() {
		Collection<MCTagMemorySpec> tmSpecs = getList();
		Map<String, List<TMFixedFieldSpec>> values = new HashMap<String, List<TMFixedFieldSpec>>();
		if (tmSpecs != null) {
			for (MCTagMemorySpec spec : tmSpecs) {
				if (!Utils.isNullOrEmpty(spec.getId())) {
					if (spec.getSpec() != null) {
						TMSpec tmSpec = spec.getSpec();
						if (tmSpec instanceof TMFixedFieldListSpec) {
							values.put(spec.getName(), new ArrayList<TMFixedFieldSpec>());												
							for (TMFixedFieldSpec f : ((TMFixedFieldListSpec) tmSpec).getFixedFields().getFixedField())
								values.get(spec.getName()).add(f);
						}  
					}
				}
			}
		}
		return values;
	}
	
	/**
	 * Returns the list of variable TMFields grouped by Tag Memory. The method
	 * {@link #fetchFieldNames(EventBus, TMFieldNamesCallback)} has to be called first.
	 * 
	 * @return variable TMFields
	 */
	public Map<String, List<TMVariableFieldSpec>> getVariableFields() {
		Collection<MCTagMemorySpec> tmSpecs = getList();
		Map<String, List<TMVariableFieldSpec>> values = new HashMap<String, List<TMVariableFieldSpec>>();
		if (tmSpecs != null) {
			for (MCTagMemorySpec spec : tmSpecs) {
				if (!Utils.isNullOrEmpty(spec.getId())) {
					if (spec.getSpec() != null) {
						TMSpec tmSpec = spec.getSpec();
						if (tmSpec instanceof TMVariableFieldListSpec) {
							values.put(spec.getName(), new ArrayList<TMVariableFieldSpec>());												
							for (TMVariableFieldSpec f : ((TMVariableFieldListSpec) tmSpec).getVariableFields().getVariableField())
								values.get(spec.getName()).add(f);
						}  
					}
				}
			}
		}
		return values;
	}

	@Override
	public void cleanSpec(MCTagMemorySpec spec) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Gets the user defined field TM names
	 */
	public void fetchFieldNames(final EventBus eventBus, final TMFieldNamesCallback callback ){
		ItemsLoadedEvent.register(eventBus, this, new ItemsLoadedEvent.Handler() {
			@Override
			public void onItemsLoaded(ItemsLoadedEvent event) {
				Map<String, List<String>> map = getFieldNames();
				callback.onGotTMFieldNames(map);
			}
			@Override
			public void onFailure(ItemsLoadedEvent event, String message) {
				if(eventBus != null)
					eventBus.fireEventFromSource(new MessageEvent(MessageEvent.MessageType.ERROR, message), this);
			}
		});
		loadList(this, false);
	}

	/**
	 * @return the newFieldName
	 */
	public String getNewFieldName() {
		return newFieldName;
	}

	/**
	 * @param newFieldName the newFieldName to set
	 */
	public void setNewFieldName(String newFieldName) {
		this.newFieldName = newFieldName;
	}

	/**
	 * @return the newSpecName
	 */
	public String getNewSpecName() {
		return newSpecName;
	}

	/**
	 * @param newSpecName the newSpecName to set
	 */
	public void setNewSpecName(String newSpecName) {
		this.newSpecName = newSpecName;
	}

}
