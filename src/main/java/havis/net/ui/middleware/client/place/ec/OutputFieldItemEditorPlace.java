package havis.net.ui.middleware.client.place.ec;

import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class OutputFieldItemEditorPlace extends CommonEditorPlace {
	
	public OutputFieldItemEditorPlace(String[] splittedToken){
		super(splittedToken);
	}
	
	public OutputFieldItemEditorPlace(ECReportSpecEditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(ListType.EC, EditorType.EC, null, place.getPathList(), place.getOpenWidgetIdList(), isNew, index, openWidgetId);
	}
	
	@Prefix("ouputFieldItem")
	public static class Tokenizer implements PlaceTokenizer<OutputFieldItemEditorPlace> {
		
		@Override
		public OutputFieldItemEditorPlace getPlace(String token) {
			//token does not contain the prefix
			String[] splitted = token.split(":");
			if (splitted.length == 4) {
				return new OutputFieldItemEditorPlace(splitted);
			} else {
				return null;
			}
		}
		
		@Override
		public String getToken(OutputFieldItemEditorPlace place) {
			String token = place.getListType().toString();
			token += ":" + place.getEditorType().toString();
			token += ":" + place.getPath();
			token += ":" + place.getOpenWidgetIds();
			return token;
		}
	}
}