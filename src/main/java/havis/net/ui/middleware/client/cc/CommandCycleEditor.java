package havis.net.ui.middleware.client.cc;

import havis.middleware.ale.service.cc.CCCmdSpec;
import havis.middleware.ale.service.mc.MCCommandCycleSpec;
import havis.net.ui.middleware.client.cc.bs.CCBoundarySpecEditor;
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

public class CommandCycleEditor extends Composite implements MCCycleSpecEditor<MCCommandCycleSpec> {

	private EditorDelegate<MCCommandCycleSpec> delegate;

	private ConstantsResource res = ConstantsResource.INSTANCE;

	private MCCommandCycleSpec mcCommandCycleSpec;

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
	CCBoundarySpecEditor boundarySpec;

	@Path("spec.cmdSpecs.cmdSpec")
	@UiField(provided = true)
	SingleColumnListWidget<CCCmdSpec> commandSpecsListWidget = new SingleColumnListWidget<CCCmdSpec>(
			new CustomRenderer<CCCmdSpec>() {

				@Override
				public String render(CCCmdSpec value) {
					return value.getName();
				}
			});

	@Ignore
	@UiField
	ConfigurationSections eventCycleConfigSections;

	private static CommandCycleEditorUiBinder uiBinder = GWT.create(CommandCycleEditorUiBinder.class);

	interface CommandCycleEditorUiBinder extends UiBinder<Widget, CommandCycleEditor> {
	}

	public CommandCycleEditor() {
		initWidget(uiBinder.createAndBindUi(this));

		logicalReadersList.setHeader(Arrays.asList(res.chooseItem("Reader")));
		commandSpecsListWidget.setHeader(Arrays.asList(res.createItem(res.commands())));

		name.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Utils.removeErrorStyle(name);
			}
		});
	}

	@Override
	public void setDelegate(EditorDelegate<MCCommandCycleSpec> delegate) {
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

			if (mcCommandCycleSpec.getSpec().getLogicalReaders() == null
					|| mcCommandCycleSpec.getSpec().getLogicalReaders() == null
					|| mcCommandCycleSpec.getSpec().getLogicalReaders().getLogicalReader().isEmpty()) {
				delegate.recordError(res.errorNoXSpecified(res.readers()), null, mcCommandCycleSpec);
				return;
			}
		}

	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(MCCommandCycleSpec value) {
		mcCommandCycleSpec = value;
	}

	@Override
	public ConfigurationSections getConfigSections() {
		return eventCycleConfigSections;
	}

	public void setBaseActivity(BaseActivity activity) {
		logicalReadersList.setBaseActivity(activity);
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
