package havis.net.ui.middleware.client.shared.report.list;

import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.NewItemEvent.Handler;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.shared.client.ConfigurationSection;
import havis.net.ui.shared.client.table.CreateRowEvent;
import havis.net.ui.shared.client.table.CustomTable;
import havis.net.ui.shared.client.table.DeleteRowEvent;
import havis.net.ui.shared.client.widgets.CustomRenderer;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

public class SingleColumnListWidget<T> extends ConfigurationSection implements Editor<List<T>>, NewItemEvent.HasHandlers {
	
	private CustomRenderer<T> renderer;
	
	@UiField
	CustomTable table; 
	
	@Path("")
	ListEditor<T, SingleColumnRowBuilder<T>> editor;
	
	private OpenItemEvent.Handler openHandler;

	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	private class SingleColumnEditorSource extends EditorSource<SingleColumnRowBuilder<T>> {

		@Override
		public SingleColumnRowBuilder<T> create(int index) {
			SingleColumnRowBuilder<T> editor = new SingleColumnRowBuilder<T>(renderer);
			editor.setIndex(index);
			if(openHandler != null)
				editor.addOpenItemHandler(openHandler);
			table.addRow(editor);
			return editor;
		}

		@Override
		public void dispose(SingleColumnRowBuilder<T> subEditor) {
			table.deleteRow(subEditor);
		}
	}
	
	private static SingleColumnListWidgetUiBinder uiBinder = GWT.create(SingleColumnListWidgetUiBinder.class);
	
	@SuppressWarnings("rawtypes")
	interface SingleColumnListWidgetUiBinder extends UiBinder<Widget, SingleColumnListWidget> { }
	
	
	public SingleColumnListWidget(CustomRenderer<T> renderer) {
		
		this.renderer = renderer;
		setText(res.reports());
		editor = ListEditor.of(new SingleColumnEditorSource());
		
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("table")
	void onCreateRow(CreateRowEvent event) {
		fireEvent(new NewItemEvent());
	}
	
	@UiHandler("table")
	public void onDeleteRow(DeleteRowEvent e) {
		editor.getList().remove(e.getIndex());
	}

	public void setHeader(List<String> header) {
		table.setHeader(header);
	}

	public void setOpenItemHandler(OpenItemEvent.Handler handler) {
		this.openHandler = handler;
	}

	@Override
	public HandlerRegistration addNewItemHandler(Handler handler) {
		return addHandler(handler, NewItemEvent.getType());
	}

	
	public boolean getEnableUpDown(){
		return table.getEnableUpDown();
	}
	
	public void setEnableUpDown(boolean enableUpDown){
		table.setEnableUpDown(enableUpDown);
	}
	
	
	public List<T> getEditorList(){
		List<T> lst = new ArrayList<T>();
		for(int i = 0; i < table.getCustomWidgetCount(); i++){
			@SuppressWarnings("unchecked")
			SingleColumnRowBuilder<T> editor = (SingleColumnRowBuilder<T>) table.getRow(i);
			lst.add(editor.getValue());
		}
		return lst;
	}
	
	public boolean isDirty(){
		return table.isDirty();
	}
	
	public int getCustomWidgetCount(){
		return table.getCustomWidgetCount();
	}
	
	public CustomTable getTable(){
		return table;
	}
	
}

