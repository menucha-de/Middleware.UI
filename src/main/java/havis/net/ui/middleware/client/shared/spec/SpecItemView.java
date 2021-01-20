package havis.net.ui.middleware.client.shared.spec;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;

import havis.middleware.ale.service.mc.MCSpec;
import havis.net.ui.middleware.client.shared.BasePresenter;
import havis.net.ui.middleware.client.shared.event.EnableSpecEvent;
import havis.net.ui.middleware.client.shared.event.SpecEvent;
import havis.net.ui.shared.client.table.CustomTable;

public interface SpecItemView extends IsWidget, Editor<MCSpec> {
	void setPresenter(Presenter presenter);
	Presenter getPresenter();
	CustomTable getTable();
	LeafValueEditor<String> id();
	LeafValueEditor<String> name();
	LeafValueEditor<Boolean> enable();
	void setLoading(boolean isLoading);
	
	interface Presenter extends BasePresenter {
		SpecItemView getView();
		void setSource(Object source);
		String getBaseExportURL();
		void onDeleteSubscriber(int index);
		void onAddSubscriber();
		void onShowSubscribers();
		void onEnable(boolean value);
		void onDispose();
	}
	HandlerRegistration addEnableSpecHandler(EnableSpecEvent.Handler handler);
	HandlerRegistration addSpecEventHandler(SpecEvent.Handler handler);
	void setHasSubscribers(boolean hasSubscribers);
}
