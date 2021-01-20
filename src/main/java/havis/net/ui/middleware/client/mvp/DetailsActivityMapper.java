package havis.net.ui.middleware.client.mvp;

import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.SubscriberPlace;
import havis.net.ui.middleware.client.place.TriggerPlace;
import havis.net.ui.middleware.client.place.ds.AssociationEntriesEditorPlace;
import havis.net.ui.middleware.client.place.ds.CachePatternEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterItemEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterPatternItemEditorPlace;
import havis.net.ui.middleware.client.place.ec.GroupPatternItemEditorPlace;
import havis.net.ui.middleware.client.place.ec.OutputFieldItemEditorPlace;
import havis.net.ui.middleware.client.place.pc.OpSpecEditorPlace;
import havis.net.ui.middleware.client.place.pc.PcReportSpecItemEditorPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

public class DetailsActivityMapper implements ActivityMapper {
	
	@Inject
	@Named("lr-editor")
	private Provider<Activity> lrEditorProvider;
	
	@Inject
	@Named("tm-editor")
	private Provider<Activity> tmEditorProvider;
	
	@Inject
	@Named("ec-editor")
	private Provider<Activity> ecEditorProvider;
	
	@Inject
	@Named("pc-editor")
	private Provider<Activity> pcEditorProvider;
	
	@Inject
	@Named("dsca-editor")
	private Provider<Activity> dscaEditorProvider;
	
	@Inject
	@Named("dsas-editor")
	private Provider<Activity> dsasEditorProvider;
	
	@Inject
	@Named("dsrn-editor")
	private Provider<Activity> dsrnEditorProvider;
	
	@Inject
	@Named("cc-editor")
	private Provider<Activity> ccEditorProvider;
	
	@Inject
	@Named("pc-rep-editor")
	private Provider<Activity> pcReportProvider;
	
	@Inject
	@Named("pc-opspec-editor")
	private Provider<Activity> pcOpSpecItemEditorProvider;
	
	@Inject
	@Named("cc-opspec-editor")
	private Provider<Activity> ccOpSpecItemEditorProvider;
	
	
	@Inject
	@Named("sub-editor")
	private Provider<Activity> subEditorProvider;
	
	@Inject
	@Named("ec-reps-editor")
	private Provider<Activity> ecRepEditorProvider;
	
	@Inject
	@Named("pc-reps-editor")
	private Provider<Activity> pcRepEditorProvider;
	
	@Inject
	@Named("ec-rep-editor")
	private Provider<Activity> ecReportProvider;
	
	@Inject
	@Named("rep-outfield-editor")
	private Provider<Activity> reportOutputFieldProvider;
	
	@Inject
	@Named("rep-filter-editor")
	private Provider<Activity> reportFilterProvider;
	
	@Inject
	@Named("rep-grouppattern-editor")
	private Provider<Activity> reportGroupPatternProvider;
	
	@Inject
	@Named("rep-filterpattern-editor")
	private Provider<Activity> reportFilterPatternProvider;
	
	@Inject
	@Named("tr-editor")
	private Provider<Activity> triggerEditorProvider;
	
	@Inject
	@Named("cc-cmdspec-editor")
	private Provider<Activity> cmdSpecItemEditorProvider;
	
	@Inject
	@Named("cc-reps-editor")
	private Provider<Activity> ccRepEditorProvider;
	
	@Inject
	@Named("ds-cachepattern-editor")
	private Provider<Activity> dsCachePatternProvider;
	
