package havis.net.ui.middleware.client.ec;

import havis.middleware.ale.service.ec.ECReportSpec;
import havis.middleware.ale.service.mc.MCEventCycleSpec;
import havis.net.ui.middleware.client.ec.bs.ECBoundarySpecEditor;
import havis.net.ui.middleware.client.shared.BaseActivity;
import havis.net.ui.middleware.client.shared.CommonListEditor;
import havis.net.ui.middleware.client.shared.HasTriggerHandlers;
import havis.net.ui.middleware.client.shared.MCCycleSpecEditor;
import havis.net.ui.middleware.client.shared.pattern.Pattern;
import havis.net.ui.middleware.client.shared.report.list.SingleColumnListWidget;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.ConfigurationSections;
import havis.net.ui.shared.client.widgets.CustomRenderer;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class EventCycleEditor extends Composite implements MCCycleSpecEditor<MCEventCycleSpec> {

	private EditorDelegate<MCEventCycleSpec> delegate;

	private ConstantsResource res = ConstantsResource.INSTANCE;

	private MCEventCycleSpec mcEventCycleSpec;

	@UiField
	TextBox name;

	@UiField
	ToggleButton enable;

	@Path("spec.includeSpecInReports")
	@UiField
	ToggleButton includeSpecInReports;

	@Path("spec.logicalReaders.logicalReader")
	@UiField
	CommonListEditor logicalReadersList;

	@Path("spec.extension.primaryKeyFields.primaryKeyField")
	@UiField
	CommonListEditor primaryKeysList;

	@Path("spec.boundarySpec")
	@UiField
	ECBoundarySpecEditor boundarySpec;

	@Path("spec.reportSpecs.reportSpec")
	@UiField(provided = true)
	SingleColumnListWidget<ECReportSpec> reportSpecsListWidget = new SingleColumnListWidget<ECReportSpec>(
			new CustomRenderer<ECReportSpec>() {

				@Override
				public String render(ECReportSpec value) {
					return value.getReportName();
				}
			});

	@Ignore
	@UiField
	ConfigurationSections eventCycleConfigSections;

	private static EventCycleEditorUiBinder uiBinder = GWT.create(EventCycleEditorUiBinder.class);

	interface EventCycleEditorUiBinder extends UiBinder<Widget, EventCycleEditor> {
	}

	public EventCycleEditor() {

		initWidget(uiBinder.createAndBindUi(this));

		logicalReadersList.setText(res.readers());
		logicalReadersList.setHeader(Arrays.asList(res.chooseItem("Reader")));

		primaryKeysList.setText(res.primaryKeys());
		primaryKeysList.setHeader(Arrays.asList(res.chooseItem("Field")));

		reportSpecsListWidget.setText(res.reports());
		reportSpecsListWidget.setHeader(Arrays.asList(res.createItem("Report")));

		name.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Utils.removeErrorStyle(name);
			}
		});
	}

	@Override
	public void setDelegate(EditorDelegate<MCEventCycleSpec> delegate) {
		this.delegate = delegate;

	}

	public ConfigurationSections getConfigSections() {
		return eventCycleConfigSections;
	}

	@Override
	public void flush() {
		if (delegate != null) {
			if (Utils.isNullOrEmpty(name.getValue())) {
				delegate.recordError(res.errorInvalidEmptyField(res.name()), name.getValue(), name.getValue());
				Utils.addErrorStyle(name);
				return;
			}
			if (Pattern.match(Pattern.PatternWhiteSpace, name.getValue())) {
				delegate.recordError(res.errorInvalidFieldNameWhiteSpaces(), name.getValue(), name.getValue());
				Utils.addErrorStyle(name);
				return;
			}
			if (Pattern.match(Pattern.PatternSyntax, name.getValue())) {
				delegate.recordError(res.errorInvalidFieldNameSpecialChar(), name.getValue(), name.getValue());
				Utils.addErrorStyle(name);
				return;
			}

			if (mcEventCycleSpec.getSpec().getLogicalReaders() == null
					|| mcEventCycleSpec.getSpec().getLogicalReaders() == null
					|| mcEventCycleSpec.getSpec().getLogicalReaders().getLogicalReader().isEmpty()) {
				delegate.recordError(res.errorNoXSpecified(res.readers()), null, mcEventCycleSpec);
				return;
			}
		}
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(MCEventCycleSpec value) {
		mcEventCycleSpec = value;
	}

	public void setBaseActivity(BaseActivity activity) {
		logicalReadersList.setBaseActivity(activity);
		primaryKeysList.setBaseActivity(activity);
	}

	@Override
	public ToggleButton enable() {
		return enable;
	}

	@Override
	public HasTriggerHandlers boundarySpec() {
		return boundarySpec;
	}

	@Override
	@Path("spec.logicalReaders.logicalReader")
	public CommonListEditor logicalReadersList() {
		return logicalReadersList;
	}

}
