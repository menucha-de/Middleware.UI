package havis.net.ui.middleware.client.shared.event;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class SpecLoadedEvent extends Event<SpecLoadedEvent.Handler> {

	public interface Handler {
		void onSpecLoaded(SpecLoadedEvent event);

		void onFailure(SpecLoadedEvent event, String message);
	}

	private static final Type<SpecLoadedEvent.Handler> TYPE = new Type<SpecLoadedEvent.Handler>();

	public static HandlerRegistration register(EventBus eventBus, Object obj, SpecLoadedEvent.Handler handler) {
		return eventBus.addHandlerToSource(TYPE, obj, handler);
	}

	private Object spec;
	private boolean failed;
	private String message;

	public Object getSpec() {
		return spec;
	}

	public SpecLoadedEvent(Object spec) {
		this.spec = spec;
	}

	public SpecLoadedEvent(boolean failed, String message) {
		this.failed = failed;
		this.message = message;
	}

	@Override
	public Type<SpecLoadedEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		if (failed)
			handler.onFailure(this, message);
		else
			handler.onSpecLoaded(this);
	}

	public static Type<SpecLoadedEvent.Handler> getType() {
		return TYPE;
	}
}
