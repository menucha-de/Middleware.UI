package havis.net.ui.middleware.client.ds.association;

import havis.middleware.ale.service.cc.AssocTableEntry;
import havis.middleware.ale.service.mc.MCAssociationSpec;
import havis.net.ui.middleware.client.shared.CustomListRenderer;
import havis.net.ui.middleware.client.shared.MCSpecEditor;
import havis.net.ui.middleware.client.shared.report.list.MultipleColumnListWidget;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.tm.data.DataType;
import havis.net.ui.middleware.client.tm.data.Format;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.ConfigurationSections;
import havis.net.ui.shared.client.widgets.CustomListBox;
import havis.net.ui.shared.client.widgets.CustomRenderer;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class AssociationEditor extends Composite implements MCSpecEditor<MCAssociationSpec> {

	private EditorDelegate<MCAssociationSpec> delegate;

	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	@UiField
	TextBox name;

	@UiField
	ToggleButton enable;

	@Ignore
	@UiField
	ConfigurationSections assEntrySections;

	@Path("entries.entries.entry")
	@UiField(provided = true)
	MultipleColumnListWidget<AssocTableEntry> configSecEntries = new MultipleColumnListWidget<AssocTableEntry>(
			new CustomListRenderer<AssocTableEntry>() {
				@Override
				public List<String> render(AssocTableEntry value) {
					if (value == null)
						return null;

					return Arrays.asList(value.getKey(), value.getValue());
				}
			}, 2);

	@Path("spec.datatype")
	@UiField(provided = true)
	CustomListBox<String> datatype = new CustomListBox<String>(new CustomRenderer<String>() {

		@Override
		public String render(String value) {
			if(value != null){
				DataType d = DataType.getDataType(value);
				if(DataType.ISO_15962_STRING.equals(d)){
					return "iso";
				} else {
					return d.getDatatype();
				}				
			}
			return null;
		}
	});

	@Path("spec.format")
	@UiField
	CustomListBox<String> format;

	private static AssociationEditorUiBinder uiBinder = GWT.create(AssociationEditorUiBinder.class);

	interface AssociationEditorUiBinder extends UiBinder<Widget, AssociationEditor> {
	}

	public AssociationEditor() {

		configSecEntries.setHeader(Arrays.asList(res.epc(), res.value()));		

		initWidget(uiBinder.createAndBindUi(this));
		
		for(DataType d : DataType.values()){
			datatype.addItem(d.getDatatype());
		}

		name.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Utils.removeErrorStyle(name);
			}
		});
	}

	@Override
	public void setDelegate(EditorDelegate<MCAssociationSpec> delegate) {
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
			
			if (Utils.isNullOrEmpty(datatype.getSelectedValue())) {
				delegate.recordError(res.errorNotSpecified("Datatype"), null, datatype);
				Utils.addErrorStyle(datatype);
				return;
			}
			Utils.removeErrorStyle(datatype);
			
			if (!Utils.isNullOrEmpty(datatype.getSelectedValue()) && Utils.isNullOrEmpty(format.getSelectedValue())) {
				delegate.recordError(res.errorNotSpecified("Format"), null, format);
				Utils.addErrorStyle(format);
				return;
			}
			Utils.removeErrorStyle(format);

			if (configSecEntries.getCustomWidgetCount() < 1) {
				delegate.recordError(res.errorNotSpecified(res.entries()), null, format);
				configSecEntries.setOpen(true);

				Utils.addErrorStyle(configSecEntries.getTable());
				return;
			}
			Utils.removeErrorStyle(configSecEntries.getTable());
		}
	}

	@UiHandler("datatype")
	void onDataTypeChange(ChangeEvent event) {
		Utils.removeErrorStyle(datatype);
		format.clear();
		if (datatype.getValue() != null) {
			DataType d = DataType.getDataType(datatype.getValue());
			if(d != null){
				for (Format f : d.getValidFormats()) {
					format.addItem(f.getFormat());
				}
			}			
		}
	}

	@UiHandler("format")
	void onFormatChange(ChangeEvent event) {
		Utils.removeErrorStyle(format);
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(MCAssociationSpec value) {
	}

	public ConfigurationSections getConfigSections() {
		return assEntrySections;
	}

	@Override
	public ToggleButton enable() {
		return enable;
	}
}
