package havis.net.ui.middleware.client.shared.trigger;

import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.trigger.model.RtcTrigger;


import havis.net.ui.middleware.client.utils.Utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.Widget;

public class RtcTriggerEditor extends Composite implements ValueAwareEditor<RtcTrigger> {

	private EditorDelegate<RtcTrigger> delegate;
	
	private ConstantsResource res = ConstantsResource.INSTANCE;

	@UiField
	LongBox periodHours;
	@UiField
	LongBox periodMinutes;
	@UiField
	LongBox periodSeconds;
	@UiField
	LongBox periodMilliseconds;

	@UiField
	LongBox offsetHours;
	@UiField
	LongBox offsetMinutes;
	@UiField
	LongBox offsetSeconds;
	@UiField
	LongBox offsetMilliseconds;

	private static RtcTriggerEditorUiBinder uiBinder = GWT.create(RtcTriggerEditorUiBinder.class);
	interface RtcTriggerEditorUiBinder extends UiBinder<Widget, RtcTriggerEditor> { }

	private String hr;
	private String min;
	private String sec;
	private String mils;
	
	
	KeyUpHandler longBoxKeyUp = new KeyUpHandler() {

		@Override
		public void onKeyUp(KeyUpEvent event) {
			LongBox box = (LongBox)event.getSource();
			try {
				Long value = box.getValueOrThrow();
				if(value == null){
					return;
				}
				if(value < 0){
					box.setValue((long) 0);
					return;
				}
				
				String title = box.getTitle().toLowerCase();
				if(title.startsWith(hr) && value > 23){
					box.setValue((long) 23);
				}
				else if(title.startsWith(min) && value > 59){
					box.setValue((long) 59);
				}
				else if(title.startsWith(sec) && value > 59){
					box.setValue((long) 59);			
				}
				else if(title.startsWith(mils) && value > 999){
					box.setValue((long) 999);
				}else{
					box.setValue((long) value);
				}
				
				Utils.removeErrorStyle(box);
				
			} catch (java.text.ParseException e) {
				box.setValue((long) 0);
			}
		}
	};
	
	
	@UiConstructor
	public RtcTriggerEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		
		hr = res.hours().toLowerCase().substring(0, 3);
		min = res.minutes().toLowerCase().substring(0, 3);
		sec = res.seconds().toLowerCase().substring(0, 3);
		mils = res.milliseconds().toLowerCase().substring(0, 3);
		
		periodHours.addKeyUpHandler(longBoxKeyUp);
		periodMinutes.addKeyUpHandler(longBoxKeyUp);
		periodSeconds.addKeyUpHandler(longBoxKeyUp);
		periodMilliseconds.addKeyUpHandler(longBoxKeyUp);
		
