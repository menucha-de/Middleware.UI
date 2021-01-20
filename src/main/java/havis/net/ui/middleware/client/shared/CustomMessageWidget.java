package havis.net.ui.middleware.client.shared;

import havis.net.ui.middleware.client.shared.event.MessageEvent;
import havis.net.ui.middleware.client.shared.event.MessageEvent.MessageType;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;
import havis.net.ui.middleware.client.utils.Utils;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.EventBus;

public class CustomMessageWidget extends PopupPanel {

	private Image image;

	private FocusPanel position;

	private FlowPanel container;

	private Label label;

	private SimplePanel errorDot;

	public CustomMessageWidget() {
		super(true);
		position = new FocusPanel();
		position.setStylePrimaryName(ResourceBundle.INSTANCE.css().webuiMessagePopupPanel());

		container = new FlowPanel();

		errorDot = new SimplePanel();
		errorDot.setStyleName(ResourceBundle.INSTANCE.css().webuiMessagePopupPanelErrorDot());

		image = new Image(ResourceBundle.INSTANCE.errorIcon());

		label = new Label();

		container.add(errorDot);
		container.add(image);
		container.add(label);

		position.add(container);

		setWidget(position);

		Document.get().getBody().appendChild(this.getElement());
	}

	public void register(EventBus eventBus, Object source) {
		MessageEvent.register(eventBus, source, new MessageEvent.Handler() {

			@Override
			public void onMessage(MessageEvent event) {
				showMessage(event.getMessage(), event.getMessageType());
			}
		});
	}

	public void showMessage(String message) {
		showMessage(message, null);
	}

	public void showMessage(String message, MessageType type) {
		if(isVisible()){
			hide();
		}
		label.setText(message);
		label.setTitle(message);

		final int scrollTop = Utils.getContentScrollTop();
		final int innerHeight = Utils.getWindowParentInnerHeight();
		final int offsetTop = Utils.getContentOffsetTop();

		if (type == null) {
			type = MessageType.INFO;
		}

		position.getElement().getStyle().setTop(scrollTop + innerHeight + offsetTop, Unit.PX);

		show();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				position.getElement().getStyle().setTop(scrollTop + innerHeight - 150 - offsetTop, Unit.PX);
			}
		});		
	}
}