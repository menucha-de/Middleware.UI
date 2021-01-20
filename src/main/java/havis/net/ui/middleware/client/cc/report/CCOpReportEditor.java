package havis.net.ui.middleware.client.cc.report;

import havis.middleware.ale.service.cc.CCOpReport;
import havis.net.ui.middleware.client.shared.AutoHeightTextArea;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.ConfigurationSection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CCOpReportEditor extends ConfigurationSection implements ValueAwareEditor<havis.middleware.ale.service.cc.CCOpReport> {

	@UiField
	TextBox opStatus;
		
	@UiField
	AutoHeightTextArea data;
	
	@Ignore
	@UiField
	FlowPanel panel;
	
	@Ignore
	@UiField FlowPanel dataPanel;
	
	private ConstantsResource res = ConstantsResource.INSTANCE;

	private static PCOpReportEditorUiBinder uiBinder = GWT.create(PCOpReportEditorUiBinder.class);
	interface PCOpReportEditorUiBinder extends UiBinder<Widget, CCOpReportEditor> { }
	

	public CCOpReportEditor(final int index) {
		initWidget(uiBinder.createAndBindUi(this));
		setText(res.operationReport());
	}


	@Override
	public void setDelegate(EditorDelegate<CCOpReport> delegate) { }

	@Override
	public void flush() { }

	@Override
	public void onPropertyChange(String... paths) { }

	@Override
	public void setValue(final CCOpReport value) {
		if(value != null){
			setText(value.getOpName());			
			dataPanel.setVisible(!Utils.isNullOrEmpty(value.getData()));
		}
	}
	
	public void calculateHeight(){
		data.calculateHeight();
	}
}
