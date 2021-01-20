package havis.net.ui.middleware.client.ec.report;

import havis.net.ui.shared.client.table.CustomWidgetRow;
import havis.net.ui.shared.resourcebundle.ResourceBundle;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.user.client.ui.Label;

public class ValueWidgetRow extends CustomWidgetRow {
	private Label key = new Label();
	private Label value = new Label();

	public ValueWidgetRow(String key, String value) {
		this.key.setText(key);
		this.key.addStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableLabel());
		this.value.setText(value);
		this.value.addStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableLabel());
		this.value.getElement().setAttribute("style", "word-break:break-all");
		this.key.getElement().getStyle().setColor("#000");
		this.key.getElement().getStyle().setCursor(Cursor.DEFAULT);
		this.value.getElement().getStyle().setColor("#000");
		this.value.getElement().getStyle().setCursor(Cursor.DEFAULT);
		addColumn(this.key);
		addColumn(this.value);
	}

	public String getKey() {
		return key.getText();
	}

	public void setKey(String key) {
		this.key.setText(key);
	}

	public String getValue() {
		return value.getText();
	}

	public void setValue(String value) {
		this.value.setText(value);
	}

	public void addValue(String value) {
		this.value.getElement().setInnerHTML(this.value.getElement().getInnerHTML() + "<br>" + value);
	}

}
