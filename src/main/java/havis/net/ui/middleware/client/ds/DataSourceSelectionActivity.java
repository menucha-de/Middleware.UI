package havis.net.ui.middleware.client.ds;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import havis.net.ui.middleware.client.shared.BaseActivity;

public class DataSourceSelectionActivity extends BaseActivity implements DataSourceSelectionView.Presenter {
	
	@Inject
	private DataSourceSelectionView view;
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		panel.setWidget(view.asWidget());
	}

	@Override
	public void bind() {
	}
}
