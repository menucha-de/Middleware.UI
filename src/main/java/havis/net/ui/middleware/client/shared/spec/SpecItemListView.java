package havis.net.ui.middleware.client.shared.spec;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.IsWidget;

import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.shared.BasePresenter;
import havis.net.ui.shared.client.upload.MultipleFileUpload;

public interface SpecItemListView extends IsWidget {

	void setPresenter(Presenter presenter);
	InsertPanel getList();
	MultipleFileUpload getImportField();
	Anchor getExportLink();
	void reset();
	
	interface Presenter extends BasePresenter {
		void onLoad();
		void onAdd();
		void onRefresh();
		void onImport();
		void onClose();
		ListType getListType();
	}
}
