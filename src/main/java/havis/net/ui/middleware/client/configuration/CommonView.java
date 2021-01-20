package havis.net.ui.middleware.client.configuration;

import havis.net.ui.middleware.client.shared.BasePresenter;

import com.google.gwt.user.client.ui.IsWidget;

public interface CommonView extends IsWidget {
	void setPresenter(Presenter presenter);
	void setAleId(String aleId);
	void setMaxThreads(String threads);
	void setDuration(String duration);
	void setCount(String count);
	void setLifetime(String lifetime);
	void setName(String name);
	void setVersion(String version);
	void setAuthor(String author);
	void setDocumentation(String url);
	void setHomepage(String url);
	void setTimeout(String timeout);
	void setExtendedMode(Boolean extendedMode);
	void setSoapService(Boolean soapService);
	
	public interface Presenter extends BasePresenter {
		void updateAleId(String aleId);
		void updateMaxThreads(String threads);
		void updateDuration(String duration);
		void updateCount(String count);
		void updateLifetime(String lifetime);
		void updateTimeout(String timeout);
		void updateExtendedMode(Boolean value);
		void updateSoapService(Boolean value);
	}
}
