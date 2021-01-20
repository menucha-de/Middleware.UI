package havis.net.ui.middleware.client.place.ec;

import havis.net.ui.middleware.client.place.EditorPlace;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class FilterPatternItemEditorPlace extends CommonEditorPlace {
	
	/**
	 * c'tor used by updateOpenWidgetId()
	 * @param splittedToken
	 */
	public FilterPatternItemEditorPlace(String[] splittedToken){
		super(splittedToken);
	}
	
	public FilterPatternItemEditorPlace(FilterItemEditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(place.getListType(), place.getEditorType(), null, place.getPathList(), place.getOpenWidgetIdList(), isNew, index, openWidgetId);
	}
	
	
	public FilterPatternItemEditorPlace(EditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(place.getListType(), place.getEditorType(), null, place.getPathList(), place.getOpenWidgetIdList(), isNew, index, openWidgetId);
	}

	
	@Prefix("fltPatternItem")
	public static class Tokenizer implements PlaceTokenizer<FilterPatternItemEditorPlace> {
		@Override
		public FilterPatternItemEditorPlace getPlace(String token) {
			//token does not contain the prefix
			String[] splitted = token.split(":");
			if (splitted.length == 4) {
				return new FilterPatternItemEditorPlace(splitted);
			} else {
				return null;
			}
		}
		
		@Override
		public String getToken(FilterPatternItemEditorPlace place) {
			String token = place.getListType().toString();
			token += ":" + place.getEditorType().toString();
			token += ":" + place.getPath();
			token += ":" + place.getOpenWidgetIds();
			return token;
		}
	}
	
}
