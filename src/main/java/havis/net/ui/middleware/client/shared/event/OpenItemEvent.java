package havis.net.ui.middleware.client.shared.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class OpenItemEvent extends GwtEvent<OpenItemEvent.Handler> {

	public interface Handler extends EventHandler {
		void onOpenItem(OpenItemEvent event);
	}
	
	public interface HasHandlers {
		HandlerRegistration addOpenItemHandler(OpenItemEvent.Handler handler);
	}
	
	private static final Type<OpenItemEvent.Handler> TYPE = new Type<>();
	private int index;
	private int column; //from left to right
	
	public OpenItemEvent(int index, int column) {
		this.index = index;
		this.column = column;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getColumn() {
		return column;
	}
	
	@Override
	public Type<OpenItemEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(OpenItemEvent.Handler handler) {
		handler.onOpenItem(this);
	}

	public static Type<OpenItemEvent.Handler> getType(){
		return TYPE;
	}
}
