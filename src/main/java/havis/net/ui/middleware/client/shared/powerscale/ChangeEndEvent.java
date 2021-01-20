package havis.net.ui.middleware.client.shared.powerscale;

import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.event.dom.client.DomEvent;

/**
 * Represents a native change event.
 */
public class ChangeEndEvent extends DomEvent<ChangeEndHandler> {

	private Integer value;
	
  /**
   * Event type for change events. Represents the meta-data associated with this
   * event.
   */
  private static final Type<ChangeEndHandler> TYPE = new Type<ChangeEndHandler>(
      BrowserEvents.CHANGE, new ChangeEndEvent(0));

  public Integer getValue(){
	  return value;
  }
  
  
  /**
   * Gets the event type associated with change events.
   * 
   * @return the handler type
   */
  public static Type<ChangeEndHandler> getType() {
    return TYPE;
  }

  /**
   * Protected constructor, use
   * {@link DomEvent#fireNativeEvent(com.google.gwt.dom.client.NativeEvent, com.google.gwt.event.shared.HasHandlers)}
   * to fire change events.
   */
  protected ChangeEndEvent(Integer value) {
	  this.value = value;
  }

  @Override
  public final Type<ChangeEndHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(ChangeEndHandler handler) {
    handler.onChangeEnd(this);
  }

}
