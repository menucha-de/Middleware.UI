package havis.net.ui.middleware.client.ec.rep.output;

import havis.middleware.ale.service.ec.ECReportOutputFieldSpec;
import havis.middleware.ale.service.ec.ECReportOutputSpec;
import havis.middleware.ale.service.ec.ECReportOutputSpecExtension;
import havis.middleware.ale.service.ec.ECReportSpec;
import havis.middleware.ale.service.ec.ECReportSpecExtension;
import havis.net.ui.middleware.client.shared.report.list.SingleColumnListWidget;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.shared.client.ConfigurationSection;
import havis.net.ui.shared.client.ConfigurationSections;
import havis.net.ui.shared.client.widgets.CustomRenderer;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class OutputSpecEditor extends ConfigurationSection implements ValueAwareEditor<ECReportSpec>{
	
	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	@Path("output")
	@UiField
	OutputIncludeEditor configSecOutputInclude;
	
	
	@Path("output.extension.fieldList.field")
	@UiField(provided = true)
	public
	SingleColumnListWidget<ECReportOutputFieldSpec> configSecOutputFields = new SingleColumnListWidget<ECReportOutputFieldSpec>(
		new CustomRenderer<ECReportOutputFieldSpec>() {

			@Override
			public String render(ECReportOutputFieldSpec value) {
				String name = value.getName();
				if (name != null && !name.isEmpty()) {
					return name;
				}
				return value.getFieldspec().getFieldname();
			}
		});

	@Path("extension.statProfileNames.statProfileName")
	@UiField
	OutputStatisticsEditor configSecOutputStatistics;
	
	@Ignore
	@UiField
	ConfigurationSections reportConfigSections;
	
	
	private static OutputSpecEditorUiBinder uiBinder = GWT
			.create(OutputSpecEditorUiBinder.class);

	interface OutputSpecEditorUiBinder extends UiBinder<Widget, OutputSpecEditor> {
	}

	private ECReportSpec spec;
	
	
	public OutputSpecEditor() {
		
		configSecOutputFields.setText(res.field());
		configSecOutputFields.setHeader(Arrays.asList(res.createItem(res.field())));
		
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setDelegate(EditorDelegate<ECReportSpec> delegate) {
	}

	@Override
	public void flush() {
		if(spec != null){
			if(spec.getExtension() != null){
				if(spec.getExtension().getStatProfileNames() == null ||
					spec.getExtension().getStatProfileNames().getStatProfileName().isEmpty()) {
					spec.setExtension(null);
				}
			}
			
			ECReportOutputSpec ros = spec.getOutput();
			if(ros != null){
				if(ros.getExtension() != null){
					if (ros.getExtension().getFieldList().getField().isEmpty()) {
						ros.setExtension(null);
					}
				}
				
				if (!ros.isIncludeEPC()
						&& !ros.isIncludeTag()
						&& !ros.isIncludeRawHex()
						&& !ros.isIncludeRawDecimal()
						&& !ros.isIncludeCount()
						&& ros.getExtension() == null) {
					spec.setOutput(null);
				}
			}
		}
	}

	@Override
	public void setValue(ECReportSpec value) {
		spec = value;
		
		ECReportOutputSpec ros = spec.getOutput();
		if (ros == null) {
			ros = new ECReportOutputSpec();
			spec.setOutput(ros);
		}

		ECReportOutputSpecExtension rose = ros.getExtension();
		if (rose == null) {
			rose = new ECReportOutputSpecExtension();
			ros.setExtension(rose);
		}
		
		if (rose.getFieldList() == null) {
			rose.setFieldList(new ECReportOutputSpecExtension.FieldList());
		}
		
		ECReportSpecExtension rse = spec.getExtension();
		if (rse == null) {
			rse = new ECReportSpecExtension();
			spec.setExtension(rse);
		}
		
		if (rse.getStatProfileNames() == null) {
			rse.setStatProfileNames(new ECReportSpecExtension.StatProfileNames());
		}
	}
	
	@Override
	public void onPropertyChange(String... paths) { }
	
	
	public ConfigurationSections getConfigSections(){
		return reportConfigSections;
	}
}
