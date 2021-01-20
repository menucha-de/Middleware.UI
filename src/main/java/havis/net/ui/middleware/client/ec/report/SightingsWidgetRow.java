package havis.net.ui.middleware.client.ec.report;

import havis.net.ui.shared.client.table.CustomWidgetRow;
import havis.net.ui.shared.resourcebundle.ResourceBundle;

import java.util.Date;

import org.fusesource.restygwt.client.Defaults;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;

public class SightingsWidgetRow extends CustomWidgetRow {
	private Label reader = new Label();
	private Label antenna = new Label();
	private Label strength = new Label();
	private Label timestamp = new Label();

	public SightingsWidgetRow(String reader, int antenna, int strength, Date timestamp) {
		this(reader, "" + antenna, "" + strength, DateTimeFormat.getFormat(Defaults.getDateFormat()).format(timestamp));
	}

	public SightingsWidgetRow(String reader, String antenna, String strength, String timestamp) {
		this.reader.setText(reader);
		this.reader.addStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableLabel());
		this.reader.getElement().setAttribute("style", "word-break:break-all");
		this.antenna.setText(antenna);
		this.antenna.addStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableLabel());
		this.strength.setText(strength);
		this.strength.addStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableLabel());
		this.timestamp.setText(timestamp);
		this.timestamp.addStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableLabel());
		this.reader.getElement().getStyle().setCursor(Cursor.DEFAULT);
		this.antenna.getElement().getStyle().setCursor(Cursor.DEFAULT);
		this.strength.getElement().getStyle().setCursor(Cursor.DEFAULT);
		this.timestamp.getElement().getStyle().setCursor(Cursor.DEFAULT);

		addColumn(this.reader);
		addColumn(this.antenna);
		addColumn(this.strength);
		addColumn(this.timestamp);
	}
}