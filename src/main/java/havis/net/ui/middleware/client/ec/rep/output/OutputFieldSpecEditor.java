package havis.net.ui.middleware.client.ec.rep.output;

import havis.middleware.ale.service.ec.ECReportOutputFieldSpec;
import havis.net.ui.middleware.client.shared.field.ECFieldSpecEditor;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.utils.Utils;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class OutputFieldSpecEditor extends Composite implements ValueAwareEditor<ECReportOutputFieldSpec> {

	private EditorDelegate<ECReportOutputFieldSpec> delegate;

	private ConstantsResource res = ConstantsResource.INSTANCE;

	@UiField
	TextBox name;

	@UiField
	ToggleButton includeFieldSpecInReport;

	@Path("fieldspec")
	@UiField
	public ECFieldSpecEditor fieldSpecEditor;
	
	private ECReportOutputFieldSpec ecReportOutputFieldSpec;

	private static OutputFieldItemEditorUiBinder uiBinder = GWT.create(OutputFieldItemEditorUiBinder.class);
	interface OutputFieldItemEditorUiBinder extends UiBinder<Widget, OutputFieldSpecEditor> { }
	

	public OutputFieldSpecEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		name.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Utils.removeErrorStyle(name);
				if(ecReportOutputFieldSpec != null){
					ecReportOutputFieldSpec.setName(name.getText());
					fieldSpecEditor.setParentFieldName(name.getText());
					//TODO: better use Object getNewTMSpec();
				}
			}
		});
		fieldSpecEditor.setIgnoreEmptyDataType(true);
	}

	@Override
	public void setValue(ECReportOutputFieldSpec value) {
		ecReportOutputFieldSpec = value;
	}

	@Override
	public void flush() {
		if (!Utils.isNullOrEmpty(name.getText())) {
	
			if (name.getText() == null || name.getText().trim().length() < 1) {
				delegate.recordError(res.errorIsInvalid(ConstantsResource.INSTANCE.fieldName()), null, name);
				Utils.addErrorStyle(name);
				return;
			}
		}

		if (fieldSpecEditor.getFieldtypeListBox().getSelectedIndex() < 1) {
			delegate.recordError(res.errorNotSpecified("Fieldtype"), null, null);
			Utils.addErrorStyle(fieldSpecEditor.getFieldtypeListBox());
			return;
		}
		Utils.removeErrorStyle(fieldSpecEditor.getFieldtypeListBox());
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setDelegate(EditorDelegate<ECReportOutputFieldSpec> delegate) {
		this.delegate = delegate;
	}
	
}
