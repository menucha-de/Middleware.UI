package havis.net.ui.middleware.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class CommonPlace extends Place {
	private String page;
	
	public CommonPlace(String token) {
		this.page = token;
	}
	
	public String getPage() {
		return page;
	}
	
	@Prefix("common")
	public static class Tokenizer implements PlaceTokenizer<CommonPlace> {
		@Override
		public CommonPlace getPlace(String token) {
			return new CommonPlace(token);
		}
		
		@Override
		public String getToken(CommonPlace place) {
			return place.getPage();
		}
	}
}
