package havis.net.ui.middleware.client.shared.storage;

import java.util.List;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import havis.middleware.ale.service.mc.MCSpec;
import havis.net.rest.middleware.shared.HasSubscribers;
import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;

public abstract class CycleSpecStorage<T extends MCSpec> extends BaseSpecStorage<T> {

	private HasSubscribers service;
	
	public CycleSpecStorage(JsonEncoderDecoder<T> codec) {
		super(codec);
	}

	protected void setSubscribersService(HasSubscribers service) {
		this.service = service;
	}
	
	public HasSubscribers getSubscribersService() {
		return service;
	}

	public abstract List<String> getTriggerList(CommonEditorPlace place, T spec);
	public abstract void resetTriggerList(CommonEditorPlace place, String specId);
}
