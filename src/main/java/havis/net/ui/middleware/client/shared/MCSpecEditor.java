package havis.net.ui.middleware.client.shared;

import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.user.client.ui.ToggleButton;

public interface MCSpecEditor<T> extends ValueAwareEditor<T> {
	ToggleButton enable();
}
