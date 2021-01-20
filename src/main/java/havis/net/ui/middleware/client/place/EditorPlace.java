package havis.net.ui.middleware.client.place;

import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.google.gwt.user.client.History;

public class EditorPlace extends CommonEditorPlace implements HasListType {
	
	/**
	 * 'first init' constructor
	 * @param listType
	 * @param editorType
	 * @param specId
	 * @param isNew
	 */
	public EditorPlace(ListType listType, EditorType editorType, String specId, boolean isNew) {
		//-1 prevents adding something more than 'specId' to path of super (CommonEditorPlace)
		super(listType, editorType, null, Arrays.asList(specId), null, isNew, -1, "0");
	}
	
	/**
	 * 'normal' constructor
	 * @param listType
	 * @param editorType
	 * @param specId
	 * @param isNew
	 * @param openWidgetId
	 * 	0=nothing expanded, then widget count order from top to bottom
	 */
	public EditorPlace(ListType listType, EditorType editorType, String specId, boolean isNew, String openWidgetId) {
		super(listType, editorType, null, Arrays.asList(specId), Arrays.asList(openWidgetId), isNew, 0, openWidgetId);
	}
	
	
	/**
	 * 'go back' constructor
	 * @param lt
	 * @param et
	 * @param pathList
	 * @param openWidgetIdList
	 */
	public EditorPlace(ListType lt, EditorType et, List<String> pathList, List<String> openWidgetIdList) {
		
		super(lt,et,pathList,openWidgetIdList);
	}

	/**
	 * Registers the ConfigChangedEvent to listen to fired events from the editor when the user opens or closes
	 * different ConfigSections
	 */
	public void updateOpenWidgetId(String id){
		int desLength = 5;
		
		setOpenWidgetId(0,id);
		//updates the current 'OpenWidgetId' containing the id of the current expanded widget.
		//sample content of token, e.g.: "editor:EC:EC:e4888f8f-bca9-4faf-a796-73d2360bd168:0:0"
		String token = History.getToken();
		String[] splitted = token.split(":");
		if (splitted.length == desLength) {
			splitted[4] = getOpenWidgetId(0);
			token = splitted[0];
			for(int i = 1; i < desLength; i++){
				token += ":";
				token += splitted[i];
			}
			History.replaceItem(token, false);
		}
		else{
			GWT.log("EditorPlace.updateOpenWidgetId splitted.length does not fit.");
		}
	}
	
	public String toString(){
		String token = "editor" ;
		token += ":" + getListType().toString();
		token += ":" + getEditorType().toString();
		token += ":" + getSpecId();
		token += ":" + getOpenWidgetId(0);
		return token;
	}
	
	@Prefix("editor")
	public static class Tokenizer implements PlaceTokenizer<EditorPlace> {
		boolean isNew;
		
		@Override
		public EditorPlace getPlace(String token) {
			String[] splitted = token.split(":");
			if (splitted.length == 4) {
				ListType lst = ListType.valueOf(splitted[0]);
				EditorType edt = EditorType.valueOf(splitted[1]);
				String specID = splitted[2];
				String openWidgetId = splitted[3];
				return new EditorPlace(lst, edt, specID, isNew, openWidgetId);
			} else {
				return null;
			}
		}
		
		@Override
		public String getToken(EditorPlace place) {
			
			String token = place.getListType().toString();
			token += (":" + place.getEditorType().toString());
			token += ":" + place.getSpecId();
			token += (":" + place.getOpenWidgetId(0));
			return token;
		}
		
	}
}
