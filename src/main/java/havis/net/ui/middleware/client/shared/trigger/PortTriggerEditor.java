package havis.net.ui.middleware.client.shared.trigger;

import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.trigger.model.PortTrigger;
import havis.net.ui.middleware.client.shared.trigger.model.PortTrigger.PinType;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.widgets.CustomListBox;
import havis.net.ui.shared.client.widgets.CustomRenderer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Widget;

public class PortTriggerEditor extends Composite implements ValueAwareEditor<PortTrigger> {

	private EditorDelegate<PortTrigger> delegate;
	
	private ConstantsResource res = ConstantsResource.INSTANCE;

	@UiField
	FlowPanel stateArea;

	@UiField(provided = true)
	CustomListBox<String> reader = new CustomListBox<String>(new CustomRenderer<String>() {

		@Override
		public String render(String value) {
			return value;
		}
	}, true);

	@Ignore
	@UiField
	IntegerBox customId;

	@UiField
	CustomListBox<PinType> pinType;

	@UiField(provided = true)
	CustomListBox<Integer> id = new CustomListBox<Integer>(new CustomRenderer<Integer>() {

		@Override
		public String render(Integer value) {
			if (value == null) {
				return null;
			} else {
				switch (value) {
				case PortTrigger.PIN_ANY:
					return "Any";
				case PortTrigger.PIN_CUSTOM:
					return "Custom";
				default:
					return value.toString();
				}
			}

		}
	}) {

		@Override
		public Integer getValue() {
			//calls on loading and unloading, not on change
			Integer value = super.getValue();
			if (value == null) return PortTrigger.PIN_ANY;
			
			switch (value) {
				case PortTrigger.PIN_CUSTOM: //-2
					if (customId.getValue() == null) return PortTrigger.PIN_CUSTOM;
					return customId.getValue();
				default:
					return value;
			}
			
		}
	};

	@UiField
	PinStateSwitch state;

	private static PortTriggerEditorUiBinder uiBinder = GWT.create(PortTriggerEditorUiBinder.class);

	interface PortTriggerEditorUiBinder extends UiBinder<Widget, PortTriggerEditor> {
	}

	public PortTriggerEditor() {
		initWidget(uiBinder.createAndBindUi(this));

		id.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (id.getSelectedValue() == PortTrigger.PIN_ANY) {
					stateArea.setVisible(false);
				} else {
					stateArea.setVisible(true);
				}
				
				Integer selval = id.getSelectedValue();
				if (selval == PortTrigger.PIN_CUSTOM) {
					boolean canClearId = (selval == null || selval == PortTrigger.PIN_CUSTOM);
					if(canClearId)
						customId.setValue(null);
					
					customId.setVisible(true);
				} else {
					customId.setVisible(false);
				}
			}
		});

		pinType.setItems(PortTrigger.PinType.values());
		id.setItems(new Integer[] { PortTrigger.PIN_ANY, 1, 2, 3, 4, PortTrigger.PIN_CUSTOM });
	}

	@Override
	public void setDelegate(EditorDelegate<PortTrigger> delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void flush() {
		if (delegate != null) {
			if (reader.getSelectedValue() == null) {
				delegate.recordError(res.errorNotSpecified(res.reader()), reader.getSelectedValue(), reader);
				Utils.addErrorStyle(reader);
				return;
			}
			Utils.removeErrorStyle(reader);
			
			if (pinType.getSelectedValue() == null) {
				delegate.recordError(res.errorNotSpecified(res.pinType()), pinType.getSelectedValue(), pinType);
				Utils.addErrorStyle(pinType);
				return;
			}
			Utils.removeErrorStyle(pinType);
			
			if (id.getSelectedValue() == PortTrigger.PIN_CUSTOM){
				
				if (customId.getValue() == null) {
					delegate.recordError(res.errorInvalidEmptyField("PIN"), customId.getValue(), customId);
					Utils.addErrorStyle(customId);
					return;
				}
				if(customId.getValue() == 0) {
					delegate.recordError( res.errorInvalid("PIN") + " " + res.errorOutOfRange(), customId.getValue(), customId);
					Utils.addErrorStyle(customId);
					return;
				} else if (customId.getValue() < 0 || customId.getValue() > 254) {
					delegate.recordError( res.errorInvalid("PIN") + " " + res.errorNotUCHAR(), customId.getValue(), customId);
					Utils.addErrorStyle(customId);
					return;
				}
			}
			Utils.removeErrorStyle(customId);
			
		}
	}

	@Override
	public void onPropertyChange(String... paths) { }
	

	@Override
	public void setValue(PortTrigger value) {
		if(value != null){
			setPcOpSpecIdWidgets(value.getId());
		}
	}
	
	/**
	 * Sets the involved widgets 'id' and 'customId' according to given value
	 * Used in setValue(...)
	 * @param svalue
	 */
	private void setPcOpSpecIdWidgets(int svalue){
		final int uvalue = svalue & 0xFF;
		
		if(uvalue > 4 && uvalue < 255){
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					id.setSelectedValue(PortTrigger.PIN_CUSTOM);
					customId.setValue(uvalue);
					customId.setVisible(true);
				}
			});
		}
		if(uvalue == 255){
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					id.setSelectedValue(PortTrigger.PIN_ANY);
				}
			});
		}
	}
}
