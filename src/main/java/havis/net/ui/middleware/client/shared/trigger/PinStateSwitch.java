package havis.net.ui.middleware.client.shared.trigger;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasValue;

import havis.net.ui.middleware.client.shared.trigger.model.PinState;

public class PinStateSwitch extends FocusPanel implements HasValue<PinState>, IsEditor<LeafValueEditor<PinState>> {

	private static final String BASE_STYLE = "webui-PinStateSwitch";

	private PinState pinState;
	private LeafValueEditor<PinState> editor;

	public PinStateSwitch() {
		this.setStylePrimaryName(BASE_STYLE);
		addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (pinState == PinState.OFF) {
					setValue(PinState.IRRELEVANT);
				} else if (pinState == PinState.IRRELEVANT) {
					setValue(PinState.ON);
				} else if (pinState == PinState.ON) {
					setValue(PinState.OFF);
				}
			}
		});
	}

	private String getStyle(PinState type) {
		return type.toString().toLowerCase();
	}
	
	private void updateStyle() {
		this.removeStyleDependentName(getStyle(PinState.OFF));
		this.removeStyleDependentName(getStyle(PinState.ON));
		this.removeStyleDependentName(getStyle(PinState.IRRELEVANT));
		
		this.setStyleDependentName(pinState.toString().toLowerCase(), true);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<PinState> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public PinState getValue() {
		return pinState;
	}

	@Override
	public void setValue(PinState value) {
		this.pinState = value != null ? value : PinState.IRRELEVANT;
		ValueChangeEvent.fire(this, value);
		updateStyle();
	}

	@Override
	public void setValue(PinState value, boolean fireEvents) {
		setValue(pinState);
	}

	@Override
	public LeafValueEditor<PinState> asEditor() {
		if (editor == null) {
			editor = TakesValueEditor.of(this);
		}
		return editor;
	}
}
