package havis.net.ui.middleware.client.configuration;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import havis.net.ui.middleware.client.configuration.CommonView.Presenter;
import havis.net.ui.middleware.client.shared.powerscale.ChangeEndEvent;
import havis.net.ui.middleware.client.shared.powerscale.ChangeEndHandler;
import havis.net.ui.middleware.client.shared.powerscale.PowerScaleWidget;
import havis.net.ui.shared.client.ConfigurationSection;

public class SubscriberSection extends ConfigurationSection {

	@UiField
	TextBox timeout;

	@UiField
	PowerScaleWidget timeoutScale;

	private String saveTimeout = "0";
	private Presenter presenter;

	private List<Integer> timeoutRange = Arrays.asList(1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000,
			11000, 12000, 13000, 14000, 15000, 16000, 17000, 18000);

	private static SubscriberSectionUiBinder uiBinder = GWT.create(SubscriberSectionUiBinder.class);

	interface SubscriberSectionUiBinder extends UiBinder<Widget, SubscriberSection> {
	}

	@UiConstructor
	public SubscriberSection(String name) {
		super(name);
		initWidget(uiBinder.createAndBindUi(this));
		timeoutScale.setRange(timeoutRange);

		timeoutScale.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				timeout.setValue(timeoutScale.getValue().toString(), true);
			}
		});

		timeoutScale.addChangeEndHandler(new ChangeEndHandler() {
			@Override
			public void onChangeEnd(ChangeEndEvent event) {
				presenter.updateTimeout(timeout.getValue());
			}
		});
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@UiHandler("timeout")
	public void onTimeoutChange(ValueChangeEvent<String> event) {
		try {
			timeoutScale.setValue(Integer.parseInt(event.getValue()));
			saveTimeout = event.getValue();
			presenter.updateTimeout(event.getValue());
		} catch (NumberFormatException e) {
			timeout.setValue(saveTimeout);
		}
	}

	public void setTimeout(String timeout) {
		try {
			timeoutScale.setValue(Integer.parseInt(timeout));
			saveTimeout = timeout;
		} catch (NumberFormatException e) {
		}
		this.timeout.setValue(timeout);
	}

}
