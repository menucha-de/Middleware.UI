package havis.net.ui.middleware.client.pc.bs;

import havis.middleware.ale.service.pc.PCBoundarySpec;
import havis.net.ui.middleware.client.shared.ECTimeEditor;
import havis.net.ui.middleware.client.shared.HasTriggerHandlers;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.trigger.TriggerListWidget;
import havis.net.ui.shared.client.ConfigurationSection;
import havis.net.ui.shared.client.ConfigurationSections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class PCBoundarySpecEditor extends ConfigurationSection implements ValueAwareEditor<PCBoundarySpec>, HasTriggerHandlers {
	
	
//	protected PCBoundarySpec.StartTriggerList startTriggerList;
//    protected ECTime repeatPeriod;
//    protected PCBoundarySpec.StopTriggerList stopTriggerList;
//    protected ECTime duration;
//    protected ECTime noNewEventsInterval;
//    protected Boolean whenDataAvailable;
//    protected PCBoundarySpecExtension extension;
	
	@Path("repeatPeriod")
	@UiField
	ECTimeEditor repeatPeriod;
	
	@Path("duration")
	@UiField
	ECTimeEditor duration;
	
	@Path("noNewEventsInterval")
	@UiField
	ECTimeEditor noNewEventsInterval;
	
	@Path("whenDataAvailable")
	@UiField
	ToggleButton whenDataAvailable;
	
	@Path("startTriggerList.startTrigger")
	@UiField
	TriggerListWidget boundaryStartTriggerList;
	
	@Path("stopTriggerList.stopTrigger")
	@UiField
	TriggerListWidget boundaryStopTriggerList;
	
	@Ignore
	@UiField
	ConfigurationSections boundarySpecConfigSections;
	
	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	private EditorDelegate<PCBoundarySpec> delegater;

	private static PCBoundarySpecEditorUiBinder uiBinder = GWT
			.create(PCBoundarySpecEditorUiBinder.class);

	interface PCBoundarySpecEditorUiBinder extends UiBinder<Widget, PCBoundarySpecEditor> {
	}
	
	public PCBoundarySpecEditor() {
		
		initWidget(uiBinder.createAndBindUi(this));

		setText(res.boundaries());
		boundaryStartTriggerList.setText(res.startTrigger());
		boundaryStopTriggerList.setText(res.stopTrigger());
	}

	
	public void addNewStartTriggerHandler(NewItemEvent.Handler handler) {
		boundaryStartTriggerList.addNewItemHandler(handler);
	}
	
	public void addNewStopTriggerHandler(NewItemEvent.Handler handler) {
		boundaryStopTriggerList.addNewItemHandler(handler);
	}
	
	public void setOpenStartTriggerHandler(OpenItemEvent.Handler handler) {
		boundaryStartTriggerList.setOpenItemHandler(handler);
	}
	
	public void setOpenStopTriggerHandler(OpenItemEvent.Handler handler) {
		boundaryStopTriggerList.setOpenItemHandler(handler);
		
	}


	@Override
	public void setDelegate(EditorDelegate<PCBoundarySpec> delegate) {
		delegater = delegate;
	}
		
	


	@Override
	public void flush() {
		
		if (delegater != null) {
			if (!repeatPeriod.isEmpty()){
				
				if(repeatPeriod.getParsingError()) {
					delegater.recordError(res.errorInValidTimeValue(res.repeatPeriod()), null,repeatPeriod);
					//Utils.addErrorStyle(duration);
					return;	
				}
				
				if(repeatPeriod.getValue() < 0) {
					delegater.recordError(res.errorNegTimeValue(res.repeatPeriod()), null,repeatPeriod);
					//Utils.addErrorStyle(duration);
					return;	
				}
				
			}
			
			if (!duration.isEmpty()){
				
				if(duration.getParsingError()) {
					delegater.recordError(res.errorInValidTimeValue(res.duration()), null,repeatPeriod);
					//Utils.addErrorStyle(duration);
					return;	
				}
				
				if (duration.getValue() < 0) {
					delegater.recordError(res.errorNegTimeValue(res.duration()), null,duration);
					//Utils.addErrorStyle(duration);
					return;
				}
			}
			
			if (!noNewEventsInterval.isEmpty()){
				
				if(noNewEventsInterval.getParsingError()) {
					delegater.recordError(res.errorInValidTimeValue(res.noNewGpioEvents()), null,repeatPeriod);
					//Utils.addErrorStyle(duration);
					return;	
				}
				
				
				if (noNewEventsInterval.getValue() < 0) {
					delegater.recordError(res.errorNegTimeValue(res.noNewGpioEvents()), null,noNewEventsInterval);
					//Utils.addErrorStyle(duration);
					return;
				}
			}
		}
	}


	@Override
	public void onPropertyChange(String... paths) { }
		
	


	@Override
	public void setValue(PCBoundarySpec value) {
		
	}
	
}
