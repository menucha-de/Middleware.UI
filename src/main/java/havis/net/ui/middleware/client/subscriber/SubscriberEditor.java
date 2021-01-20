package havis.net.ui.middleware.client.subscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

import havis.middleware.ale.service.mc.MCProperty;
import havis.middleware.ale.service.mc.MCSubscriberSpec;
import havis.net.ui.middleware.client.utils.Utils;
import havis.transport.ui.client.TransportPanel;
import havis.transport.ui.client.TransportType;
import havis.transport.ui.client.event.SaveTransportEvent;
import havis.transport.ui.client.event.SaveTransportEvent.Handler;
import havis.transport.ui.client.event.TransportErrorEvent;

public class SubscriberEditor extends Composite implements ValueAwareEditor<MCSubscriberSpec>, SaveTransportEvent.HasHandlers {

	@UiField
	ToggleButton enable;

	@UiField
	TransportPanel editor;

	private MCSubscriberSpec spec;

	private EditorDelegate<MCSubscriberSpec> delegate;

	private static SubscriberEditorUiBinder uiBinder = GWT.create(SubscriberEditorUiBinder.class);

	interface SubscriberEditorUiBinder extends UiBinder<Widget, SubscriberEditor> {
	}

	public SubscriberEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private Map<String, String> getPropertiesMap(List<MCProperty> properties) {
		Map<String, String> result = new HashMap<String, String>();
		for (MCProperty property : properties) {
			result.put(property.getName(), property.getValue());
		}
		return result;
	}

	private MCSubscriberSpec.Properties getProperties(Map<String, String> properties) {
		MCSubscriberSpec.Properties result = new MCSubscriberSpec.Properties();
		for (Map.Entry<String, String> property : properties.entrySet()) {
			result.getProperty().add(new MCProperty(property.getKey(), property.getValue()));
		}
		return result;
	}

	public void setTransportTypes(List<TransportType> types) {
		editor.setTypes(types);
	}

	@UiHandler("editor")
	void onSaveTransport(SaveTransportEvent event) {
		spec.setUri(event.getUri());
		spec.setProperties(getProperties(event.getProperties()));
	}

	@UiHandler("editor")
	void onTransportError(TransportErrorEvent event) {
		String error = event.isException() ? Utils.getReason(event.getException()) : event.getErrorMessage();
		delegate.recordError(error, null, null);
	}

	@Override
	public void setValue(MCSubscriberSpec value) {
		spec = value;
		editor.setData(spec.getUri(), getPropertiesMap(value.getProperties().getProperty()));
	}

	@Override
	public void flush() {
		editor.saveTransportData();
	}

	@Override
	public void setDelegate(EditorDelegate<MCSubscriberSpec> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public HandlerRegistration addSaveTransportHandler(Handler handler) {
		return editor.addSaveTransportHandler(handler);
	}
}