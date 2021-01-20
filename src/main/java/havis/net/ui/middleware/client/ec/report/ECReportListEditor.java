package havis.net.ui.middleware.client.ec.report;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import havis.middleware.ale.service.ec.ECReport;
import havis.net.ui.shared.client.ConfigurationSection;

public class ECReportListEditor extends ConfigurationSection implements Editor<List<ECReport>> {
	
	@UiField
	FlowPanel reports;

	private static ECReportListUiBinder uiBinder = GWT.create(ECReportListUiBinder.class);
	interface ECReportListUiBinder extends UiBinder<Widget, ECReportListEditor> { }
	
	@Path("")
	ListEditor<ECReport, ECReportEditor> reportsEditor;

	private class ECReportSource extends EditorSource<ECReportEditor> {

		@Override
		public ECReportEditor create(int index) {
			ECReportEditor editor = new ECReportEditor();
			reports.insert(editor, index);
			return editor;
		}

		@Override
		public void dispose(ECReportEditor subEditor) {
			reports.remove(subEditor);			
		}
	}
	
	public ECReportListEditor(){
		initWidget(uiBinder.createAndBindUi(this));
		reportsEditor = ListEditor.of(new ECReportSource());
	}
}
