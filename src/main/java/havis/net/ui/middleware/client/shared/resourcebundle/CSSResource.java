package havis.net.ui.middleware.client.shared.resourcebundle;

import com.google.gwt.resources.client.CssResource;

import havis.net.ui.shared.client.table.CustomTable;

public interface CSSResource extends CssResource {

	@ClassName("webui-Property")
	String webuiProperty();
	
	/**
	 * A style for a loading spinner
	 * 
	 * @return webui-Spinner-Bounce
	 */
	@ClassName("webui-Spinner-Bounce")
	String webuiSpinnerBounce();
	
	/**
	 * Add this style to all TextBoxes which are not children of a
	 * {@link CustomTable}
	 * 
	 * @return webui-TextBox
	 */
	@ClassName("webui-TextBox")
	String webuiTextBox();
	
	/**
	 * Add this style to all TextAreas which are not children of a
	 * {@link CustomTable}
	 * 
	 * @return webui-TextArea
	 */
	@ClassName("webui-TextArea")
	String webuiTextArea();

	/**
	 * Add this style to all ListBoxes which are not children of a
	 * {@link CustomTable}
	 * 
	 * @return webui-ListBox
	 */
	@ClassName("webui-ListBox")
	String webuiListBox();

	/**
	 * Add this style to Labels which are children in {@link CustomTable}
	 * 
	 * @return webui-CustomTable-Label
	 */
	@ClassName("webui-CustomTable-Label")
	String webuiCustomTableLabel();

	/**
	 * Add this style to TextBoxes which are children in {@link CustomTable}
	 * 
	 * @return webui-CustomTable-TextBox
	 */
	@ClassName("webui-CustomTable-TextBox")
	String webuiCustomTableTextBox();

	/**
	 * Add this style to ListBoxes which are children in {@link CustomTable}
	 * 
	 * @return webui-CustomTable-ListBox
	 */
	@ClassName("webui-CustomTable-ListBox")
	String webuiCustomTableListBox();
	
	@ClassName("webui-CustomTable-ListBox-noArrow")
	String webuiCustomTableListBoxnoArrow();

	@ClassName("webui-power-scale-empty")
	String webuiPowerScaleEmpty();
	
	@ClassName("webui-power-scale")
	String webuiPowerScale();
	
	@ClassName("webui-power-scale-full")
	String webuiPowerScaleFull();
	

	@ClassName("webui-Decrease-Button")
	String webuiDecreaseButton();

	@ClassName("webui-Increase-Button")
	String webuiIncreaseButton();

	/**
	 * The style for {@link CustomTable}
	 * 
	 * @return webui-CustomTable
	 */
	@ClassName("webui-CustomTable")
	String webuiCustomTable();
	
	@ClassName("webui-CustomTable-MoveUp")
	String webuiCustomTableUp();
	
	@ClassName("webui-CustomTable-MoveDown")
	String webuiCustomTableDown();
	/**
	 * Add this style to input fields which shall be highlighted as invalid
	 * 
	 * @return webui-Input-Error
	 */
	@ClassName("webui-Input-Error")
	String webuiInputError();
	
	@ClassName("webui-Cancel-Button")
	String webuiCancelButton();
	
	@ClassName("webui-Export-Button")
	String webuiExportButton();
	
	@ClassName("webui-Apply-Button")
	String webuiApplyButton();
	
	String specItemRow();
	String item();
	String button();
	String specItemNameRow();
	String name();
	String run();
	String export();
	String delete();
	String subscriber();
	String closed();
	
	String specItemList();
	String toolbar();
	String refreshButton();
	String addButton();
	String importButton();
	String label();
	String sectionContent();
	
	String enableButton();
	
	String propertyRow();
	String confirmDialog();
	String confirmIcon();
	
	String labelWidth();
	String mainLabel();
	String commonLabel();
	String ECreportMemberFieldLabel();
	String anchor();
	
	@ClassName("ale-id")
	String aleid();
	
	String left();
	
	@ClassName("name-input")
	String nameinput();
	
	String dsnameinput();
	String specialMargin();
	String switcher();

	String app();
	String details();
	String text();
	String toggle();
	String confText();
	String toggleDistance();
	String area();
	String arealike();
	String ecarea();
	String caption();
	String graycaption();
	String url();
	
	@ClassName("open-Button")
	String openButton();
	
	@ClassName("max-width")
	String maxWidth();
	
	String scalefill();
	String marginToggleLabel();
	String includeLabel();
	String statisticsLabel();
	String data();	
		
	@ClassName("webui-EditorBackground")
	String webuiEditorBackground();
	
	@ClassName("webui-EditorDialog")
	String webuiEditorDialog();
	
	@ClassName("webui-EditorHeader")
	String webuiEditorHeader();
	
	@ClassName("webui-EditorFooter")
	String webuiEditorFooter();
	
	@ClassName("webui-EditorPanel")
	String webuiEditorPanel();
	
	String title();
	String topFourLabels();
	String bottomLabels();
	String gap();
	String fields();
	
	String boundarySpecRow();
	String ccOpReportRow();
	String commonRow();
	String cycleAssocRow();
	String ecFieldFilterItemRow();
	String ecPcReportRow();
	String ecReportsConditionsRow();
	String ecReportTagStatRow();
	String header();
	String patternItemRow();
	String productInfoRow();
	String readerRow();
	String reportsConditionsRow();
	String reportsRow();
	String sectionRow();
	String sections();
	String specItemOutputRow();
	String specItemsRow();
	String specTriggerRow();
	String specOpRow();
	String tmFieldListRow();
	
	@ClassName("webui-message-popup-panel")
	String webuiMessagePopupPanel();	
	
	@ClassName("webui-message-popup-panel-error-dot")
	String webuiMessagePopupPanelErrorDot();
	
	@ClassName("propertyRow-TextArea")
	String propertyRowTextArea();
	
	@ClassName("spec-state-changing")
	String specStateChanging();
	
	@ClassName("subscriber-list")
	String subscriberList();
	
	@ClassName("cc-operation-data-init")
	String ccOperationDataInit();
	
	String closeButton();
}
