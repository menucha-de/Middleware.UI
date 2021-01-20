package havis.net.ui.middleware.client.cc.report;

import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.TextBox;

import havis.middleware.ale.service.cc.CCParameterListEntry;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;
import havis.net.ui.shared.client.table.CustomWidgetRow;

public class CCParameterEditor extends CustomWidgetRow implements Editor<CCParameterListEntry> {

	TextBox name = new TextBox();

	TextBox value = new TextBox();

	public CCParameterEditor() {
		name.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableTextBox());
		name.setEnabled(false);
		name.getElement().getStyle().setColor("#333");
		addColumn(name);

		value.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableTextBox());
		addColumn(value);
	}

	public String getName() {
		return name.getValue();
	}

	private native void selectAll(Element element) /*-{
		element.setSelectionRange(0, element.value.length);
	}-*/;

	public void setStartFocus() {
		value.setFocus(true);
		selectAll(value.getElement());
		
	}
}
