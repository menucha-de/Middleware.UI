package havis.net.ui.middleware.client.pc.rep.operation;

import havis.middleware.ale.service.pc.PCOpSpec;
import havis.net.ui.middleware.client.shared.ECTimeEditor;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.trigger.model.PortTrigger;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.widgets.CustomListBox;
import havis.net.ui.shared.client.widgets.CustomRenderer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class PCOpSpecEditor extends Composite implements ValueAwareEditor<PCOpSpec> {
	
	private EditorDelegate<PCOpSpec> delegate;
	
	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	private RegExp checkNumbers = RegExp.compile("^[0-9-]+$");
	
	private PCOpSpec pcOpSpec;
		
	private String readString = "READ";
		
	@UiField TextBox opName;
	
	@UiField
	FlowPanel stateArea;
	
	@UiField
	FlowPanel durationArea;
	
	
	@UiField
	CustomListBox<String> opType;
	
	@Path("portSpec.reader")
	@UiField (provided = true)
	CustomListBox<String> reader = new CustomListBox<String>(true);
	
	@Path("portSpec.type")
	@UiField
	CustomListBox<String> pinType;

	
	@Path("portSpec.id")
	@UiField(provided = true)
	CustomListBox<Integer> id = new CustomListBox<Integer>(new CustomRenderer<Integer>() {

		@Override
		public String render(Integer value) {
			if (value == null) {
				return null;
			} else {
				switch (value) {
				case PortTrigger.PIN_ANY:
					return "Any";
				case PortTrigger.PIN_CUSTOM:
					return "Custom";
				default:
					return value.toString();
				}
			}

		}
	}) {

		@Override
		public Integer getValue() {
			//calls on loading and unloading, not on change
			Integer value = super.getValue();
			
			if (value == null) {
				return PortTrigger.PIN_ANY;
			} else {
				switch (value) {
					case PortTrigger.PIN_CUSTOM:
						if (customId.getValue() != null) {
							return customId.getValue();
						} else {
							return PortTrigger.PIN_ANY;
						}
					default:
						return value;
				}
			}
			
		}
	};

	@Ignore
	@UiField IntegerBox customId;

	@UiField ToggleButton state;
	
	@Path("duration")
	@UiField
	ECTimeEditor duration;
	
	private static PCOpSpecEditorUiBinder uiBinder = GWT.create(PCOpSpecEditorUiBinder.class);
	interface PCOpSpecEditorUiBinder extends UiBinder<Widget, PCOpSpecEditor> { }
	
	public PCOpSpecEditor(){				
		//id.setItems(new Integer[] { PortTrigger.PIN_ANY, 1, 2, 3, 4, PortTrigger.PIN_CUSTOM });
		initWidget(uiBinder.createAndBindUi(this));
		
		opType.addItem(readString);
		opType.addItem("WRITE");
		
		pinType.addItem("INPUT");
		pinType.addItem("OUTPUT");
		
		reader.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				Utils.removeErrorStyle(reader);
			}
		});
		
		pinType.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				Utils.removeErrorStyle(pinType);
			}
		});
		
		opType.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				setStateDurationVisibility();
				Utils.removeErrorStyle(opType);
				
				ListBox lb = (ListBox)event.getSource();
				initializeIdListBox(lb.getSelectedValue());
			}
		});
		
		id.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				setStateDurationVisibility();
				Integer selval = id.getSelectedValue();
				boolean canVisibleId = (selval != null && selval == PortTrigger.PIN_CUSTOM);
				customId.setVisible(canVisibleId);
			}
		});
		
		id.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Integer selval = id.getSelectedValue();
				boolean canClearId = (selval == null || selval == PortTrigger.PIN_CUSTOM);
				if(canClearId)
					customId.setValue(null);
			}
		});
		
		opName.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Utils.removeErrorStyle(opName);
			}
		});
		duration.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Utils.removeErrorStyle(duration);
			}
		});
	}
	
	@Override
	public void setDelegate(EditorDelegate<PCOpSpec> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void flush() {
		
		if (delegate != null) {
			if(Utils.isNullOrEmpty(opName.getText())){
				delegate.recordError(res.errorInvalidEmptyField(res.name()), null ,opName);
				Utils.addErrorStyle(opName);
				return;
			}
			if (opType.getSelectedValue() == null) {
				delegate.recordError(res.errorNotSpecified(res.type()), opType.getSelectedValue(), opType);
				Utils.addErrorStyle(opType);
				return;
			}
			Utils.removeErrorStyle(opType);
			
			if (reader.getSelectedValue() == null) {
				delegate.recordError(res.errorNotSpecified(res.reader()), reader.getSelectedValue(), reader);
				Utils.addErrorStyle(reader);
				return;
			}
			Utils.removeErrorStyle(reader);
			
			if (pinType.getSelectedValue() == null) {
				delegate.recordError(res.errorNotSpecified(res.pinType()), pinType.getSelectedValue(), pinType);
				Utils.addErrorStyle(pinType);
				return;
			}
			Utils.removeErrorStyle(pinType);
			
			if(id.getSelectedValue() == PortTrigger.PIN_CUSTOM){
								
				if (customId.getText() == null) {
					delegate.recordError(res.errorInvalidEmptyField("PIN ID"), customId.getValue(), customId);
					Utils.addErrorStyle(customId);
					return;
				}
				
				if (checkNumbers.exec(customId.getText()) == null) {
					delegate.recordError(res.errorInvalidChars("PIN ID"), customId.getValue(), customId);
					Utils.addErrorStyle(customId);
					return;
				}
				
				if (customId.getValue() == 0) {
					delegate.recordError( res.errorInvalid("PIN") + " " + res.errorOutOfRange(), customId.getValue(), customId);
					Utils.addErrorStyle(customId);
					return;
				}
				
				if (customId.getValue() < 0 || customId.getValue() > 254) {
					delegate.recordError( res.errorInvalid("PIN") + " " + res.errorNotUCHAR(), customId.getValue(), customId);
					Utils.addErrorStyle(customId);
					return;
				}
			}
			Utils.removeErrorStyle(customId);
			
			if(opType.getSelectedValue().startsWith("WRITE")){
				
				if (!duration.isEmpty()){
					if(duration.getParsingError()) {
						delegate.recordError(res.errorInValidTimeValue(res.duration()), null,duration);
						Utils.addErrorStyle(duration);
						return;	
					}
					if (duration.getValue() < 0) {
						delegate.recordError(res.errorNegTimeValue(res.duration()), null,duration);
						Utils.addErrorStyle(duration);
						return;
					}
				}
			}
			else{
				pcOpSpec.setDuration(null);
			}
			Utils.removeErrorStyle(duration);
			
		}
		
		if(pcOpSpec != null){
			if(pcOpSpec.getPortSpec() != null)
				pcOpSpec.getPortSpec().setId(id.getSelectedValue());
			
			//READ needs the state set to null (false does not pan out)! otherwise:
			//ValidationException: Failed to define port cycle 'Portcycle1'. Report 'report1' is invalid.
			//Invalid operation specification 'ReadPC'. Data must be omitted for read operation 'ReadPC'
			if(pcOpSpec.getOpType().startsWith(readString)){
				pcOpSpec.setState(null);
			}
		}
		
		pcOpSpec.getPortSpec().setId(getPcOpSpecIdFromWidgets());
	}

	@Override
	public void onPropertyChange(String... paths) { }
	

	@Override
	public void setValue(PCOpSpec value) {
		pcOpSpec = value;
		if(pcOpSpec != null && pcOpSpec.getPortSpec() != null){
			setPcOpSpecIdWidgets(pcOpSpec.getPortSpec().getId());
		}
	}

	/**
	 * Setter, used in the activity
	 * @param lst
	 */
	public void setReaderItems(List<String> lst){
		reader.setItems(lst);
	}
	
	/** Composes the Id from the involved widgets 'id' and 'customId'
	 *  Used in flush()
	 * @return the id as int
	 */
	private int getPcOpSpecIdFromWidgets(){
		Integer sel = id.getSelectedValue();
		if(sel != null){
			int isel = sel & 0xFF;
			
			if(isel > 0 && isel < 5 || sel > 254){
				return isel;
			}
			else if(sel == PortTrigger.PIN_CUSTOM){
				return customId.getValue();
			}
		}
		//0 should not happen.
		return PortTrigger.PIN_ANY;
	}
	
	/**
	 * Sets the involved widgets 'id' and 'customId' according to given value
	 * Used in setValue(...)
	 * @param id
	 */
	private void setPcOpSpecIdWidgets(int svalue){
		final int uvalue = svalue & 0xFF;
		
		//customId.setValue(uvalue);
		
		if(uvalue > 4 && uvalue < 255){
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					id.setSelectedValue(PortTrigger.PIN_CUSTOM);
					customId.setValue(uvalue);
					customId.setVisible(true);
					setStateDurationVisibility();
				}
			});
		}
		if(uvalue == 255){
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					id.setSelectedValue(PortTrigger.PIN_ANY);
					setStateDurationVisibility();
				}
			});
		}
	}
	
	
	/**
	 * Sets the visibility of stateArea and durationArea acc. to given opType and id settings
	 */
	private void setStateDurationVisibility(){
		boolean visible = (opType.getSelectedValue() != null && opType.getSelectedValue().startsWith("WRITE") &&
				id.getSelectedValue() != null && id.getSelectedValue() != PortTrigger.PIN_ANY);
		
		stateArea.setVisible(visible);
		durationArea.setVisible(visible);
	}
	
	/**
	 * initializes the CustomListBox acc. to set opType
	 * @param selValue
	 */
	private void initializeIdListBox(String selValue){
		Integer save = id.getSelectedValue();
		
		if(selValue.startsWith(readString)){
			id.setItems(new Integer[] { 1, 2, 3, 4, PortTrigger.PIN_CUSTOM });
			if(save != null && save != PortTrigger.PIN_ANY){
				id.setSelectedValue(save);
				return;
			}
			id.setSelectedValue(1);
			return;
		}
		
		id.setItems(new Integer[] { PortTrigger.PIN_ANY, 1, 2, 3, 4, PortTrigger.PIN_CUSTOM });
		if(save != null){
			id.setSelectedValue(save);
			return;
		}
		id.setSelectedValue(PortTrigger.PIN_ANY);
	}
}
