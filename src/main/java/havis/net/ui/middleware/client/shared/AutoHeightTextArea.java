package havis.net.ui.middleware.client.shared;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * AutoHeightTextArea calculates the height of the TextArea by means of the HTML5 canvas/context method measureText(...)
 * The canvas has to be embedded into a HTMLPanel and that into a FlowPanel or the RootPanel
 * On order to avoid multiple measurePanel's on a DOM with more than one AutoHeightTextArea's a shared panel can be injected from outside
 * The height calculation itself has to be external triggered using calculateHeight() after the text has been loaded.
 * (calculation via changedHandler did not pan out - might be too early)
 *
 */
public class AutoHeightTextArea extends TextArea{
	
	private int txtWidth;
	private int initialHeight;
	
	private TextArea me;
	private HTMLPanel htmlWrapper;
	
	private Canvas canvas;
	private String canvasId;
	private FlowPanel measurePanel;
	
	/**
	 * Normal c'tor 
	 */
	public AutoHeightTextArea(){
		this.measurePanel = null;
		initialize();
	}
	
	/**
	 * C'tor to use an external measurePanel
	 * (for more than one AutoHeightTextArea's per DOM)
	 */
	public AutoHeightTextArea(FlowPanel measurePanel){
		this.measurePanel = measurePanel; 
		initialize();
	}
	
	private void initialize(){
		me = this;
		
		htmlWrapper = new HTMLPanel("");
		
		initialHeight = -1;
		txtWidth = -1;
		
		if(this.measurePanel == null || this.measurePanel.getWidgetCount() < 1){
		
			canvas = Canvas.createIfSupported();
	        if (canvas == null) {
	        	me.setText("HTML5 Canvas not supported!");
	              return;
	        }
	        
	        canvasId = "measureCanvas_" + Document.get().createUniqueId();
			canvas.setWidth("384px");
	        canvas.setHeight("2px"); 
	        canvas.getElement().setId(canvasId);
	        
	        htmlWrapper.add(canvas);
		}
        if(this.measurePanel == null){
        	RootPanel.get().add(htmlWrapper);
        	return;
        }
        
        if(this.measurePanel.getWidgetCount() < 1)
        	this.measurePanel.add(htmlWrapper);
        else{
        	//obtain the id from the already created canvas
        	HTMLPanel hp = (HTMLPanel) this.measurePanel.getWidget(0);
        	Canvas ca = (Canvas)hp.getWidget(0);
        	canvasId = ca.getElement().getId();
        }
	}
	
	/**
	 * Calculating the height, res. the number of lines of the text area
	 * to be triggered from 'outside'
	 */
	public void calculateHeight(){
		
		if(txtWidth < 0){
			//time point of measurement is important!
			txtWidth = canvasMeasureText(canvasId, me.getText(),"" + me.getCharacterWidth());
			//GWT.log("txtWidth: " + txtWidth);
		}
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				if(initialHeight < 0){
					//time point of measurement is important!
					initialHeight = (int) (me.getOffsetHeight() * 0.75);
					//GWT.log("initialHeight: " + initialHeight);
				}
				
				//GWT.log("ClientWidth: " + me.getOffsetWidth());
				int lines = (int) (txtWidth / me.getOffsetWidth());
				lines++;
				//GWT.log("lines: " + lines);
				//GWT.log("New Height: " + (initialHeight * lines) + "px" );
				
				if(lines < 2){
					initialHeight = me.getOffsetHeight();
				}
				
				me.setHeight((initialHeight * lines) + "px");
			}
		});
	}
	
	public void setNoOfLines(final int lines){
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				if(initialHeight < 0){
					//time point of measurement is important!
					initialHeight = (int) (me.getOffsetHeight() * 0.75);
					//GWT.log("initialHeight: " + initialHeight);
				}
				if(lines < 2){
					initialHeight = me.getOffsetHeight();
				}
				me.setHeight((initialHeight * lines) + "px");
			}
		});
	}
	
	private native int canvasMeasureText(String canId, String txt, String charWidth) /*-{
		
		function measure(_id, _txt, _w) {
			var ctx = $doc.getElementById(_id).getContext("2d");
			ctx.font = _w + "px Arial"; //not really correct but worked.
  			var text = ctx.measureText(_txt); //TextMetrics object
  			return text.width;
		}
		return measure(canId, txt, charWidth);
	}-*/;
}
