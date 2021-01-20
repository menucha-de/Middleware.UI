package havis.net.ui.middleware.client.place.pc;

import java.util.List;

import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.cc.CCCmdSpecEditorPlace;
import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class OpSpecEditorPlace extends CommonEditorPlace{
	
	/**
	 * c'tor used by updateOpenWidgetId()
	 * @param splittedToken
	 */
	public OpSpecEditorPlace(String[] splittedToken){
		super(splittedToken);
	}
	
	public OpSpecEditorPlace(PcReportSpecItemEditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(ListType.PC, EditorType.PC, null, place.getPathList(), place.getOpenWidgetIdList(), isNew, index, openWidgetId);
	}
	
	public OpSpecEditorPlace(CCCmdSpecEditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(ListType.CC, EditorType.CC, null, place.getPathList(), place.getOpenWidgetIdList(), isNew, index, openWidgetId);
	}
	
	/**
	 * 'go back' constructor
	 * @param lt
	 * @param et
	 * @param pathList
	 * @param openWidgetIdList
	 */
	public OpSpecEditorPlace(ListType lt, EditorType et, List<String> pathList,
			List<String> openWidgetIdList) {
		super(lt,et,pathList,openWidgetIdList);
	}
	
	@Prefix("opSpecItem")
	public static class Tokenizer implements PlaceTokenizer<OpSpecEditorPlace> {
		
		@Override
		public OpSpecEditorPlace getPlace(String token) {
			//token does not contain the prefix
			String[] splitted = token.split(":");
			if (splitted.length == 4) {
				return new OpSpecEditorPlace(splitted);
			} else {
				return null;
			}
		}
		
		@Override
		public String getToken(OpSpecEditorPlace place) {
			String token = place.getListType().toString();
			token += ":" + place.getEditorType().toString();
			token += ":" + place.getPath();
			token += ":" + place.getOpenWidgetIds();
			return token;
		}
	}
}
