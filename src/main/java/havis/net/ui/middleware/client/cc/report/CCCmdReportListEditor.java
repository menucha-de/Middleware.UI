package havis.net.ui.middleware.client.cc.report;

import havis.middleware.ale.service.cc.CCCmdReport;
import havis.net.ui.shared.client.ConfigurationSection;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class CCCmdReportListEditor extends ConfigurationSection implements Editor<List<CCCmdReport>> {
	
	@UiField
	FlowPanel cmdReports;

	private static CCCmdReportListEditorUiBinder uiBinder = GWT.create(CCCmdReportListEditorUiBinder.class);
	interface CCCmdReportListEditorUiBinder extends UiBinder<Widget, CCCmdReportListEditor> { }
	
	@Path("")
	ListEditor<CCCmdReport, CCCmdReportEditor> cmdReportsEditor;

	private class CCReportSource extends EditorSource<CCCmdReportEditor> {

		@Override
		public CCCmdReportEditor create(int index) {
			CCCmdReportEditor editor = new CCCmdReportEditor();
			cmdReports.insert(editor, index);
			editor.setOpen(true);
			return editor;
		}

		@Override
		public void dispose(CCCmdReportEditor subEditor) {
			cmdReports.remove(subEditor);			
		}
	}
	
	public CCCmdReportListEditor(){
		initWidget(uiBinder.createAndBindUi(this));
		cmdReportsEditor = ListEditor.of(new CCReportSource());
	}
}
