package havis.net.ui.middleware.client.place.ec;

import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.cc.CCCmdSpecEditorPlace;
import havis.net.ui.middleware.client.place.pc.PcReportSpecItemEditorPlace;

import java.util.List;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class FilterItemEditorPlace extends CommonEditorPlace {
//	private String prefix;
//	private ListType listType = ListType.EC;
//	private EditorType editorType = EditorType.EC;
//	private List<String> path;
//	private boolean isNew;
//	private List<String> openWidgetIds;
	
	
	/**
	 * c'tor used by updateOpenWidgetId()
	 * @param splittedToken
	 */
	public FilterItemEditorPlace(String[] splittedToken){
		super(splittedToken);
	}
	
	
	public FilterItemEditorPlace(ECReportSpecEditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(ListType.EC, EditorType.EC, null, place.getPathList(), place.getOpenWidgetIdList(), isNew, index, openWidgetId);
	}
	
	public FilterItemEditorPlace(PcReportSpecItemEditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(ListType.PC, EditorType.PC, null, place.getPathList(), place.getOpenWidgetIdList(), isNew, index, openWidgetId);
	}
	
	public FilterItemEditorPlace(CCCmdSpecEditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(ListType.CC, EditorType.CC, null, place.getPathList(), place.getOpenWidgetIdList(), isNew, index, openWidgetId);
	}
	

	/**
	 * 'go back' constructor
	 * @param lt
	 * @param et
	 * @param pathList
	 * @param openWidgetIdList
	 */
	public FilterItemEditorPlace(ListType lt, EditorType et, List<String> pathList, List<String> openWidgetIdList) {
		super(lt,et,pathList,openWidgetIdList);
	}



	@Prefix("filterItem")
	public static class Tokenizer implements PlaceTokenizer<FilterItemEditorPlace> {
		@Override
		public FilterItemEditorPlace getPlace(String token) {
			//token does not contain the prefix
			String[] splitted = token.split(":");
			if (splitted.length == 4) {
				return new FilterItemEditorPlace(splitted);
			} else {
				return null;
			}
		}
		
		@Override
		public String getToken(FilterItemEditorPlace place) {
			String token = place.getListType().toString();
			token += ":" + place.getEditorType().toString();
			token += ":" + place.getPath();
			token += ":" + place.getOpenWidgetIds();
			return token;
		}
	}
	
}
