package havis.net.ui.middleware.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import havis.net.ui.middleware.client.place.HasListType;
import havis.net.ui.middleware.client.place.ListType;

public class DataSourceActivityMapper implements ActivityMapper {

	private ListType currentListType;
	private Activity currentListActivity;

	@Inject
	@Named("dsrn-list")
	private Provider<Activity> dsrnProvider;

	@Inject
	@Named("dsca-list")
	private Provider<Activity> dscaProvider;

	@Inject
	@Named("dsas-list")
	private Provider<Activity> dsasProvider;

	@Override
	public Activity getActivity(Place place) {
		if (place instanceof HasListType) {
			ListType listType = ((HasListType) place).getListType();
			if (listType == ListType.DSCA || listType == ListType.DSAS || listType == ListType.DSRN) {
				return getActivity(listType);
			}
		}
		return null;
	}

	private Activity getActivity(ListType type) {
		if (currentListType != type) {
			currentListType = type;
			switch (type) {
			case DSCA:
				currentListActivity = dscaProvider.get();
				break;
			case DSAS:
				currentListActivity = dsasProvider.get();
				break;
			case DSRN:
				currentListActivity = dsrnProvider.get();
				break;
			default:
				return null;
			}
		}
		return currentListActivity;
	}

}
