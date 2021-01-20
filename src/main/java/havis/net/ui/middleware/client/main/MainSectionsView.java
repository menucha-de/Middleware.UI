package havis.net.ui.middleware.client.main;

import java.util.Map;

import com.google.gwt.user.client.ui.IsWidget;

import havis.net.ui.middleware.client.shared.SectionType;

public interface MainSectionsView extends IsWidget {
	void setPresenter(Presenter presenter);
	Map<SectionType, MainSection> getSections();
	void resetCurrentSection();
	
	interface Presenter {
		void setView(MainSectionsView view);
		void onOpenSection(SectionType sectionType);
		void onCloseSection(MainSection ms);
	}
}
