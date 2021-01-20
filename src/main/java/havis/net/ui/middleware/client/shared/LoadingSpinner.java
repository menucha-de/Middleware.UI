package havis.net.ui.middleware.client.shared;

import com.google.gwt.user.client.ui.SimplePanel;

import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;

public class LoadingSpinner extends SimplePanel {
	public LoadingSpinner() {
		SimplePanel loader = new SimplePanel();
		loader.setStyleName(ResourceBundle.INSTANCE.css().webuiSpinnerBounce());
		this.add(loader);
	}
}
