package havis.net.ui.middleware.client.pc.report;

import havis.middleware.ale.service.pc.PCEventReport;
import havis.middleware.ale.service.pc.PCReport;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.ConfigurationSection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class PCReportEditor extends ConfigurationSection implements ValueAwareEditor<PCReport> {

	private static PCReportUiBinder uiBinder = GWT.create(PCReportUiBinder.class);
	interface PCReportUiBinder extends UiBinder<Widget, PCReportEditor> { }

	@UiField
	FlowPanel eventReports;

	
	@Path("eventReports.eventReport")
	ListEditor<PCEventReport, PCEventReportEditor> eventReportsEditor;
	
	
	private class PCEventReportSource extends EditorSource<PCEventReportEditor> {

		@Override
		public PCEventReportEditor create(int index) {
			PCEventReportEditor editor = new PCEventReportEditor();
			eventReports.insert(editor, index);
			return editor;
		}

		@Override
		public void dispose(PCEventReportEditor subEditor) {
			eventReports.remove(subEditor);
		}
	}

	public PCReportEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		eventReportsEditor = ListEditor.of(new PCEventReportSource());
	}
	

	@Override
	public void flush() { }
	

	@Override
	public void setValue(PCReport value) {
		if(value != null && !Utils.isNullOrEmpty(value.getReportName())){
			setText(value.getReportName());
		}
		else{
			setText("...");
		}
	}
	
	@Override
	public void setDelegate(EditorDelegate<PCReport> delegate) { }
	
	
	@Override
	public void onPropertyChange(String... paths) { }

}