		offsetHours.addKeyUpHandler(longBoxKeyUp);
		offsetMinutes.addKeyUpHandler(longBoxKeyUp);
		offsetSeconds.addKeyUpHandler(longBoxKeyUp);
		offsetMilliseconds.addKeyUpHandler(longBoxKeyUp);
	}

	@Override
	public void setDelegate(EditorDelegate<RtcTrigger> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void flush() {
		if (delegate != null) {
			if (periodHours.getValue() == null || periodHours.getValue() > 23 || periodHours.getValue() < 0) {
				delegate.recordError(res.errorInvalidOrGreater("Period", "hours"), periodHours.getValue(), periodHours);
			} else if (periodMinutes.getValue() == null || periodMinutes.getValue() > 59 || periodMinutes.getValue() < 0) {
				delegate.recordError(res.errorInvalidOrGreater("Period", "minutes"), periodMinutes.getValue(), periodMinutes);
			} else if (periodSeconds.getValue() == null || periodSeconds.getValue() > 59 || periodSeconds.getValue() < 0) {
				delegate.recordError(res.errorInvalidOrGreater("Period", "seconds"), periodSeconds.getValue(), periodSeconds);
			} else if (periodMilliseconds.getValue() == null || periodMilliseconds.getValue() > 999 || periodMilliseconds.getValue() < 0) {
				delegate.recordError(res.errorInvalidOrGreater("Period", "milliseconds"), periodMilliseconds.getValue(),
						periodMilliseconds);
			} else if (offsetHours.getValue() == null || offsetHours.getValue() > 23 || offsetHours.getValue() < 0) {
				
				delegate.recordError(res.errorInvalid("Offset hours"), offsetHours.getValue(), offsetHours);
			} else if (offsetMinutes.getValue() == null || offsetMinutes.getValue() > 59 || offsetMinutes.getValue() < 0) {
				delegate.recordError(res.errorInvalid("Offset minutes"), offsetMinutes.getValue(), offsetMinutes);
			} else if (offsetSeconds.getValue() == null || offsetSeconds.getValue() > 59 || offsetSeconds.getValue() < 0) {
				delegate.recordError(res.errorInvalid("Offset seconds"), offsetSeconds.getValue(), offsetSeconds);
			} else if (offsetMilliseconds.getValue() == null || periodMilliseconds.getValue() > 999 || periodMilliseconds.getValue() < 0) {
				delegate.recordError(res.errorInvalid("Offset milliseconds"), offsetMilliseconds.getValue(),
						offsetMilliseconds); 
			} else {
				RtcTrigger temp = new RtcTrigger();
				temp.setPeriodHours(periodHours.getValue());
				temp.setPeriodMinutes(periodMinutes.getValue());
				temp.setPeriodSeconds(periodSeconds.getValue());
				temp.setPeriodMilliseconds(periodMilliseconds.getValue());

				temp.setOffsetHours(offsetHours.getValue());
				temp.setOffsetMinutes(offsetMinutes.getValue());
				temp.setOffsetSeconds(offsetSeconds.getValue());
				temp.setOffsetMilliseconds(offsetMilliseconds.getValue());
				if (temp.getPeriod() == 0) {
					delegate.recordError(res.errorInvalid("Period"), offsetMilliseconds.getValue(), offsetMilliseconds);
				} else if (temp.getPeriod() < temp.getOffset() + 1) {
					delegate.recordError(res.errorOffsetGreaterPeriod(), offsetMilliseconds.getValue(),
							offsetMilliseconds);
				}
			}
		}
	}

	@Override
	public void onPropertyChange(String... paths) { }
	

	@Override
	public void setValue(RtcTrigger value) { }
	
	/**
	 * Method to check the null content of the LongBoxes before flush is called
	 * Otherwise if any box contains a null value flush() of RtcTriggerEditor is not called due to
	 * a crash before.
	 * @param del the delegate of the caller
	 * @return the widget caused the trouble or null if all is OK.
	 */
	public LongBox checkNullValues(EditorDelegate<String> del){
		
		if(periodHours.getValue() == null){
			del.recordError(res.errorInvalid(res.period() + ", " + res.hours().toLowerCase()), periodHours.getValue(), periodHours);
			return periodHours;
		}
		if(periodMinutes.getValue() == null){
			del.recordError(res.errorInvalid(res.period() + ", " + res.minutes().toLowerCase()), periodMinutes.getValue(), periodMinutes);
			return periodMinutes;
		}
		if(periodSeconds.getValue() == null){
			del.recordError(res.errorInvalid(res.period() + ", " + res.seconds().toLowerCase()), periodSeconds.getValue(), periodSeconds);
			return periodSeconds;
		}
		if(periodMilliseconds.getValue() == null){
			del.recordError(res.errorInvalid(res.period() + ", " + res.milliseconds().toLowerCase()), periodMilliseconds.getValue(), periodMilliseconds);
			return periodMilliseconds;
		}

		if(offsetHours.getValue() == null){
			del.recordError(res.errorInvalid(res.offset() + ", " + res.hours().toLowerCase()), offsetHours.getValue(), offsetHours);
			return offsetHours;
		}
		if(offsetMinutes.getValue() == null){
			del.recordError(res.errorInvalid(res.offset() + ", " + res.minutes().toLowerCase()), offsetMinutes.getValue(), offsetMinutes);
			return offsetMinutes;
		}
		if(offsetSeconds.getValue() == null){
			del.recordError(res.errorInvalid(res.offset() + ", " +  res.seconds().toLowerCase()), offsetSeconds.getValue(), offsetSeconds);
			return offsetSeconds;
		}
		if(offsetMilliseconds.getValue() == null){
			del.recordError(res.errorInvalid(res.offset() + ", " + res.milliseconds().toLowerCase()), offsetMilliseconds.getValue(), offsetMilliseconds);
			return offsetMilliseconds;
		}
		return null;
	}
	
}
