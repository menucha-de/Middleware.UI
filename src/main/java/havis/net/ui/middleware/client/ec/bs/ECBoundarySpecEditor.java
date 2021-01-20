package havis.net.ui.middleware.client.ec.bs;

import havis.middleware.ale.service.ec.ECBoundarySpec;
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

public class ECBoundarySpecEditor extends ConfigurationSection implements ValueAwareEditor<ECBoundarySpec>, HasTriggerHandlers {
	
	private EditorDelegate<ECBoundarySpec> delegater;
	
	@Path("repeatPeriod")
	@UiField
	ECTimeEditor repeatPeriod;
	
	@Path("duration")
	@UiField
	ECTimeEditor duration;
	
	@Path("stableSetInterval")
	@UiField
	ECTimeEditor stableSetInterval;
	
	@Path("extension.whenDataAvailable")
	@UiField
	ToggleButton whenDataAvailable;
	
	@Path("extension.startTriggerList.startTrigger")
	@UiField
	TriggerListWidget boundaryStartTriggerList;
	
	@Path("extension.stopTriggerList.stopTrigger")
	@UiField
	TriggerListWidget boundaryStopTriggerList;
	
	@Ignore
	@UiField
	ConfigurationSections boundarySpecConfigSections;
	
	private ConstantsResource res = ConstantsResource.INSTANCE;

	private static ECBoundarySpecEditorUiBinder uiBinder = GWT
			.create(ECBoundarySpecEditorUiBinder.class);

	interface ECBoundarySpecEditorUiBinder extends UiBinder<Widget, ECBoundarySpecEditor> {
	}
	
	public ECBoundarySpecEditor() {
		initWidget(uiBinder.createAndBindUi(this));

		setText(res.boundaries());
		this.boundaryStartTriggerList.setText(res.startTrigger());
		this.boundaryStopTriggerList.setText(res.stopTrigger());
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
	public void setDelegate(EditorDelegate<ECBoundarySpec> delegate) {
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
			
			if (!stableSetInterval.isEmpty()){
				
				if(stableSetInterval.getParsingError()) {
					delegater.recordError(res.errorInValidTimeValue(res.stableSet()), null,repeatPeriod);
					//Utils.addErrorStyle(duration);
					return;	
				}
				
				
				if (stableSetInterval.getValue() < 0) {
					delegater.recordError(res.errorNegTimeValue(res.stableSet()), null,stableSetInterval);
					//Utils.addErrorStyle(duration);
					return;
				}
			}
		}
		
	}


	@Override
	public void onPropertyChange(String... paths) { }


	@Override
	public void setValue(ECBoundarySpec value) { }
}
