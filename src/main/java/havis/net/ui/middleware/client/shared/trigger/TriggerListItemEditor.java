package havis.net.ui.middleware.client.shared.trigger;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;

import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent.Handler;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;
import havis.net.ui.shared.client.table.CustomWidgetRow;

public class TriggerListItemEditor extends CustomWidgetRow implements LeafValueEditor<String>, OpenItemEvent.HasHandlers {

	Label uri = new Label();

	private int index;

	public TriggerListItemEditor() {
		uri.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableLabel());
		uri.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new OpenItemEvent(index,0));
			}
		});
		addColumn(uri);
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public void setValue(String value) {
		this.uri.setText(value);
	}

	@Override
	public String getValue() {
		return uri.getText();		
	}

	@Override
	public HandlerRegistration addOpenItemHandler(Handler handler) {
		return addHandler(handler, OpenItemEvent.getType());
	}
}
