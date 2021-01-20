package havis.net.ui.middleware.client.pc.report;

import org.fusesource.restygwt.client.Defaults;

import havis.middleware.ale.service.pc.PCReports;
import havis.net.ui.shared.client.ConfigurationSections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PCReportsEditor extends Composite implements ValueAwareEditor<PCReports> {

	@UiField
	TextBox specName;
	
	@UiField(provided = true)
	DateLabel creationDate = new DateLabel(DateTimeFormat.getFormat(Defaults.getDateFormat()));	
	
	@Ignore
	@UiField
	Label ALEID;
	
	@UiField (provided = true)
	NumberLabel<Long> totalMilliseconds = new NumberLabel<Long>(NumberFormat.getFormat("###0"));

	@Path("")
	@UiField  (provided = true)
	PCReportsConditions conditions = new PCReportsConditions();
	
	@Path("")
	@UiField
	PCReportListEditor reports;	
	
	@Ignore
	@UiField
	ConfigurationSections pcReportsEditorConfigSections;
	
	private static PCReportsEditorUiBinder uiBinder = GWT.create(PCReportsEditorUiBinder.class);
	interface PCReportsEditorUiBinder extends UiBinder<Widget, PCReportsEditor> { }
	

	public PCReportsEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public ConfigurationSections getConfigSections(){
		return pcReportsEditorConfigSections;
	}

	@Override
	public void setDelegate(EditorDelegate<PCReports> delegate) { }
	

	@Override
	public void flush() { }
	

	@Override
	public void onPropertyChange(String... paths) { }
	

	@Override
	public void setValue(PCReports value) {
		if(value == null || 
			(value.getInitiationCondition() == null &&
			 value.getInitiationTrigger() == null &&
			 value.getTerminationCondition() == null &&
			 value.getTerminationTrigger() == null)){
			 	pcReportsEditorConfigSections.remove(conditions);
		}
		//TODO ALEID-Problem
		if(value != null){
			ALEID.setText(value.getALEID());
		}
	}
}
