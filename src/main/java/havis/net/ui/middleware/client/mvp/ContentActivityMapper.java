package havis.net.ui.middleware.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import havis.net.ui.middleware.client.place.CommonPlace;
import havis.net.ui.middleware.client.place.HasListType;
import havis.net.ui.middleware.client.place.ListType;

public class ContentActivityMapper implements ActivityMapper {
	
	private ListType currentListType;
	private Activity currentListActivity;
	
	@Inject
	@Named("common")
	private Activity commonActivity;
	
	@Inject
	@Named("lr-list")
	private Provider<Activity> lrProvider;
	
	@Inject
	@Named("tm-list")
	private Provider<Activity> tmProvider;
	
	@Inject
	@Named("ec-list")
	private Provider<Activity> ecProvider;
	
	@Inject
	@Named("cc-list")
	private Provider<Activity> ccProvider;
	
	@Inject
	@Named("pc-list")
	private Provider<Activity> pcProvider;
		
	@Inject
	@Named("ds-select")
	private Provider<Activity> dsProvider;
	
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
			return getActivity(((HasListType) place).getListType());
		} else if (place instanceof CommonPlace) {
			return commonActivity;
		}
		
		return null;
	}
	
	private Activity getActivity(ListType type) {
		if (currentListType != type) {
			currentListType = type;
			switch(type) {
			case LR:
				currentListActivity = lrProvider.get();
				break;
			case TM:
				currentListActivity = tmProvider.get();
				break;
			case EC:
				currentListActivity = ecProvider.get();
				break;
			case CC:
				currentListActivity = ccProvider.get();
				break;
			case PC:
				currentListActivity = pcProvider.get();
				break;
			case DS:
				currentListActivity = dsProvider.get();
				break;
			case DSCA:
			case DSAS:
			case DSRN:
				currentListActivity = dsProvider.get();
				break;	
			default:
				return null;
			}
		}
		return currentListActivity;
	}
}
