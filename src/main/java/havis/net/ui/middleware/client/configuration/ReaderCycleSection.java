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
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

import havis.net.ui.middleware.client.configuration.CommonView.Presenter;
import havis.net.ui.middleware.client.shared.powerscale.ChangeEndEvent;
import havis.net.ui.middleware.client.shared.powerscale.ChangeEndHandler;
import havis.net.ui.middleware.client.shared.powerscale.PowerScaleWidget;
import havis.net.ui.shared.client.ConfigurationSection;

public class ReaderCycleSection extends ConfigurationSection {

	private Presenter presenter;

	@UiField
	TextBox duration;
	@UiField
	TextBox count;
	@UiField
	TextBox lifetime;
	
	@UiField
	ToggleButton extendedMode;

	@UiField
	PowerScaleWidget durationScale;
	@UiField
	PowerScaleWidget countScale;
	@UiField
	PowerScaleWidget lifetimeScale;

	
	private String saveDuration = "0";
	private String saveCount = "0";
	private String saveLifetime = "0";
	// new acc. to: SyTestReport_20160323.txt
	// private List<Integer> durationRange =
	// Arrays.asList(0,50,100,150,200,250,300,350,400,450,500,550,600,650,700,
	// 750);
	private List<Integer> durationRange = Arrays.asList(0, 10, 25, 50, 75, 100, 125, 150, 200, 250, 300, 400, 500, 600,
			700, 800, 900, 1000);
	// private List<Integer> countRange =
	// Arrays.asList(0,1000,2000,3000,4000,5000,6000,7000,8000,9000,10000,11000,12000,13000,14000,15000);
	private List<Integer> countRange = Arrays.asList(0, 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192,
			16384, 32768, 65535);
	// private List<Integer> lifetimeRange =
	// Arrays.asList(0,10000,20000,30000,40000,50000,60000,70000,80000,90000,100000,110000,120000,130000,140000,150000);
	private List<Integer> lifetimeRange = Arrays.asList(0, 1000, 5000, 10000, 30000, 60000, 300000, 600000, 1800000,
			3600000, 7200000, 10800000, 14400000, 18000000, 21600000, 43200000, 64800000, 86400000);

	private static ReaderCycleSectionUiBinder uiBinder = GWT.create(ReaderCycleSectionUiBinder.class);

	interface ReaderCycleSectionUiBinder extends UiBinder<Widget, ReaderCycleSection> {
	}

	@UiConstructor
	public ReaderCycleSection(String name) {
		super(name);
		initWidget(uiBinder.createAndBindUi(this));

		durationScale.setRange(durationRange);
		countScale.setRange(countRange);
		lifetimeScale.setRange(lifetimeRange);

		durationScale.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				duration.setValue(durationScale.getValue().toString(), true);
			}
		});

		durationScale.addChangeEndHandler(new ChangeEndHandler() {
			@Override
			public void onChangeEnd(ChangeEndEvent event) {
				presenter.updateDuration(duration.getValue());
			}
		});

		countScale.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				count.setValue(countScale.getValue().toString(), true);
			}
		});

		countScale.addChangeEndHandler(new ChangeEndHandler() {
			@Override
			public void onChangeEnd(ChangeEndEvent event) {
				presenter.updateCount(count.getValue());
			}
		});

		lifetimeScale.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				lifetime.setValue(lifetimeScale.getValue().toString(), true);
			}
		});

		lifetimeScale.addChangeEndHandler(new ChangeEndHandler() {
			@Override
			public void onChangeEnd(ChangeEndEvent event) {
				presenter.updateLifetime(lifetime.getValue());
			}
		});
	}
	
	@UiHandler("extendedMode")
	void onExtendedModeChange(ValueChangeEvent<Boolean> event) {
		presenter.updateExtendedMode(event.getValue());
	}

	@UiHandler("duration")
	public void onDurationChange(ValueChangeEvent<String> event) {
		try {
			durationScale.setValue(Integer.parseInt(event.getValue()));
			saveDuration = event.getValue();
			presenter.updateDuration(event.getValue());
		} catch (NumberFormatException e) {
			duration.setValue(saveDuration);
		}
	}

	@UiHandler("count")
	public void onCountChange(ValueChangeEvent<String> event) {
		try {
			countScale.setValue(Integer.parseInt(event.getValue()));
			saveCount = event.getValue();
			presenter.updateCount(event.getValue());
		} catch (NumberFormatException e) {
			count.setValue(saveCount);
		}
	}

	@UiHandler("lifetime")
	public void onLifetimeChange(ValueChangeEvent<String> event) {
		try {
			lifetimeScale.setValue(Integer.parseInt(event.getValue()));
			saveLifetime = event.getValue();
			presenter.updateLifetime(event.getValue());
		} catch (NumberFormatException e) {
			lifetime.setValue(saveLifetime);
		}
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public void setDuration(String duration) {
		try {
			durationScale.setValue(Integer.parseInt(duration));
			saveDuration = duration;
		} catch (NumberFormatException e) {
		}
		this.duration.setValue(duration);
	}

	public void setCount(String count) {
		try {
			countScale.setValue(Integer.parseInt(count));
			saveCount = count;
		} catch (NumberFormatException e) {
		}
		this.count.setValue(count);
	}

	public void setLifetime(String lifetime) {
		try {
			lifetimeScale.setValue(Integer.parseInt(lifetime));
			saveLifetime = lifetime;
		} catch (NumberFormatException e) {
		}
		this.lifetime.setValue(lifetime);
	}

	public void setExtendedMode(Boolean extendedMode) {
		this.extendedMode.setValue(extendedMode);
	}
}
