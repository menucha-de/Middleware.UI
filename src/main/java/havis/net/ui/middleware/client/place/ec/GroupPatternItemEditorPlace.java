package havis.net.ui.middleware.client.place.ec;

import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class GroupPatternItemEditorPlace extends CommonEditorPlace {
	
	/**
	 * c'tor used by updateOpenWidgetId()
	 * @param splittedToken
	 */
	public GroupPatternItemEditorPlace(String[] splittedToken){
		super(splittedToken);
	}
	
	public GroupPatternItemEditorPlace(ECReportSpecEditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(ListType.EC, EditorType.EC, null, place.getPathList(), place.getOpenWidgetIdList(), isNew, index, openWidgetId);
	}
	
	@Prefix("grpPatternItem")
	public static class Tokenizer implements PlaceTokenizer<GroupPatternItemEditorPlace> {
		boolean isNew;
		
		@Override
		public GroupPatternItemEditorPlace getPlace(String token) {
			String[] splitted = token.split(":");
			if (splitted.length == 4) {
				return new GroupPatternItemEditorPlace(splitted);
			} else {
				return null;
			}
		}
		
		@Override
		public String getToken(GroupPatternItemEditorPlace place) {
			String token = place.getListType().toString();
			token += ":" + place.getEditorType().toString();
			token += ":" + place.getPath();
			token += ":" + place.getOpenWidgetIds();
			return token;
		}

	}

	
}
