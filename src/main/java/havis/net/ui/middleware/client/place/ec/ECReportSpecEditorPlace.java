package havis.net.ui.middleware.client.place.ec;

import java.util.List;

import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ECReportSpecEditorPlace extends CommonEditorPlace  {

	/**
	 * c'tor used by updateOpenWidgetId()
	 * @param splittedToken
	 */
	public ECReportSpecEditorPlace(String[] splittedToken){
		super(splittedToken);
	}
	
	
	public ECReportSpecEditorPlace(EditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(ListType.EC, EditorType.EC, place, isNew, index, openWidgetId);
	}
	
	/**
	 * 'go back' constructor
	 * @param lt
	 * @param et
	 * @param pathList
	 * @param openWidgetIdList
	 */
	public ECReportSpecEditorPlace(ListType lt, EditorType et, List<String> pathList, List<String> openWidgetIdList) {
		super(lt,et, pathList, openWidgetIdList);
	}

	@Prefix("repSpecItem")
	public static class Tokenizer implements PlaceTokenizer<ECReportSpecEditorPlace> {
		
		@Override
		public ECReportSpecEditorPlace getPlace(String token) {
			//token does not contain the prefix
			String[] splitted = token.split(":");
			if (splitted.length == 4) {
				return new ECReportSpecEditorPlace(splitted);
			} else {
				return null;
			}
		}
		
		@Override
		public String getToken(ECReportSpecEditorPlace place) {
			String token = place.getListType().toString();
			token += ":" + place.getEditorType().toString();
			token += ":" + place.getPath();
			token += ":" + place.getOpenWidgetIds();
			return token;
		}
	}
}
