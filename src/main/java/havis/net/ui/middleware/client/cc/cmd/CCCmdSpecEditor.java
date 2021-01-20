package havis.net.ui.middleware.client.cc.cmd;

import havis.middleware.ale.service.ECFilterListMember;
import havis.middleware.ale.service.cc.CCCmdSpec;
import havis.middleware.ale.service.cc.CCOpSpec;
import havis.net.ui.middleware.client.ec.rep.output.OutputStatisticsEditor;
import havis.net.ui.middleware.client.shared.CustomListRenderer;
import havis.net.ui.middleware.client.shared.report.list.MultipleColumnListWidget;
import havis.net.ui.middleware.client.shared.report.list.SingleColumnListWidget;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.ConfigurationSections;
import havis.net.ui.shared.client.widgets.CustomRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class CCCmdSpecEditor extends Composite implements ValueAwareEditor<CCCmdSpec> {

	private EditorDelegate<CCCmdSpec> delegate;

	@Path("name")
	@UiField
	TextBox name;

	@Path("reportIfEmpty")
	@UiField
	ToggleButton reportIfEmpty;

	@Path("filterSpec.filterList.filter")
	@UiField(provided = true)
	MultipleColumnListWidget<ECFilterListMember> configSecFilterSpecs = new MultipleColumnListWidget<ECFilterListMember>(
			new CustomListRenderer<ECFilterListMember>() {

				@Override
				public List<String> render(ECFilterListMember value) {
					if (value == null)
						return null;

					List<String> lst = new ArrayList<String>();

					lst.add(Utils.isNullOrEmpty(value.getIncludeExclude()) ? "" : value.getIncludeExclude());

					String tmp = "";
					if (value.getFieldspec() != null && !Utils.isNullOrEmpty(value.getFieldspec().getFieldname())) {
						tmp = value.getFieldspec().getFieldname();
					} else {
						tmp = "epc";
					}
					lst.add(tmp);
					tmp = "";
					if (value.getPatList() != null) {
						for (String s : value.getPatList().getPat()) {
							tmp += (s + "\r\n");
						}
						if (tmp.endsWith("\r\n"))
							tmp = tmp.substring(0, tmp.length() - 2);
					}
					lst.add(tmp);
					return lst;
				}
			}, 3);

	@Path("opSpecs.opSpec")
	@UiField(provided = true)
	SingleColumnListWidget<CCOpSpec> configSecOperations = new SingleColumnListWidget<CCOpSpec>(
			new CustomRenderer<CCOpSpec>() {

				@Override
				public String render(CCOpSpec value) {
					return value.getOpName();
				}
			});

	@Path("statProfileNames.statProfileName")
	@UiField
	OutputStatisticsEditor configSecStatProfileNames;

	@Ignore
	@UiField
	ConfigurationSections ccSpecsSections;

	private CCCmdSpec ccCmdSpec;

	private ConstantsResource res = ConstantsResource.INSTANCE;

	private static ReportSpecEditorUiBinder uiBinder = GWT.create(ReportSpecEditorUiBinder.class);

	interface ReportSpecEditorUiBinder extends UiBinder<Widget, CCCmdSpecEditor> {
	}

	public CCCmdSpecEditor() {
		initWidget(uiBinder.createAndBindUi(this));

		configSecOperations.setHeader(Arrays.asList(res.createItem(res.operation())));
		configSecFilterSpecs.setHeader(Arrays.asList(res.type(), res.field(), res.patternList()));

		name.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Utils.removeErrorStyle(name);
			}
		});
	}

	@Override
	public void setDelegate(EditorDelegate<CCCmdSpec> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setValue(CCCmdSpec value) {
		ccCmdSpec = value;
	}

	public void flush() {

		if (configSecOperations.isDirty()) {
			ccCmdSpec.getOpSpecs().getOpSpec().clear();
			for (CCOpSpec spec : configSecOperations.getEditorList()) {
				ccCmdSpec.getOpSpecs().getOpSpec().add(spec);
			}
		}

		String pattern = "([a-zA-Z])([a-zA-Z0-9_-]+)";

		if (Utils.isNullOrEmpty(name.getText())) {
			delegate.recordError(res.errorInvalidEmptyField("Name"), null, name);
			Utils.addErrorStyle(name);
			return;
		}

		RegExp regExp = RegExp.compile(pattern);
		MatchResult result = regExp.exec(name.getText());

		if (result == null) {
			delegate.recordError(res.errorUnicodeFound(name.getText()), null, name);
			Utils.addErrorStyle(name);
			return;
		}
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	public ConfigurationSections getConfigSections() {
		return ccSpecsSections;
	}
}
