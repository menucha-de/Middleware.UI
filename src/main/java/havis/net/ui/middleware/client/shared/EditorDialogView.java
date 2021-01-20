package havis.net.ui.middleware.client.shared;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public interface EditorDialogView extends IsWidget {

	void setPresenter(Presenter presenter);
	void setEditor(Widget widget);
	void setEditorTitle(String title);
	void setEnableButton(ToggleButton enable);
	void setEnabledAcceptButton(boolean enabled);
	void setExportButton(String url, String filename);
	void setEnabledExportButton(boolean enabled);
	
	interface Presenter extends BasePresenter {
		void onClose();
		void onAccept();
	}
}
