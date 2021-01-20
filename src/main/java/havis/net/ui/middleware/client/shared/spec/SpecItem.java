package havis.net.ui.middleware.client.shared.spec;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.adapters.SimpleEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

import havis.net.ui.middleware.client.shared.event.EnableSpecEvent;
import havis.net.ui.middleware.client.shared.event.SpecEvent;
import havis.net.ui.middleware.client.shared.event.SpecEvent.Action;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;
import havis.net.ui.middleware.client.utils.ConfirmationPanel;
import havis.net.ui.shared.client.table.CreateRowEvent;
import havis.net.ui.shared.client.table.CustomTable;
import havis.net.ui.shared.client.table.DeleteRowEvent;

public class SpecItem extends Composite implements SpecItemView, EnableSpecEvent.HasHandlers, SpecEvent.HasHandlers {

	SimpleEditor<String> id = SimpleEditor.of();

	private ResourceBundle res = ResourceBundle.INSTANCE;
	
	@UiField
	Label name;
	
	@UiField
	ToggleButton enable;

	@Ignore
	@UiField
	Label export;

	@Ignore
	@UiField
	Label delete;

	@Ignore
	@UiField
	ToggleButton subscriber;

	@Ignore
	@UiField
	Label run;

	@Ignore
	@UiField
	ToggleButton extend;

	@Ignore
	@UiField
	FlowPanel innerButton;

	@UiField
	CustomTable table;
	
	private boolean hasSubscribers;
	private Presenter presenter;
	
	private static SpecItemUiBinder uiBinder = GWT.create(SpecItemUiBinder.class);
	interface SpecItemUiBinder extends UiBinder<Widget, SpecItem> {
	}

	public SpecItem() {
		initWidget(uiBinder.createAndBindUi(this));
		table.setColumnWidth(1, 8, Unit.EM);		
		export.getElement().setAttribute("download", "spec.xml");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		subscriber.setVisible(hasSubscribers);
	}

	@UiHandler("extend")
	void onChangeExtend(ValueChangeEvent<Boolean> event) {
		if (hasSubscribers)
			run.setStyleName(res.css().closed(), !event.getValue());
		export.setStyleName(res.css().closed(), !event.getValue());
		delete.setStyleName(res.css().closed(), !event.getValue());
		extend.setStyleName(res.css().closed(), !event.getValue());
	}
	
	@UiHandler("focus")
	void onMouseOver(MouseOverEvent event) {
		extend.setValue(true, true);
	}
	
	@UiHandler("focus")
	void onMouseOut(MouseOutEvent event) {
		extend.setValue(false, true);
	}

	@UiHandler("name")
	void onShowSpecClick(ClickEvent event) {
		extend.setValue(false, true);
		fireEvent(new SpecEvent(id.getValue(), Action.EDIT));
	}

	@UiHandler("delete")
	void onDeleteSpecClick(ClickEvent event) {
		new ConfirmationPanel(name.getText(), new ConfirmationPanel.ResultCallback() {
			@Override
			public void onConfirm(boolean result) {
				if (result) {
					fireEvent(new SpecEvent(id.getValue(), Action.DELETE));
				}
			}
		});
	}

	@UiHandler("enable")
	void onEnableSpecChange(ValueChangeEvent<Boolean> event) {
		fireEvent(new EnableSpecEvent(id.getValue(), event.getValue()));
	}
	
	@UiHandler("export")
	void onExport(ClickEvent event) {
		fireEvent(new SpecEvent(id.getValue(), Action.EXPORT));
	}

	@UiHandler("run")
	void onRunSpecEvent(ClickEvent event) {
		extend.setVisible(false);
		extend.setValue(false, false);
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				extend.setVisible(true);
				fireEvent(new SpecEvent(id.getValue(), Action.RUN));
			}
		});
		
	}
	
	@UiHandler("subscriber")
	public void onSubscriberClick(ValueChangeEvent<Boolean> event) {
		table.setVisible(event.getValue());
		if (event.getValue()) {
			table.setHeader(Arrays.asList("New Subscriber"));
			presenter.onShowSubscribers();
		}
	}

	@Override
	public HandlerRegistration addEnableSpecHandler(EnableSpecEvent.Handler handler) {
		return addHandler(handler, EnableSpecEvent.getType());
	}

	@Override
	public HandlerRegistration addSpecEventHandler(SpecEvent.Handler handler) {
		return addHandler(handler, SpecEvent.getType());
	}

	public boolean getHasSubscribers() {
		return hasSubscribers;
	}

	public void setHasSubscribers(boolean hasSubscribers) {
		this.hasSubscribers = hasSubscribers;
	}
	
	@UiHandler("table")
	void onDeleteRow(final DeleteRowEvent event) {
		SubscriberListItemEditor editor = (SubscriberListItemEditor) event.getRow();
		
		new ConfirmationPanel(editor.getUri(), new ConfirmationPanel.ResultCallback() {
			@Override
			public void onConfirm(boolean result) {
				if (result) {
					presenter.onDeleteSubscriber(event.getIndex());
				}
			}
		});
	}
	
	@UiHandler("table")
	void onCreateRow(CreateRowEvent event) {
		presenter.onAddSubscriber();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		if (presenter == null) {
			this.presenter.onDispose();
		}
		this.presenter = presenter;
	}
	
	@Override
	public Presenter getPresenter() {
		return presenter;
	}

	@Override
	public CustomTable getTable() {
		return table;
	}

	@Override
	public SimpleEditor<String> id() {
		return id;
	}

	@Override
	public LeafValueEditor<String> name() {
		return name.asEditor();
	}

	@Override
	public LeafValueEditor<Boolean> enable() {
		return enable.asEditor();
	}

	@Override
	public void setLoading(boolean isLoading) {
		enable.setEnabled(!isLoading);
		if(isLoading) {
			enable.addStyleName(ResourceBundle.INSTANCE.css().specStateChanging());
		} else {
			enable.removeStyleName(ResourceBundle.INSTANCE.css().specStateChanging());
		}
	}
}
