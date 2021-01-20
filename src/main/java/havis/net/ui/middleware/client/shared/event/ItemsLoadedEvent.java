package havis.net.ui.middleware.client.shared.event;

import java.util.List;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class ItemsLoadedEvent extends Event<ItemsLoadedEvent.Handler> {

	public interface Handler {
		void onItemsLoaded(ItemsLoadedEvent event);

		void onFailure(ItemsLoadedEvent event, String message);
	}

	public interface HasHandlers {
		HandlerRegistration addItemsLoadedHandler(ItemsLoadedEvent.Handler handler);
	}

	private static final Type<ItemsLoadedEvent.Handler> TYPE = new Type<>();

	public static HandlerRegistration register(EventBus eventBus, Object source, ItemsLoadedEvent.Handler handler) {
		return eventBus.addHandlerToSource(TYPE, source, handler);
	}

	private List<String> items;
	private boolean failed;
	private String message;

	public List<String> getItems() {
		return items;
	}

	public ItemsLoadedEvent() {

	}

	public ItemsLoadedEvent(boolean failed, String message) {
		this.failed = failed;
		this.message = message;
	}

	public ItemsLoadedEvent(List<String> items) {
		this.items = items;
	}

	@Override
	public Type<ItemsLoadedEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ItemsLoadedEvent.Handler handler) {
		if (failed)
			handler.onFailure(this, message);
		else
			handler.onItemsLoaded(this);
	}

	public static Type<ItemsLoadedEvent.Handler> getType() {
		return TYPE;
	}
}
