package havis.net.ui.middleware.client.shared.field;

import havis.middleware.ale.service.ECFieldSpec;
import havis.middleware.ale.service.tm.TMFixedFieldSpec;
import havis.middleware.ale.service.tm.TMVariableFieldSpec;
import havis.middleware.tdt.PackedObjectInvestigator.ColumnName;
import havis.net.ui.middleware.client.cc.cmd.operation.CCOpType;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.NewItemEvent.Handler;
import havis.net.ui.middleware.client.shared.pattern.Pattern;
import havis.net.ui.middleware.client.shared.report.model.FieldType;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.tm.data.DataType;
import havis.net.ui.middleware.client.tm.data.Fieldname;
import havis.net.ui.middleware.client.tm.data.Format;
import havis.net.ui.middleware.client.tm.data.TagMemoryBank;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.widgets.CustomListBox;
import havis.net.ui.shared.client.widgets.CustomRenderer;
import havis.net.ui.shared.client.widgets.CustomSuggestBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.SimpleEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ECFieldSpecEditor extends Composite implements ValueAwareEditor<ECFieldSpec>, NewItemEvent.HasHandlers {

	/**
	 * See 6.1.9.1 of ALE Specification, Version 1.1.1
	 */
	private static final String PATTERN_FIXED = "^@([0-9]+)\\.([0-9]+)\\.([0-9]+)$";
	private static final int FIXED_GROUP_BANK = 1;
	private static final int FIXED_GROUP_LENGTH = 2;
	private static final int FIXED_GROUP_OFFSET = 3;

	/**
	 * See 6.1.9.2 of ALE Specification, Version 1.1.1
	 */
	private static final String PATTERN_VARIABLE = "^@([0-9]+)\\.(urn:oid:\\d+(\\.[0-9A-Z]+)*(\\.\\*)?)$";
	private static final int VARIABLE_GROUP_BANK = 1;
	private static final int VARIABLE_GROUP_OID = 2;

	private boolean isFilterPatternView = false;
	private boolean isGroupingFilterPatternView = false;

	private ECFieldSpec spec;
	private EditorDelegate<ECFieldSpec> delegate;
	private RegExp checkNumbers = RegExp.compile("^[0-9]+$");

	private ConstantsResource res = ConstantsResource.INSTANCE;

	private boolean ignoreEmptyDataType = false;

	@UiField(provided = true)
	CustomListBox<String> datatype = new CustomListBox<String>(true);

	@UiField(provided = true)
	CustomListBox<String> format = new CustomListBox<String>(true);

	@Ignore
	@UiField(provided = true)
	CustomListBox<String> fieldnameList = new CustomListBox<String>(true);

	@Ignore
	@UiField
	CustomListBox<TagMemoryBank> bank;

	@Ignore
	@UiField
	CustomListBox<FieldType> fieldtype;

	SimpleEditor<String> fieldname = SimpleEditor.of();

	@Ignore
	@UiField
	TextBox offset;

	@Ignore
	@UiField
	TextBox length;

	@Ignore
	@UiField(provided = true)
	CustomSuggestBox<String> oid = new CustomSuggestBox<String>(CustomSuggestBox.getStringParser());

	@Ignore
	@UiField
	FlowPanel fieldtypeRow;

	@Ignore
	@UiField
	FlowPanel fieldnameListRow;

	@Ignore
	@UiField
	FlowPanel bankRow;

	@Ignore
	@UiField
	FlowPanel oidRow;

	@Ignore
	@UiField
	FlowPanel lengthRow;

	@Ignore
	@UiField
	FlowPanel offsetRow;

	@Ignore
	@UiField
	FlowPanel datatypeRow;

	@Ignore
	@UiField
	FlowPanel formatRow;

	private String parentFieldName;

	private Map<String, List<TMFixedFieldSpec>> tmFixedFieldSpecs = new HashMap<String, List<TMFixedFieldSpec>>();

	private Map<String, List<TMVariableFieldSpec>> tmVariableFieldSpecs = new HashMap<String, List<TMVariableFieldSpec>>();

	private static ECFieldSpecEditor2UiBinder uiBinder = GWT.create(ECFieldSpecEditor2UiBinder.class);

	interface ECFieldSpecEditor2UiBinder extends UiBinder<Widget, ECFieldSpecEditor> {
	}

	public ECFieldSpecEditor() {
		initWidget(uiBinder.createAndBindUi(this));

		setFieldNameList();
		oid.getListBox().setRenderer(new CustomRenderer<String>() {

			@Override
			public String render(String value) {
				Map<ColumnName, String> entry = CommonStorage.getOids().get(value);
				if (entry != null) {
					return value.substring("urn:oid:1.0.15961.9.".length()) + " - " + entry.get(ColumnName.FORMAT_9_NAME);
				} else if (Objects.equals("urn:oid:1.0.15961.*", value)) {
					return ConstantsResource.INSTANCE.oidListAllDataFormats();
				} else if (Objects.equals("urn:oid:1.0.15961.9.*", value)) {
					return ConstantsResource.INSTANCE.oidListDataFormat9();
				} else if (Objects.equals("urn:oid:1.0.15434.3.*", value)) {
					return ConstantsResource.INSTANCE.oidListDataFormat3();
				}
				return value;
			}
		});
		oid.getListBox().setItems(new String[] { "urn:oid:1.0.15961.*", "urn:oid:1.0.15961.9.*", "urn:oid:1.0.15434.3.*" });
		oid.getListBox().addItems(new ArrayList<String>(Arrays.asList("urn:oid:1.0.15434.3.37S")),
				"<" + ConstantsResource.INSTANCE.oidListDataFormat3() + ">");
		oid.getListBox().addItems(new ArrayList<String>(CommonStorage.getOids().keySet()),
				"<" + ConstantsResource.INSTANCE.oidListDataFormat9() + ">");
	}

	@Ignore
	public CustomListBox<FieldType> getFieldtypeListBox() {
		return fieldtype;
	}

	public String getLength() {
		return length.getText();
	}

	public String getOffset() {
		return offset.getText();
	}

	@Override
	public HandlerRegistration addNewItemHandler(Handler handler) {
		return addHandler(handler, NewItemEvent.getType());
	}

	public void setParentFieldName(String name) {
		parentFieldName = name;
	}

	@Override
	public void setDelegate(EditorDelegate<ECFieldSpec> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void flush() {

		if (fieldtype.getValue() == FieldType.USER_DEFINED_FIXED) {
			if (bank.getSelectedValue() == null) {
				delegate.recordError(res.errorNotSpecified(res.bank()), null, bank);
				Utils.addErrorStyle(bank);
				return;
			}
			Utils.removeErrorStyle(bank);

			if (Utils.isNullOrEmpty(length.getValue())) {
				delegate.recordError(res.errorInvalidEmptyField(res.length()), null, length);
				Utils.addErrorStyle(length);
				return;
			}
			if (checkNumbers.exec(length.getValue()) == null) {
				delegate.recordError(res.errorInvalidChars(res.length()), null, length);
				Utils.addErrorStyle(length);
				return;
			}
			if (Integer.parseInt(length.getValue()) < 1) {
				delegate.recordError(res.errorMustBeGreater(res.length(), "0"), null, length);
				Utils.addErrorStyle(length);
				return;
			}
			Utils.removeErrorStyle(length);

			if (Utils.isNullOrEmpty(offset.getValue())) {
				delegate.recordError(res.errorInvalidEmptyField(res.offset()), null, offset);
				Utils.addErrorStyle(offset);
				return;
			}

			if (checkNumbers.exec(offset.getValue()) == null) {
				delegate.recordError(res.errorInvalidChars(res.offset()), null, offset);
				Utils.addErrorStyle(offset);
				return;
			}
			Utils.removeErrorStyle(offset);

		} else if (fieldtype.getValue() == FieldType.USER_DEFINED_VARIABLE) {
			if (bank.getSelectedValue() == null) {
				delegate.recordError(res.errorNotSpecified(res.bank()), null, bank);
				Utils.addErrorStyle(bank);
				return;
			}
			Utils.removeErrorStyle(bank);

			if (oid.getValue() == null) {
				delegate.recordError(res.errorNotSpecified(res.oid()), null, oid);
				Utils.addErrorStyle(oid.getListBox());
				return;
			}

			if (!Pattern.match(Pattern.PatternOid, oid.getValue())) {
				delegate.recordError(res.errorInvalid(res.oid()), null, oid);
				Utils.addErrorStyle(oid.getListBox());
				return;
			}
			Utils.removeErrorStyle(oid.getListBox());
		} else if (fieldtype.getValue() == FieldType.PRE_DEFINED) {
			if (Utils.isNullOrEmpty(fieldnameList.getSelectedValue())) {
				delegate.recordError(res.errorNotSpecified(res.field()), null, fieldnameList);
				Utils.addErrorStyle(fieldnameList);
				return;
			}
			Utils.removeErrorStyle(fieldnameList);
		}

		if (fieldtype.getValue() != null) {

			if (Fieldname.getFieldname(fieldnameList.getSelectedValue()) != null) {

				if (Utils.isNullOrEmpty(datatype.getSelectedValue()) && !ignoreEmptyDataType) {
					delegate.recordError(res.errorNotSpecified(res.dataType()), null, datatype);
					Utils.addErrorStyle(datatype);
					return;
				}
				Utils.removeErrorStyle(datatype);

				if (!Utils.isNullOrEmpty(datatype.getSelectedValue()) && Utils.isNullOrEmpty(format.getSelectedValue())) {
					delegate.recordError(res.errorNotSpecified(res.format()), null, format);
					Utils.addErrorStyle(format);
					return;
				}
				Utils.removeErrorStyle(format);
			}

			Utils.removeErrorStyle(fieldtype);
		}
	}

	/**
	 * @param value
	 *            If true no error will be thrown on validation if datatype is
	 *            empty.
	 */
	public void setIgnoreEmptyDataType(boolean value) {
		ignoreEmptyDataType = value;
	}

	/**
	 * @param isFilterPatternView
	 *            If true the ECFieldSpecEditor is used for Filter Pattern
	 */
	public void setFilterPatternView(boolean isFilterPatternView) {
		this.isFilterPatternView = isFilterPatternView;
		if (isFilterPatternView)
			isGroupingFilterPatternView = false;
	}

	/**
	 * 
	 * @param isGroupingFilterPatternView
	 *            If true the ECFieldSpecEditor is used for Grouping Filter
	 *            Pattern
	 */
	public void setGroupingFilterPatternView(boolean isGroupingFilterPatternView) {
		this.isGroupingFilterPatternView = isGroupingFilterPatternView;
		if (isGroupingFilterPatternView)
			isFilterPatternView = false;
	}

	/**
	 * Sets the available tm fields
	 * 
	 * @param tmFixedFieldSpecs
	 * @param tmVariableFieldSpecs
	 */
	public void setTMFields(Map<String, List<TMFixedFieldSpec>> tmFixedFieldSpecs,
			Map<String, List<TMVariableFieldSpec>> tmVariableFieldSpecs) {
		if (tmFixedFieldSpecs != null) {
			this.tmFixedFieldSpecs = tmFixedFieldSpecs;
		} else {
			this.tmFixedFieldSpecs = new HashMap<String, List<TMFixedFieldSpec>>();
		}
		if (tmVariableFieldSpecs != null) {
			this.tmVariableFieldSpecs = tmVariableFieldSpecs;
		} else {
			this.tmVariableFieldSpecs = new HashMap<String, List<TMVariableFieldSpec>>();
		}
		setFieldNameList();
	}

	/**
	 * Refreshing the field name list
	 */
	private void setFieldNameList() {
		fieldnameList.clear();

		if (CCOpType.CHECK.equals(ccOpType) || CCOpType.INITIALIZE.equals(ccOpType)) {
			fieldnameList.addItem(Fieldname.EPC_BANK.getFieldname(), "<Predefined>");
			fieldnameList.addItem(Fieldname.USER_BANK.getFieldname(), "<Predefined>");
		} else {
			// Add new tm entry
			fieldnameList.addSilentItem(null, res.newEntry());

			// add pre defined fieldnames
			String groupName = "<Predefined>";
			List<String> preFieldnames = new ArrayList<String>();
			for (Fieldname f : Fieldname.values()) {
				boolean add = false;
				for (DataType d : f.getValidDataTypes()) {
					if (isFilterPatternView) {
						if (d.hasPatternSyntax()) {
							add = true;
						}
					} else if (isGroupingFilterPatternView) {
						if (d.hasGroupingPatternSyntax()) {
							add = true;
						}
					} else {
						add = true;
					}
				}
				if (add) {
					preFieldnames.add(f.getFieldname());
				}
			}
			fieldnameList.addItems(preFieldnames, groupName);

			// Add fixed tm specs
			for (Map.Entry<String, List<TMFixedFieldSpec>> tmSpec : tmFixedFieldSpecs.entrySet()) {
				// get spec name. Will be used as group name
				String name = tmSpec.getKey();
				List<String> fieldnames = new ArrayList<String>();
				for (TMFixedFieldSpec spec : tmSpec.getValue()) {
					fieldnames.add(spec.getFieldname());
				}
				if (fieldnames.size() > 0) {
					fieldnameList.addItems(fieldnames, name);
				}
			}

			// Add variable tm specs
			if (!isFilterPatternView && !isGroupingFilterPatternView) {
				for (Map.Entry<String, List<TMVariableFieldSpec>> tmSpec : tmVariableFieldSpecs.entrySet()) {
					// get spec name. Will be used as group name
					String name = tmSpec.getKey();
					List<String> fieldnames = new ArrayList<String>();
					for (TMVariableFieldSpec spec : tmSpec.getValue()) {
						fieldnames.add(spec.getFieldname());
					}
					if (fieldnames.size() > 0) {
						fieldnameList.addItems(fieldnames, name);
					}
				}
			}
		}
	}

	@Override
	public void onPropertyChange(String... paths) {

	}

	@Override
	public void setValue(ECFieldSpec value) {
		// initialize view
		setFieldTypeValues();

		// initialize values
		this.spec = value;
		if (spec != null) {
			RegExp regExp;
			MatchResult result;
			int bankId;
			// initialize view by field type
			fieldtype.setSelectedValue(getFieldType(value));
			if (fieldtype.getValue() != null) {
				switch (fieldtype.getValue()) {
				case USER_DEFINED_FIXED:
					try {
						regExp = RegExp.compile(PATTERN_FIXED);
						result = regExp.exec(spec.getFieldname());

						bankId = Integer.valueOf(result.getGroup(FIXED_GROUP_BANK));

						bank.setItems(TagMemoryBank.values());

						bank.setSelectedValue(TagMemoryBank.getTagMemoryBank(bankId));

						length.setValue(result.getGroup(FIXED_GROUP_LENGTH));

						offset.setValue(result.getGroup(FIXED_GROUP_OFFSET));

						setDataTypeValues();

						datatype.setValue(spec.getDatatype(), true);

						format.setValue(spec.getFormat());
					} catch (Exception e) {
						GWT.log("Exception.ECFieldSpecEditor.setValue.USER_DEFINED_FIXED: " + e.getMessage());
					}
					break;
				case USER_DEFINED_VARIABLE:
					try {
						regExp = RegExp.compile(PATTERN_VARIABLE);
						result = regExp.exec(spec.getFieldname());

						// Only epcBank and userBank are valid for variable fields
						// See 6.1.9.2 of ALE Specification, Version 1.1.1
						bank.setItems(Arrays.asList(new TagMemoryBank[] { TagMemoryBank.EPC_UII, TagMemoryBank.USER }));

						bankId = Integer.valueOf(result.getGroup(VARIABLE_GROUP_BANK));
						bank.setSelectedValue(TagMemoryBank.getTagMemoryBank(bankId));

						oid.setValue(result.getGroup(VARIABLE_GROUP_OID));
					} catch (Exception e) {
						GWT.log("Exception.ECFieldSpecEditor.setValue.USER_DEFINED_VARIABLE: " + e.getMessage());
					}
					break;
				default:
					fieldnameList.setValue(spec.getFieldname());

					setDataTypeValues();

					datatype.setValue(spec.getDatatype(), true);

					format.setValue(spec.getFormat());
					break;
				}
			}
			setVisibility();
		}
	}

	/**
	 * @return The specified field type of the ECFieldSpec.
	 */
	private FieldType getFieldType(ECFieldSpec spec) {
		RegExp regExp = RegExp.compile(PATTERN_FIXED);
		if (regExp.exec(spec.getFieldname()) != null) {
			return FieldType.USER_DEFINED_FIXED;
		}

		regExp = RegExp.compile(PATTERN_VARIABLE);
		if (regExp.exec(spec.getFieldname()) != null) {
			return FieldType.USER_DEFINED_VARIABLE;
		}

		return spec.getFieldname() != null ? FieldType.PRE_DEFINED : null;
	}

	/**
	 * Sets the allowed field types. Variable fields are not allowed in
	 * (grouping) filter pattern
	 */
	private void setFieldTypeValues() {
		fieldtype.clear();
		if (isFilterPatternView || isGroupingFilterPatternView) {
			for (FieldType f : FieldType.values()) {
				if (!FieldType.USER_DEFINED_VARIABLE.equals(f)) {
					fieldtype.addItem(f);
				}
			}
		} else {
			fieldtype.setItems(FieldType.values());
		}
		if (CCOpType.INITIALIZE.equals(ccOpType) || CCOpType.CHECK.equals(ccOpType)) {
			fieldtype.setValue(FieldType.PRE_DEFINED);
		}
	}

	/**
	 * Inserts the valid data types in the drop down list.
	 */
	private void setDataTypeValues() {
		datatype.clear();
		format.clear();

		DataType[] values = DataType.values();
		Fieldname name = Fieldname.getFieldname(fieldname.getValue());
		if (FieldType.PRE_DEFINED.equals(fieldtype.getValue()) && name != null) {
			values = new DataType[0];
			values = name.getValidDataTypes().toArray(values);
		}
		for (DataType d : values) {
			if (isFilterPatternView) {
				if (d.hasPatternSyntax()) {
					datatype.addItem(d.getDatatype());
				}
			} else if (isGroupingFilterPatternView) {
				if (d.hasGroupingPatternSyntax()) {
					datatype.addItem(d.getDatatype());
				}
			} else if (!DataType.ISO_15962_STRING.equals(d)) {
				datatype.addItem(d.toString());
			}
		}
	}

	/**
	 * See 6.1.9 of ALE Specification, Version 1.1.1
	 * 
	 * @return The field name
	 */
	private String getFieldname() {
		if (fieldtype.getValue() != null) {
			switch (fieldtype.getValue()) {
			case USER_DEFINED_FIXED:
				if (bank.getValue() != null)
					return "@" + bank.getValue().getBank() + "." + length.getValue() + "." + offset.getValue();
				else
					return null;
			case USER_DEFINED_VARIABLE:
				if (bank.getValue() != null)
					return "@" + bank.getValue().getBank() + "." + oid.getValue();
				else
					return null;
			default:
				return fieldnameList.getValue();
			}
		} else {
			return null;
		}
	}

	/**
	 * Change visibility of input fields.
	 */
	private void setVisibility() {
		FieldType ft = fieldtype.getValue();
		boolean preDefined = FieldType.PRE_DEFINED.equals(ft);
		boolean userDefinedFixed = FieldType.USER_DEFINED_FIXED.equals(ft);
		boolean userDefinedVariable = (ft == FieldType.USER_DEFINED_VARIABLE);

		// only visible if field type is a predefined
		fieldnameListRow.setVisible(preDefined);

		// only visible for user defined fields
		bankRow.setVisible(userDefinedFixed || userDefinedVariable);

		// only visible for user defined fixed fields
		lengthRow.setVisible(userDefinedFixed);
		offsetRow.setVisible(userDefinedFixed);

		// only visible for user defined variable fields
		oidRow.setVisible(userDefinedVariable);

		if (CCOpType.INITIALIZE.equals(ccOpType) || CCOpType.CHECK.equals(ccOpType)) {
			fieldtypeRow.setVisible(false);
			datatypeRow.setVisible(false);
			formatRow.setVisible(false);
		} else {
			fieldtypeRow.setVisible(true);
			// only visible for predefined and user defined fixed fields
			datatypeRow.setVisible(preDefined || userDefinedFixed);
			formatRow.setVisible(preDefined || userDefinedFixed);
		}
	}

	@UiHandler("fieldtype")
	void onFieldTypeChange(ChangeEvent event) {
		// remove error style
		Utils.removeErrorStyle(fieldtype);

		// clear fields
		fieldnameList.clear();
		fieldnameList.setValue(null);

		bank.clear();

		length.setText("");
		offset.setText("");

		oid.setValue("");

		datatype.clear();
		datatype.setValue(null);
		format.clear();
		format.setValue(null);

		// fill DropDown lists
		setFieldNameList();
		setDataTypeValues();

		if (fieldtype.getValue() != null) {
			switch (fieldtype.getValue()) {
			case USER_DEFINED_VARIABLE:
				bank.setItems(Arrays.asList(new TagMemoryBank[] { TagMemoryBank.EPC_UII, TagMemoryBank.USER }));
				break;
			default:
				bank.setItems(TagMemoryBank.values());
				break;
			}
		}

		setVisibility();
	}

	@UiHandler("fieldnameList")
	void onFieldnameListChange(ChangeEvent event) {
		// remove error style
		Utils.removeErrorStyle(fieldnameList);

		if (fieldnameList.getSelectedItemText() != null && fieldnameList.getSelectedItemText().startsWith(res.newEntry())) {
			// Go to Tag Memory View
			fireEvent(new NewItemEvent(parentFieldName));
		} else {
			// clear fields
			datatype.clear();
			datatype.setValue(null);
			format.clear();
			format.setValue(null);

			// set fieldname
			this.fieldname.setValue(getFieldname());

			datatype.setValue(null);
			format.setValue(null);

			String fieldname = fieldnameList.getSelectedValue();
			if (fieldname != null) {
				boolean isVariableField = false;
				if (CCOpType.CHECK.equals(ccOpType) || CCOpType.INITIALIZE.equals(ccOpType)) {
					isVariableField = true;
				}
				if (!isVariableField) {
					for (Map.Entry<String, List<TMVariableFieldSpec>> entry : tmVariableFieldSpecs.entrySet()) {
						for (TMVariableFieldSpec spec : entry.getValue()) {
							if (fieldname.equals(spec.getFieldname())) {
								isVariableField = true;
								break;
							}
						}
						if (isVariableField) {
							break;
						}
					}
				}
				datatypeRow.setVisible(!isVariableField);
				formatRow.setVisible(!isVariableField);

				if (!isVariableField) {
					setDataTypeValues();
				}
			}
		}
	}

	@UiHandler({ "bank", "length", "offset", "oid" })
	void onFieldBankChange(ChangeEvent event) {
		fieldname.setValue(getFieldname());
	}

	public HandlerRegistration addOidValueChangeHandler(ValueChangeHandler<String> handler) {
		return oid.getTextBox().addValueChangeHandler(handler);
	}

	public HandlerRegistration addFieldTypeChangeHandler(ChangeHandler handler) {
		return fieldtype.addChangeHandler(handler);
	}
	
	public HandlerRegistration addFieldNameValueChangeHandler(ValueChangeHandler<String> handler) {		
		return fieldnameList.addValueChangeHandler(handler);
	}

	@UiHandler("datatype")
	void onDataTypeChange(ChangeEvent event) {
		// remove error style
		Utils.removeErrorStyle(datatype);

		// clear fields
		format.clear();

		if (datatype.getValue() != null) {
			// set valid datatype values
			DataType dt = DataType.getDataType(datatype.getValue());
			for (Format vf : dt.getValidFormats()) {
				if (isFilterPatternView) {
					if (dt.hasPatternSyntax(vf))
						format.addItem(vf.toString());
				} else if (isGroupingFilterPatternView) {
					if (dt.hasGroupingPatternSyntax(vf))
						format.addItem(vf.toString());
				} else {
					format.addItem(vf.toString());
				}
			}
		}
		Utils.removeErrorStyle(datatype);
	}

	@UiHandler("format")
	void onFormatChange(ChangeEvent event) {
		// remove error style
		Utils.removeErrorStyle(format);
	}

	public void setFieldTypeErrorStyle() {
		FieldType ft = fieldtype.getValue();
		if (ft == FieldType.USER_DEFINED_FIXED || ft == FieldType.PRE_DEFINED)
			return;

		delegate.recordError(res.errorNotSpecified(res.fieldtype()), fieldtype, fieldtype);
		Utils.addErrorStyle(fieldtype);
	}

	/**
	 * Clearing the input fields
	 */
	public void clear() {
		fieldtype.setValue(null);
	}

	/**
	 * Stores the defined ccOpType.
	 */
	private CCOpType ccOpType;

	/**
	 * Enables specific behavior for CHECK and INITIALIZE operation
	 * 
	 * @param ccOpType
	 *            The current selected ccOpType
	 */
	public void setCCOpType(CCOpType ccOpType) {
		if (ccOpType != null) {
			oid.getListBox().removeItem("urn:oid:1.0.15961.*", false);
			oid.getListBox().removeItem("urn:oid:1.0.15961.9.*", false);
			oid.getListBox().removeItem("urn:oid:1.0.15434.3.*", false);
		}
		this.ccOpType = ccOpType;
		setFieldTypeValues();
		setFieldNameList();
		setVisibility();
	}

	public String getOid() {
		return oid.getValue();
	}
}
