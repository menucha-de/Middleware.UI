package havis.net.ui.middleware.client.shared.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class NewItemEvent extends GwtEvent<NewItemEvent.Handler> {

	private String name;
	
	
	public NewItemEvent() {
		this.name = null;
	}
	
	public NewItemEvent(String name) {
		this.name = name;
	}
	
	public interface Handler extends EventHandler {
		void onNewItem(NewItemEvent event);
	}
	
	public interface HasHandlers {
		HandlerRegistration addNewItemHandler(NewItemEvent.Handler handler);
	}
	
	private static final Type<NewItemEvent.Handler> TYPE = new Type<>();
	
	
	public String getName() {
		return name;
	}
	
	
	@Override
	public Type<NewItemEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NewItemEvent.Handler handler) {
		handler.onNewItem(this);
	}

	public static Type<NewItemEvent.Handler> getType(){
		return TYPE;
	}
}
