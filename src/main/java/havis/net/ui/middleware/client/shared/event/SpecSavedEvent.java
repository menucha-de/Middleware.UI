package havis.net.ui.middleware.client.shared.event;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class SpecSavedEvent extends Event<SpecSavedEvent.Handler> {

	public interface Handler {
		void onSpecSaved(SpecSavedEvent event);

		void onFailure(SpecSavedEvent event, String message);
	}

	private static final Type<SpecSavedEvent.Handler> TYPE = new Type<SpecSavedEvent.Handler>();

	public static HandlerRegistration register(EventBus eventBus, Object obj, SpecSavedEvent.Handler handler) {
		return eventBus.addHandlerToSource(TYPE, obj, handler);
	}

	private boolean failed;
	private String message;
	private String id;
	private boolean newSpec;
	
	public SpecSavedEvent(String id, boolean newSpec) {
		this.id = id;
		this.newSpec = newSpec;
	}
	
	public SpecSavedEvent(boolean failed, String message) {
		this.failed = failed;
		this.message = message;
	}
	
	public String getId() {
		return id;
	}

	public boolean isNewSpec() {
		return newSpec;
	}
	
	@Override
	public Type<SpecSavedEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		if (failed)
			handler.onFailure(this, message);
		else
			handler.onSpecSaved(this);
	}

	public static Type<SpecSavedEvent.Handler> getType() {
		return TYPE;
	}
}
