package havis.net.ui.middleware.client.shared.event;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class MessageEvent extends Event<MessageEvent.Handler> {

	public interface Handler {
		void onMessage(MessageEvent event);
	}
	
	public enum MessageType{
		INFO,
		WARNING,
		ERROR;
	}
	
	private static final Type<MessageEvent.Handler> TYPE = new Type<>();
	
	public static HandlerRegistration register(EventBus eventBus, Object source, MessageEvent.Handler handler) {
		return eventBus.addHandlerToSource(TYPE, source, handler);
	}
	
	private String message;
	private MessageType messageType;
	
	public MessageEvent(MessageType messageType, String message) {
		this.message = message;
		this.messageType = messageType;
	}
		
	public String getMessage() {
		return message;
	}
	
	public MessageType getMessageType() {
		return messageType;
	}
	
	@Override
	public Type<MessageEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MessageEvent.Handler handler) {		
		handler.onMessage(this);
	}

	public static Type<MessageEvent.Handler> getType(){
		return TYPE;
	}
}
