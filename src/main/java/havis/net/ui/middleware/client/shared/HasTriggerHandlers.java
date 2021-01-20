package havis.net.ui.middleware.client.shared;

import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;

public interface HasTriggerHandlers {
	void addNewStartTriggerHandler(NewItemEvent.Handler handler);
	void addNewStopTriggerHandler(NewItemEvent.Handler handler);
	void setOpenStartTriggerHandler(OpenItemEvent.Handler handler);
	void setOpenStopTriggerHandler(OpenItemEvent.Handler handler);
}
