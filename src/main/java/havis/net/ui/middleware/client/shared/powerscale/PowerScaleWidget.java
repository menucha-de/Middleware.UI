package havis.net.ui.middleware.client.shared.powerscale;

import havis.net.ui.middleware.client.shared.resourcebundle.CSSResource;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class PowerScaleWidget extends Composite implements HasChangeHandlers, HasChangeEndHandlers {

	private List<ChangeHandler> handlers = new ArrayList<ChangeHandler>();
	
	private List<ChangeEndHandler> endHandlers = new ArrayList<ChangeEndHandler>();

	@UiField
	FlowPanel lifeScale;
	
	@UiField
	FlowPanel space;
	
	@UiField
	Button increase;
	
	@UiField
	Button decrease;

	private Integer value;
	private List<Integer> range;
	private int listIndex;
	private CSSResource css = ResourceBundle.INSTANCE.css();
	private int maxIndex = 17;
	
	private Timer repeatTimer;
	private Timer waitTimer;
	
	private boolean autoRepeat = false;
	private boolean mouseWasDown = false;
	
	private MouseOutHandler mouseOut = new MouseOutHandler() {
		
		@Override
		public void onMouseOut(MouseOutEvent event) {
			if(repeatTimer != null) repeatTimer.cancel();
			if(waitTimer != null) waitTimer.cancel();
			if(mouseWasDown)fireChangeEnd();
			mouseWasDown = false;
		}
	};
	
	
	private MouseUpHandler mouseUp = new MouseUpHandler() {
		@Override
		public void onMouseUp(MouseUpEvent event) {
			if(repeatTimer != null) repeatTimer.cancel();
			if(waitTimer != null) waitTimer.cancel();
			fireChangeEnd();
			mouseWasDown = false;
		}
	};
	
	private HandlerRegistration increaseOutHdlReg;
	private HandlerRegistration decreaseOutHdlReg;
	private HandlerRegistration increaseDownHdlReg;
	private HandlerRegistration decreaseDownHdlReg;
	private HandlerRegistration increaseUpHdlReg;
	private HandlerRegistration decreaseUpHdlReg;
	

	private static PowerScaleWidgetUiBinder uiBinder = GWT.create(PowerScaleWidgetUiBinder.class);

	interface PowerScaleWidgetUiBinder extends UiBinder<Widget, PowerScaleWidget> {
	}

	@UiConstructor
	public PowerScaleWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		lifeScale.setStyleName(css.webuiPowerScaleEmpty());
		listIndex = 0;
	}

	/**
	 * Max amount of values is 18
	 * 
	 * @param range
	 */
	public void setRange(List<Integer> range) {
		this.range = range;
	}

	/**
	 * Switch the autoRepeat feature on or off
	 * @param value
	 */
	public void setAutoRepeat(boolean value) {
		autoRepeat = value;
		
		if(autoRepeat){
			//+ button clicked
			increaseDownHdlReg = increase.addMouseDownHandler(new MouseDownHandler() {
				@Override
				public void onMouseDown(MouseDownEvent event) {
					mouseWasDown = true;
					doAutoRepeat(true);
				}
			});
			//- button clicked
			decreaseDownHdlReg = decrease.addMouseDownHandler(new MouseDownHandler() {
				@Override
				public void onMouseDown(MouseDownEvent event) {
					mouseWasDown = true;
					doAutoRepeat(false);
				}
			});
			
			increaseOutHdlReg = increase.addMouseOutHandler(mouseOut);
			decreaseOutHdlReg = decrease.addMouseOutHandler(mouseOut);
			
			increaseUpHdlReg = increase.addMouseUpHandler(mouseUp);
			decreaseUpHdlReg = decrease.addMouseUpHandler(mouseUp);
		}
		else{
			
			if(increaseOutHdlReg != null) increaseOutHdlReg.removeHandler();
			if(decreaseOutHdlReg != null) decreaseOutHdlReg.removeHandler();
			
			if(increaseDownHdlReg != null) increaseDownHdlReg.removeHandler();
			if(decreaseDownHdlReg != null) increaseDownHdlReg.removeHandler();
			if(increaseUpHdlReg != null) increaseDownHdlReg.removeHandler();
			if(decreaseUpHdlReg != null) increaseDownHdlReg.removeHandler();
		}
	}

	/**
	 * Getter for autoRepeat
	 * @return autoRepeat
	 */
	public boolean getAutoRepeat() {
		return autoRepeat;
	}
	
	
	/**
	 * Sets the internal value of the widget
	 * @param value
	 */
	public void setValue(Integer value) {
		this.value = value;
		
		while (space.getWidgetCount() > 0) {
			space.remove(0);
		}
		listIndex = 0;
		
		if (range != null) {			
			for (Integer i : range) {
				if (i < value) {
					addBar();
				}
			}
			updateStyle();
		}
	}
	
	/**
	 * Getter for value
	 * @return value
	 */
	public Integer getValue() {
		return value;
	}

	
	@Override
	public HandlerRegistration addChangeHandler(final ChangeHandler handler) {
		handlers.add(handler);
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {
				handlers.remove(handler);
			}
		};
	}
	
	@Override
	public HandlerRegistration addChangeEndHandler(final ChangeEndHandler handler) {
		endHandlers.add(handler);
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {
				endHandlers.remove(handler);
			}
		};
	}

	/**
	 * Fires all registered ChangeEndHandlers
	 */
	private void fireChangeEnd() {
		if (endHandlers != null) {
			//Timer is important.
			Timer endTimer = new Timer() {
				@Override
				public void run() {
					for (ChangeEndHandler handler : endHandlers) {
						handler.onChangeEnd(new ChangeEndEvent(value));
					}
					//GWT.log("onChangeEND: " + value);
				}
			};
			endTimer.schedule(100);
		}
	}
	
	
	/**
	 * Fires all registered ChangeHandlers
	 */
	private void fireChange() {
		if (handlers != null) {
			for (ChangeHandler handler : handlers) {
				handler.onChange(null);
			}
		}
	}

	@UiHandler("increase")
	public void increaseValue(ClickEvent e) {
		addBar(); //Adds a bar image if needed.
		updateStyle(); //Changes the underlying image.
		
		updateValue(listIndex);
		fireChange();
	}

	@UiHandler("decrease")
	public void decreaseValue(ClickEvent e) {
		removeBar();
		updateStyle(); //Changes the underlying image.
		
		updateValue(listIndex);
		fireChange();
	}

	/**
	 * Gets the indexed value from the range list
	 */
	private void updateValue(int idx) {
		if (range != null) {
			if (range.size() > idx) {
				value = range.get(idx);
			} else {
				value = null;
			}
		}
	}
	
	/**
	 * Adds a bar image if needed. The positions 0 and the last position only change the underlying image
	 */
	private void addBar(){
		if(listIndex < 1 || listIndex == (maxIndex-1)){
			listIndex++;
		}
		else if (listIndex < maxIndex-1) {
			space.add(new Image(ResourceBundle.INSTANCE.llrpPowerScaleActiveUnit().getSafeUri()));
			listIndex++;
		}
	}
	/**
	 * Removes a bar image if needed and possible.
	 */
	private void removeBar(){
		if (listIndex > 1 && listIndex < maxIndex-1) {
			if(space.getWidgetCount() > 0) space.remove(0);
			listIndex--;
		}
		else if (listIndex > 0){
			if(listIndex < 17 && space.getWidgetCount() > 0) space.remove(0);
			listIndex--;
		}
	}	
	
	/**
	 * Changes the underlying image. Three different states are possible: empty|active|full
	 */
	private void updateStyle() {
		if(listIndex < 1)
			lifeScale.setStyleName(css.webuiPowerScaleEmpty());
		else if(listIndex < maxIndex)
			lifeScale.setStyleName(css.webuiPowerScale());
		else{
			lifeScale.setStyleName(css.webuiPowerScaleFull());
		}
	}
	
	/**
	 * The autoRepeat worker
	 * @param increase
	 */
	private void doAutoRepeat(final boolean increase){
		waitTimer = new Timer() {
			
			@Override
			public void run() {
				if(increase){
					repeatTimer = new Timer() {
						@Override
						public void run() {
							increaseValue(null);
							if(listIndex >= maxIndex){
								repeatTimer.cancel();
							}
						}
					};
				}
				else{
					repeatTimer = new Timer() {
						@Override
						public void run() {
							decreaseValue(null);
							if(listIndex < 1){
								repeatTimer.cancel();
							}
						}
					};
				}
				repeatTimer.scheduleRepeating(100);
			}

		};
		waitTimer.schedule(400);
	}
}
