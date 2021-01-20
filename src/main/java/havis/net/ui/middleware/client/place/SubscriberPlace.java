package havis.net.ui.middleware.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class SubscriberPlace extends Place implements HasListType {
	private ListType type;
	private String specId;
	private String subscriberId;
	
	public SubscriberPlace(ListType type, String specId, String subscriberId) {
		this.type = type;
		this.specId = specId;
		this.subscriberId = subscriberId;
	}
	
	public String getSpecId() {
		return specId;
	}

	public String getSubscriberId() {
		return subscriberId;
	}
	
	@Override
	public ListType getListType() {
		return type;
	}
	
	@Prefix("subscriber")
	public static class Tokenizer implements PlaceTokenizer<SubscriberPlace> {
		@Override
		public SubscriberPlace getPlace(String token) {
			String[] splitted = token.split(":");
			if (splitted.length == 3) {
				ListType listType = ListType.valueOf(splitted[0]);
				String specID = splitted[1];
				String subscriberId = splitted[2];
				return new SubscriberPlace(listType, specID, subscriberId);
			} else {
				return null;
			}
		}
		
		@Override
		public String getToken(SubscriberPlace place) {
			String token = "";
			token += place.getListType();
			token += ":" + place.getSpecId();
			token += ":" + place.getSubscriberId();
			return token;
		}
	}
}
