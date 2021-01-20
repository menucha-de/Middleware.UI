package havis.net.ui.middleware.client.shared;

import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.widgets.Util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class EditorDialog extends Composite implements EditorDialogView {

	@Inject
	EventBus eventBus;

	@UiField
	FlowPanel headerPanel;
	@UiField
	Label titleLabel;
	@UiField
	SimplePanel editorPanel;
	@UiField
	Button acceptButton;
	@UiField
	Button exportButton;
	@UiField
	Button closeButton;
	@UiField
	CustomMessageWidget messageField;
	@UiField
	Anchor exportLink;
	@UiField
	FlowPanel dialog;

	private Presenter presenter;
	private static EditorDialogUiBinder uiBinder = GWT.create(EditorDialogUiBinder.class);

	interface EditorDialogUiBinder extends UiBinder<Widget, EditorDialog> {
	}

	

	public EditorDialog() {
		initWidget(uiBinder.createAndBindUi(this));

		addScrollHandler(this);		
	}
	
	/**
	 * Add scrollHandler to move close button. Not working on local machine for debug purposes.
	 * 
	 * @param editorDialog
	 */
	private final static native void addScrollHandler(EditorDialog editorDialog)/*-{
		window.parent.parent.document
				.addEventListener(
						"scroll",
						function(e) {
							if (editorDialog != null) {
								editorDialog.@havis.net.ui.middleware.client.shared.EditorDialog::setCloseButtonPosition()();
							}
						});
	}-*/;

	public void setEnableButton(ToggleButton enable) {
		headerPanel.clear();
		headerPanel.add(titleLabel);
		if (enable != null)
			headerPanel.add(enable);
	}

	public void setEditor(Widget editor) {
		if (editorPanel.getWidget() != null) {
			editorPanel.clear();
		}
		editorPanel.add(editor);
		setTopPositon();
	}

	/**
	 * Moving the close button.
	 */
	public void setCloseButtonPosition() {
		int cssDefaultValue = 44;
		double pos = 0;
		try {
			pos = Double.valueOf(dialog.getElement().getStyle().getTop().replaceAll("[^\\d.]", ""));
		} catch(Exception e){
			
		}
		double top = Math.max(Util.getContentOffsetTop() - 50, 0);
		double value = Math.max(Util.getContentScrollTop() - top - pos, top);
		value = Math.min(value - top + 21, dialog.getElement().getOffsetHeight() - cssDefaultValue - closeButton.getElement().getOffsetHeight());
		value = Math.max(value, cssDefaultValue);
		closeButton.getElement().getStyle().setTop(value, Unit.PX);
	}

	/**
	 * Placing the view always in the visible area of the browser window.
	 */
	private void setTopPositon() {
		int scrollTop = Utils.getContentScrollTop();
		int top = 0;
		if (scrollTop + 50 > Utils.getContentOffsetTop()) {
			top = scrollTop - Utils.getContentOffsetTop() + 50;
		}
		dialog.getElement().getStyle().setTop(top, Unit.PX);
		setCloseButtonPosition();
	}

	@Override
	public void setEditorTitle(String title) {
		titleLabel.setText(title);
	}

	@UiHandler("acceptButton")
	void onAcceptButtonButtonClick(ClickEvent event) {
		presenter.onAccept();
	}

	@UiHandler("closeButton")
	void onCloseLabelClick(ClickEvent event) {
		presenter.onClose();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		messageField.register(eventBus, this.presenter);
	}

	@Override
	public void setEnabledAcceptButton(boolean enabled) {
		acceptButton.setEnabled(enabled);
		acceptButton.setVisible(true);
		exportButton.setVisible(false);
	}

	@Override
	public void setExportButton(String url, String filename) {
		setEnableButton(null);
		acceptButton.setVisible(false);
		exportButton.setVisible(true);
		exportLink.setHref(url);
		exportLink.getElement().setAttribute("download", filename);
//		NodeList<Element> elements = this.getElement().getElementsByTagName("a");
//		for(int i = 0; i < elements.getLength(); i++){
//			if(elements.getItem(i).getId().equals("exportlink")){
//				elements.getItem(i).setPropertyString("href", url);
//				break;
//			}
//		}
	}

	@Override
	public void setEnabledExportButton(boolean enabled) {
		acceptButton.setVisible(false);
		exportButton.setVisible(true);
		exportButton.setEnabled(enabled);
	}

	@UiHandler("exportButton")
	void onExportButtonButtonClick(ClickEvent event) {
		Utils.clickElement(exportLink.getElement());
//		NodeList<Element> elements = this.getElement().getElementsByTagName("a");
//		for(int i = 0; i < elements.getLength(); i++){
//			if(elements.getItem(i).getId().equals("exportlink")){
//				Utils.clickElement(elements.getItem(i));
//				break;
//			}
//		}
	}

}
