package havis.net.ui.middleware.client.pc;

import havis.middleware.ale.service.mc.MCPortCycleSpec;
import havis.middleware.ale.service.pc.PCReportSpec;
import havis.net.ui.middleware.client.pc.bs.PCBoundarySpecEditor;
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

public class PortCycleEditor extends Composite implements MCCycleSpecEditor<MCPortCycleSpec> {

	private EditorDelegate<MCPortCycleSpec> delegate;

	private ConstantsResource res = ConstantsResource.INSTANCE;

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

	@Path("spec.boundarySpec")
	@UiField
	public PCBoundarySpecEditor boundarySpec;

	@Path("spec.reportSpecs.reportSpec")
	@UiField(provided = true)
	SingleColumnListWidget<PCReportSpec> reportSpecsListWidget = new SingleColumnListWidget<PCReportSpec>(
			new CustomRenderer<PCReportSpec>() {

				@Override
				public String render(PCReportSpec value) {
					return value.getName();
				}
			});

	@Ignore
	@UiField
	ConfigurationSections eventCycleConfigSections;

	private static PortCycleEditorUiBinder uiBinder = GWT.create(PortCycleEditorUiBinder.class);

	interface PortCycleEditorUiBinder extends UiBinder<Widget, PortCycleEditor> {
	}

	public PortCycleEditor() {
		initWidget(uiBinder.createAndBindUi(this));

		logicalReadersList.setText(res.readers());
		logicalReadersList.setHeader(Arrays.asList(res.chooseItem("Reader")));

		reportSpecsListWidget.setText(res.reports());
		reportSpecsListWidget.setHeader(Arrays.asList(res.createItem("Report")));

		name.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Utils.removeErrorStyle(name);
			}
		});
	}

	public ConfigurationSections getConfigSections() {
		return eventCycleConfigSections;
	}

	public void setBaseActivity(BaseActivity activity) {
		logicalReadersList.setBaseActivity(activity);
	}

	@Override
	public void setDelegate(EditorDelegate<MCPortCycleSpec> delegate) {
		this.delegate = delegate;
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
		}
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(MCPortCycleSpec value) {
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
