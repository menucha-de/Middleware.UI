package havis.net.ui.middleware.client.shared.powerscale;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for {@link ChangeEndEvent} events.
 */
public interface ChangeEndHandler extends EventHandler {

  /**
   * Called when a change event is fired.
   * 
   * @param event the {@link ChangeEndEvent} that was fired
   */
  void onChangeEnd(ChangeEndEvent event);
}