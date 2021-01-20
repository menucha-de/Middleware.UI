package havis.net.ui.middleware.client.shared.event;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

import havis.net.ui.middleware.client.shared.SectionType;

public class ServiceEvent extends Event<ServiceEvent.Handler> {
	
	public interface Handler {
		void onServiceEvent(ServiceEvent event);
	}
	
	private static final Type<ServiceEvent.Handler> TYPE = new Type<>();
	
	public interface HasHandlers {
		HandlerRegistration addServiceEventHandler(ServiceEvent.Handler handler);
	}

	public static HandlerRegistration register(EventBus eventBus, ServiceEvent.Handler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private SectionType sectionType;

	public ServiceEvent(SectionType sectionType) {
		this.sectionType = sectionType;
	}
	
	public SectionType getSectionType() {
		return sectionType;
	}
	
	@Override
	public Type<ServiceEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ServiceEvent.Handler handler) {
		handler.onServiceEvent(this);
	}

	public static Type<ServiceEvent.Handler> getType(){
		return TYPE;
	}
}
