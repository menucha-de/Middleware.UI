package havis.net.ui.middleware.client.shared.event;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class SpecAddedEvent extends Event<SpecAddedEvent.Handler> {

	public interface Handler {
		void onSpecAdded(SpecAddedEvent event);
	}

	private static final Type<SpecAddedEvent.Handler> TYPE = new Type<SpecAddedEvent.Handler>();

	public static HandlerRegistration register(EventBus eventBus, SpecAddedEvent.Handler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private String id;

	public String getId() {
		return id;
	}

	public SpecAddedEvent(String id) {
		this.id = id;
	}

	public SpecAddedEvent(boolean failed, String message) {
	}

	@Override
	public Type<SpecAddedEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onSpecAdded(this);
	}

	public static Type<SpecAddedEvent.Handler> getType() {
		return TYPE;
	}
}
