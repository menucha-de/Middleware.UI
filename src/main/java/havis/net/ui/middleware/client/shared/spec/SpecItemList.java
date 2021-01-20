package havis.net.ui.middleware.client.shared.spec;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import havis.net.ui.middleware.client.shared.CustomMessageWidget;
import havis.net.ui.middleware.client.shared.IsMainSection;
import havis.net.ui.middleware.client.shared.SectionType;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;
import havis.net.ui.shared.client.upload.MultipleFileUpload;

public class SpecItemList extends Composite implements SpecItemListView, IsMainSection {

	@Inject
	private EventBus eventBus;
	
	@UiField
	FlowPanel specItemList;

	@UiField
	FormPanel formPanel;
	
	@UiField
	MultipleFileUpload importField;

	@UiField
	Label importButton;
	
	@UiField
	Anchor exportLink;
	
	@UiField
	CustomMessageWidget messageField;
	
	@UiField ToggleButton extend;
	
	private ResourceBundle res = ResourceBundle.INSTANCE;

	private Presenter presenter;
	
	private static SpecItemListUiBinder uiBinder = GWT.create(SpecItemListUiBinder.class);

	interface SpecItemListUiBinder extends UiBinder<Widget, SpecItemList> {
	}

	public SpecItemList() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		messageField.register(eventBus, this.presenter);
	}

	@Override
	public InsertPanel getList() {
		return specItemList;
	}

	public void reset() {
		specItemList.clear();
	}

	@UiHandler("importField")
	void onChooseFile(ChangeEvent event) {
		presenter.onImport();
	}
	
	@UiHandler("refreshButton")
	void onRefresh(ClickEvent e) {
		presenter.onRefresh();		
	}

	@UiHandler("importButton")
	void onImport(ClickEvent e) {
		formPanel.reset();
		importField.click();		
	}
	
	@UiHandler("addButton")
	void onAdd(ClickEvent e) {
		extend.setValue(false, true);
		presenter.onAdd();
	}

	@UiHandler("focus")
	void onMouseOver(MouseOverEvent event) {
		extend.setValue(true, true);
	}
	
	@UiHandler("focus")
	void onMouseOut(MouseOutEvent event) {
		extend.setValue(false, true);
	}

	@UiHandler("extend")
	void onExtend(ValueChangeEvent<Boolean> event) {
		importButton.setStyleName(res.css().closed(), !event.getValue());
	}

	@Override
	public MultipleFileUpload getImportField() {
		return importField;
	}

	@Override
	public SectionType getSectionType() {
		return SectionType.fromListType(presenter.getListType());
	}

	@Override
	public Anchor getExportLink() {
		return exportLink;
	}
}
