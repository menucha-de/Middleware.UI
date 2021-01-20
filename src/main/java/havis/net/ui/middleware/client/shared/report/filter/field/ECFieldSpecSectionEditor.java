package havis.net.ui.middleware.client.shared.report.filter.field;

import havis.middleware.ale.service.ECFilterListMember;
import havis.middleware.ale.service.tm.TMFixedFieldSpec;
import havis.middleware.ale.service.tm.TMVariableFieldSpec;
import havis.net.ui.middleware.client.shared.field.ECFieldSpecEditor;
import havis.net.ui.shared.client.ConfigurationSection;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class ECFieldSpecSectionEditor extends ConfigurationSection implements ValueAwareEditor<ECFilterListMember>{
	
	@Path("fieldspec")
	@UiField
	public ECFieldSpecEditor fieldSpecEditor;
	
	private static ECFieldSpecSectionEditorUiBinder uiBinder = GWT.create(ECFieldSpecSectionEditorUiBinder.class);
	interface ECFieldSpecSectionEditorUiBinder extends UiBinder<Widget, ECFieldSpecSectionEditor> { }
	
	public ECFieldSpecSectionEditor(){
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setTMFields(Map<String, List<TMFixedFieldSpec>> fixed, Map<String, List<TMVariableFieldSpec>> variable){
		fieldSpecEditor.setTMFields(fixed, variable);
	}
	
	@Override
	public void setValue(ECFilterListMember value) {
		fieldSpecEditor.setValue(value.getFieldspec());
		fieldSpecEditor.setIgnoreEmptyDataType(true);
	}
	
	@Override
	public void setDelegate(EditorDelegate<ECFilterListMember> delegate) { }

	@Override
	public void flush() { }

	@Override
	public void onPropertyChange(String... paths) { }
}
