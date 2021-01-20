package havis.net.ui.middleware.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MainPlace extends Place {
	private String page;
	
	public MainPlace(String token) {
		this.page = token;
	}
	
	public String getPage() {
		return page;
	}
	
	public String toString(){
		return "main:" + page;
	}
	
	@Prefix("main")
	public static class Tokenizer implements PlaceTokenizer<MainPlace> {
		@Override
		public MainPlace getPlace(String token) {
			return new MainPlace(token);
		}
		
		@Override
		public String getToken(MainPlace place) {
			return place.getPage();
		}
	}
}
