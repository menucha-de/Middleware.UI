package havis.net.ui.middleware.client.pc.rep.stat;

import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.shared.client.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class StatProfileNamesEditor extends ConfigurationSection implements ValueAwareEditor<List<String>>{

	@Ignore
	@UiField ToggleButton eventTimestamps;

	@Ignore
	@UiField ToggleButton eventCount;
	
	@Ignore
	@UiField ToggleButton readerNames;
	
	@Ignore
	@UiField ToggleButton readerSightingSignals;
	
	private List<String> list;
	
	
	private static StatProfileNamesEditorUiBinder uiBinder = GWT
			.create(StatProfileNamesEditorUiBinder.class);

	interface StatProfileNamesEditorUiBinder extends UiBinder<Widget, StatProfileNamesEditor> {
	}
	
	private ConstantsResource res = ConstantsResource.INSTANCE;

	public StatProfileNamesEditor() {
		setText(res.eventStatistics());
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setDelegate(EditorDelegate<List<String>> delegate) { }
	

	@Override
	public void flush() {
		if(list == null)
			list = new ArrayList<String>();
		else
			list.clear();
		
		if(eventTimestamps.getValue()) list.add("EventTimestamps");
		if(eventCount.getValue()) list.add("EventCount");
		if(readerNames.getValue()) list.add("ReaderNames");
		if(readerSightingSignals.getValue()) list.add("ReaderSightingSignals");
	}

	@Override
	public void onPropertyChange(String... paths) { }
	

	@Override
	public void setValue(List<String> value) {
		if(value == null)return;
		list = value;
		eventTimestamps.setValue(value.contains("EventTimestamps"));
		eventCount.setValue(value.contains("EventCount"));
		readerNames.setValue(value.contains("ReaderNames"));
		readerSightingSignals.setValue(value.contains("ReaderSightingSignals"));
	}
}
