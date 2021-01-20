package havis.net.ui.middleware.client.tm;

import havis.middleware.ale.service.tm.TMFixedFieldSpec;
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

public class TMFixedFieldListEditor extends Composite implements ValueAwareEditor<MCTagMemoryExtendedSpec> {

	private EditorDelegate<MCTagMemoryExtendedSpec> delegater;
	
	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	@UiField
	TextBox name;
	
	@UiField
	ToggleButton enable;
	
	@UiField
	CustomTable fields;
	
	@UiField
	@Ignore
	CustomListBox<TagMemoryType> type;

	@Path("fixedFieldListSpec.fixedFields.fixedField")
	ListEditor<TMFixedFieldSpec, TMFixedFieldEditor> fieldsEditor;
	
	private class FieldEditorSource extends EditorSource<TMFixedFieldEditor> {

		@Override
		public TMFixedFieldEditor create(int index) {
			TMFixedFieldEditor editor = new TMFixedFieldEditor();			
			fields.addRow(editor);
			return editor;
		}

		@Override
		public void dispose(TMFixedFieldEditor subEditor) {
			fields.deleteRow(subEditor);
		}
	}

	private static TMFixedFieldListEditorUiBinder uiBinder = GWT.create(TMFixedFieldListEditorUiBinder.class);

	interface TMFixedFieldListEditorUiBinder extends UiBinder<Widget, TMFixedFieldListEditor> {
	}

	public TMFixedFieldListEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		
		type.addItems(TagMemoryType.values());		
		
		List<String> headers = new ArrayList<String>();
		headers.add(res.fieldName());
		headers.add(res.bank());
		headers.add(res.length());
		headers.add(res.offset());
		headers.add(res.dataType());
		headers.add(res.format());
		fields.setHeader(headers);
				
		fieldsEditor = ListEditor.of(new FieldEditorSource());
		
		fields.setColumnWidth(0, 20, Unit.PCT);
		fields.setColumnWidth(1, 20, Unit.PCT);
		fields.setColumnWidth(2, 11, Unit.PCT);
		fields.setColumnWidth(3, 11, Unit.PCT);
		fields.setColumnWidth(4, 15, Unit.PCT);
		fields.setColumnWidth(5, 18, Unit.PCT);
		
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
		fieldsEditor.getList().add(new TMFixedFieldSpec());
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
				delegater.recordError(res.errorInvalidEmptyField("Name"), name.getValue(), name);
				Utils.addErrorStyle(name);
				return;
			}
			if (Pattern.match(Pattern.PatternWhiteSpace, name.getValue())){
				delegater.recordError(res.errorInvalidSpecNameWhiteSpaces(), name.getValue(), name);
				Utils.addErrorStyle(name);
				return;
			}
			if (Pattern.match(Pattern.PatternSyntax, name.getValue())){
				delegater.recordError(res.errorInvalidSpecNameSpecialChar(), name.getValue(), name);
				Utils.addErrorStyle(name);
				return;
			}
		}
		
	}

	@Override
	public void onPropertyChange(String... paths) { }

	@Override
	public void setValue(MCTagMemoryExtendedSpec value) { }
}
