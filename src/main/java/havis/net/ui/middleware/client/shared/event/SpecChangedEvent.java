package havis.net.ui.middleware.client.shared.event;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class SpecChangedEvent extends Event<SpecChangedEvent.Handler> {

	public interface Handler {
		void onSpecChanged(SpecChangedEvent event);
	}

	private static final Type<SpecChangedEvent.Handler> TYPE = new Type<SpecChangedEvent.Handler>();

	public static HandlerRegistration register(EventBus eventBus, SpecChangedEvent.Handler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private String id;

	public String getId() {
		return id;
	}

	public SpecChangedEvent(String id) {
		this.id = id;
	}

	public SpecChangedEvent(boolean failed, String message) {
	}

	@Override
	public Type<SpecChangedEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onSpecChanged(this);
	}

	public static Type<SpecChangedEvent.Handler> getType() {
		return TYPE;
	}
}
