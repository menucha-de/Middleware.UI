package havis.net.ui.middleware.client.utils;

import havis.middleware.ale.service.ECTime;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;

import java.util.Date;

import org.fusesource.restygwt.client.FailedResponseException;
import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class Utils {

	private static final String EXCEPTION = "Exception: ";
	public static final int INVALID_TIME = -1;

	/**
	 * Returns true if the given string is null or is an empty string.
	 * 
	 * @param str
	 *            a string reference to check
	 * @return true if the string is null or is an empty string
	 */
	public static boolean isNullOrEmpty(CharSequence str) {
		if (str == null || str.toString().trim().isEmpty())
			return true;
		else
			return false;
	}

	/**
	 * Returns the reason of the {@code throwable}
	 * 
	 * @param throwable
	 * @return the reason
	 */
	public static String getReason(Throwable throwable) {

		if (throwable != null) {
			String returnMessage = throwable.getMessage();

			if (FailedResponseException.class.equals(throwable.getClass())) {
				Response response = ((FailedResponseException) throwable).getResponse();
				if (response != null) {
					if (!isNullOrEmpty(response.getText())) {
						returnMessage = response.getText();
						int offset = returnMessage.indexOf(EXCEPTION);
						if (offset >= 0) {
							returnMessage = returnMessage.substring(offset + EXCEPTION.length());
						}
					}
				}
			}

			return returnMessage;
		}
		return null;
	}

	public static ECTime getTime() {
		ECTime result = new ECTime();
		result.setValue(INVALID_TIME);
		result.setUnit("MS");
		return result;
	}

	public static boolean isTimeSet(ECTime time) {
		return time != null && time.getValue() != INVALID_TIME;
	}

	public static String getNewId() {
		return "NEW_" + new Date().getTime();
	}

	public static boolean isNewId(String id) {
		return id.startsWith("NEW_");
	}

	public static void blockUi(final PopupPanel overlay, int delayMillis) {
		if (overlay != null) {
			overlay.show();
			Timer tim = new Timer() {

				@Override
				public void run() {
					overlay.hide();
				}
			};
			tim.schedule(delayMillis);
		}
	}

	/**
	 * Performs a click event on the given element
	 * 
	 * @param elem
	 */
	public final static native void clickElement(Element elem) /*-{
		elem.click();
	}-*/;

	/**
	 * Adds the CSS class which highlights the content of the widget as invalid.
	 * 
	 * @param widget
	 */
	public static void addErrorStyle(Widget widget) {
		widget.addStyleName(ResourceBundle.INSTANCE.css().webuiInputError());
	}

	/**
	 * Removes the CSS class which highlights the content of the widget as
	 * invalid.
	 * 
	 * @param widget
	 */
	public static void removeErrorStyle(Widget widget) {
		widget.removeStyleName(ResourceBundle.INSTANCE.css().webuiInputError());
	}
	
	/**
	 * @return tab inner height
	 */
	public static final native int getWindowParentInnerHeight()/*-{
		return window.parent.parent.innerHeight;
	}-*/;

	/**
	 * @return offsetTop of iframe which serves as container for the webui
	 */
	public static final native int getContentOffsetTop()/*-{
		if (window.parent.parent.document.getElementById("content") != null) {
			return window.parent.parent.document.getElementById("content").offsetTop;
		} else {			
			return 0;
		}
	}-*/;
	
	/**
	 * @return scrollTop position of body element.
	 */
	public static final native int getContentScrollTop()/*-{
		if (window.parent.parent.document.body != null) {
			// Implementation for Chrome. IE and Firefox will always return 0
			var result = window.parent.parent.document.body.scrollTop;
			if (result == 0){
				// Implementation for IE and Firefox. Chrome will always return 0
				if (window.parent.parent.document.documentElement != null) {
					result = window.parent.parent.document.documentElement.scrollTop;
				}
			} 
			return result;
		} else {
			return 0;
		}
	}-*/;

	/**
	 * Replacing all properties in {@code dest} by {@code src} properties
	 * 
	 * @param dest
	 *            The properties of src will be copied in this object.
	 * @param src
	 *            Object which contains the new properties for the dest object
	 */
	public final static native <T extends Object> void flush(T dest, T src)/*-{
		for ( var i in dest) {
			dest[i] = null;
		}
		for ( var i in src) {
			dest[i] = src[i];
		}
	}-*/;

	/**
	 * Creates a deep copy of the value
	 * 
	 * @param codec
	 *            JsonEncoderDecoder for the class of the value
	 * @param value
	 *            The value that shall be copied.
	 * @return A deep copy of value
	 */
	public static <T extends Object> T clone(JsonEncoderDecoder<T> codec, T value) {
		String clone = codec.encode(value).toString();
		clone = clone.replace("\"schemaVersion\":\"1.1\"", "\"schemaVersion\":1.1");
		return codec.decode(clone);
	}
}
