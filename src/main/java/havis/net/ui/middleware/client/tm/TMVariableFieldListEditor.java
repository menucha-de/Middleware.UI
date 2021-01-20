package havis.net.ui.middleware.client.tm;

import havis.middleware.ale.service.tm.TMVariableFieldSpec;
import havis.net.ui.middleware.client.shared.pattern.Pattern;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.tm.data.MCTagMemoryExtendedSpec;
import havis.net.ui.middleware.client.tm.data.TagMemoryType;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.table.CreateRowEvent;
import havis.net.ui.shared.client.table.CustomTable;
import havis.net.ui.shared.client.table.DeleteRowEvent;
import havis.net.ui.shared.client.widgets.CustomListBox;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class TMVariableFieldListEditor extends Composite implements ValueAwareEditor<MCTagMemoryExtendedSpec> {

	private EditorDelegate<MCTagMemoryExtendedSpec> delegater;
	
	@UiField
	TextBox name;
	@UiField
	ToggleButton enable;		
	@UiField
	CustomTable fields;
	@Ignore
	@UiField
	CustomListBox<TagMemoryType> type;
	
	@Path("variableFieldListSpec.variableFields.variableField")
	ListEditor<TMVariableFieldSpec, TMVariableFieldEditor> fieldsEditor;

	private class FieldEditorSource extends EditorSource<TMVariableFieldEditor> {

		@Override
		public TMVariableFieldEditor create(int index) {
			TMVariableFieldEditor editor = new TMVariableFieldEditor();			
			fields.addRow(editor);
			return editor;
		}

		@Override
		public void dispose(TMVariableFieldEditor subEditor) {
			fields.deleteRow(subEditor);
		}
	}
	
	private static TMVariableFieldListEditorUiBinder uiBinder = GWT.create(TMVariableFieldListEditorUiBinder.class);

	interface TMVariableFieldListEditorUiBinder extends UiBinder<Widget, TMVariableFieldListEditor> {
	}

	public TMVariableFieldListEditor() {		
		initWidget(uiBinder.createAndBindUi(this));
		
		type.addItems(TagMemoryType.values());
		
		List<String> headers = new ArrayList<String>();
		headers.add(ConstantsResource.INSTANCE.fieldName());
		headers.add(ConstantsResource.INSTANCE.bank());
		headers.add(ConstantsResource.INSTANCE.oid());
		fields.setHeader(headers);
		
		fieldsEditor = ListEditor.of(new FieldEditorSource());
		
		fields.setColumnWidth(0, 30, Unit.PCT);
		fields.setColumnWidth(1, 20, Unit.PCT);
		fields.setColumnWidth(2, 35, Unit.PCT);
		fields.setColumnWidth(3, 5, Unit.PCT);
		
		name.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Utils.removeErrorStyle(name);
			}
		});
	}

	@Override
	public void setDelegate(EditorDelegate<MCTagMemoryExtendedSpec> delegate) {
		this.delegater = delegate;
	}

	@UiHandler("fields")
	void onCreateRow(CreateRowEvent event) {
		fieldsEditor.getList().add(new TMVariableFieldSpec());
	}

	@UiHandler("fields")
	void onDeleteRow(DeleteRowEvent event) {
		fieldsEditor.getList().remove(event.getIndex());
	}

	private void resetErrorStyle() {
		Utils.removeErrorStyle(name);
	}
	
	@Override
	public void flush() {
		resetErrorStyle();
		if(delegater != null){
			if (Utils.isNullOrEmpty(name.getValue())){
				delegater.recordError(ConstantsResource.INSTANCE.errorInvalidEmptyField("Name"), name.getValue(), name);
				Utils.addErrorStyle(name);
				return;
			}
			if (Pattern.match(Pattern.PatternWhiteSpace, name.getValue())){
				delegater.recordError(ConstantsResource.INSTANCE.errorInvalidSpecNameWhiteSpaces(), name.getValue(), name);
				Utils.addErrorStyle(name);
				return;
			}
			if (Pattern.match(Pattern.PatternSyntax, name.getValue())){
				delegater.recordError(ConstantsResource.INSTANCE.errorInvalidSpecNameSpecialChar(), name.getValue(), name);
				Utils.addErrorStyle(name);
				return;
			}
			if(fieldsEditor.getList().size() < 1){
				delegater.recordError(ConstantsResource.INSTANCE.errorInvalidEmptyField("Fields list"), 0, fields);
				return;
			}
		}
	}

	@Override
	public void onPropertyChange(String... paths) { }
	

	@Override
	public void setValue(MCTagMemoryExtendedSpec value) { }
	
}
