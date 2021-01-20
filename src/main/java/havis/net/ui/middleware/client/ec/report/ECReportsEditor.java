package havis.net.ui.middleware.client.ec.report;

import org.fusesource.restygwt.client.Defaults;

import havis.middleware.ale.service.ec.ECReports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.Widget;

public class ECReportsEditor extends Composite implements Editor<ECReports> {

	@UiField
	Label specName;
	@UiField(provided = true)
	DateLabel date = new DateLabel(DateTimeFormat.getFormat(Defaults.getDateFormat()));	
	
	
	@UiField
	Label ALEID;
	
	@UiField(provided = true)
	NumberLabel<Long> totalMilliseconds = new NumberLabel<Long>(NumberFormat.getFormat("###0"));

	@Path("")
	@UiField
	ECReportsConditions conditions;
	
	@Path("reports.report")
	@UiField
	ECReportListEditor reports;	
	
	private static ECReportEditorUiBinder uiBinder = GWT.create(ECReportEditorUiBinder.class);
	interface ECReportEditorUiBinder extends UiBinder<Widget, ECReportsEditor> { }

	public ECReportsEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
