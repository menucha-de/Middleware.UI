package havis.net.ui.middleware.client.shared.report.list;

import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent.Handler;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;
import havis.net.ui.shared.client.table.CustomWidgetRow;
import havis.net.ui.shared.client.widgets.CustomRenderer;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;


public class SingleColumnRowBuilder<T> extends CustomWidgetRow implements LeafValueEditor<T>, OpenItemEvent.HasHandlers {

	private CustomRenderer<T> renderer;
	
	T value;
	
	Label reportName = new Label();
	
	private int index;
	
	public SingleColumnRowBuilder(CustomRenderer<T> renderer) {
		reportName.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableLabel());
		this.renderer = renderer;
		reportName.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new OpenItemEvent(index,0));
			}
		});
		
		addColumn(reportName);
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
		this.reportName.setText(renderer.render(value));
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public HandlerRegistration addOpenItemHandler(Handler handler) {
		return addHandler(handler, OpenItemEvent.getType());
	}
	
	public String getName() {
		return reportName.getText();
	}
	
}
