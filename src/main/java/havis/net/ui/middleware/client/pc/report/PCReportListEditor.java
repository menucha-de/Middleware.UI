package havis.net.ui.middleware.client.pc.report;

import havis.middleware.ale.service.pc.PCReport;
import havis.middleware.ale.service.pc.PCReports;
import havis.net.ui.shared.client.ConfigurationSection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class PCReportListEditor extends ConfigurationSection implements Editor<PCReports> {
	
	@UiField
	FlowPanel reports;

	private static PCReportListUiBinder uiBinder = GWT.create(PCReportListUiBinder.class);
	interface PCReportListUiBinder extends UiBinder<Widget, PCReportListEditor> { }
	
	@Path("reports.report")
	ListEditor<PCReport, PCReportEditor> reportsEditor;

	private class PCReportSource extends EditorSource<PCReportEditor> {

		@Override
		public PCReportEditor create(int index) {
			PCReportEditor editor = new PCReportEditor();
			reports.insert(editor, index);
			return editor;
		}

		@Override
		public void dispose(PCReportEditor subEditor) {
			reports.remove(subEditor);			
		}
	}
	
	public PCReportListEditor(){
		initWidget(uiBinder.createAndBindUi(this));
		reportsEditor = ListEditor.of(new PCReportSource());
		setText("Reports");
	}


}
