package havis.net.ui.middleware.client.shared.report.list;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

import havis.net.ui.middleware.client.shared.CustomListRenderer;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.NewItemEvent.Handler;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.shared.client.ConfigurationSection;
import havis.net.ui.shared.client.table.CreateRowEvent;
import havis.net.ui.shared.client.table.CustomTable;
import havis.net.ui.shared.client.table.DeleteRowEvent;

public class MultipleColumnListWidget<T> extends ConfigurationSection implements Editor<List<T>>, NewItemEvent.HasHandlers {

	private CustomListRenderer<T> renderer;
	
	@UiField
	CustomTable table;
	
	@Path("")
	ListEditor<T, MultipleColumnListRowBuilder<T>> editor;
	
	private OpenItemEvent.Handler openHandler;

	private class MultipleColumnEditorSource extends EditorSource<MultipleColumnListRowBuilder<T>> {
		
		private int colCount; 
		
		public MultipleColumnEditorSource(int colCount){
			this.colCount = colCount;
		}
		
		@Override
		public MultipleColumnListRowBuilder<T> create(int index) {
			MultipleColumnListRowBuilder<T> editor = new MultipleColumnListRowBuilder<T>(renderer, colCount);
			
			editor.setIndex(index);
			if(openHandler != null)
				editor.addOpenItemHandler(openHandler);
			table.addRow(editor);
			return editor;
		}

		@Override
		public void dispose(MultipleColumnListRowBuilder<T> subEditor) {
			table.deleteRow(subEditor);
		}
	}
	
	
	private static MultipleColumnListWidgetUiBinder uiBinder = GWT
			.create(MultipleColumnListWidgetUiBinder.class);

	@SuppressWarnings("rawtypes")
	interface MultipleColumnListWidgetUiBinder extends
			UiBinder<Widget, MultipleColumnListWidget> {
	}
	
	public MultipleColumnListWidget(CustomListRenderer<T> renderer, int colCount) {
		
		this.renderer = renderer;
		setText(ConstantsResource.INSTANCE.filter());
		editor = ListEditor.of(new MultipleColumnEditorSource(colCount));
		
		initWidget(uiBinder.createAndBindUi(this));
		
		table.setColumnWidth(0, 20, Unit.PCT);
		table.setColumnWidth(1, 16, Unit.PCT);
	}
	
	@UiHandler("table")
	public void onCreateRow(CreateRowEvent e) {
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
