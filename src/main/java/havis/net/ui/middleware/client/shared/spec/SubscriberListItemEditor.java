package havis.net.ui.middleware.client.shared.spec;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.adapters.SimpleEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;

import havis.middleware.ale.service.mc.MCSubscriberSpec;
import havis.net.ui.middleware.client.shared.event.EnableSpecEvent;
import havis.net.ui.middleware.client.shared.event.SpecEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;
import havis.net.ui.shared.client.table.CustomWidgetRow;

public class SubscriberListItemEditor extends CustomWidgetRow implements Editor<MCSubscriberSpec>, EnableSpecEvent.HasHandlers, SpecEvent.HasHandlers {

	@Path("id")
	SimpleEditor<String> subId = SimpleEditor.of();
	
	Label uri = new Label();
	
	ToggleButton enable = new ToggleButton();
	
	public SubscriberListItemEditor() {
		uri.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableLabel());

		enable.setStyleName("webui-EnableButton subscriber " + ResourceBundle.INSTANCE.css().subscriberList());
		enable.getDownFace().setText(ConstantsResource.INSTANCE.subscriberActive());
		enable.getUpFace().setText(ConstantsResource.INSTANCE.subscriberInactive());		
		
		enable.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				fireEvent(new EnableSpecEvent(subId.getValue(), event.getValue()));
			}
		});
		uri.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new SpecEvent(subId.getValue(), SpecEvent.Action.EDIT));
			}
		});

		addColumn(uri);
		addColumn(enable);
	}

	@Override
	public HandlerRegistration addSpecEventHandler(SpecEvent.Handler handler) {
		return addHandler(handler, SpecEvent.getType());
	}

	@Override
	public HandlerRegistration addEnableSpecHandler(EnableSpecEvent.Handler handler) {
		return addHandler(handler, EnableSpecEvent.getType());
	}
	
	public String getUri(){
		return uri.getText();
	}
	
	public void setValue(boolean value){
		enable.setValue(value);
	}
	
	public void setLoading(boolean isLoading) {
		enable.setEnabled(!isLoading);
		if(isLoading) {
			enable.addStyleName(ResourceBundle.INSTANCE.css().specStateChanging());
		} else {
			enable.removeStyleName(ResourceBundle.INSTANCE.css().specStateChanging());
		}
	}
}
