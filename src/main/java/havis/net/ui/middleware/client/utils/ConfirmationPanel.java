package havis.net.ui.middleware.client.utils;

import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;


public class ConfirmationPanel extends PopupPanel {

	@UiField HTMLPanel panel;
	@UiField Button okButton;
	@UiField Button cancelButton;
	@UiField Label messageLabel;

	private ResultCallback callback; 

	public interface ResultCallback {
		void onConfirm(boolean result);
	}

	private static ConfirmationPanelUiBinder uiBinder = GWT.create(ConfirmationPanelUiBinder.class);

	interface ConfirmationPanelUiBinder extends UiBinder<Widget, ConfirmationPanel> {
	}

	public ConfirmationPanel(String message, ResultCallback callback) {
		setStyleName(ResourceBundle.INSTANCE.css().confirmDialog());
		getElement().getStyle().setPropertyPx("right", 0);
		getElement().getStyle().setPropertyPx("bottom", 0);
		
		setWidget(uiBinder.createAndBindUi(this));
		setModal(true);
		this.callback = callback;
		messageLabel.setText(ConstantsResource.INSTANCE.deleteReally(message));
		setTopPositon();
		show();
	}
	
	
	/**
	 * Placing the view always in the visible area of the browser window.
	 */
	private void setTopPositon(){
		int scrollTop = Utils.getContentScrollTop();
		int top = 0;
		if(scrollTop > Utils.getContentOffsetTop()){
			top = scrollTop - Utils.getContentOffsetTop() + 50; 
		}
		panel.getElement().getParentElement().getStyle().setTop(top, Unit.PX);
	}
	
	@UiHandler({"okButton", "cancelButton"})
	void onConfirm(ClickEvent event) {
		callback.onConfirm(event.getSource() == okButton);
		hide();
	}
}
