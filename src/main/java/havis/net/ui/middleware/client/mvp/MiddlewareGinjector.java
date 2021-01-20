package havis.net.ui.middleware.client.mvp;

import havis.net.ui.middleware.client.AppLayout;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;

@GinModules(MiddlewareGinModule.class)
public interface MiddlewareGinjector extends Ginjector {
	PlaceHistoryHandler getPlaceHistoryHandler();
	AppLayout getAppLayout();
	PlaceController getPlaceController();
}
