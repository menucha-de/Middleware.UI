package havis.net.ui.middleware.client.place.cc;

import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;

import java.util.List;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;



public class CCCmdSpecEditorPlace extends CommonEditorPlace  {

	/**
	 * c'tor used by updateOpenWidgetId()
	 * @param splittedToken
	 */
	public CCCmdSpecEditorPlace(String[] splittedToken){
		super(splittedToken);
	}
	
	public CCCmdSpecEditorPlace(EditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(ListType.CC, EditorType.CC, place, isNew, index, openWidgetId);
	}
	
	/**
	 * 'go back' constructor
	 * @param lt
	 * @param et
	 * @param pathList
	 * @param openWidgetIdList
	 */
	public CCCmdSpecEditorPlace(ListType lt, EditorType et, List<String> pathList,
			List<String> openWidgetIdList) {
		super(lt,et,pathList,openWidgetIdList);
	}
	
	
	@Prefix("ccCmdSpecItem")
	public static class Tokenizer implements PlaceTokenizer<CCCmdSpecEditorPlace> {
		
		@Override
		public CCCmdSpecEditorPlace getPlace(String token) {
			//token does not contain the prefix
			String[] splitted = token.split(":");
			if (splitted.length == 4) {
				return new CCCmdSpecEditorPlace(splitted);
			} else {
				return null;
			}
		}
		
		@Override
		public String getToken(CCCmdSpecEditorPlace place) {
			String token = place.getListType().toString();
			token += ":" + place.getEditorType().toString();
			token += ":" + place.getPath();
			token += ":" + place.getOpenWidgetIds();
			return token;
		}

		
	}
}
