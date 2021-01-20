package havis.net.ui.middleware.client.ec.report;

import havis.middleware.ale.service.ec.ECReports;
import havis.net.ui.shared.client.ConfigurationSection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ECReportsConditions extends ConfigurationSection implements Editor<ECReports> {

	@UiField
	Label initiationCondition;
	@UiField
	Label initiationTrigger;
	
	@UiField
	Label terminationCondition;
	@UiField
	Label terminationTrigger;
	
	private static ECReportsConditionsUiBinder uiBinder = GWT.create(ECReportsConditionsUiBinder.class);

	interface ECReportsConditionsUiBinder extends UiBinder<Widget, ECReportsConditions> {
	}

	public ECReportsConditions(){
		initWidget(uiBinder.createAndBindUi(this));
	}
	
}
