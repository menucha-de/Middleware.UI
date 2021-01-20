package havis.net.ui.middleware.client.pc.report;

import havis.middleware.ale.service.pc.PCOpReport;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.ConfigurationSection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class PCOpReportEditor extends ConfigurationSection implements ValueAwareEditor<PCOpReport> {

	@UiField
	TextBox opStatus;
	
	@UiField
	ToggleButton state;
	
	@Ignore
	@UiField
	FlowPanel stateSection;
	
	private static PCOpReportEditorUiBinder uiBinder = GWT.create(PCOpReportEditorUiBinder.class);
	interface PCOpReportEditorUiBinder extends UiBinder<Widget, PCOpReportEditor> { }

	public PCOpReportEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setDelegate(EditorDelegate<PCOpReport> delegate) { }

	@Override
	public void flush() { }

	@Override
	public void onPropertyChange(String... paths) { }

	@Override
	public void setValue(PCOpReport value) {
		if(value != null && !Utils.isNullOrEmpty(value.getOpName())){
			setText(value.getOpName());
		}
		else{
			setText("no opName found");
		}
		stateSection.setVisible(!(value.isState() == null));		
	}
}
