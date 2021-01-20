package havis.net.ui.middleware.client.shared;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import havis.net.ui.middleware.client.shared.event.MessageEvent;
import havis.net.ui.middleware.client.utils.Utils;

public abstract class BaseActivity extends AbstractActivity {
	
	private EventBus eventBus;
	
	@Inject
	private PlaceController placeController;
	
	protected EventBus getEventBus() {
		return eventBus;
	}

	protected void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	public void showException(Throwable exception) {
		assert eventBus != null;
		eventBus.fireEventFromSource(new MessageEvent(MessageEvent.MessageType.ERROR, Utils.getReason(exception)), this);
	}
	
	public void showErrorMessage(String message) {
		assert eventBus != null;
		eventBus.fireEventFromSource(new MessageEvent(MessageEvent.MessageType.ERROR, message), this);
	}

	public abstract void start(AcceptsOneWidget panel, EventBus eventBus);
	
	@Override
	public void start(AcceptsOneWidget panel, com.google.gwt.event.shared.EventBus eventBus) {
		start(panel, (EventBus) eventBus);
	}

	protected void goTo(Place place) {
		placeController.goTo(place);
	}
}
