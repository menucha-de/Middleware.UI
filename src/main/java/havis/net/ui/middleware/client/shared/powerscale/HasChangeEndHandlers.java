package havis.net.ui.middleware.client.shared.powerscale;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;


/**
 * A widget that implements this interface provides registration for
 * {@link ChangeHandler} instances.
 */
public interface HasChangeEndHandlers extends HasHandlers {
  /**
   * Adds a {@link ChangeEvent} handler.
   * 
   * @param handler the change handler
   * @return {@link HandlerRegistration} used to remove this handler
   */
  HandlerRegistration addChangeEndHandler(ChangeEndHandler handler);
}
