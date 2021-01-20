package havis.net.ui.middleware.client.shared;

import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.NewItemEvent.Handler;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;
import havis.net.ui.shared.client.table.CustomWidgetRow;
import havis.net.ui.shared.client.widgets.CustomListBox;
import havis.net.ui.shared.client.widgets.CustomRenderer;

import java.util.List;
import java.util.Map;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;



public class ListItemsEditor extends CustomWidgetRow implements LeafValueEditor<String>, NewItemEvent.HasHandlers {
	
	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	private List<String> items;
	
	CustomListBox<String> itemBox = new CustomListBox<String>(new CustomRenderer<String>() {
		@Override
		public String render(String object) {
			return object;
		}		
		
	}, true);
	
	public ListItemsEditor() {
		itemBox.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableListBox());
		itemBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if (itemBox.getSelectedItemText().equals(res.newEntry())) {
					fireEvent(new NewItemEvent());
				}
			}
		});
		
		
		addColumn(itemBox);
	}
	
	@Override
	public void setValue(String value) {
		itemBox.setValue(value);
		if (items != null) {
			itemBox.setItems(items);
		}
	}

	public void setValues(List<String> values) {
		itemBox.clear();
		itemBox.addSilentItem(null, res.newEntry());
		itemBox.addItems(values);
	}
	
	public void setValues(Map<String, List<String>> values) {
		itemBox.clear();
		itemBox.addSilentItem(null, res.newEntry());
		itemBox.addItems(values);		
	}

	@Override
	public String getValue() {
		return itemBox.getValue();
	}

	@Override
	public HandlerRegistration addNewItemHandler(Handler handler) {
		return addHandler(handler, NewItemEvent.getType());
	}
}
