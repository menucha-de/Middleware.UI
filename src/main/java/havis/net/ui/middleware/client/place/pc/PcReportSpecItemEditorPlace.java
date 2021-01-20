package havis.net.ui.middleware.client.place.pc;

import java.util.List;

import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class PcReportSpecItemEditorPlace extends CommonEditorPlace  {

	/**
	 * c'tor used by updateOpenWidgetId()
	 * @param splittedToken
	 */
	public PcReportSpecItemEditorPlace(String[] splittedToken){
		super(splittedToken);
	}
	
	public PcReportSpecItemEditorPlace(EditorPlace place, boolean isNew, int index, String openWidgetId) {
		super(ListType.PC, EditorType.PC, place, isNew, index, openWidgetId);
	}
	
	/**
	 * 'go back' constructor
	 * @param lt
	 * @param et
	 * @param pathList
	 * @param openWidgetIdList
	 */
	public PcReportSpecItemEditorPlace(ListType lt, EditorType et, List<String> pathList,
			List<String> openWidgetIdList) {
		super(lt,et,pathList,openWidgetIdList);
	}
	
	
	@Prefix("pcRepSpecItem")
	public static class Tokenizer implements PlaceTokenizer<PcReportSpecItemEditorPlace> {
		
		@Override
		public PcReportSpecItemEditorPlace getPlace(String token) {
			//token does not contain the prefix
			String[] splitted = token.split(":");
			if (splitted.length == 4) {
				return new PcReportSpecItemEditorPlace(splitted);
			} else {
				return null;
			}
		}
		
		@Override
		public String getToken(PcReportSpecItemEditorPlace place) {
			String token = place.getListType().toString();
			token += ":" + place.getEditorType().toString();
			token += ":" + place.getPath();
			token += ":" + place.getOpenWidgetIds();
			return token;
		}
	}
}
