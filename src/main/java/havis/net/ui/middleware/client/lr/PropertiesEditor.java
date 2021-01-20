package havis.net.ui.middleware.client.lr;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

import havis.middleware.ale.service.lr.LRProperty;
import havis.middleware.ale.service.lr.LRSpec;
import havis.middleware.ale.service.lr.LRSpec.Properties;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.ConfigurationSection;
import havis.net.ui.shared.client.table.CreateRowEvent;
import havis.net.ui.shared.client.table.CustomTable;
import havis.net.ui.shared.client.table.DeleteRowEvent;

public class PropertiesEditor extends ConfigurationSection implements ValueAwareEditor<LRSpec.Properties> {

	private EditorDelegate<Properties> delegate;

	@UiField
	CustomTable properties;

	@Path("property")
	ListEditor<LRProperty, PropertyEditor> editor;

	private class PropertyEditorSource extends EditorSource<PropertyEditor> {

		@Override
		public PropertyEditor create(int index) {
			final PropertyEditor prpEditor = new PropertyEditor();

			prpEditor.setValueKeyDownHandler(new KeyDownHandler() {

				@Override
				public void onKeyDown(KeyDownEvent event) {
					// Tab handling for entering values into the properties
					// table
					if (event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
						event.preventDefault();
						event.stopPropagation();
						PropertyEditor pe = null;
						int index = editor.getList().size() - 1;
						if (index < 0)
							return;

						pe = (PropertyEditor) properties.getRow(index);
						// block inserting if already an empty row exist
						if (pe != null && !Utils.isNullOrEmpty(pe.getName())) {
							onCreateRow(null);
							index = editor.getList().size() - 1;
							if (index < 0)
								return;
							pe = (PropertyEditor) properties.getRow(index);
							pe.setStartFocus();
						}
					}

				}
			});

			properties.addRow(prpEditor);
			return prpEditor;
		}

		@Override
		public void dispose(PropertyEditor subEditor) {
			properties.deleteRow(subEditor);
		}
	}

	private static PropertySectionUiBinder uiBinder = GWT.create(PropertySectionUiBinder.class);

	interface PropertySectionUiBinder extends UiBinder<Widget, PropertiesEditor> {
	}

	@UiHandler("properties")
	void onCreateRow(CreateRowEvent event) {
		editor.getList().add(new LRProperty());
	}

	@UiHandler("properties")
	void onDeleteRow(final DeleteRowEvent event) {
		editor.getList().remove(event.getIndex());
	}

	public PropertiesEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		setText("Properties");
		editor = ListEditor.of(new PropertyEditorSource());
		properties.setHeader(Arrays.asList("Name", "Value"));

	}

	@Override
	public void setDelegate(EditorDelegate<Properties> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void flush() {
		int i = 0;
		while (i < editor.getList().size()) {
			LRProperty p = editor.getList().get(i);
			if (Utils.isNullOrEmpty(p.getName())) {
				editor.getList().remove(i);
			} else {
				if (Utils.isNullOrEmpty(p.getValue())) {
					delegate.recordError("Value cannot be empty or null for property '" + p.getName() + "'!", p.getName(), p);
				}
				++i;
			}
		}
		editor.flush();
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(Properties value) {
	}
}
