package havis.net.ui.middleware.client.shared.resourcebundle;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;

public interface ResourceBundle extends ClientBundle {

	public static final ResourceBundle INSTANCE =
			GWT.create(ResourceBundle.class);
	
	@Source("resources/OverallStyle.css")
	CSSResource css();
	
	@Source("resources/LLRP_Power_Scale_ActiveUnit.png")
	ImageResource llrpPowerScaleActiveUnit();
	
	@Source("resources/spec_item_add.png")
	DataResource specItemAdd();
	
	@Source("resources/spec_item_delete.png")
	DataResource specItemDelete();
	
	@Source("resources/spec_item_export.png")
	DataResource specItemExport();
	
	@Source("resources/spec_item_import.png")
	DataResource specItemImport();
	
	@Source("resources/spec_item_menu.png")
	DataResource specItemMenu();
	
	@Source("resources/spec_item_list_refresh.png")
	DataResource specItemListRefresh();
	
	@Source("resources/spec_item_run.png")
	DataResource specItemRun();
	
	@Source("resources/LLRP_Power_Scale.png")
	DataResource llrpPowerScaleEmpty();
	
	@Source("resources/LLRP_Power_Scale_Active.png")
	DataResource llrpPowerScaleActive();
	
	@Source("resources/LLRP_Power_Scale_Active_full.png")
	DataResource llrpPowerScaleFull();
	
	@Source("resources/LLRP_Power_Scale_BT_Minus.png")
	DataResource llrpPowerScaleMinusBT();
	
	@Source("resources/LLRP_Power_Scale_BT_Plus.png")
	DataResource llrpPowerScalePlusBT();
	
	@Source("resources/LLRP_Region_Dropdown_Arrow_disabled.png")
	DataResource dropDownArrowDisabled();
	
	@Source("resources/LLRP_Region_Dropdown_Arrow.png")
	DataResource dropDownArrow();
	
	@Source("resources/delete_row.png")
	DataResource deleteRow();
		
	@Source("resources/Icn_message_misentry_32x32 .png")
	ImageResource errorIcon();
	
	@Source("resources/list-scroll-up.png")
	DataResource listScrollUp();
	
	@Source("resources/list-scroll-down.png")
	DataResource listScrollDown();
	
	@Source("resources/editor_close.png")
	DataResource editorClose();
	
	@Source("resources/service_starting.png")
	DataResource specStateChanging();
}
