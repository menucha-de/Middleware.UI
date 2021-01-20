package havis.net.ui.middleware.client.shared.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class EnableSpecEvent extends GwtEvent<EnableSpecEvent.Handler> {

	public interface Handler extends EventHandler {
		void onEnableSpec(EnableSpecEvent event);
	}

	public interface HasHandlers {
		HandlerRegistration addEnableSpecHandler(EnableSpecEvent.Handler handler);
	}

	private static final Type<EnableSpecEvent.Handler> TYPE = new Type<>();
	
	private String id;
	private boolean enable;
	
	public EnableSpecEvent(String id, boolean enable) {
		this.id = id;
		this.enable = enable;
	}
	
	public String getId() {
		return id;
	}
	
	public boolean getEnable() {
		return enable;
	}
	
	@Override
	public Type<EnableSpecEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EnableSpecEvent.Handler handler) {
		handler.onEnableSpec(this);
	}
	
	public static Type<EnableSpecEvent.Handler> getType(){
		return TYPE;
	}
}
