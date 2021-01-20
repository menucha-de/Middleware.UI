package havis.net.ui.middleware.client.shared.trigger;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.NewItemEvent.Handler;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.shared.client.ConfigurationSection;
import havis.net.ui.shared.client.table.CreateRowEvent;
import havis.net.ui.shared.client.table.CustomTable;
import havis.net.ui.shared.client.table.DeleteRowEvent;

public class TriggerListWidget extends ConfigurationSection implements Editor<List<String>>, NewItemEvent.HasHandlers {

	@UiField
	CustomTable table;

	@Ignore
	Label uri = new Label();
	
	@Path("")
	ListEditor<String, TriggerListItemEditor> editor;

	private OpenItemEvent.Handler openHandler;

	private class TriggerEditorSource extends EditorSource<TriggerListItemEditor> {

		@Override
		public TriggerListItemEditor create(int index) {
			TriggerListItemEditor editor = new TriggerListItemEditor();
			editor.setIndex(index);
			if(openHandler != null)
				editor.addOpenItemHandler(openHandler);
			table.addRow(editor);
			return editor;
		}

		@Override
		public void dispose(TriggerListItemEditor subEditor) {
			table.deleteRow(subEditor);
		}
	}

	private static TriggerListWidgetUiBinder uiBinder = GWT.create(TriggerListWidgetUiBinder.class);

	interface TriggerListWidgetUiBinder extends UiBinder<Widget, TriggerListWidget> {
	}

	public TriggerListWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		table.setHeader(Arrays.asList(new String[] { "New Trigger" }));
		editor = ListEditor.of(new TriggerEditorSource());
	}

	@UiHandler("table")
	public void onCreateRow(CreateRowEvent event) {
		fireEvent(new NewItemEvent());
	}

	@UiHandler("table")
	public void onDeleteRow(DeleteRowEvent event) {
		editor.getList().remove(event.getIndex());
	}

	public void setOpenItemHandler(OpenItemEvent.Handler handler) {
		this.openHandler = handler;
	}

	@Override
	public HandlerRegistration addNewItemHandler(Handler handler) {
		return addHandler(handler, NewItemEvent.getType());
	}
}
