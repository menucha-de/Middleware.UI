package havis.net.ui.middleware.client.cc.cmd.operation;

import havis.middleware.ale.service.cc.CCOpSpec;
import havis.middleware.tdt.PackedObjectInvestigator.ColumnName;
import havis.net.ui.middleware.client.shared.field.ECFieldSpecEditor;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.widgets.CustomListBox;
import havis.net.ui.shared.client.widgets.CustomRenderer;
import havis.net.ui.shared.client.widgets.CustomSuggestBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class CCOpSpecEditor extends Composite implements ValueAwareEditor<CCOpSpec> {

	private EditorDelegate<CCOpSpec> delegate;

	private ConstantsResource res = ConstantsResource.INSTANCE;

	@UiField
	FlowPanel dataSpecRow;
	@UiField
	FlowPanel initForceRow;
	@UiField
	FlowPanel fieldSpecRow;
	@UiField
	FlowPanel litData;

	@UiField
	TextBox opName;

	@Path("opType")
	@UiField
	CustomListBox<String> opTypeList;

	@Path("dataSpec.specType")
	@UiField(provided = true)
	CustomListBox<String> specType = new CustomListBox<String>(true);

	@Ignore
	@UiField
	TextBox opDataText;

	@Path("dataSpec.data")
	LeafValueEditor<String> opData = new LeafValueEditor<String>() {

		@Override
		public void setValue(String value) {
			opDataText.setValue(value);
			if (CCOpDataSpecType.getValue(specType.getValue()) == CCOpDataSpecType.LITERAL) {
				getLiteralCommand();
			}
			opDataList.setValue(value);
			opDataSuggestList.setValue(value);
			initForce.setValue(value != null && value.toLowerCase().endsWith(".force"));
		}

		@Override
		public String getValue() {
			if (specType.getValue() != null) {
				try {
					CCOpDataSpecType type = CCOpDataSpecType.valueOf(specType.getValue());
					switch (type) {
					case CACHE:
					case RANDOM:
					case ASSOCIATION:
						return opDataList.getValue();
					case LITERAL:
						if (opTypeList.getValue() != null && CCOpType.valueOf(opTypeList.getValue()) == CCOpType.INITIALIZE) {
							return opDataSuggestList.getValue();
						}
						break;
					default:
						break;
					}
				} catch (Exception e) {

				}
			}
			return opDataText.getValue();
		}
	};

	@Ignore
	@UiField
	CustomListBox<CCLockOperation> opDataListLock;
	
	@Ignore
	@UiField(provided = true)
	CustomListBox<String> opDataList = new CustomListBox<String>(true);

	@Ignore
	@UiField(provided = true)
	CustomSuggestBox<String> opDataSuggestList = new CustomSuggestBox<String>(CustomSuggestBox.getStringParser());

	@Path("fieldspec")
	@UiField
	ECFieldSpecEditor fieldSpecEditor;

	@Ignore
	@UiField
	ToggleButton initForce;
	
	@Ignore
	@UiField(provided = true)
	CustomListBox<CCTagManufacturer> litTagManufacturer;
	
	@Ignore
	@UiField(provided = true)
	CustomListBox<CCTagModel> litTagModel;
	
	@Ignore
	@UiField(provided = true)
	CustomListBox<CCTagCommand> litTagCommand;

	private CCOpSpec ccOpSpec;

	private static PCOpSpecEditorUiBinder uiBinder = GWT.create(PCOpSpecEditorUiBinder.class);

	interface PCOpSpecEditorUiBinder extends UiBinder<Widget, CCOpSpecEditor> {
	}

	public CCOpSpecEditor() {
		litTagManufacturer = new CustomListBox<CCTagManufacturer>(new CustomRenderer<CCTagManufacturer>() {
			@Override
			public String render(CCTagManufacturer tmf) {
				return tmf.getName();
			}
		});
		
		litTagModel = new CustomListBox<CCTagModel>(new CustomRenderer<CCTagModel>() {
			@Override
			public String render(CCTagModel tm) {
				return tm.getName();
			}
		});
		
		litTagCommand = new CustomListBox<CCTagCommand>(new CustomRenderer<CCTagCommand>() {
			@Override
			public String render(CCTagCommand tc) {
				return tc.getName();
			}
		});
		initWidget(uiBinder.createAndBindUi(this));

		for (CCOpType t : CCOpType.values()) {
			opTypeList.addItem(t.getCCOpType());
		}
		
		litTagManufacturer.setItems(CCTagManufacturer.values());

		opDataListLock.addItems(CCLockOperation.values());

		fieldSpecEditor.setIgnoreEmptyDataType(true);

		fieldSpecEditor.addFieldTypeChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				opDataText.getElement().removeAttribute("placeholder");
			}
		});
		fieldSpecEditor.addOidValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				setOpDataTextPlaceholder();
			}
		});
		
		fieldSpecEditor.addFieldNameValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (CCOpType.INITIALIZE.equals(CCOpType.getCCOpType(opTypeList.getSelectedValue()))) {
					if (event.getValue() != null) {
						switch (event.getValue()) {
						case "epcBank":
							setOpDataSuggestListBox(true, false);
							break;
						case "userBank":
							setOpDataSuggestListBox(false, true);
							break;
						default:
							setOpDataSuggestListBox(true, true);
							break;
						}
					} else
						setOpDataSuggestListBox(true, true);
				}
			}


		});
	}

	private void setOpDataSuggestListBox(boolean epcBank, boolean userBank) {
		opDataSuggestList.getListBox().clear();
		List<String> dataFormat9 = new ArrayList<>();
		List<String> dataFormat3 = new ArrayList<>();
		if (epcBank) {
			dataFormat9.add("urn:epcglobal:ale:init:iso15962:x00.x89");
			dataFormat3.add("urn:epcglobal:ale:init:iso15962:xA1");
			dataFormat3.add("urn:epcglobal:ale:init:iso15962:xA4");
		}
		if (userBank) {
			dataFormat9.add("urn:epcglobal:ale:init:iso15962:x89");
			dataFormat3.add("urn:epcglobal:ale:init:iso15962:x03");
		}
		opDataSuggestList.getListBox().addItems(new ArrayList<String>(dataFormat9), "<" + ConstantsResource.INSTANCE.oidListDataFormat9() + ">");
		opDataSuggestList.getListBox().addItems(new ArrayList<String>(dataFormat3), "<" + ConstantsResource.INSTANCE.oidListDataFormat3() + ">");
	}

	@Override
	public void setDelegate(EditorDelegate<CCOpSpec> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void flush() {
		if (delegate != null) {
			if (Utils.isNullOrEmpty(opName.getText())) {
				delegate.recordError(res.errorInvalidEmptyField(res.name()), null, opName);
				Utils.addErrorStyle(opName);
				return;
			}
			CCOpType ccOpType = CCOpType.getCCOpType(opTypeList.getSelectedValue());
			if (ccOpType == null) {
				delegate.recordError(res.errorNotSpecified(res.type()), opTypeList.getSelectedValue(), opTypeList);
				Utils.addErrorStyle(opTypeList);
				return;
			} else {
				/**
				 * See 9.3.5 of ALE Specification, Version 1.1.1
				 */
				// Remove dataSpec if necessary
				switch (ccOpType) {
				case READ:
				case DELETE:
					ccOpSpec.setDataSpec(null);
					break;
				default:
					break;
				}
				/**
				 * See 9.3.5 of ALE Specification, Version 1.1.1
				 */
				// Remove fieldSpec if necessary
				switch (ccOpType) {
				case PASSWORD:
				case KILL:
					ccOpSpec.setFieldspec(null);
					break;
				default:
					break;
				}
			}

			if (dataSpecRow.isVisible()) {
				if (specType.getValue() == null) {
					delegate.recordError(res.errorInvalidEmptyField(res.source()), null, specType);
					Utils.addErrorStyle(specType);
					return;
				}

				if (Utils.isNullOrEmpty(opData.getValue()) && opDataText.isEnabled()) {
					if(opDataList.isVisible()){
						delegate.recordError(res.errorInvalidEmptyField(res.data()), null, opDataList);
						Utils.addErrorStyle(opDataList);
					} else if(opDataListLock.isVisible()){
						delegate.recordError(res.errorInvalidEmptyField(res.data()), null, opDataListLock);
						Utils.addErrorStyle(opDataListLock);
					} else if(opDataSuggestList.isVisible()){
						delegate.recordError(res.errorInvalidEmptyField(res.data()), null, opDataSuggestList);
						Utils.addErrorStyle(opDataSuggestList);
					} else {
						delegate.recordError(res.errorInvalidEmptyField(res.data()), null, opDataText);
						Utils.addErrorStyle(opDataText);
					}					
					return;
				}
			}
		}

	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	private boolean init = false;

	@Override
	public void setValue(CCOpSpec value) {
		init = true;
		ccOpSpec = value;
		if (ccOpSpec != null) {
			if (ccOpSpec.getDataSpec() != null) {
				if (CCOpType.LOCK.equals(CCOpType.getCCOpType(ccOpSpec.getOpType()))) {
					opDataListLock.setValue(CCLockOperation.getCCLockOperation(ccOpSpec.getDataSpec().getData()));
				}				
			}
			fieldSpecEditor.setCCOpType(CCOpType.getCCOpType(ccOpSpec.getOpType()));
		}
	}

	@UiHandler("opName")
	public void onChangeOpName(ChangeEvent e) {
		Utils.removeErrorStyle(opName);
	}

	@UiHandler("opName")
	public void onOpNameKeyUp(KeyUpEvent e) {
		Utils.removeErrorStyle(opName);
		if (ccOpSpec != null) {
			ccOpSpec.setOpName(opName.getText());
			fieldSpecEditor.setParentFieldName(opName.getText());
		}
	}

	@UiHandler("opTypeList")
	public void onOpTypeListChange(ChangeEvent e) {
		// Remove error style
		Utils.removeErrorStyle(opTypeList);
		Utils.removeErrorStyle(specType);
		Utils.removeErrorStyle(opDataSuggestList);
		Utils.removeErrorStyle(opDataListLock);
		Utils.removeErrorStyle(opDataList);
		Utils.removeErrorStyle(opDataText);
		// clear fields
		if (!init) {
			fieldSpecEditor.clear();
			specType.setValue(null);
			opDataText.setValue(null);
			opDataListLock.setValue(null);
			opDataList.setValue(null);
			opDataSuggestList.setValue(null);
		} else {
			init = false;
		}

		opDataListLock.setVisible(false);
		opDataList.setVisible(false);
		opDataSuggestList.setVisible(false);
		opDataText.setVisible(true);
		initForceRow.setVisible(false);
		CCOpType selected = CCOpType.getCCOpType(opTypeList.getSelectedValue());
		setSpecTypeList(selected);
		if (selected != null) {
			/**
			 * See 9.3.5 of ALE Specification, Version 1.1.1
			 */
			switch (selected) {
			case READ:
				dataSpecRow.setVisible(false);
				fieldSpecRow.setVisible(true);
				break;
			case CHECK:
				dataSpecRow.setVisible(false);
				fieldSpecRow.setVisible(true);
				break;
			case INITIALIZE:
				dataSpecRow.setVisible(true);
				opDataSuggestList.setVisible(true);
				opDataText.setVisible(false);
				fieldSpecRow.setVisible(true);
				initForceRow.setVisible(true);
				break;
			case ADD:
				dataSpecRow.setVisible(true);
				fieldSpecRow.setVisible(true);
				break;
			case WRITE:
				dataSpecRow.setVisible(true);
				fieldSpecRow.setVisible(true);
				break;
			case DELETE:
				dataSpecRow.setVisible(false);
				fieldSpecRow.setVisible(true);
				break;
			case PASSWORD:
				dataSpecRow.setVisible(true);
				fieldSpecRow.setVisible(false);
				break;
			case KILL:
				dataSpecRow.setVisible(true);
				fieldSpecRow.setVisible(false);
				break;
			case LOCK:
				dataSpecRow.setVisible(true);
				opDataListLock.setVisible(true);
				opDataText.setVisible(false);
				fieldSpecRow.setVisible(true);
				break;
			case CUSTOM:
				dataSpecRow.setVisible(true);
				fieldSpecRow.setVisible(false);
				break;
			default:
				dataSpecRow.setVisible(false);
				fieldSpecRow.setVisible(false);
				break;
			}
		} else {
			dataSpecRow.setVisible(false);
			fieldSpecRow.setVisible(false);
		}
		fieldSpecEditor.setCCOpType(CCOpType.getCCOpType(opTypeList.getValue()));
	}

	@UiHandler("specType")
	public void onChangeSpecType(ChangeEvent e) {
		Utils.removeErrorStyle(specType);
		Utils.removeErrorStyle(opDataSuggestList);
		Utils.removeErrorStyle(opDataListLock);
		Utils.removeErrorStyle(opDataList);
		Utils.removeErrorStyle(opDataText);
		setOpDataTextPlaceholder();
		setOpDataList();
		setLiteralFields();
	}

	private void setLiteralFields() {
		litData.setVisible(opTypeList.getValue().equals(CCOpType.CUSTOM.getCCOpType())
			&& specType.getValue().equals(CCOpDataSpecType.LITERAL.getSpecType()));
	}

	@UiHandler("litTagManufacturer")
	public void onChangeTagManufacturer(ChangeEvent e) {
		List<CCTagModel> models = litTagManufacturer.getValue().getModels();
		litTagModel.setItems(models);
		if (models.size() > 0) {
			litTagModel.setValue(models.get(0), true);
		}
		setLiteralCommand();
	}
	
	@UiHandler("litTagModel")
	public void onChangeTagModel(ChangeEvent e) {
		CCTagModel model = litTagModel.getValue();
		List<CCTagCommand> commands = model.getCommands();
		litTagCommand.setItems(commands);
		if (litTagModel.getValue() == CCTagModel.RFM3254) {
			litTagCommand.setValue(CCTagCommand.GET_TEMPERATURE_VALUE, true);
		} else {
			if (commands.size() > 0) {
				litTagCommand.setValue(commands.get(0), true);
			}
		}
		setLiteralCommand();
	}
	
	@UiHandler("litTagCommand")
	public void onChangeTagCommand(ChangeEvent e) {
		setLiteralCommand();
	}

	@UiHandler("opDataSuggestList")
	public void onChangeOpDataSuggestList(ChangeEvent e) {
		Utils.removeErrorStyle(opDataSuggestList);
		Utils.removeErrorStyle(opDataListLock);
		Utils.removeErrorStyle(opDataList);
		Utils.removeErrorStyle(opDataText);
	}

	@UiHandler("opDataListLock")
	public void onChangeOpDataListLock(ChangeEvent e) {
		Utils.removeErrorStyle(opDataSuggestList);
		Utils.removeErrorStyle(opDataListLock);
		Utils.removeErrorStyle(opDataList);
		Utils.removeErrorStyle(opDataText);
	}
	
	@UiHandler("opDataList")
	public void onChangeOpDataList(ChangeEvent e) {
		Utils.removeErrorStyle(opDataSuggestList);
		Utils.removeErrorStyle(opDataListLock);
		Utils.removeErrorStyle(opDataList);
		Utils.removeErrorStyle(opDataText);
	}

	@UiHandler("opDataText")
	public void onChangeOpDataText(ChangeEvent e) {
		Utils.removeErrorStyle(opDataSuggestList);
		Utils.removeErrorStyle(opDataListLock);
		Utils.removeErrorStyle(opDataList);
		Utils.removeErrorStyle(opDataText);
	}

	/**
	 * Setting the valid CCOpDataSpecType
	 * 
	 * @param ccOpType
	 */
	private void setSpecTypeList(CCOpType ccOpType) {
		specType.clear();
		if (ccOpType != null) {
			switch (ccOpType) {
			case CHECK:
				/**
				 * See 9.3.5.1 of ALE Specification, Version 1.1.1
				 */
				opDataText.setValue("urn:epcglobal:ale:check:iso15962");
			case INITIALIZE:				
			case LOCK:
				// only LITERAL is valid
				specType.addItem(CCOpDataSpecType.LITERAL.getSpecType());
				specType.setValue(CCOpDataSpecType.LITERAL.getSpecType());
				break;
			default:
				for (CCOpDataSpecType c : CCOpDataSpecType.values()) {
					specType.addItem(c.getSpecType());
				}
				break;
			}
		}
	}

	@UiHandler("opDataListLock")
	public void onOpDataListLockChange(ChangeEvent e) {
		CCLockOperation value = opDataListLock.getSelectedValue();
		if (value != null) {
			opDataText.setValue(value.getCCLockOperation());
		} else {
			opDataText.setValue(null);
		}
	}
	
	@UiHandler("opDataList")
	public void onOpDataListChange(ChangeEvent e) {
		opDataText.setValue(opDataList.getValue());		
	}

	@UiHandler("opDataSuggestList")
	public void onOpDataSuggestListChange(ChangeEvent e) {
		opDataText.setValue(opDataSuggestList.getValue());		
	}

	@UiHandler("initForce")
	public void onInitForceChange(ValueChangeEvent<Boolean> e) {
		if (initForce.getValue()) {
			if (opData.getValue() != null) {
				if (!opData.getValue().endsWith(".force")) {
					opData.setValue(opData.getValue() + ".force");
				}
			}
		} else {
			if (opData.getValue() != null) {
				if (opData.getValue().endsWith(".force")) {
					opData.setValue(opData.getValue().substring(0, opData.getValue().indexOf(".force")));
				}
			}
		}
	}

	/**
	 * Sets the operation data field placeholder. For variable fields the format
	 * string will be shown.
	 */
	private void setOpDataTextPlaceholder() {
		Map<ColumnName, String> oid = CommonStorage.getOids().get(fieldSpecEditor.getOid());
		if (oid != null && CCOpDataSpecType.LITERAL.getSpecType().equals(specType.getValue())) {
			opDataText.getElement().setAttribute("placeholder", oid.get(ColumnName.FORMAT_9_FORMAT_STRING));
		} else {
			opDataText.getElement().removeAttribute("placeholder");
		}
	}
	
	private List<String> associationNames = new ArrayList<String>();
	public void setAssociationNames(List<String> names){
		this.associationNames = names;
		onChangeSpecType(null);
	}
	
	private List<String> randomNames = new ArrayList<String>();
	public void setRandomNames(List<String> names){
		this.randomNames = names;
		onChangeSpecType(null);
	}
	
	private List<String> cacheNames = new ArrayList<String>();
	public void setCacheNames(List<String> names){
		this.cacheNames = names;
		onChangeSpecType(null);
	}
	
	private void setOpDataList(){		
		try {
			CCOpDataSpecType type = CCOpDataSpecType.valueOf(specType.getValue());
			boolean isInitialize = CCOpType.INITIALIZE.name().equals(opTypeList.getValue());
			boolean visible = false;
			opDataList.setValue(opDataText.getValue(), false);
			switch (type) {			
			case CACHE:
				opDataList.setItems(cacheNames);
				visible = true;
				break;
			case ASSOCIATION:
				opDataList.setItems(associationNames);
				visible = true;
				break;
			case RANDOM:
				opDataList.setItems(randomNames);
				visible = true;
				break;			
			default:				
				break;
			}			
			opDataList.setVisible(visible);
			opDataText.setVisible(!visible && !isInitialize);
		} catch (Exception e){
			opDataList.clear();
		}
	}
	
	private void getLiteralCommand() {
		String literal = opDataText.getValue();
		if (literal.length() >= 8) {
			CCTagManufacturer tmf = CCTagManufacturer.fromLiteral(literal.substring(0, 8));
			if (tmf != null && literal.length() >= 12) {
				CCTagModel tm = CCTagModel.fromLiteral(literal.substring(8, 12));
				if (tm != null && literal.length() >= 14) {
					CCTagCommand tc = CCTagCommand.fromLiteral(literal.substring(12, literal.length()));
					if (tc != null) {
						litTagManufacturer.setValue(tmf);
						litTagModel.setValue(tm);
						litTagCommand.setValue(tc);
					}
				}
			}
		}
	}
	
	private void setLiteralCommand() {
		CCTagManufacturer manufacturer = litTagManufacturer.getValue();
		CCTagModel model = litTagModel.getValue();
		CCTagCommand command = litTagCommand.getValue();
		if (manufacturer != null && model != null && command != null) {
			opDataText.setValue(manufacturer.getLiteral() + model.getLiteral() + command.getLiteral());
		}
	}
}
