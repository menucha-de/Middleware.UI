package havis.net.ui.middleware.client.lr;

import havis.middleware.ale.service.lr.LRProperty;
import havis.net.ui.middleware.client.lr.Property.Group;
import havis.net.ui.middleware.client.shared.resourcebundle.ResourceBundle;
import havis.net.ui.shared.client.table.CustomWidgetRow;
import havis.net.ui.shared.client.widgets.CustomRenderer;
import havis.net.ui.shared.client.widgets.CustomSuggestBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;

public class PropertyEditor extends CustomWidgetRow implements Editor<LRProperty> {

	CustomSuggestBox<String> name = new CustomSuggestBox<String>(CustomSuggestBox.getStringParser());

	CustomSuggestBox<String> value = new CustomSuggestBox<String>(CustomSuggestBox.getStringParser());

	private ChangeHandler changeHandler = new ChangeHandler() {

		@Override
		public void onChange(ChangeEvent event) {
			if (Property.PROPERTIES.containsKey(name.getValue())) {
				if (Property.PROPERTIES.get(name.getValue()).getValues() != null) {
					value.getListBox().setItems(Property.PROPERTIES.get(name.getValue()).getValues());
					value.getListBox().getElement().getStyle().setProperty("visibility", "");
				} else {
					value.getListBox().setVisibility(Visibility.HIDDEN);
				}
			} else {
				value.getListBox().setVisibility(Visibility.HIDDEN);
			}
		}
	};

	public PropertyEditor() {
		// Set renderer to show label instead of name in the listbox
		name.getListBox().setRenderer(new CustomRenderer<String>() {

			@Override
			public String render(String value) {
				if (Property.PROPERTIES.containsKey(value)) {
					return Property.PROPERTIES.get(value).getLabel();
				}
				return value;
			}
		});
		// Adding predefined properties to the list
		HashMap<String, List<String>> items = new HashMap<String, List<String>>();
		for (Group g : Group.values()) {
			ArrayList<String> list = new ArrayList<String>();
			for (String p : Property.getProperties(g).keySet()) {
				list.add(p);
			}
			items.put("<" + g.toString() + ">", list);
		}

		// add change handler
		name.addChangeHandler(changeHandler);
		// set style
		name.getListBox().setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableListBox());
		name.setAddTextBoxStyleNames(ResourceBundle.INSTANCE.css().webuiCustomTableTextBox());
		// add column
		addColumn(name);

		value.getListBox().setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableListBox());
		value.setAddTextBoxStyleNames(ResourceBundle.INSTANCE.css().webuiCustomTableTextBox());
		value.getListBox().setVisibility(Visibility.HIDDEN);

		addColumn(value);

		name.getListBox().addItems(items);
	}

	public String getName() {
		return name.getValue();
	}

	public void setStartFocus() {
		name.setFocus(true);
	}

	public void setValueKeyUpHandler(KeyUpHandler handler) {
		value.getTextBox().addKeyUpHandler(handler);
	}

	public void setValueKeyDownHandler(KeyDownHandler handler) {
		value.getTextBox().addKeyDownHandler(handler);
	}
}
