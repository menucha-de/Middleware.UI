package havis.net.ui.middleware.client.place.ds;

import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterItemEditorPlace;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class CachePatternEditorPlace extends CommonEditorPlace {
	
	/**
	 * c'tor used by updateOpenWidgetId()
	 * @param splittedToken
	 */
	public CachePatternEditorPlace(String[] splittedToken){
		super(splittedToken);
	}
	
	public CachePatternEditorPlace(FilterItemEditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(place.getListType(), place.getEditorType(), null, place.getPathList(), place.getOpenWidgetIdList(), isNew, index, openWidgetId);
	}
	
	
	public CachePatternEditorPlace(EditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(place.getListType(), place.getEditorType(), null, place.getPathList(), place.getOpenWidgetIdList(), isNew, index, openWidgetId);
	}

	
	@Prefix("dscPatternItem")
	public static class Tokenizer implements PlaceTokenizer<CachePatternEditorPlace> {
		@Override
		public CachePatternEditorPlace getPlace(String token) {
			//token does not contain the prefix
			String[] splitted = token.split(":");
			if (splitted.length == 4) {
				return new CachePatternEditorPlace(splitted);
			} else {
				return null;
			}
		}
		
		@Override
		public String getToken(CachePatternEditorPlace place) {
			String token = place.getListType().toString();
			token += ":" + place.getEditorType().toString();
			token += ":" + place.getPath();
			token += ":" + place.getOpenWidgetIds();
			return token;
		}
	}
	
}
