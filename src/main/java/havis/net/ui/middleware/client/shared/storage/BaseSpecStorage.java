package havis.net.ui.middleware.client.shared.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fusesource.restygwt.client.JsonEncoderDecoder;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.Scheduler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import havis.middleware.ale.service.ECFilterListMember;
import havis.middleware.ale.service.mc.MCSpec;
import havis.net.rest.middleware.shared.HasSpecs;
import havis.net.rest.shared.data.SerializableValue;
import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;
import havis.net.ui.middleware.client.shared.event.ItemsLoadedEvent;
import havis.net.ui.middleware.client.shared.event.SpecLoadedEvent;
import havis.net.ui.middleware.client.shared.event.SpecSavedEvent;
import havis.net.ui.middleware.client.utils.Utils;

public abstract class BaseSpecStorage<T extends MCSpec> {

	@Inject
	private EventBus eventBus;

	private HasSpecs<T> specs;
	private Map<String, T> cache = new HashMap<>();
	private JsonEncoderDecoder<T> codec;
	private String clone;

	public BaseSpecStorage(JsonEncoderDecoder<T> codec) {
		this.codec = codec;
	}

	public void clear() {
		cache.clear();
	}

	protected void setSpecs(HasSpecs<T> specs) {
		this.specs = specs;
	}

	public void loadList(final Object source) {
		loadList(source, false);
	}

	/**
	 * Loads all specs. Will fire a {@link ItemsLoadedEvent} when all specs have
	 * been loaded.
	 * 
	 * @param source
	 *            The source for the event.
	 * @param force
	 *            If true the list will be reloaded despite it is already in
	 *            cache
	 */
	public void loadList(final Object source, boolean force) {
		if (cache != null && !cache.isEmpty() && !force) {
			// specs are already loaded.
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					// fire event
					eventBus.fireEventFromSource(new ItemsLoadedEvent(), source);
				}
			});
		} else {
			cache = new HashMap<String, T>();
			// Load specs
			specs.getList(new MethodCallback<List<T>>() {

				@Override
				public void onFailure(Method method, Throwable exception) {
					cache = null;
					eventBus.fireEventFromSource(new ItemsLoadedEvent(true, Utils.getReason(exception)), source);
				}

				@Override
				public void onSuccess(Method method, List<T> response) {
					// save the logical readers in cache
					if (response != null) {
						for (T spec : response) {
							if (!cache.containsKey(spec.getId()))
								cache.put(spec.getId(), spec);
						}
					}
					// fire event
					eventBus.fireEventFromSource(new ItemsLoadedEvent(), source);
				}
			});
		}
	}

	/**
	 * Returns the list of specs. The method {@link #loadList(Object)} has to be
	 * called first.
	 * 
	 * @return list of specs
	 */
	public Collection<T> getList() {
		if (cache == null)
			return null;
		return cache.values();
	}

	public void loadSpec(final CommonEditorPlace place, final Object source) {
		loadSpec(place, source, false);
	}

	/**
	 * Loads the ECSpec with the specified id. Will fire a
	 * {@link SpecLoadedEvent} when the ECSpec has been loaded.
	 * 
	 * @param place
	 *            The place 
	 * @param source
	 *            The source for the event.
	 * @param force
	 *            If true the spec will be reloaded despite it is already in
	 *            cache
	 */
	public void loadSpec(final CommonEditorPlace place, final Object source, boolean force) {
		// uuid
		final String id = place.getSpecId();

		if ((Utils.isNullOrEmpty(id) || Utils.isNewId(id) || (cache != null && cache.containsKey(id) && cache.get(id) != null))
				&& (!force || Utils.isNewId(id))) {
			// id is an ALE uuid or is undefined or the specified spec is
			// already loaded.
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {

					Object result = getInitializedSpec(place, cache.get(id));
					// fire event
					eventBus.fireEventFromSource(new SpecLoadedEvent(result), source);
				}
			});
		} else {
			// loading spec
			specs.get(id, new MethodCallback<T>() {

				@Override
				public void onFailure(Method method, Throwable exception) {
					eventBus.fireEventFromSource(new SpecLoadedEvent(true, Utils.getReason(exception)), source);
				}

				@Override
				public void onSuccess(Method method, T response) {

					Object result = getInitializedSpec(place, response);
					// update cache
					cache.put(id, response);
					// fire event
					eventBus.fireEventFromSource(new SpecLoadedEvent(result), source);
				}
			});
		}
	}

	protected boolean isListEmpty(Object spec) {
		List<?> list = (List<?>) spec;
		return (list == null || list.isEmpty());
	}

	public abstract void cleanSpec(T spec);

	public void saveSpec(final T spec, final Object source) {
		if (Utils.isNullOrEmpty(spec.getId())) {
			specs.add(spec, new MethodCallback<SerializableValue<String>>() {

				@Override
				public void onFailure(Method method, Throwable exception) {
					eventBus.fireEventFromSource(new SpecSavedEvent(true, Utils.getReason(exception)), source);
				}

				@Override
				public void onSuccess(Method method, SerializableValue<String> response) {
					eventBus.fireEventFromSource(new SpecSavedEvent(response.getValue(), true), source);
				}
			});
		} else {
			specs.set(spec.getId(), spec, new MethodCallback<Void>() {

				@Override
				public void onSuccess(Method method, Void response) {
					eventBus.fireEventFromSource(new SpecSavedEvent(spec.getId(), false), source);
				}

				@Override
				public void onFailure(Method method, Throwable exception) {
					eventBus.fireEventFromSource(new SpecSavedEvent(true, Utils.getReason(exception)), source);
				}
			});
		}

	}

	public void removeEmptyLists(CommonEditorPlace place, Object spec) {

	}

	/**
	 * Saves the given spec into the cache
	 * 
	 * @param id
	 * @param spec
	 */
	public void setSpec(String id, T spec) {
		cache.put(id, spec);
	}

	public T getSpec(String id) {
		return cache.get(id);
	}

	protected Object getInitializedSpec(CommonEditorPlace place, T spec) {
		return spec;
	}

	/**
	 * Checks integrity of List<ECFilterListMember>
	 * 
	 * @param theFilterList
	 * @return theFilterList.isEmpty()
	 */
	protected boolean canResetFilterSpec(List<ECFilterListMember> theFilterList) {
		List<ECFilterListMember> toDelete = new ArrayList<ECFilterListMember>();
		for (ECFilterListMember ecflm : theFilterList) {
			if (ecflm.getPatList() == null || ecflm.getPatList().getPat().isEmpty()) {
				// / No fieldname or pattern found
				toDelete.add(ecflm);
			}
		}

		for (ECFilterListMember ecflm : toDelete) {
			// delete incomplete objects
			theFilterList.remove(ecflm);
		}
		return theFilterList.isEmpty();
	}

	/**
	 * Converts the given MCEventCycleSpec object to an internal string.
	 * 
	 * @param spec The MCEventCycleSpec
	 */
	public void clone(T spec) {
		clone = codec.encode(spec).toString();
	}

	/**
	 * Restores the saved MCEventCycleSpec object to MCEventCycleSpec and
	 * empties the string
	 * 
	 * @return the newSpec
	 */
	public T getClone() {
		clone = clone.replace("\"schemaVersion\":\"1.1\"", "\"schemaVersion\":1.1");
		T newSpec = codec.decode(clone);
		clone = "";
		return newSpec;
	}

	public String getJSONString(T spec) {
		return codec.encode(spec).toString();
	}
	/**
	 * Empties the string
	 */
	public void resetClone() {
		clone = "";
	}
}
