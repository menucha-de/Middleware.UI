package havis.net.ui.middleware.client.lr;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

import havis.middleware.ale.service.mc.MCLogicalReaderSpec;
import havis.net.ui.middleware.client.shared.CommonListEditor;
import havis.net.ui.middleware.client.shared.pattern.Pattern;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;
import havis.net.ui.middleware.client.utils.Utils;

public class ReaderEditor extends Composite implements ValueAwareEditor<MCLogicalReaderSpec> {

	private EditorDelegate<MCLogicalReaderSpec> delegate;

	@UiField
	TextBox name;

	@UiField
	ToggleButton enable;

	@Path("spec.isComposite")
	@UiField
	ToggleButton composite;

	@Path("spec.readers.reader")
	@UiField
	CommonListEditor reader;

	@Path("spec.properties")
	@UiField
	PropertiesEditor properties;

	ResourceBundle res = ResourceBundle.INSTANCE;

	private static ReaderEditorUiBinder uiBinder = GWT.create(ReaderEditorUiBinder.class);

	interface ReaderEditorUiBinder extends UiBinder<Widget, ReaderEditor> {
	}

	public ReaderEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		reader.setHeader(Arrays.asList("Add Reader"));
	}

	@UiHandler("composite")
	void onChangeComposite(ValueChangeEvent<Boolean> event) {
		reader.setVisible(event.getValue());
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		reader.setVisible(composite.getValue());
	}

	@Override
	public void setDelegate(EditorDelegate<MCLogicalReaderSpec> delegate) {
		this.delegate = delegate;
	}

	private void resetErrorStyle() {
		Utils.removeErrorStyle(name);
	}

	@Override
	public void flush() {
		resetErrorStyle();
		if (Utils.isNullOrEmpty(name.getValue())) {
			delegate.recordError(ConstantsResource.INSTANCE.errorInvalidEmptyField("Name"), name.getValue(),
					name.getValue());
			Utils.addErrorStyle(name);
		}
		if (Pattern.match(Pattern.PatternWhiteSpace, name.getValue())) {
			delegate.recordError(ConstantsResource.INSTANCE.errorInvalidSpecNameWhiteSpaces(), name.getValue(),
					name.getValue());
			Utils.addErrorStyle(name);
		}
		if (Pattern.match(Pattern.PatternSyntax, name.getValue())) {
			delegate.recordError(ConstantsResource.INSTANCE.errorInvalidSpecNameSpecialChar(), name.getValue(),
					name.getValue());
			Utils.addErrorStyle(name);
		}
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(MCLogicalReaderSpec value) {
	}
}
