package havis.net.ui.middleware.client.pc.report;

import havis.middleware.ale.service.pc.PCReports;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.shared.client.ConfigurationSection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PCReportsConditions extends ConfigurationSection implements Editor<PCReports> {

	@UiField
	Label initiationCondition;
	@UiField
	Label initiationTrigger;

	@UiField
	Label terminationCondition;
	@UiField
	Label terminationTrigger;

	private ConstantsResource res = ConstantsResource.INSTANCE;

	private static PCReportsConditionsUiBinder uiBinder = GWT.create(PCReportsConditionsUiBinder.class);

	interface PCReportsConditionsUiBinder extends UiBinder<Widget, PCReportsConditions> {
	}

	public PCReportsConditions() {
		initWidget(uiBinder.createAndBindUi(this));
		setText(res.conditions());
	}
}
