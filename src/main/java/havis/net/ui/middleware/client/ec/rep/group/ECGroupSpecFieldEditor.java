package havis.net.ui.middleware.client.ec.rep.group;
import havis.middleware.ale.service.ec.ECGroupSpecExtension;
import havis.net.ui.middleware.client.shared.field.ECFieldSpecEditor;
import havis.net.ui.shared.client.ConfigurationSection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class ECGroupSpecFieldEditor extends ConfigurationSection
	implements Editor<ECGroupSpecExtension>{

	private static ECGroupSpecEditorUiBinder uiBinder = GWT
			.create(ECGroupSpecEditorUiBinder.class);

	interface ECGroupSpecEditorUiBinder extends UiBinder<Widget, ECGroupSpecFieldEditor> {
	}
	
	@Path("fieldspec") 
	@UiField
	public ECFieldSpecEditor ecFieldSpecEditor;
	
	
	public ECGroupSpecFieldEditor() {
		setText("Field");
		initWidget(uiBinder.createAndBindUi(this));
		ecFieldSpecEditor.setIgnoreEmptyDataType(true);
		ecFieldSpecEditor.setGroupingFilterPatternView(true);
	}

}

