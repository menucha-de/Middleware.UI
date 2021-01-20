package havis.net.ui.middleware.client.main;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import havis.net.ui.middleware.client.shared.SectionType;
import havis.net.ui.shared.resourcebundle.ResourceBundle;

public class MainSection extends FlowPanel implements HasClickHandlers {

	private Label sectionToggle;
	private SectionType sectionType;
	private ResourceBundle res = ResourceBundle.INSTANCE;
	
	@UiConstructor
	public MainSection(String name, SectionType sectionType) {
		super();
		
		sectionToggle = new Label(name);
		sectionToggle.setText(name);
		sectionToggle.setStyleName(res.css().sectionToggle());
		
		this.sectionType = sectionType;
		add(sectionToggle);
	}
	
	public SectionType getSectionType() {
		return sectionType;
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return sectionToggle.addClickHandler(handler);
	}
	
	public void setOpen(boolean value) {
		setStyleName(res.css().open(), value);
		sectionToggle.setStyleName(res.css().open(), value);
	}
	
	public void setHidden(boolean value) {
		setStyleName(res.css().hidden(), value);
		setStyleName(res.css().open(), !value);
	}
	
	public int getHeaderHeight() {
		return sectionToggle.getOffsetHeight();
	}
}
