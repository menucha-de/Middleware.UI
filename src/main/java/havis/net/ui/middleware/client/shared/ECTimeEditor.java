package havis.net.ui.middleware.client.shared;

import havis.middleware.ale.service.ECTime;
import havis.net.ui.middleware.client.utils.Utils;

import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.SimpleEditor;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;

public class ECTimeEditor extends Composite implements ValueAwareEditor<ECTime> {
	
	@Ignore
	TextBox value = new TextBox();
	
	SimpleEditor<String> unit = SimpleEditor.of("unit");
	
	private ECTime ecTime;

	private boolean parsingError;
	
	public ECTimeEditor() {
		initWidget(value);
		parsingError = false;
	}
	
	
	@Override
	public void flush() {
		parsingError = false;
		
		if (!value.getText().isEmpty()) {
			try {
				long result = Long.valueOf(value.getText());
				//if (result >= 0) {
					ecTime.setValue(result);
				//} BUG! - getValue() < 0 does not work!
			} catch (NumberFormatException e) {
				parsingError = true;
			}
			
		} else {
			ecTime.setValue(Utils.INVALID_TIME);
		}
	}

	@Override
	public void setValue(ECTime value) {
		ecTime = value != null ? value : Utils.getTime();
		if (ecTime != null && ecTime.getValue() >= 0) {
			this.value.setText(String.valueOf(ecTime.getValue()));
		}
	}
	
	public long getValue(){
		flush();
		return ecTime.getValue();
	}
	
	public boolean isEmpty(){
		return value.getText().isEmpty();
	}
	
	
	public boolean getParsingError(){
		flush();
		return parsingError;
	}
	
	public void addKeyUpHandler(KeyUpHandler handler){
		value.addKeyUpHandler(handler);
	}
	

	@Override
	public void onPropertyChange(String... paths) {}

	@Override
	public void setDelegate(EditorDelegate<ECTime> delegate) {}
}
