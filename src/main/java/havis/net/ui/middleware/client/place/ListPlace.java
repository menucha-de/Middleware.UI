package havis.net.ui.middleware.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ListPlace extends Place implements HasListType {
	private ListType type;
	
	public ListPlace(ListType type) {
		this.type = type;
	}
	
	@Override
	public ListType getListType() {
		return type;
	}
	
	@Prefix("list")
	public static class Tokenizer implements PlaceTokenizer<ListPlace> {
		@Override
		public ListPlace getPlace(String token) {
			return new ListPlace(ListType.valueOf(token));
		}
		
		@Override
		public String getToken(ListPlace place) {
			return place.getListType().toString();
		}
	}
}
