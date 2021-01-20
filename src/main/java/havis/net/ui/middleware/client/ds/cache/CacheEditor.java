package havis.net.ui.middleware.client.ds.cache;

import havis.middleware.ale.service.mc.MCCacheSpec;
import havis.net.ui.middleware.client.shared.MCSpecEditor;
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

public class CacheEditor extends Composite implements MCSpecEditor<MCCacheSpec> {

	private EditorDelegate<MCCacheSpec> delegate;

	private ConstantsResource res = ConstantsResource.INSTANCE;

	@UiField
	TextBox name;

	@UiField
	ToggleButton enable;

	@Path("patterns.patterns.pattern")
	@UiField(provided = true)
	SingleColumnListWidget<String> patternListEditor = new SingleColumnListWidget<String>(new CustomRenderer<String>() {

		@Override
		public String render(String value) {
			return value;
		}
	});

	@Ignore
	@UiField
	ConfigurationSections patternConfigSection;

	private static CacheEditorUiBinder uiBinder = GWT.create(CacheEditorUiBinder.class);

	interface CacheEditorUiBinder extends UiBinder<Widget, CacheEditor> {
	}

	public CacheEditor() {
		patternListEditor.setHeader(Arrays.asList(res.pattern()));

		initWidget(uiBinder.createAndBindUi(this));

		name.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Utils.removeErrorStyle(name);
			}
		});
	}

	@Override
	public void setDelegate(EditorDelegate<MCCacheSpec> delegate) {
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
			Utils.removeErrorStyle(name);
			
			if (patternListEditor.getCustomWidgetCount() < 1) {
				delegate.recordError(res.errorNotSpecified(res.pattern()), null, patternListEditor);
				patternListEditor.setOpen(true);

				Utils.addErrorStyle(patternListEditor.getTable());
				return;
			}
			Utils.removeErrorStyle(patternListEditor.getTable());
		}
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(MCCacheSpec value) {
	}

	public ConfigurationSections getConfigSections() {
		return patternConfigSection;
	}

	@Override
	public ToggleButton enable() {
		return enable;
	}

}
