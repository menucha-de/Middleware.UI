package havis.net.ui.middleware.client.tm;

import havis.middleware.ale.service.tm.TMFixedFieldSpec;
import havis.net.ui.middleware.client.shared.pattern.Pattern;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;
import havis.net.ui.middleware.client.tm.data.DataType;
import havis.net.ui.middleware.client.tm.data.Format;
import havis.net.ui.middleware.client.tm.data.TagMemoryBank;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.table.CustomWidgetRow;
import havis.net.ui.shared.client.widgets.CustomListBox;
import havis.net.ui.shared.client.widgets.CustomRenderer;
import havis.net.ui.shared.client.widgets.CustomValueBox;

import java.io.IOException;
import java.text.ParseException;

import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.TextBox;

public class TMFixedFieldEditor extends CustomWidgetRow
		implements ValueAwareEditor<TMFixedFieldSpec> {

	private EditorDelegate<TMFixedFieldSpec> delegater;
	
	private ConstantsResource res = ConstantsResource.INSTANCE;

	@Ignore
	TextBox fieldnameBox = new TextBox();
	CustomValueBox<String> fieldname = new CustomValueBox<String>(fieldnameBox, new Renderer<String>() {

		@Override
		public String render(String object) {
			return object;
		}

		@Override
		public void render(String object, Appendable appendable) throws IOException {
			appendable.append(object);
		}
	}, new Parser<String>() {

		@Override
		public String parse(CharSequence text) throws ParseException {
			if (text == null)
				return null;
			return text.toString().trim();
		}
	});

	CustomListBox<Integer> bank = new CustomListBox<Integer>(new CustomRenderer<Integer>() {

		@Override
		public String render(Integer value) {
			TagMemoryBank bank = TagMemoryBank.getTagMemoryBank(value);
			if (bank != null)
				return bank.toString();
			return null;
		}
	});

	@Ignore
	TextBox lengthBox = new TextBox();
	CustomValueBox<Integer> length = new CustomValueBox<Integer>(lengthBox, new Renderer<Integer>() {

		@Override
		public String render(Integer object) {
			if (object == null)
				return null;
			return object.toString();
		}

		@Override
		public void render(Integer object, Appendable appendable) throws IOException {
			appendable.append(render(object));
		}
	}, new Parser<Integer>() {

		@Override
		public Integer parse(CharSequence text) throws ParseException {
			if (Utils.isNullOrEmpty(text))
				return 0;
			try {
				return Integer.parseInt(text.toString(), 10);
			} catch (NumberFormatException e) {
				return 0;
			}
		}
	});

	@Ignore
	TextBox offsetBox = new TextBox();
	CustomValueBox<Integer> offset = new CustomValueBox<Integer>(offsetBox, new Renderer<Integer>() {

		@Override
		public String render(Integer object) {
			if (object == null)
				return null;
			return object.toString();
		}

		@Override
		public void render(Integer object, Appendable appendable) throws IOException {
			appendable.append(render(object));
		}
	}, new Parser<Integer>() {

		@Override
		public Integer parse(CharSequence text) throws ParseException {
			if (text == null)
				return -1;
			try {
				return Integer.parseInt(text.toString(), 10);
			} catch (NumberFormatException e) {
				return -1;
			}
		}
	});

	CustomListBox<String> defaultDatatype = new CustomListBox<String>();

	CustomListBox<String> defaultFormat = new CustomListBox<String>();

	public TMFixedFieldEditor() {

		for (TagMemoryBank b : TagMemoryBank.values())
			bank.addItem(b.getBank());

		for (DataType t : DataType.values()){
			if(t != DataType.ISO_15962_STRING)
				defaultDatatype.addItem(t.getDatatype());
		}

		defaultDatatype.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				DataType type = DataType.getDataType(defaultDatatype.getSelectedValue());
				defaultFormat.clear();
				if (type != null) {
					for (Format f : type.getValidFormats())
						defaultFormat.addItem(f.toString());
				}
			}
		});

		fieldnameBox.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableTextBox());
		bank.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableListBox());
		lengthBox.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableTextBox());
		offsetBox.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableTextBox());
		defaultDatatype.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableListBox());
		defaultFormat.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableListBox());

		addColumn(fieldnameBox);
		addColumn(bank);
		addColumn(lengthBox);
		addColumn(offsetBox);
		addColumn(defaultDatatype);
		addColumn(defaultFormat);
	}

	private void resetErrorStyle() {
		Utils.removeErrorStyle(fieldnameBox);
		Utils.removeErrorStyle(bank);
		Utils.removeErrorStyle(lengthBox);
		Utils.removeErrorStyle(offsetBox);
		Utils.removeErrorStyle(defaultDatatype);
		Utils.removeErrorStyle(defaultFormat);
	}

	@Override
	public void setDelegate(EditorDelegate<TMFixedFieldSpec> delegate) {
		this.delegater = delegate;
	}

	@Override
	public void flush() {
		resetErrorStyle();
		if (delegater != null) {
			if (Utils.isNullOrEmpty(fieldname.getValue())) {
				delegater.recordError(res.errorInvalidEmptyField(res.fieldName()), fieldname.getValue(),
						fieldname.getValue());
				Utils.addErrorStyle(fieldname);
				return;
			}
			if (Pattern.match(Pattern.PatternWhiteSpace, fieldname.getValue())) {
				delegater.recordError(res.errorInvalidFieldNameWhiteSpaces(),
						fieldname.getValue(), fieldname.getValue());
				Utils.addErrorStyle(fieldname);
				return;
			}
			if (Pattern.match(Pattern.PatternSyntax, fieldname.getValue())) {
				delegater.recordError(res.errorInvalidFieldNameSpecialChar(),
						fieldname.getValue(), fieldname.getValue());
				Utils.addErrorStyle(fieldname);
				return;
			}
			if (bank.getValue() == null) {
				delegater.recordError(res.errorNotSpecified("Bank"), fieldname.getValue(),
						fieldname.getValue());
				Utils.addErrorStyle(bank);
				return;
			}
			if (length.getValue() < 1) {
				delegater.recordError(res.errorInvalidLength(), length.getValue(),
						length.getValue());
				Utils.addErrorStyle(lengthBox);
				return;
			}
			if (offset.getValue() < 0) {
				delegater.recordError(res.errorInvalidOffset(), offset.getValue(),
						offset.getValue());
				Utils.addErrorStyle(offsetBox);
				return;
			}
			if (defaultDatatype.getValue() == null) {
				delegater.recordError(res.errorNotSpecified(res.dataType()), defaultDatatype.getValue(),
						defaultDatatype.getValue());
				Utils.addErrorStyle(defaultDatatype);
				return;
			}
			if (defaultFormat.getValue() == null) {
				delegater.recordError(res.errorNotSpecified(res.format()), defaultFormat.getValue(),
						defaultFormat.getValue());
				Utils.addErrorStyle(defaultFormat);
			}
		}
		
	}

	@Override
	public void onPropertyChange(String... paths) { }
	

	@Override
	public void setValue(TMFixedFieldSpec value) { }

	public String getFieldname(){
		return fieldnameBox.getText();
	}
	
}
