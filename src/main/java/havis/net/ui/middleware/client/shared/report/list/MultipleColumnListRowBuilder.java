package havis.net.ui.middleware.client.shared.report.list;

import java.util.List;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;

import havis.net.ui.middleware.client.shared.CustomListRenderer;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.table.CustomWidgetRow;

public class MultipleColumnListRowBuilder<T> extends CustomWidgetRow implements LeafValueEditor<T>, OpenItemEvent.HasHandlers{

	private CustomListRenderer<T> renderer;
	
	T value;
	
	Label typeLabel;
	Label fieldLabel;
	Label patternListLabel;
	
	private int index;
	//private int colCnt;
	
	ClickHandler clickHandler = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			int column = -1;
			
			if((Label)(event.getSource()) == typeLabel){
				column = 0;
			}
			else if((Label)(event.getSource()) == fieldLabel){
				column = 1;
			}
			else if((Label)(event.getSource()) == patternListLabel){
				column = 2;
			}
			
			fireEvent(new OpenItemEvent(index, column));
		}
	};
	
	public MultipleColumnListRowBuilder(CustomListRenderer<T> renderer, int cols) {
		
		this.renderer = renderer;
		if(cols < 1) cols = 1;
		if(cols > 3) cols = 3;
		//colCnt = cols;
		
		if(cols > 0){
			typeLabel = new Label();
			typeLabel.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableLabel());
			typeLabel.addClickHandler(clickHandler);
			addColumn(typeLabel);
		}
		if(cols > 1){
			fieldLabel = new Label();
			fieldLabel.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableLabel());
			fieldLabel.addClickHandler(clickHandler);
			addColumn(fieldLabel);
		}
		if(cols > 2){
			patternListLabel = new Label();
			patternListLabel.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableLabel());
			patternListLabel.addClickHandler(clickHandler);
			addColumn(patternListLabel);
		}
	}
	
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	public void setValue(T value) {
		this.value = value;
		List<String> list = renderer.render(value);
		if(list != null){
			int size = list.size();
			if(size > 0)
				typeLabel.setText(list.get(0));
			if(size > 1)
				fieldLabel.setText(list.get(1));
			if(size > 2)
				patternListLabel.setText(list.get(2));
		}
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public HandlerRegistration addOpenItemHandler(
			havis.net.ui.middleware.client.shared.event.OpenItemEvent.Handler handler) {
		return addHandler(handler, OpenItemEvent.getType());
	}

	
	public String getName() {
		
		String name = "";
		if(!Utils.isNullOrEmpty(typeLabel.getText())){
			name += (typeLabel.getText() +"-");
		}
		if(!Utils.isNullOrEmpty(fieldLabel.getText())){
			name += (fieldLabel.getText() +"-");
		}
		if(!Utils.isNullOrEmpty(patternListLabel.getText())){
			name += patternListLabel.getText();
		}
		
		if(name.endsWith("-")){
			name = name.substring(0, name.length()-1);
		}
		
		return name;
	}
}
