package havis.net.ui.middleware.client.cc.report;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

import havis.middleware.ale.service.cc.CCParameterListEntry;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.ConfigurationSection;
import havis.net.ui.shared.client.table.CreateRowEvent;
import havis.net.ui.shared.client.table.CustomTable;

public class CCParametersEditor extends ConfigurationSection implements ValueAwareEditor<List<CCParameterListEntry>> {

	private EditorDelegate<List<CCParameterListEntry>> delegate;

	@UiField
	CustomTable parameters;

	@Path("")
	ListEditor<CCParameterListEntry, CCParameterEditor> editor;

	private CCParameterEditor first;

	private class ParameterEditorSource extends EditorSource<CCParameterEditor> {

		@Override
		public CCParameterEditor create(int index) {
			final CCParameterEditor prpEditor = new CCParameterEditor();
			parameters.addRow(prpEditor);
			if (index == 0) {
				first = prpEditor;
			}
			return prpEditor;
		}

		@Override
		public void dispose(CCParameterEditor subEditor) {
			parameters.deleteRow(subEditor);
		}
	}

	private static CCParametersEditorUiBinder uiBinder = GWT.create(CCParametersEditorUiBinder.class);

	interface CCParametersEditorUiBinder extends UiBinder<Widget, CCParametersEditor> {
	}

	@UiHandler("parameters")
	void onCreateRow(CreateRowEvent event) {
		focusFirst();
	}

	public CCParametersEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		setText("Parameters");
		editor = ListEditor.of(new ParameterEditorSource());
		parameters.setHeader(Arrays.asList("Name", "Value"));
	}

	@Override
	public void setDelegate(EditorDelegate<List<CCParameterListEntry>> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void flush() {
		int i = 0;
		while (i < editor.getList().size()) {
			CCParameterListEntry p = editor.getList().get(i);
			if (Utils.isNullOrEmpty(p.getName())) {
				editor.getList().remove(i);
			} else {
				if (Utils.isNullOrEmpty(p.getValue())) {
					delegate.recordError("Value cannot be empty or null for Parameter '" + p.getName() + "'!",
							p.getName(), p);
				}
				++i;
			}
		}
		editor.flush();
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(List<CCParameterListEntry> value) {
		focusFirst();
	}

	public void focusFirst() {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				first.setStartFocus();
			}
		});
	}
}
