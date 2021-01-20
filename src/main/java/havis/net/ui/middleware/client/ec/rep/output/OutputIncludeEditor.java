package havis.net.ui.middleware.client.ec.rep.output;

import havis.middleware.ale.service.ec.ECReportOutputSpec;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.shared.client.ConfigurationSection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class OutputIncludeEditor extends ConfigurationSection implements Editor<ECReportOutputSpec>{

	@UiField ToggleButton includeEPC;

	@UiField ToggleButton includeTag;
	
	@UiField ToggleButton includeRawHex;
	
	@UiField ToggleButton includeRawDecimal;
	
	@UiField ToggleButton includeCount;
	
	private static ReportSpecItemOutputIncludeEditorUiBinder uiBinder = GWT
			.create(ReportSpecItemOutputIncludeEditorUiBinder.class);

	interface ReportSpecItemOutputIncludeEditorUiBinder extends UiBinder<Widget, OutputIncludeEditor> {
	}
	
	private ConstantsResource res = ConstantsResource.INSTANCE;

	public OutputIncludeEditor() {
		setText(res.include());
		initWidget(uiBinder.createAndBindUi(this));
	}
}
