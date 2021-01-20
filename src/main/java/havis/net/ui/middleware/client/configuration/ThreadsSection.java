package havis.net.ui.middleware.client.configuration;

import havis.net.ui.middleware.client.configuration.CommonView.Presenter;
import havis.net.ui.middleware.client.shared.powerscale.PowerScaleWidget;
import havis.net.ui.shared.client.ConfigurationSection;

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

public class ThreadsSection extends ConfigurationSection {

	@UiField
	TextBox threads;
	@UiField
	PowerScaleWidget threadsScale;

	private List<Integer> threadsRange = Arrays.asList(1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192,
			16384, 32768, 65535, 65535);

	private Presenter presenter;

	private static ThreadsSectionUiBinder uiBinder = GWT.create(ThreadsSectionUiBinder.class);

	interface ThreadsSectionUiBinder extends UiBinder<Widget, ThreadsSection> {
	}

	@UiConstructor
	public ThreadsSection(String name) {
		super(name);
		initWidget(uiBinder.createAndBindUi(this));

		threadsScale.setRange(threadsRange);

		threadsScale.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String val = threadsScale.getValue().toString();
				threads.setValue(val, true);
			}
		});
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public void setMaxThreads(String threads) {
		try {
			threadsScale.setValue(Integer.parseInt(threads));
		} catch (NumberFormatException e) {
		}
		this.threads.setValue(threads);
	}

	@UiHandler("threads")
	public void onThreads(ValueChangeEvent<String> event) {
		try {
			threadsScale.setValue(Integer.parseInt(threads.getValue()));
			presenter.updateMaxThreads(threads.getValue());
		} catch (NumberFormatException e) {
		}
	}

}
