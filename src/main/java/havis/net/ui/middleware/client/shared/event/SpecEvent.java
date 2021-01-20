package havis.net.ui.middleware.client.shared.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class SpecEvent extends GwtEvent<SpecEvent.Handler> {
	
	public enum Action {
		EDIT,
		DELETE,
		EXPORT,
		RUN
	}

	public interface Handler extends EventHandler {
		void onSpecEvent(SpecEvent event);
	}
	
	private static final Type<SpecEvent.Handler> TYPE = new Type<>();
	
	private String id;
	private Action action;
	
	public interface HasHandlers {
		HandlerRegistration addSpecEventHandler(SpecEvent.Handler handler);
	}
	
	public SpecEvent(String id, Action action) {
		this.id = id;
		this.action = action;
	}
	
	public String getId() {
		return id;
	}
	
	public Action getAction() {
		return action;
	}
	
	@Override
	public Type<SpecEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SpecEvent.Handler handler) {
		handler.onSpecEvent(this);
	}

	public static Type<SpecEvent.Handler> getType(){
		return TYPE;
	}
}
