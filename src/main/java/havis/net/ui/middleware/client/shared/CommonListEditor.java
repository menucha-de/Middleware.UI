package havis.net.ui.middleware.client.shared;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.ConfigurationSection;
import havis.net.ui.shared.client.table.CreateRowEvent;
import havis.net.ui.shared.client.table.CustomTable;
import havis.net.ui.shared.client.table.DeleteRowEvent;

public class CommonListEditor extends ConfigurationSection
		implements ValueAwareEditor<List<String>> {

	private Map<String, List<String>> groupValues = new HashMap<String, List<String>>();
	
	private ConstantsResource res = ConstantsResource.INSTANCE;

	@UiField
	CustomTable table;

	@Path("")
	ListEditor<String, ListItemsEditor> editor;
	
	private BaseActivity activity;

	private NewItemEvent.Handler handler;

	private static CommonListEditorUiBinder uiBinder = GWT.create(CommonListEditorUiBinder.class);

	interface CommonListEditorUiBinder extends UiBinder<Widget, CommonListEditor> {
	}

	private class CommonEditorSource extends EditorSource<ListItemsEditor> {
		@Override
		public ListItemsEditor create(final int index) {
			ListItemsEditor editor = new ListItemsEditor();
			if (handler != null)
				editor.addNewItemHandler(handler);
			editor.setValues(groupValues);
			table.addRow(editor);
			return editor;
		}

		@Override
		public void dispose(ListItemsEditor subEditor) {
			table.deleteRow(subEditor);
		}
	}

	public CommonListEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		editor = ListEditor.of(new CommonEditorSource());
	}

	@UiHandler("table")
	void onCreateRow(CreateRowEvent event) {
		
		List<ListItemsEditor> editors = editor.getEditors();
		if(editors.size() > 0){
			//check if user tries to add more than one empty row (without having selected a reader previously)
			if(activity == null){
				GWT.log("CommonListEditor, activity is null! Can't display errors.");
			}
			
			for(ListItemsEditor edit : editors){
				
				if(edit.getValue() == null){
					if(activity != null){
						activity.showErrorMessage(res.errorSelectListItem());						
					}
					return;
				}
			}
		}
		
		editor.getList().add("<empty>");
	}

	@UiHandler("table")
	void onDeleteRow(DeleteRowEvent event) {
		editor.getList().remove(event.getIndex());
	}

	public void setHeader(List<String> header) {
		table.setHeader(header);
	}

	/**
	 * Sets values
	 * 
	 * @param values
	 */
	public void setAcceptableValues(List<String> values) {
		this.groupValues = new HashMap<>();
		groupValues.put(null, values);
		for (ListItemsEditor re : editor.getEditors()) {
			re.setValues(groupValues);
		}
	}

	/**
	 * Sets grouped values. Values with key NULL won't be in a group.
	 * 
	 * @param values
	 */
	public void setAcceptableValues(Map<String, List<String>> values) {
		this.groupValues = values;
		for (ListItemsEditor re : editor.getEditors()) {
			re.setValues(groupValues);
		}
	}

	public void setNewItemHandler(NewItemEvent.Handler handler) {
		this.handler = handler;
	}
	

	@Override
	public void setDelegate(EditorDelegate<List<String>> delegate) { }
	

	@Override
	public void flush() {
		if (editor != null && editor.getList() != null) {
			for (int i = 0; i < editor.getList().size(); i++)
				if (Utils.isNullOrEmpty(editor.getList().get(i))) {
					editor.getList().remove(i);
					i = -1;
				}
			editor.flush();
		}
	}

	@Override
	public void onPropertyChange(String... paths) { }
	

	@Override
	public void setValue(List<String> value) { }
	
	/**
	 * Setter to be able to use e.g. showErrorMessage()
	 * @param activity
	 */
	public void  setBaseActivity(BaseActivity activity){
		this.activity = activity;
	}
}