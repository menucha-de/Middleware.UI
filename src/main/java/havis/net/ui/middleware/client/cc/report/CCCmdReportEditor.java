package havis.net.ui.middleware.client.cc.report;

import havis.middleware.ale.service.cc.CCCmdReport;
import havis.middleware.ale.service.cc.CCTagReport;
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

public class CCCmdReportEditor extends ConfigurationSection implements ValueAwareEditor<CCCmdReport> {

	@UiField
	FlowPanel tagReports;

	private static PCEventReportEditorUiBinder uiBinder = GWT.create(PCEventReportEditorUiBinder.class);
	interface PCEventReportEditorUiBinder extends UiBinder<Widget, CCCmdReportEditor> { }
	
	@Path("tagReports.tagReport")
	ListEditor<CCTagReport, CCTagReportEditor> tagReportsEditor;

	private class CCTagReportSource extends EditorSource<CCTagReportEditor> {

		@Override
		public CCTagReportEditor create(int index) {
			CCTagReportEditor editor = new CCTagReportEditor();
			tagReports.insert(editor, index);
			editor.setOpen(true);
			return editor;
		}

		@Override
		public void dispose(CCTagReportEditor subEditor) {
			tagReports.remove(subEditor);
		}
	}
	
	public CCCmdReportEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		tagReportsEditor = ListEditor.of(new CCTagReportSource());
	}

	@Override
	public void flush() { }

	@Override
	public void onPropertyChange(String... paths) { }

	@Override
	public void setDelegate(EditorDelegate<CCCmdReport> delegate) { }

	@Override
	public void setValue(CCCmdReport value) {
		if(value != null && !Utils.isNullOrEmpty(value.getCmdSpecName())){
			setText(value.getCmdSpecName());
		}
		else{
			setText("...");
		}
	}
}