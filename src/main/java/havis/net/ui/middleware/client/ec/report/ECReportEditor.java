package havis.net.ui.middleware.client.ec.report;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import havis.middleware.ale.service.ec.ECReport;
import havis.middleware.ale.service.ec.ECReportGroup;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.shared.client.ConfigurationSection;

public class ECReportEditor extends ConfigurationSection implements ValueAwareEditor<ECReport> {

	private static ECReportUiBinder uiBinder = GWT.create(ECReportUiBinder.class);
	interface ECReportUiBinder extends UiBinder<Widget, ECReportEditor> { }
	
	private ConstantsResource res = ConstantsResource.INSTANCE;

	@UiField
	FlowPanel groups;

	@Path("group")
	ListEditor<ECReportGroup, ECReportGroupEditor> reportsEditor;

	private class ECReportGroupSource extends EditorSource<ECReportGroupEditor> {

		@Override
		public ECReportGroupEditor create(int index) {
			ECReportGroupEditor editor = new ECReportGroupEditor();
			groups.insert(editor, index);
			return editor;
		}

		@Override
		public void dispose(ECReportGroupEditor subEditor) {
			groups.remove(subEditor);
		}
	}

	public ECReportEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		reportsEditor = ListEditor.of(new ECReportGroupSource());
	}

	@Override
	public void setDelegate(EditorDelegate<ECReport> delegate) {

	}

	@Override
	public void flush() {

	}

	@Override
	public void onPropertyChange(String... paths) {

	}

	@Override
	public void setValue(ECReport value) {
		if(value != null){
			setText(res.report() + ": " + value.getReportName());
		}
	}

}