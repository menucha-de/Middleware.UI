package havis.net.ui.middleware.client.shared;

import java.util.List;

public interface CustomListRenderer<T> {
	/**
	 * Renders object as plain List<String> for Lists with more columns. Should never throw any exceptions!
	 * 
	 * @param value
	 *            the value
	 * @return the List<String> representation
	 */
	List<String> render(T value);
}
