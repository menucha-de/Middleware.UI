package havis.net.ui.middleware.client.tm;

import havis.middleware.ale.service.tm.TMVariableFieldSpec;
import havis.middleware.tdt.PackedObjectInvestigator.ColumnName;
import havis.net.ui.middleware.client.shared.pattern.Pattern;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.tm.data.TagMemoryBank;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.table.CustomWidgetRow;
import havis.net.ui.shared.client.widgets.CustomListBox;
import havis.net.ui.shared.client.widgets.CustomRenderer;
import havis.net.ui.shared.client.widgets.CustomSuggestBox;
import havis.net.ui.shared.client.widgets.CustomValueBox;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.TextBox;

public class TMVariableFieldEditor extends CustomWidgetRow implements ValueAwareEditor<TMVariableFieldSpec> {

	private EditorDelegate<TMVariableFieldSpec> delegater;

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

	CustomSuggestBox<String> oid = new CustomSuggestBox<String>(CustomSuggestBox.getStringParser());

	public TMVariableFieldEditor() {
		// add valid bank items
		bank.setItems(Arrays.asList(new Integer[] { TagMemoryBank.EPC_UII.getBank(), TagMemoryBank.USER.getBank() }));		

		oid.getListBox().setRenderer(new CustomRenderer<String>() {

			@Override
			public String render(String value) {
				Map<ColumnName, String> entry = CommonStorage.getOids().get(value);
				if (entry != null) {
					return value.substring("urn:oid:1.0.15961.9.".length()) + " - " + entry.get(ColumnName.FORMAT_9_NAME);
				} else if (value.startsWith("urn:oid:1.0.15434.3.")) {
					return value.substring("urn:oid:1.0.15434.3.".length());
				}
				return value;
			}
		});
		this.oid.getListBox().addItems(new ArrayList<String>(Arrays.asList("urn:oid:1.0.15434.3.37S")), "<DATA FORMAT 3>");
		this.oid.getListBox().addItems(new ArrayList<String>(CommonStorage.getOids().keySet()), "<DATA FORMAT 9>");

		// set style
		fieldnameBox.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableTextBox());
		bank.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableListBox());
		oid.getListBox().setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableListBox());
		oid.getTextBox().setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableTextBox());

		addColumn(fieldnameBox);
		addColumn(bank);
		addColumn(oid);
	}

	@Override
	public void setDelegate(EditorDelegate<TMVariableFieldSpec> delegate) {
		this.delegater = delegate;
	}

	private void resetErrorStyle(){
		Utils.removeErrorStyle(fieldname);
		Utils.removeErrorStyle(oid);
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
			if (!Pattern.match(Pattern.PatternOid, oid.getValue())) {				
				delegater.recordError(res.errorInvalid("OID"), oid.getValue(), oid);
				Utils.addErrorStyle(oid);
				return;
			}
		}
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(TMVariableFieldSpec value) {
		bank.setSelectedValue(value.getBank());		
		if(bank.getSelectedIndex() < 1){
			bank.setSelectedIndex(1);
		}
	}

	public String getFieldname() {
		return fieldnameBox.getText();
	}
}
