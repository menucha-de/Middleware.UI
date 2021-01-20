package havis.net.ui.middleware.client.utils;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.text.client.IntegerParser;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.ValueBox;

import elemental.events.KeyboardEvent.KeyCode;

/**
 * No Separator IntegerBox
 * To overcome the thousands separator problems on common IntegerBoxes
 * The best option is to implement your own IntegerBox widget:
 * http://stackoverflow.com/questions/15978638/longbox-in-gwt-remove-thousand-delimiter-formatting
 */
public class NoSepIntegerBox extends ValueBox<Integer> {
	
	public NoSepIntegerBox() {
		super(Document.get().createTextInputElement(), 
          new AbstractRenderer<Integer>() {
            public String render(Integer l) {
              return l == null ? "" : l.toString();
            }
          },
         IntegerParser.instance());
		
		final RegExp regExp = RegExp.compile("^[0-9]+$");
		
		super.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				int nativeKeyCode = event.getNativeKeyCode();
				
				if (nativeKeyCode == KeyCode.TAB ||
					nativeKeyCode == KeyCode.BACKSPACE ||
					nativeKeyCode == KeyCode.DELETE) return;
				
				if(!event.isAnyModifierKeyDown()){
					if (nativeKeyCode > 47 && nativeKeyCode < 58 ) return; //normal numbers
					if (nativeKeyCode > 95 && nativeKeyCode < 106 ) return;	//number block
				}
				
				if (event.isControlKeyDown()) {
			         if (nativeKeyCode == KeyCode.V) {
			        	 //handle clip board insertions
			        	 Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			     			@Override
			     			public void execute() {
			     				String txt = getText();
			     				if(Utils.isNullOrEmpty(txt))return;
			     				MatchResult result =  regExp.exec(txt);
			    				if(result == null){
			    					setText("");
			    				}
			     			}
			     		});
			        	return;
			         }
			    }
				event.preventDefault();
				event.stopPropagation();
			}
		});
		
		super.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(Utils.isNullOrEmpty(getText()))return;
				
				MatchResult result =  regExp.exec(getText());
				
				//2147483647
				if(result != null && getValue() >= Integer.MAX_VALUE){
					setValue(Integer.MAX_VALUE);
				}
			}
		});
	}
	
	public boolean isEmpty(){
		return Utils.isNullOrEmpty(getText());
	}
	
}
