package havis.net.ui.middleware.client.configuration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import havis.net.ui.middleware.client.shared.CustomMessageWidget;
import havis.net.ui.middleware.client.shared.IsMainSection;
import havis.net.ui.middleware.client.shared.SectionType;

public class Common extends Composite implements CommonView, IsMainSection {

	@Inject
	PlaceController placeController;

	@Inject
	EventBus eventBus;

	@UiField
	TextBox aleId;
	
	@UiField
	ToggleButton soapService;

	@UiField
	ThreadsSection threadsSection;

	@UiField
	ReaderCycleSection readerSection;

	@UiField
	ProductInfoSection productInfoSection;

	@UiField
	SubscriberSection subscriberSection;

	@UiField
	CustomMessageWidget message;

	private Presenter presenter;

	private static CommonUiBinder uiBinder = GWT.create(CommonUiBinder.class);

	interface CommonUiBinder extends UiBinder<Widget, Common> {
	}

	public Common() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		message.register(eventBus, this.presenter);
		threadsSection.setPresenter(presenter);
		readerSection.setPresenter(presenter);
		subscriberSection.setPresenter(presenter);
	}

	@Override
	public void setAleId(String aleId) {
		this.aleId.setText(aleId);
	}

	@UiHandler("aleId")
	public void onAleId(ChangeEvent event) {
		presenter.updateAleId(aleId.getValue());
	}
	
	@UiHandler("soapService")
	public void onSoapServiceToggle(ValueChangeEvent<Boolean> event) {
		presenter.updateSoapService(event.getValue());
	}

	@Override
	public void setMaxThreads(String threads) {
		threadsSection.setMaxThreads(threads);
	}

	@Override
	public void setDuration(String duration) {
		readerSection.setDuration(duration);
	}

	@Override
	public void setCount(String count) {
		readerSection.setCount(count);
	}

	@Override
	public void setLifetime(String lifetime) {
		readerSection.setLifetime(lifetime);
	}

	@Override
	public void setName(String name) {
		productInfoSection.setName(name);
	}

	@Override
	public void setVersion(String version) {
		productInfoSection.setVersion(version);
	}

	@Override
	public void setAuthor(String author) {
		productInfoSection.setAuthor(author);
	}

	@Override
	public void setDocumentation(String url) {
		productInfoSection.setDocumentation(url);
	}

	@Override
	public void setHomepage(String url) {
		productInfoSection.setHomepage(url);
	}

	@Override
	public SectionType getSectionType() {
		return SectionType.CM;
	}

	@Override
	public void setTimeout(String timeout) {
		subscriberSection.setTimeout(timeout);
	}

	@Override
	public void setExtendedMode(Boolean extendedMode) {
		readerSection.setExtendedMode(extendedMode);
	}
	
	@Override
	public void setSoapService(Boolean soapService) {
		this.soapService.setValue(soapService);
	}
}
