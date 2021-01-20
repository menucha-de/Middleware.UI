package havis.net.ui.middleware.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import havis.net.ui.middleware.client.main.MainSections;
import havis.net.ui.middleware.client.main.MainSectionsPresenter;
import havis.net.ui.middleware.client.place.ListPlace;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.MainPlace;

public class AppLayout extends Composite {

	@UiField SimplePanel details;
	@UiField MainSections sections;
	@UiField MainSections dsSections;
	
	private static AppLayoutUiBinder uiBinder = GWT
			.create(AppLayoutUiBinder.class);

	interface AppLayoutUiBinder extends UiBinder<Widget, AppLayout> {
	}

	@Inject
	public AppLayout(
			@Named("content") ActivityManager contentActivityManager,
			@Named("dataSource") ActivityManager dataSourceActivityManager,
			@Named("details") ActivityManager detailsActivityManager,
			Provider<MainSectionsPresenter> msProvider) {
		initWidget(uiBinder.createAndBindUi(this));

		detailsActivityManager.setDisplay(details);
		
		MainSectionsPresenter main = msProvider.get();
		main.setParentPlace(new MainPlace("0"));
		sections.setPresenter(main);
		contentActivityManager.setDisplay(sections);
		
		MainSectionsPresenter dataSource = msProvider.get();
		dataSource.setParentPlace(new ListPlace(ListType.DS));
		dsSections.setPresenter(dataSource);
		dataSourceActivityManager.setDisplay(dsSections);
	}
}
