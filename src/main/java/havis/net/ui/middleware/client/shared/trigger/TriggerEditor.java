package havis.net.ui.middleware.client.shared.trigger;

import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.trigger.model.HttpTrigger;
import havis.net.ui.middleware.client.shared.trigger.model.PortTrigger;
import havis.net.ui.middleware.client.shared.trigger.model.RtcTrigger;
import havis.net.ui.middleware.client.shared.trigger.model.Trigger;
import havis.net.ui.middleware.client.shared.trigger.model.TriggerScheme;
import havis.net.ui.middleware.client.shared.trigger.model.TriggerType;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.widgets.CustomListBox;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.SimpleEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.Widget;

public class TriggerEditor extends Composite implements ValueAwareEditor<String> {

	@Path("")
	SimpleEditor<String> urn;
	
	private Trigger trigger;
	private EditorDelegate<String> delegate;
	
	@UiField
	@Ignore
	RtcTriggerEditor rtcTriggerEditor;

	@UiField
	@Ignore
	HttpTriggerEditor httpTriggerEditor;

	@UiField
	@Ignore
	PortTriggerEditor portTriggerEditor;
	
	private ConstantsResource res = ConstantsResource.INSTANCE;

	private RtcTriggerDriver rtcTriggerDriver = GWT.create(RtcTriggerDriver.class);
	interface RtcTriggerDriver extends SimpleBeanEditorDriver<RtcTrigger, RtcTriggerEditor> {}

	private HttpTriggerDriver httpTriggerDriver = GWT.create(HttpTriggerDriver.class);
	interface HttpTriggerDriver extends SimpleBeanEditorDriver<HttpTrigger, HttpTriggerEditor> {}

	private PortTriggerDriver portTriggerDriver = GWT.create(PortTriggerDriver.class);
	interface PortTriggerDriver extends SimpleBeanEditorDriver<PortTrigger, PortTriggerEditor> {}

	@UiField
	@Ignore
	CustomListBox<TriggerScheme> scheme;

	@UiField
	@Ignore
	CustomListBox<TriggerType> type;

	private static TriggerEditorUiBinder uiBinder = GWT.create(TriggerEditorUiBinder.class);

	interface TriggerEditorUiBinder extends UiBinder<Widget, TriggerEditor> {
	}

	public TriggerEditor() {
		urn = SimpleEditor.of("");
		
		initWidget(uiBinder.createAndBindUi(this));
		
		scheme.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				TriggerScheme triggerScheme = TriggerEditor.this.scheme.getSelectedValue();
				type.clear();
				if (triggerScheme != null)
					type.setItems(triggerScheme.getTriggerTypes());
				update("");
				Utils.removeErrorStyle(scheme);
			}
		});
		scheme.setItems(TriggerScheme.values());
		type.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				update("");
				Utils.removeErrorStyle(type);
			}
		});
		
		rtcTriggerDriver.initialize(rtcTriggerEditor);
		httpTriggerDriver.initialize(httpTriggerEditor);
		portTriggerDriver.initialize(portTriggerEditor);
	}

	@Override
	public void setValue(final String value) {
		TriggerScheme scheme = Trigger.getTriggerScheme(value.toString());
		TriggerType type = null;
		this.scheme.setValue(scheme);
		if (scheme != null) {
			type = Trigger.getTriggerType(value.toString());
			this.type.setValue(type, false);
		} else {
			this.type.setValue(null);
		}
		update(value);
	}

	private void update(String urn) {
		trigger = getTrigger(scheme.getSelectedValue(), type.getSelectedValue(), urn.toString());		
		rtcTriggerEditor.setVisible(false);
		httpTriggerEditor.setVisible(false);
		portTriggerEditor.setVisible(false);

		if (trigger instanceof RtcTrigger) {
			rtcTriggerDriver.edit((RtcTrigger) trigger);
			rtcTriggerEditor.setVisible(true);
		} else if (trigger instanceof HttpTrigger) {
			httpTriggerDriver.edit((HttpTrigger) trigger);
			httpTriggerEditor.setVisible(true);
		} else if (trigger instanceof PortTrigger) {
			portTriggerDriver.edit((PortTrigger) trigger);
			portTriggerEditor.setVisible(true);
		}
	}

	/**
	 * Determines the trigger type and returns the corresponding class
	 * 
	 * @param scheme
	 * @param type
	 * @param urn
	 * @return the trigger class
	 */
	private Trigger getTrigger(TriggerScheme scheme, TriggerType type, final String urn) {
		Trigger trigger = null;
		if (scheme != null) {
			if (scheme == TriggerScheme.EPC_GLOBAL && type == TriggerType.RTC) {
				trigger = new RtcTrigger(urn);
			} else if (scheme == TriggerScheme.HAVIS) {
				if (type == TriggerType.HTTP) {
					trigger = new HttpTrigger(urn);
				} else if (type == TriggerType.PORT) {
					trigger = new PortTrigger(urn);
				}
			}
		} else {
			trigger = new Trigger() {
				@Override
				public String toUri() {
					return urn;
				}
			};
		}
		return trigger;
	}

	@Override
	public void setDelegate(EditorDelegate<String> delegate) {
		this.delegate = delegate;
	}

	private void collectErrors(List<EditorError> errors) {
		for (EditorError error : errors) {
			delegate.recordError(error.getMessage(), error.getValue(), error.getUserData());
		}
	}
	
	@Override
	public void flush() {
		
		if(scheme.getSelectedValue() == null){
			delegate.recordError(res.errorNotSpecified(res.scheme()), scheme, scheme);
			Utils.addErrorStyle(scheme);
			return;
		}
		
		if(type.getSelectedValue() == null){
			delegate.recordError(res.errorNotSpecified(res.type()), type, type);
			Utils.addErrorStyle(type);
			return;
		}
		
		boolean hasErrors = false;
		if (trigger instanceof RtcTrigger) {
			LongBox tmpBox;
			if((tmpBox = rtcTriggerEditor.checkNullValues(delegate)) != null){
				Utils.addErrorStyle(tmpBox);
				return;
			}
			
			rtcTriggerDriver.flush();
			if (rtcTriggerDriver.hasErrors()) {
				collectErrors(rtcTriggerDriver.getErrors());
				hasErrors = true;
			}
		} else if (trigger instanceof HttpTrigger) {
			httpTriggerDriver.flush();
			if (httpTriggerDriver.hasErrors()) {
				collectErrors(httpTriggerDriver.getErrors());
				hasErrors = true;
			}
		} else if (trigger instanceof PortTrigger) {
			portTriggerDriver.flush();
			if (portTriggerDriver.hasErrors()) {
				collectErrors(portTriggerDriver.getErrors());
				hasErrors = true;
			}
		}
		
		
		
		
		if (!hasErrors) {
			urn.setValue(trigger.toUri());
		}
	}

	@Override
	public void onPropertyChange(String... paths) {}	
}