	@Inject
	@Named("ds-associationentries-editor")
	private Provider<Activity> dsAssociationEntriesProvider;
	
	
//	@Inject
//	private PlaceController placeController;
//	private Activity currentActivity;
	
//	public DetailsActivityMapper(){
//		
//		History.addValueChangeHandler(new ValueChangeHandler<String>() {
//			@Override
//			public void onValueChange(ValueChangeEvent<String> event) {
//				GWT.log("--------onValueChange--------");
//				//onValueChange is fired only if the forward/backward browser buttons will be clicked
//				String oldPlace =  placeController.getWhere().toString();
//				String newPlace = event.getValue();
//				
//				if(!Utils.isNullOrEmpty(oldPlace) && !Utils.isNullOrEmpty(newPlace)){
//					String[] oldPlaceArr = oldPlace.split(":");
//					String[] newPlaceArr = newPlace.split(":");
//					
//					if(oldPlaceArr != null && oldPlaceArr.length > 3 &&
//						newPlaceArr != null &&  newPlaceArr.length > 3){
//						
//						String[] oldId = oldPlaceArr[3].split(",");
//						String[] newId = newPlaceArr[3].split(",");
//						
//						if(oldId != null && newId != null){
//							boolean isHierarchyUp = oldId.length > newId.length;
//							GWT.log("1-ALT: " + oldPlace);
//							GWT.log("2-NEU: " + newPlace);
//							
//							
//							if(currentActivity != null){
//								GWT.log("currentActivity: " + currentActivity.toString());
//								
//								BaseActivity act = (BaseActivity)currentActivity;
//								//act.onHierarchyChange(isHierarchyUp);
//							}
//						}
//					}
//				}
//			}
//		});
//		
//	}

	
	@Override
	public Activity getActivity(Place place) {
		
		if (place instanceof EditorPlace) {
			EditorPlace editorPlace = (EditorPlace) place;
			EditorType type = editorPlace.getEditorType();

			switch (type) {
				case LR:
					return lrEditorProvider.get();
				case TM:
					return tmEditorProvider.get();
				case EC:
					return ecEditorProvider.get();
				case ECREP:
					return ecRepEditorProvider.get();
				case PC:
					return pcEditorProvider.get();
				case PCREP:
					return pcRepEditorProvider.get();
				case CC:
					return ccEditorProvider.get();	
				case CCREP:
					return ccRepEditorProvider.get();	
				case DSCA:
					return dscaEditorProvider.get();
				case DSAS:
					return dsasEditorProvider.get();
				case DSRN:
					return dsrnEditorProvider.get();
				
				default:
					break;
			}

		} else if (place instanceof TriggerPlace) {
			return triggerEditorProvider.get();
		} else if (place instanceof SubscriberPlace) {
			return subEditorProvider.get();
		} else if (place instanceof havis.net.ui.middleware.client.place.ec.ECReportSpecEditorPlace) {
			return ecReportProvider.get();
			
		} else if (place instanceof OutputFieldItemEditorPlace) {
			return reportOutputFieldProvider.get();	
		} else if (place instanceof GroupPatternItemEditorPlace) {
			return reportGroupPatternProvider.get();	
		} else if (place instanceof FilterItemEditorPlace) {
			return reportFilterProvider.get();	
		}else if (place instanceof FilterPatternItemEditorPlace) {
			return reportFilterPatternProvider.get();	
		
		} else if (place instanceof PcReportSpecItemEditorPlace) {
			return pcReportProvider.get();
		} else if (place instanceof OpSpecEditorPlace) {
			OpSpecEditorPlace opSpecItemEditorPlace = (OpSpecEditorPlace) place;
			EditorType type = opSpecItemEditorPlace.getEditorType();
			switch (type) {
				case PC:
					return pcOpSpecItemEditorProvider.get();
				case CC:
					return ccOpSpecItemEditorProvider.get();
				default:
					break;
			}
		} else if (place instanceof havis.net.ui.middleware.client.place.cc.CCCmdSpecEditorPlace) {
			return cmdSpecItemEditorProvider.get();
		} else if (place instanceof CachePatternEditorPlace) {
			return dsCachePatternProvider.get();
		} else if (place instanceof AssociationEntriesEditorPlace) {
			return dsAssociationEntriesProvider.get();
		}
		
		
		return null;
	}
	
}
