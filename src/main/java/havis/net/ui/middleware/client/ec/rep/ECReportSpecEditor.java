package havis.net.ui.middleware.client.ec.rep;

import havis.middleware.ale.service.ECFilterListMember;
import havis.middleware.ale.service.ec.ECReportSetSpec;
import havis.middleware.ale.service.ec.ECReportSpec;
import havis.net.ui.middleware.client.ec.rep.group.ECGroupSpecEditor;
import havis.net.ui.middleware.client.ec.rep.output.OutputSpecEditor;
import havis.net.ui.middleware.client.shared.CustomListRenderer;
import havis.net.ui.middleware.client.shared.report.list.MultipleColumnListWidget;
import havis.net.ui.middleware.client.shared.report.model.ReportSet;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.ConfigurationSections;
import havis.net.ui.shared.client.widgets.CustomListBox;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
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

public class ECReportSpecEditor extends Composite implements ValueAwareEditor<ECReportSpec>{

	private EditorDelegate<ECReportSpec> delegater;
	
	@Path("reportName")
	@UiField TextBox name; 
	
	@Path("reportSet.set")
	@UiField
	CustomListBox<String> reportSet;
	
	@Path("reportIfEmpty")
	@UiField ToggleButton reportIfEmpty;
	
	@Path("reportOnlyOnChange")
	@UiField ToggleButton reportOnChangeOnly;

	@Path("") //'cause of ECReportSpecExtension extension...
	@UiField OutputSpecEditor configSecOutput;
	
	@Path("filterSpec.extension.filterList.filter")
	@UiField (provided=true)
	MultipleColumnListWidget<ECFilterListMember> configSecFilter = new MultipleColumnListWidget<ECFilterListMember>(
		new CustomListRenderer<ECFilterListMember>() {
			
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
	
	
	
	@Path("groupSpec") 
	@UiField ECGroupSpecEditor configSecGroup;

	@Ignore
	@UiField ConfigurationSections reportConfigSections;
	
	private ECReportSpec spec;
	
	private ConstantsResource res = ConstantsResource.INSTANCE;

	
	private static ECReportSpecEditorUiBinder uiBinder = GWT
			.create(ECReportSpecEditorUiBinder.class);

	interface ECReportSpecEditorUiBinder extends UiBinder<Widget, ECReportSpecEditor> {
	}
	
	
	public ECReportSpecEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		
		configSecFilter.setText(res.filter());
		
		List<String> lst = new ArrayList<String>();
		lst.add(res.type());
		lst.add(res.field());
		lst.add(res.patternList());
		configSecFilter.setHeader(lst);
		
		for (ReportSet n : ReportSet.values()) {
			reportSet.addItem(n.getName());
		}
		
		configSecGroup.setText(res.group());
		configSecOutput.setText(res.output());
		
		name.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Utils.removeErrorStyle(name);
			}
		});
	}

	@Override
	public void setDelegate(EditorDelegate<ECReportSpec> delegate) {
		delegater = delegate;
	}

	@Override
	public void setValue(ECReportSpec value) {
		spec = value;
		
		if(spec != null && spec.getReportSet() != null && spec.getReportSet().getSet() != null){
			reportSet.setValue(spec.getReportSet().getSet());
		}
	}
	
	
	@Override
	public void flush() {
		if(Utils.isNullOrEmpty(name.getText())){
			delegater.recordError(res.errorInvalidEmptyField("Name"), null ,name);
			Utils.addErrorStyle(name);
			return;
		}
		
		if(reportSet.getSelectedIndex() < 1){
			delegater.recordError(res.errorSetNotSelected(), null , reportSet);
			Utils.addErrorStyle(reportSet);
			return;
		}
		
		if(spec.getReportSet() == null){
			spec.setReportSet(new ECReportSetSpec());
			spec.getReportSet().setSet(reportSet.getSelectedValue());
		}
		
	}

	@Override
	public void onPropertyChange(String... paths) {
		for(String s : paths){
			GWT.log(s + " has changed.");
		}
	}
	
	@UiHandler("reportSet")
	void onReportSetChange(ChangeEvent event) {
		Utils.removeErrorStyle(reportSet);
	}
	
	
	public ConfigurationSections getConfigSections(){
		return reportConfigSections;
	}
}
