package havis.net.ui.middleware.client.cc.bs;

import havis.middleware.ale.service.cc.CCBoundarySpec;
import havis.net.ui.middleware.client.shared.ECTimeEditor;
import havis.net.ui.middleware.client.shared.HasTriggerHandlers;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.trigger.TriggerListWidget;
import havis.net.ui.middleware.client.utils.NoSepIntegerBox;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.ConfigurationSection;
import havis.net.ui.shared.client.ConfigurationSections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class CCBoundarySpecEditor extends ConfigurationSection implements ValueAwareEditor<CCBoundarySpec>, HasTriggerHandlers {

	private EditorDelegate<CCBoundarySpec> delegate;

	@Path("repeatPeriod")
	@UiField
	ECTimeEditor repeatPeriod;

	@Path("duration")
	@UiField
	ECTimeEditor duration;

	@Path("noNewTagsInterval")
	@UiField
	ECTimeEditor noNewTagsInterval;

	@Path("afterError")
	@UiField
	ToggleButton afterError;

	@Ignore
	@UiField
	NoSepIntegerBox tagsProcessedCount;

	@Path("startTriggerList.startTrigger")
	@UiField
	TriggerListWidget startTriggerList;

	@Path("stopTriggerList.stopTrigger")
	@UiField
	TriggerListWidget stopTriggerList;

	@Ignore
	@UiField
	ConfigurationSections boundarySpecConfigSections;

	KeyUpHandler ecTimeKeyUp = new KeyUpHandler() {

		@Override
		public void onKeyUp(KeyUpEvent event) {
			TextBox txt = (TextBox) event.getSource();
			Utils.removeErrorStyle(txt);
		}
	};

	private ConstantsResource res = ConstantsResource.INSTANCE;

	private CCBoundarySpec ccBoundarySpec;

	private static ECBoundarySpecEditorUiBinder uiBinder = GWT.create(ECBoundarySpecEditorUiBinder.class);

	interface ECBoundarySpecEditorUiBinder extends UiBinder<Widget, CCBoundarySpecEditor> {
	}

	public CCBoundarySpecEditor() {
		initWidget(uiBinder.createAndBindUi(this));

		setText(res.boundaries());
		this.startTriggerList.setText(res.startTrigger());
		this.stopTriggerList.setText(res.stopTrigger());

		repeatPeriod.addKeyUpHandler(ecTimeKeyUp);
		duration.addKeyUpHandler(ecTimeKeyUp);
		noNewTagsInterval.addKeyUpHandler(ecTimeKeyUp);
	}

	public void addNewStartTriggerHandler(NewItemEvent.Handler handler) {
		startTriggerList.addNewItemHandler(handler);
	}

	public void addNewStopTriggerHandler(NewItemEvent.Handler handler) {
		stopTriggerList.addNewItemHandler(handler);
	}

	public void setOpenStartTriggerHandler(OpenItemEvent.Handler handler) {
		startTriggerList.setOpenItemHandler(handler);
	}

	public void setOpenStopTriggerHandler(OpenItemEvent.Handler handler) {
		stopTriggerList.setOpenItemHandler(handler);

	}

	@Override
	public void setDelegate(EditorDelegate<CCBoundarySpec> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void flush() {

		if (delegate != null) {
			if (!repeatPeriod.isEmpty()) {
				if (repeatPeriod.getParsingError()) {
					delegate.recordError(res.errorInValidTimeValue(res.repeatPeriod()), null, repeatPeriod);
					Utils.addErrorStyle(repeatPeriod);
					return;
				}
				if (repeatPeriod.getValue() < 0) {
					delegate.recordError(res.errorNegTimeValue(res.repeatPeriod()), null, repeatPeriod);
					Utils.addErrorStyle(repeatPeriod);
					return;
				}
			} else {
				ccBoundarySpec.setRepeatPeriod(null);
			}

			if (!duration.isEmpty()) {
				if (duration.getParsingError()) {
					delegate.recordError(res.errorInValidTimeValue(res.duration()), null, duration);
					Utils.addErrorStyle(duration);
					return;
				}
				if (duration.getValue() < 0) {
					delegate.recordError(res.errorNegTimeValue(res.duration()), null, duration);
					Utils.addErrorStyle(duration);
					return;
				}
			} else {
				ccBoundarySpec.setDuration(null);
			}

			if (!noNewTagsInterval.isEmpty()) {

				if (noNewTagsInterval.getParsingError()) {
					delegate.recordError(res.errorInValidTimeValue(res.stableSet()), null, noNewTagsInterval);
					Utils.addErrorStyle(noNewTagsInterval);
					return;
				}
				if (noNewTagsInterval.getValue() < 0) {
					delegate.recordError(res.errorNegTimeValue(res.stableSet()), null, noNewTagsInterval);
					Utils.addErrorStyle(noNewTagsInterval);
					return;
				}
			} else {
				ccBoundarySpec.setNoNewTagsInterval(null);
			}

			if (!tagsProcessedCount.isEmpty()) {
				if (tagsProcessedCount.getValue() < 0) {
					delegate.recordError(res.errorNegTimeValue(res.tagsProcessedCnt()), null, tagsProcessedCount);
					Utils.addErrorStyle(tagsProcessedCount);
					return;
				}
				ccBoundarySpec.setTagsProcessedCount(tagsProcessedCount.getValue());
			} else {
				ccBoundarySpec.setTagsProcessedCount(null);
			}
		}

	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(CCBoundarySpec value) {
		if (value != null) {
			ccBoundarySpec = value;
			if (value.getTagsProcessedCount() != null) {
				tagsProcessedCount.setValue(value.getTagsProcessedCount());
			}
		}
	}
}
