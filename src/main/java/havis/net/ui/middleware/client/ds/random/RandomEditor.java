package havis.net.ui.middleware.client.ds.random;

import havis.middleware.ale.service.mc.MCRandomSpec;
import havis.net.ui.middleware.client.shared.MCSpecEditor;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.utils.NoSepIntegerBox;
import havis.net.ui.middleware.client.utils.Utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class RandomEditor extends Composite implements MCSpecEditor<MCRandomSpec> {

	private EditorDelegate<MCRandomSpec> delegate;

	private ConstantsResource res = ConstantsResource.INSTANCE;

	@UiField
	TextBox name;

	@UiField
	ToggleButton enable;

	@Ignore
	@UiField
	NoSepIntegerBox length;

	private static RandomEditorUiBinder uiBinder = GWT.create(RandomEditorUiBinder.class);

	interface RandomEditorUiBinder extends UiBinder<Widget, RandomEditor> {
	}

	private MCRandomSpec mcRandomSpec;

	KeyUpHandler longBoxKeyUp = new KeyUpHandler() {

		@Override
		public void onKeyUp(KeyUpEvent event) {
			IntegerBox box = (IntegerBox) event.getSource();
			try {
				Integer value = box.getValueOrThrow();
				if (value == null || value < 0) {
					box.setValue((int) 1);
					return;
				}

			} catch (java.text.ParseException e) {
				box.setValue((int) 1);
			}
		}
	};

	public RandomEditor() {

		initWidget(uiBinder.createAndBindUi(this));

		name.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Utils.removeErrorStyle(name);
			}
		});

		length.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				Utils.removeErrorStyle(length);
			}
		});
	}

	@Override
	public void setDelegate(EditorDelegate<MCRandomSpec> delegate) {
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

			if (length.getValue() == null) {
				delegate.recordError(res.errorInvalidEmptyField(res.length()), null, null);
				Utils.addErrorStyle(length);
				return;
			}

			if (length.getValue() < 1) {
				delegate.recordError(res.errorInvalidLength(), length, length.getValue());
				Utils.addErrorStyle(length);
				return;
			}
			Utils.removeErrorStyle(length);

			mcRandomSpec.getSpec().setLength(length.getValue());
		}
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(MCRandomSpec value) {
		if (value != null) {
			mcRandomSpec = value;
			if (value.getSpec() != null) {
				length.setValue(value.getSpec().getLength());
				return;
			}
		}
		length.setValue(1);
	}

	@Override
	public ToggleButton enable() {
		return enable;
	}
}
