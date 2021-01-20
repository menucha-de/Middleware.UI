package havis.net.ui.middleware.client.shared.trigger;

import havis.net.ui.middleware.client.shared.pattern.Pattern;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.trigger.model.HttpTrigger;
import havis.net.ui.middleware.client.utils.Utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class HttpTriggerEditor extends Composite implements ValueAwareEditor<HttpTrigger> {

	private EditorDelegate<HttpTrigger> delegate;
	
	@UiField
	TextBox name;

	private static HttpTriggerEditorUiBinder uiBinder = GWT.create(HttpTriggerEditorUiBinder.class);
	interface HttpTriggerEditorUiBinder extends UiBinder<Widget, HttpTriggerEditor> { }

	@UiConstructor
	public HttpTriggerEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		name.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				Utils.removeErrorStyle(name);
			}
		});
	}

	@Override
	public void setDelegate(EditorDelegate<HttpTrigger> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void flush() {
		if(delegate != null){
			if (Utils.isNullOrEmpty(name.getValue())){
				delegate.recordError(ConstantsResource.INSTANCE.errorInvalidEmptyField("Name"), name.getValue(), name);
				Utils.addErrorStyle(name);
				return;
			}
			if (Pattern.match(Pattern.PatternWhiteSpace, name.getValue())){
				delegate.recordError(ConstantsResource.INSTANCE.errorInvalidSpecNameWhiteSpaces(), name.getValue(), name);
				Utils.addErrorStyle(name);
				return;
			}
			if (Pattern.match(Pattern.PatternSyntax, name.getValue())){
				delegate.recordError(ConstantsResource.INSTANCE.errorInvalidSpecNameSpecialChar(), name.getValue(), name);
				Utils.addErrorStyle(name);
				return;
			}
			Utils.removeErrorStyle(name);
		}
	}

	@Override
	public void onPropertyChange(String... paths) { }
	

	@Override
	public void setValue(HttpTrigger value) { }
	
}
