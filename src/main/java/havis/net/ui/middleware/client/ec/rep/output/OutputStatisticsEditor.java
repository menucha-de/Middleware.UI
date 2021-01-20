package havis.net.ui.middleware.client.ec.rep.output;

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

public class OutputStatisticsEditor extends ConfigurationSection implements ValueAwareEditor<List<String>>{

	private static final String READER_SIGHTING_SIGNALS = "ReaderSightingSignals";
	private static final String READER_NAMES = "ReaderNames";
	private static final String TAG_COUNT = "TagCount";
	private static final String TAG_TIMESTAMPS = "TagTimestamps";

	@UiField
	@Ignore
	ToggleButton tagTimestamps;

	@UiField
	@Ignore
	ToggleButton tagCount;
	
	@UiField
	@Ignore
	ToggleButton readerNames;
	
	@UiField
	@Ignore
	ToggleButton readerSightingSignals;
	
	private List<String> values;
	
	private ConstantsResource res = ConstantsResource.INSTANCE;

	private static OutputStatisticsEditorUiBinder uiBinder = GWT
			.create(OutputStatisticsEditorUiBinder.class);

	interface OutputStatisticsEditorUiBinder extends UiBinder<Widget, OutputStatisticsEditor> {
	}
	
	public OutputStatisticsEditor() {
		setText(res.statistics());
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setValue(List<String> value) {
		values = value;
		tagTimestamps.setValue(false);
		tagCount.setValue(false);
		readerNames.setValue(false);
		readerSightingSignals.setValue(false);
		if(values != null){
			for (String name : values) {
				if (name.equals(TAG_TIMESTAMPS))
					tagTimestamps.setValue(true);
				else if (name.equals(TAG_COUNT))
					tagCount.setValue(true);
				else if (name.equals(READER_NAMES))
					readerNames.setValue(true);
				else if (name.equals(READER_SIGHTING_SIGNALS))
					readerSightingSignals.setValue(true);
			}
		}
	}

	@Override
	public void flush() {
		if(values != null){
			values.clear();
		}
		if (tagTimestamps.getValue()){
			if(values == null)values = new ArrayList<String>();
			values.add(TAG_TIMESTAMPS);
		}
		
		if (tagCount.getValue()){
			if(values == null)values = new ArrayList<String>();
			values.add(TAG_COUNT);
		}
			
		if (readerNames.getValue()){
			if(values == null)values = new ArrayList<String>();
			values.add(READER_NAMES);
		}
			
		if (readerSightingSignals.getValue()){
			if(values == null)values = new ArrayList<String>();
			values.add(READER_SIGHTING_SIGNALS);
		}
	}

	@Override
	public void setDelegate(EditorDelegate<List<String>> delegate) {}

	@Override
	public void onPropertyChange(String... paths) {}
}
