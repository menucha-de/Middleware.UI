package havis.net.ui.middleware.client.pc.rep;

import havis.middleware.ale.service.ECFilterListMember;
import havis.middleware.ale.service.pc.PCOpSpec;
import havis.middleware.ale.service.pc.PCReportSpec;
import havis.net.ui.middleware.client.pc.rep.stat.StatProfileNamesEditor;
import havis.net.ui.middleware.client.shared.CustomListRenderer;
import havis.net.ui.middleware.client.shared.report.list.MultipleColumnListWidget;
import havis.net.ui.middleware.client.shared.report.list.SingleColumnListWidget;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.ConfigurationSections;
import havis.net.ui.shared.client.widgets.CustomRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class PCReportSpecEditor extends Composite implements ValueAwareEditor<PCReportSpec>{

	private EditorDelegate<PCReportSpec> delegater;
	
	@Path("name")
	@UiField TextBox name; 
	
	@Path("reportIfEmpty")
	@UiField ToggleButton reportIfEmpty;
	
	@Path("filterSpec.filterList.filter")
	@UiField (provided=true)
	MultipleColumnListWidget<ECFilterListMember> configSecFilter = new MultipleColumnListWidget<ECFilterListMember>(new CustomListRenderer<ECFilterListMember>() {
		
		@Override
		public List<String> render(ECFilterListMember value) {
			
			if(value == null) return null;
			
			List<String> lst = new ArrayList<String>();
			
			lst.add(Utils.isNullOrEmpty(value.getIncludeExclude())?"":value.getIncludeExclude());
			
			String tmp = "";
			if(value.getFieldspec() != null && !Utils.isNullOrEmpty(value.getFieldspec().getFieldname())){
				tmp = value.getFieldspec().getFieldname();
			} else {
				tmp = "epc";
			}
			lst.add(tmp);		
			tmp = "";
			if(value.getPatList() != null){
				for(String s : value.getPatList().getPat()){
					tmp += (s + "\r\n");
				};
				if(tmp.endsWith("\r\n"))tmp = tmp.substring(0, tmp.length()-2);
			}
			lst.add(tmp);
			return lst;
		}
	
		
	}, 3);
	
	@Path("triggerList.trigger")
	@UiField (provided=true)
	SingleColumnListWidget<String> configSecTrigger = new SingleColumnListWidget<String>(new CustomRenderer<String>() {
	
		@Override
		public String render(String value) {
			return value;
		}
	});
	
	@Path("opSpecs.opSpec")
	@UiField (provided=true)
	SingleColumnListWidget<havis.middleware.ale.service.pc.PCOpSpec> configSecOperation = new SingleColumnListWidget<havis.middleware.ale.service.pc.PCOpSpec>(new CustomRenderer<havis.middleware.ale.service.pc.PCOpSpec>() {
	
		@Override
		public String render(PCOpSpec value) {
			return value.getOpName();
		}
	});
	
	
	@Path("statProfileNames.statProfileName")
	@UiField
	StatProfileNamesEditor configStatistics;
	
	
	@Ignore
	@UiField ConfigurationSections reportConfigSections;
	
	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	private PCReportSpec pcReportSpec;

	
	private static PCReportSpecItemEditorUiBinder uiBinder = GWT
			.create(PCReportSpecItemEditorUiBinder.class);

	interface PCReportSpecItemEditorUiBinder extends UiBinder<Widget, PCReportSpecEditor> {
	}	
	
	
	public PCReportSpecEditor() {
		
		configSecFilter.setText(res.filter());
		
		List<String> lst = new ArrayList<String>();
		lst.add(res.type());
		lst.add(res.field());
		lst.add(res.patternList());
		configSecFilter.setHeader(lst);
		
		configSecTrigger.setText(res.trigger());
		configSecOperation.setText(res.operation());
		
		initWidget(uiBinder.createAndBindUi(this));
		
		configSecTrigger.setHeader(Arrays.asList(res.createItem(res.trigger())));
		configSecOperation.setHeader(Arrays.asList(res.createItem(res.operation())));
		
		name.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Utils.removeErrorStyle(name);
			}
		});
	}
	
	@Override
	public void flush() {
		
		if (configSecOperation.isDirty()) {
			pcReportSpec.getOpSpecs().getOpSpec().clear();
			for (PCOpSpec spec : configSecOperation.getEditorList()) {
				pcReportSpec.getOpSpecs().getOpSpec().add(spec);
			}
		}
		
		if(Utils.isNullOrEmpty(name.getText())){
			delegater.recordError(res.errorInvalidEmptyField("Name"), null ,name);
			Utils.addErrorStyle(name);
			return;
		}
		
		if(pcReportSpec.getOpSpecs() == null || pcReportSpec.getOpSpecs().getOpSpec().isEmpty() && 
				(pcReportSpec.getFilterSpec() == null || pcReportSpec.getFilterSpec().getFilterList() == null ||
					pcReportSpec.getFilterSpec().getFilterList().getFilter().isEmpty())){
				
			delegater.recordError(res.errorNoXSpecified(res.operations()), null, pcReportSpec);
				return;
		}
	}

	@Override
	public void setValue(PCReportSpec value) {
		pcReportSpec = value;
	}
	
	
	@Override
	public void setDelegate(EditorDelegate<PCReportSpec> delegate) {
		delegater = delegate;
	}
	
	@Override
	public void onPropertyChange(String... paths) { }
	
	
	public ConfigurationSections getConfigSections(){
		return reportConfigSections;
	}
}
