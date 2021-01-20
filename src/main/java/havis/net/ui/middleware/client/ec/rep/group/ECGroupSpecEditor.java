package havis.net.ui.middleware.client.ec.rep.group;

import havis.middleware.ale.service.ec.ECGroupSpec;
import havis.middleware.ale.service.tm.TMFixedFieldSpec;
import havis.middleware.ale.service.tm.TMVariableFieldSpec;
import havis.net.ui.middleware.client.shared.report.list.SingleColumnListWidget;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.shared.client.ConfigurationSection;
import havis.net.ui.shared.client.widgets.CustomRenderer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class ECGroupSpecEditor extends ConfigurationSection implements Editor<ECGroupSpec>{
		
	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	@Path("extension") 
	@UiField
	public ECGroupSpecFieldEditor groupFieldEditor;
	
	@Path("pattern")
	@UiField (provided=true)
	public
	SingleColumnListWidget<String> groupPatternListWidget = new SingleColumnListWidget<String>(
		new CustomRenderer<String>() {
			@Override
			public java.lang.String render(String value) {
				return value;
			}
		});
	
	private static ECGroupSpecEditorUiBinder uiBinder = GWT.create(ECGroupSpecEditorUiBinder.class);
	interface ECGroupSpecEditorUiBinder extends UiBinder<Widget, ECGroupSpecEditor> { }
	
	public ECGroupSpecEditor() {
		setText(res.group());
		groupPatternListWidget.setText("Pattern list");
		groupPatternListWidget.setHeader(Arrays.asList(res.createItem(res.pattern())));
		initWidget(uiBinder.createAndBindUi(this));		
	}
	
	public void setTMFields(Map<String, List<TMFixedFieldSpec>> tmFixedFieldSpecs, Map<String, List<TMVariableFieldSpec>> tmVariableFieldSpecs){
		groupFieldEditor.ecFieldSpecEditor.setTMFields(tmFixedFieldSpecs, tmVariableFieldSpecs);
	}
	
	@Ignore
	public SingleColumnListWidget<String> getGroupPatternListWidget() {
		return groupPatternListWidget;
	}
}



