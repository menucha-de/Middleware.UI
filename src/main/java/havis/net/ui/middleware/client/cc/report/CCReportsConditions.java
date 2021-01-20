package havis.net.ui.middleware.client.cc.report;

import havis.middleware.ale.service.cc.CCReports;
import havis.net.ui.shared.client.ConfigurationSection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CCReportsConditions extends ConfigurationSection implements Editor<CCReports> {

	@UiField
	Label initiationCondition;
	@UiField
	Label initiationTrigger;

	@UiField
	Label terminationCondition;
	@UiField
	Label terminationTrigger;

	private static CCReportsConditionsUiBinder uiBinder = GWT.create(CCReportsConditionsUiBinder.class);

	interface CCReportsConditionsUiBinder extends UiBinder<Widget, CCReportsConditions> {
	}

	public CCReportsConditions() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
