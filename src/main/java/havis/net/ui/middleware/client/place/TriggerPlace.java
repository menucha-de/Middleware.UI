package havis.net.ui.middleware.client.place;

import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;

import java.util.List;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class TriggerPlace extends CommonEditorPlace implements HasListType {
	
	/**
	 * 'go next' constructor
	 * @param place
	 * @param triggerType
	 * @param isNew
	 * @param index
	 * @param openWidgetId
	 */
	public TriggerPlace(CommonEditorPlace place, TriggerListType triggerType, boolean isNew, int index, String openWidgetId) {
		super(place.getListType(), EditorType.TR, triggerType, place.getPathList(), place.getOpenWidgetIdList(), isNew, index, openWidgetId);
	}
	
	/**
	 * 'go back' constructor
	 * @param lt
	 * @param et
	 * @param pathList
	 * @param openWidgetIdList
	 */
	public TriggerPlace(ListType lt, EditorType et, List<String> pathList, List<String> openWidgetIdList) {
		super(lt,et,pathList,openWidgetIdList);
	}
	
	/**
	 * c'tor used by updateOpenWidgetId()
	 * @param splittedToken
	 */
	public TriggerPlace(String[] splittedToken){
		super(splittedToken);
	}
	
		
	public int getIndex() {
		return getPathAsInt(1);
	}
	
	public String toString(){
		String token = "trigger:";
		token += getListType();
		token += ":" + combineEditorTypeAndTriggerListType();
		token += ":" + getSpecId();
		token += ":" + getIndex();
		return token;
	}
	
	
	@Prefix("trigger")
	public static class Tokenizer implements PlaceTokenizer<TriggerPlace> {
		@Override
		public TriggerPlace getPlace(String token) {
			
			//token does not contain the prefix
			String[] splitted = token.split(":");
			if (splitted.length == 4) {
				return new TriggerPlace(splitted);
			} else {
				return null;
			}
		}
		
		@Override
		public String getToken(TriggerPlace place) {
			String token = "";
			token += place.getListType();
			token += ":" + place.combineEditorTypeAndTriggerListType();
			token += ":" + place.getPath();
			token += ":" + place.getOpenWidgetIds();
			return token;
		}
	}
}
