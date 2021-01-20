package havis.net.ui.middleware.client.cc.report;

import java.util.List;

import org.fusesource.restygwt.client.Defaults;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import havis.middleware.ale.service.cc.CCParameterListEntry;
import havis.middleware.ale.service.cc.CCReports;

public class CCReportsEditor extends Composite implements ValueAwareEditor<CCReports> {

	@UiField
	@Ignore
	CCParametersEditor parameters;

	@UiField
	Button executeButton;

	@UiField
	FlowPanel parametersPanel;

	@UiField
	TextBox specName;

	@UiField(provided = true)
	DateLabel date = new DateLabel(DateTimeFormat.getFormat(Defaults.getDateFormat()));

	@UiField
	Label ALEID;

	@UiField(provided = true)
	NumberLabel<Long> totalMilliseconds = new NumberLabel<Long>(NumberFormat.getFormat("###0"));

	@Path("")
	@UiField
	CCReportsConditions conditions;

	@Path("cmdReports.cmdReport")
	@UiField
	CCCmdReportListEditor cmdReports;

	private List<CCParameterListEntry> entries;

	private static CCReportsEditorUiBinder uiBinder = GWT.create(CCReportsEditorUiBinder.class);

	interface CCReportsEditorUiBinder extends UiBinder<Widget, CCReportsEditor> {
	}

	private Driver driver = GWT.create(Driver.class);

	interface Driver extends SimpleBeanEditorDriver<List<CCParameterListEntry>, CCParametersEditor> {
	}

	public CCReportsEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		driver.initialize(parameters);
	}

	void setParameters(List<CCParameterListEntry> parameters) {
		if (parameters != null && parameters.size() > 0) {
			entries = parameters;
			driver.edit(parameters);
			this.parametersPanel.setVisible(true);
			this.parameters.setOpen(true);
		}
	}

	List<CCParameterListEntry> getParameters() {
		driver.flush();
		return entries;
	}

	public void focusFirst() {
		parameters.focusFirst();
	}

	@Override
	public void setDelegate(EditorDelegate<CCReports> delegate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPropertyChange(String... paths) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(CCReports value) {
		cmdReports.setOpen(true);
	}
}
