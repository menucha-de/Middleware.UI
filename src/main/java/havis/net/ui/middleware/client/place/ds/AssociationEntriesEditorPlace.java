package havis.net.ui.middleware.client.place.ds;

import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterItemEditorPlace;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class AssociationEntriesEditorPlace extends CommonEditorPlace {
	
	/**
	 * c'tor used by updateOpenWidgetId()
	 * @param splittedToken
	 */
	public AssociationEntriesEditorPlace(String[] splittedToken){
		super(splittedToken);
	}
	
	public AssociationEntriesEditorPlace(FilterItemEditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(place.getListType(), place.getEditorType(), null, place.getPathList(), place.getOpenWidgetIdList(), isNew, index, openWidgetId);
	}
	
	
	public AssociationEntriesEditorPlace(EditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(place.getListType(), place.getEditorType(), null, place.getPathList(), place.getOpenWidgetIdList(), isNew, index, openWidgetId);
	}

	
	@Prefix("dsaEntryItem")
	public static class Tokenizer implements PlaceTokenizer<AssociationEntriesEditorPlace> {
		@Override
		public AssociationEntriesEditorPlace getPlace(String token) {
			//token does not contain the prefix
			String[] splitted = token.split(":");
			if (splitted.length == 4) {
				return new AssociationEntriesEditorPlace(splitted);
			} else {
				return null;
			}
		}
		
		@Override
		public String getToken(AssociationEntriesEditorPlace place) {
			String token = place.getListType().toString();
			token += ":" + place.getEditorType().toString();
			token += ":" + place.getPath();
			token += ":" + place.getOpenWidgetIds();
			return token;
		}
	}
	
}
