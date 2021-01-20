package havis.net.ui.middleware.client.shared.report.filter;

import havis.middleware.ale.service.ECFilterListMember;
import havis.middleware.ale.service.tm.TMFixedFieldSpec;
import havis.middleware.ale.service.tm.TMVariableFieldSpec;
import havis.net.ui.middleware.client.shared.report.filter.field.ECFieldSpecSectionEditor;
import havis.net.ui.middleware.client.shared.report.list.SingleColumnListWidget;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.shared.client.ConfigurationSections;
import havis.net.ui.shared.client.widgets.CustomRenderer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class ECFilterListMemberEditor extends Composite implements ValueAwareEditor<ECFilterListMember> {

	private ConstantsResource res = ConstantsResource.INSTANCE;

	private EditorDelegate<ECFilterListMember> delegate;

	@Ignore
	@UiField
	ToggleButton toggleInclude;

	@Ignore
	@UiField
	ToggleButton toggleExclude;

	@Path("")
	@UiField
	public ECFieldSpecSectionEditor filterListMemberFieldEditor;

	@Path("patList.pat")
	@UiField(provided = true)
	SingleColumnListWidget<String> patternListEditor = new SingleColumnListWidget<String>(new CustomRenderer<String>() {

		@Override
		public String render(String value) {
			return value;
		}
	});

	@Ignore
	@UiField
	ConfigurationSections filterConfigSections;

	private ECFilterListMember ecFilterListMember;

	private static ECFilterListMemberEditorUiBinder uiBinder = GWT.create(ECFilterListMemberEditorUiBinder.class);

	interface ECFilterListMemberEditorUiBinder extends UiBinder<Widget, ECFilterListMemberEditor> {
	}

	public ECFilterListMemberEditor() {

		patternListEditor.setText(res.patternList());
		patternListEditor.setHeader(Arrays.asList(res.pattern()));

		initWidget(uiBinder.createAndBindUi(this));

		filterListMemberFieldEditor.setText(res.field());
		filterListMemberFieldEditor.fieldSpecEditor.setFilterPatternView(true);

		toggleInclude.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				toggleExclude.setDown(!toggleInclude.isDown());
				if (toggleExclude.isDown()) {
					ecFilterListMember.setIncludeExclude("INCLUDE");
				}

			}
		});
		toggleExclude.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				toggleInclude.setDown(!toggleExclude.isDown());
				ecFilterListMember.setIncludeExclude("EXCLUDE");
			}
		});
	}

	public ConfigurationSections getConfigSections() {
		return filterConfigSections;
	}

	@Override
	public void setDelegate(EditorDelegate<ECFilterListMember> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void flush() {

		if (!toggleInclude.isDown() && !toggleExclude.isDown()) {
			delegate.recordError(res.errorNoIncludeExclude(), null, null);
			return;
		}

		if (toggleInclude.isDown()) {
			ecFilterListMember.setIncludeExclude("INCLUDE");
		} else if (toggleExclude.isDown()) {
			ecFilterListMember.setIncludeExclude("EXCLUDE");
		}

		// 4.66 TC-ID: EC_ER_ECREPORTFILTERSPEC_27
		if (ecFilterListMember.getPatList() == null || ecFilterListMember.getPatList().getPat().isEmpty()) {
			delegate.recordError(res.errorNoPatterns(), null, ecFilterListMember);
			return;
		}
	}

	@Override
	public void setValue(ECFilterListMember value) {
		ecFilterListMember = value;
		if (ecFilterListMember != null && ecFilterListMember.getIncludeExclude() != null) {

			if (ecFilterListMember.getIncludeExclude().startsWith("INCLUDE")) {
				toggleInclude.setDown(true);
			} else if (ecFilterListMember.getIncludeExclude().startsWith("EXCLUDE")) {
				toggleExclude.setDown(true);
			}
		}

		filterListMemberFieldEditor.setValue(ecFilterListMember);
	}

	public void setTMFields(Map<String, List<TMFixedFieldSpec>> fixed, Map<String, List<TMVariableFieldSpec>> variable) {
		filterListMemberFieldEditor.setTMFields(fixed, variable);
	}

	
	@Override
	public void onPropertyChange(String... paths) {
	}
}
