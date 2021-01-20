package havis.net.ui.middleware.client.place.ds;

import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;

import java.util.List;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;


public class CacheEditorPlace extends CommonEditorPlace  {

	/**
	 * c'tor used by updateOpenWidgetId()
	 * @param splittedToken
	 */
	public CacheEditorPlace(String[] splittedToken){
		super(splittedToken);
	}
	
	public CacheEditorPlace(EditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(ListType.DS, EditorType.DSCA, place, isNew, index, openWidgetId);
	}
	
	/**
	 * 'go back' constructor
	 * @param lt
	 * @param et
	 * @param pathList
	 * @param openWidgetIdList
	 */
	public CacheEditorPlace(ListType lt, EditorType et, List<String> pathList,
			List<String> openWidgetIdList) {
		super(lt,et,pathList,openWidgetIdList);
	}
	
	
	@Prefix("dsCacheSpecItem")
	public static class Tokenizer implements PlaceTokenizer<CacheEditorPlace> {
		
		@Override
		public CacheEditorPlace getPlace(String token) {
			//token does not contain the prefix
			String[] splitted = token.split(":");
			if (splitted.length == 4) {
				return new CacheEditorPlace(splitted);
			} else {
				return null;
			}
		}
		
		@Override
		public String getToken(CacheEditorPlace place) {
			String token = place.getListType().toString();
			token += ":" + place.getEditorType().toString();
			token += ":" + place.getPath();
			token += ":" + place.getOpenWidgetIds();
			return token;
		}

		
	}
}
