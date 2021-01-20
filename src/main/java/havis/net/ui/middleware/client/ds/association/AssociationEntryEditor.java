package havis.net.ui.middleware.client.ds.association;

import havis.middleware.ale.service.cc.AssocTableEntry;
import havis.net.ui.middleware.client.shared.BaseActivity;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.utils.Utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AssociationEntryEditor extends Composite implements ValueAwareEditor<AssocTableEntry>{

	private EditorDelegate<AssocTableEntry> delegate;

	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	@UiField
	TextBox key;

	@UiField
	TextBox value;
	
	private static EventCycleEditorUiBinder uiBinder = GWT.create(EventCycleEditorUiBinder.class);
	interface EventCycleEditorUiBinder extends UiBinder<Widget, AssociationEntryEditor> { }
	

	public AssociationEntryEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		key.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Utils.removeErrorStyle(key);
			}
		});
	}

	@Override
	public void setDelegate(EditorDelegate<AssocTableEntry> delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void flush() {
		if (delegate != null) {
			if (Utils.isNullOrEmpty(key.getValue())) {
				delegate.recordError(res.errorInvalidEmptyField(res.epc()), key.getValue(), key.getValue());
				Utils.addErrorStyle(key);
				return;
			}
			
			if (Utils.isNullOrEmpty(value.getValue())) {
				delegate.recordError(res.errorInvalidEmptyField(res.value()), value.getValue(), value.getValue());
				Utils.addErrorStyle(value);
				return;
			}
		}
	}

	@Override
	public void onPropertyChange(String... paths) { }

	@Override
	public void setValue(AssocTableEntry value) { }
	
	public void setBaseActivity(BaseActivity activity){ }
}
