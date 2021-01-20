package havis.net.ui.middleware.client.shared.pattern;

import havis.net.ui.middleware.client.shared.report.model.PatternLengthType;
import havis.net.ui.middleware.client.shared.report.model.PatternModel;
import havis.net.ui.middleware.client.shared.report.model.PatternType;
import havis.net.ui.middleware.client.shared.report.model.SchemeType;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.widgets.CustomListBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;


public class PatternItemEditor extends Composite implements ValueAwareEditor<PatternModel> {
	
	private enum ViewTypes{
		VIEW_EPC, VIEW_Uint;
	}
	
	private EditorDelegate<PatternModel> delegater;
	
	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	@Ignore
	@UiField
	CustomListBox<PatternType> patternType;
	
	@Ignore
	@UiField
	Label uintValueLabel;
	
	@Ignore
	@UiField
	TextBox valueBox;
	
	@UiField
	CustomListBox<SchemeType> schemeBox;
	
	
	@Path("scheme")
	@UiField
	TextBox scheme;
	
	@Path("length")
	@UiField
	CustomListBox<String> length;
	
	
	@Ignore
	@UiField
	Label schemeLabel;
	
	@Ignore
	@UiField
	Label lengthLabel;
	
	@Ignore
	@UiField
	Label filterLabel;
	
	
	@Ignore
	@UiField
	Label component2Label;
	
	@Ignore
	@UiField
	Label component3Label;
	
	@Ignore
	@UiField
	Label component4Label;
		
	
	@Path("component1")
	@UiField
	TextBox filterBox;
	
	@Path("component2")
	@UiField
	TextBox component2Box;
		
	@Path("component3")
	@UiField
	TextBox component3Box;
	
	@Path("component4")
	@UiField
	TextBox component4Box;
	
	private PatternModel patternModel;
	
	private static PatternItemEditorUiBinder uiBinder = GWT.create(PatternItemEditorUiBinder.class);
	interface PatternItemEditorUiBinder extends UiBinder<Widget, PatternItemEditor> { }
	
	
	@UiConstructor
	@Inject
	public PatternItemEditor(final EventBus eventBus) {
		
		initWidget(uiBinder.createAndBindUi(this));
		initializeListBoxes();
		setEmptyView();
	}
	
	private ChangeHandler changeHandler = new ChangeHandler() {
		@Override
		public void onChange(ChangeEvent event) {
			TextBox txt = (TextBox)event.getSource();
			Utils.removeErrorStyle(txt);
		}
	};
	
	/**
	 * initializeListBoxes
	 */
	private void initializeListBoxes(){
		
		for(PatternType pt : PatternType.values()){
			patternType.addItem(pt);
		}
		
		patternType.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				typeSelectionChanged(patternType.getSelectedValue());
			}
		});
		
		schemeBox.clear();
		for(SchemeType st : SchemeType.values()){
			schemeBox.addItem(st);
		}
		schemeBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {

				schemeSelectionChanged(schemeBox.getSelectedValue());
				Utils.removeErrorStyle(schemeBox);
			}
		});
		
		length.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				Utils.removeErrorStyle(length);
			}
		});
		
		filterBox.addChangeHandler(changeHandler);
		component2Box.addChangeHandler(changeHandler);
		component3Box.addChangeHandler(changeHandler);
		component4Box.addChangeHandler(changeHandler);
	}

	/**
	 * Sets all the values which could not be set via annotation
	 */
	@Override
	public void setValue(final PatternModel value) {
		if(value == null){
			return;
		}
				
		patternModel = value;
		PatternType pt = PatternType.EPC_TAG;
		String epc = value.getEpc();
		
		if(value.getUrn().startsWith("new")){
			
			value.setScheme("");
			
			if(epc.startsWith(PatternType.EPC_PURE.getScheme()) ){
				pt = PatternType.EPC_PURE;
			}
			else if(epc.startsWith(PatternType.EPC_TAG.getScheme()) ){
				pt = PatternType.EPC_TAG;
			}
			else if(epc.startsWith(PatternType.INT_DEC.getScheme()) ){
				pt = PatternType.INT_DEC;
			}
			else if(epc.startsWith(PatternType.INT_HEX.getScheme()) ){
				pt = PatternType.INT_HEX;
			}
			else{
				return;
			}
			
		}
		else{
			
			if(Utils.isNullOrEmpty(patternModel.getScheme())){
				if(epc.startsWith(PatternType.INT_DEC.getScheme()) ){
					pt = PatternType.INT_DEC;
				}
				else if(epc.startsWith(PatternType.INT_HEX.getScheme()) ){
					pt = PatternType.INT_HEX;
				}
				valueBox.setText(patternModel.getComponent1());
			}
			else{
				int idx = SchemeType.getIndex(value.getScheme());
				GWT.log("SchemeType.getIndex: " + idx);
				schemeBox.setSelectedIndex(idx+1);
				schemeSelectionChanged(schemeBox.getSelectedValue());
				
				pt = PatternType.EPC_PURE;
				if(value.getLength() != null && value.getLength().length() > 0){
					//GWT.log("PatternLengthType.getIndex: " + idx1);
					//GWT.log("value.getLength(): " + value.getLength());
					length.setEnabled(true);
					length.setSelectedValue(value.getLength());
					
					Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
						@Override
						public void execute() {
							length.setSelectedValue(value.getLength());
						}
					});
					
					pt = PatternType.EPC_TAG;
				}
			}
			
		}
		patternType.setSelectedValue(pt);
		typeSelectionChanged(patternType.getSelectedValue());
	}

	@Override
	public void flush() {
		
		PatternType pt = patternType.getSelectedValue();
		updatePatternModel(pt);
		
		if(pt == PatternType.EPC_PURE || pt == PatternType.EPC_TAG){
			if (schemeBox.getSelectedIndex() < 1){
				delegater.recordError(res.errorNotSpecified(res.scheme()), null ,schemeBox);
				Utils.addErrorStyle(schemeBox);
				return;
			}
			Utils.removeErrorStyle(schemeBox);
			
			if (length.isEnabled() && length.getSelectedIndex() < 1){
				delegater.recordError(res.errorNotSpecified(res.length()), null ,length);
				Utils.addErrorStyle(length);
				return;
			}
			Utils.removeErrorStyle(length);
		}
		else{
			if(Utils.isNullOrEmpty(valueBox.getText())){
				delegater.recordError(res.errorInvalidEmptyField(res.value()), null ,valueBox);
				Utils.addErrorStyle(valueBox);
				return;
			}
			
			if(pt == PatternType.INT_HEX){
				String hexNumberPattern = "([xX])([a-fA-F0-9]+)";
				RegExp regExp = RegExp.compile(hexNumberPattern);
				MatchResult result = regExp.exec(valueBox.getText());
				if(result == null){
					delegater.recordError(res.errorNoHexValue(res.value()), null ,valueBox);
					Utils.addErrorStyle(valueBox);
					return;
				}
					
			}
			
			if(pt == PatternType.INT_DEC){
				String decNumberPattern = "([0-9]+)";
				RegExp regExp = RegExp.compile(decNumberPattern);
				MatchResult result = regExp.exec(valueBox.getText());
				if(result == null){
					delegater.recordError(res.errorNotANumber(res.value()), null ,valueBox);
					Utils.addErrorStyle(valueBox);
					return;
				}
					
			}
			Utils.removeErrorStyle(valueBox);
		}
		
		if(patternType.getSelectedValue() != PatternType.EPC_PURE){
			if(filterBox.isVisible() && filterBox.isEnabled()){
				if (Utils.isNullOrEmpty(filterBox.getText())){
					delegater.recordError(res.errorInvalidEmptyField(res.filter()), null ,filterBox);
					Utils.addErrorStyle(filterBox);
					return;
				}
				
				if(!isContentValid(filterBox)){
					delegater.recordError(res.errorInvalid(res.filter()), null ,filterBox);
					Utils.addErrorStyle(filterBox);
					return;
				}
			}
		}
		Utils.removeErrorStyle(filterBox);
		
		
		if(component2Box.isVisible()){
			if (Utils.isNullOrEmpty(component2Box.getText())){
				delegater.recordError(res.errorInvalidEmptyField(component2Label.getText()), null ,component2Box);
				Utils.addErrorStyle(component2Box);
				return;
			}
			if(!isContentValid(component2Box)){
				delegater.recordError(res.errorInvalid(res.companyPrefix()), null ,component2Box);
				Utils.addErrorStyle(component2Box);
				return;
			}
		}
		Utils.removeErrorStyle(component2Box);
		
		
		if(component3Box.isVisible()){
			if (Utils.isNullOrEmpty(component3Box.getText()) ){
				delegater.recordError(res.errorInvalidEmptyField(component3Label.getText()), null ,component3Box);
				Utils.addErrorStyle(component3Box);
				return;
			}
			if(!isContentValid(component3Box)){
				delegater.recordError(res.errorInvalid(res.itemReference()), null ,component3Box);
				Utils.addErrorStyle(component3Box);
				return;
			}
		}
		Utils.removeErrorStyle(component3Box);
		
		if(component4Box.isVisible()){
			if (Utils.isNullOrEmpty(component4Box.getText()) ){
				delegater.recordError(res.errorInvalidEmptyField(component4Label.getText()), null ,component4Box);
				Utils.addErrorStyle(component4Box);
				return;
			}
			if(!isContentValid(component4Box) ){
				delegater.recordError(res.errorInvalid(res.serialNumber()), null ,component4Box);
				Utils.addErrorStyle(component4Box);
				return;
			}
			Utils.removeErrorStyle(component4Box);
		}
	}
	
	private boolean isContentValid(TextBox tbox){
		if(tbox.getText().equals("*")) return true;
		if(tbox.getText().toUpperCase().equals("X")) return true;
		
		String commonNumberPattern = "(^[\\[\\]0-9-]+$)";
		RegExp regExp = RegExp.compile(commonNumberPattern);
		MatchResult result = regExp.exec(tbox.getText());
		return(result != null);
	}

	@Override
	public void onPropertyChange(String... paths) { }
	
	
	@Override
	public void setDelegate(EditorDelegate<PatternModel> delegate) {
		delegater = delegate;
	}
	
	/**
	 * updates the PatternModel
	 */
	private void updatePatternModel(PatternType pt){
		
		if(patternModel == null) //should not happen, but anyway...
			patternModel = new PatternModel("");
		
		if(patternModel.getUrn().startsWith("new")){
			patternModel.setUrn("urn");
		}
		
		switch(pt){
			case EPC_PURE:
				patternModel.setEpc("epc");
				patternModel.setId("idpat");
				patternModel.setComponent1("");
				String currScheme = "";
				if(schemeBox.getValue() != null){
					currScheme = schemeBox.getValue().getName();
				}
				patternModel.setScheme(currScheme);
				if(currScheme.startsWith("sscc") || currScheme.startsWith("giai") || currScheme.startsWith("gsrn")){
					component4Box.setText("");
				}
				break;
			case EPC_TAG:
				patternModel.setEpc("epc");
				patternModel.setId("pat");
				patternModel.setComponent1(filterBox.getValue());
				patternModel.setLength(length.getSelectedValue());
				break;
			case INT_DEC:
			case INT_HEX:
				//if scheme is empty we generate numbers only and put them to component1
				patternModel.setComponent1(valueBox.getText());
				patternModel.setComponent2("");
				patternModel.setComponent3("");
				patternModel.setComponent4("");
				return;
		}
		
		patternModel.setComponent2(component2Box.isVisible()?component2Box.getText():"");
		patternModel.setComponent3(component3Box.isVisible()?component3Box.getText():"");
		patternModel.setComponent4(component4Box.isVisible()?component4Box.getText():"");
	}
	
	/**
	 * Actions on patternType selection changed
	 * @param patternType
	 */
	private void typeSelectionChanged(PatternType patternType)
    {
		 if (patternType != null)
         {
			 switch (patternType)
             {
                 case EPC_PURE:
                	 showViewType(ViewTypes.VIEW_EPC);
                	 length.setEnabled(false);
                     break;
                 case EPC_TAG:
                	 showViewType(ViewTypes.VIEW_EPC);
                     length.setEnabled(true);
                     break;
                 case INT_HEX:
                	 showViewType(ViewTypes.VIEW_Uint);
                     break;
                 case INT_DEC:
                	 showViewType(ViewTypes.VIEW_Uint);
                     break;
				default:
					break;
             }


         }
		 length.setSelectedIndex(-1);
     }

	private void schemeSelectionChanged(SchemeType st){
		 
		length.clear();
		
		if(st == null){
			setComponentCtrlsVisibility(false);
			setFilterEnableCondition(false);
			return;
		} 
		scheme.setText(st.getName());
				 
		component2Label.setText(res.companyPrefix());
		
		setComponentCtrlsVisibility(true);
		setFilterEnableCondition(true);
		
		length.addItem(PatternLengthType.Length_96.toString());
		
		fillWithAsterisks();
		
		switch(st){
			case SGTIN:
				component3Label.setText(res.itemReference());
				component4Label.setText(res.serialNumber());
				
				length.addItem(PatternLengthType.Length_198.toString());
				break;
			case SSCC:
				component3Label.setText(res.serialReference());
				component4Label.setVisible(false);
				component4Box.setVisible(false);
				break;
			case SGLN:
				component3Label.setText(res.locationReference());
				component4Label.setText(res.extension());
				break;
			case GRAI:
				component3Label.setText(res.assetType());
				component4Label.setText(res.serialNumber());
				
				length.addItem(PatternLengthType.Length_170.toString());
				break;
			case GIAI:
				component3Label.setText(res.individualAssetRef());
				component4Label.setVisible(false);
				component4Box.setVisible(false);
				
				length.addItem(PatternLengthType.Length_202.toString());
				break;
			case GSRN:
				component3Label.setText(res.serialReference());
				component4Label.setVisible(false);
				component4Box.setVisible(false);
				
				break;
			case GDTI:
				component3Label.setText(res.documentType());
				component4Label.setText(res.serialNumber());
				
				length.addItem(PatternLengthType.Length_113.toString());
				break;
			case GID:
				component2Label.setText(res.managerNumber());
				component3Label.setText(res.objectClass());
				component4Label.setText(res.serialNumber());
				
				setFilterEnableCondition(false);
				break;
		}
		
		
	}
	 
	private void showViewType(ViewTypes viewType){
		
		switch(viewType){
			case VIEW_EPC:
				valueBox.setVisible(false);
				uintValueLabel.setVisible(false);
				schemeBox.setVisible(true);
				length.setVisible(true);
				filterBox.setVisible(true);
				schemeLabel.setVisible(true);
				lengthLabel.setVisible(true);
				filterLabel.setVisible(true);
				break;
			case VIEW_Uint:
				valueBox.setVisible(true);
				uintValueLabel.setVisible(true);
				schemeBox.setVisible(false);
				length.setVisible(false);
				filterBox.setVisible(false);
				schemeLabel.setVisible(false);
				lengthLabel.setVisible(false);
				filterLabel.setVisible(false);
				break;
		}
		
		boolean schemeSelected = schemeBox.getSelectedValue() != null;
		setComponentCtrlsVisibility(schemeSelected);
		setFilterEnableCondition(schemeSelected);
	}
	
	
	private void setComponentCtrlsVisibility(boolean visible) {
		component2Box.setVisible(visible);
		component3Box.setVisible(visible);
		component4Box.setVisible(visible);
		
		component2Label.setVisible(visible);
		component3Label.setVisible(visible);
		component4Label.setVisible(visible);
	}
	
	private void fillWithAsterisks(){
		component2Box.setText("*");
		component3Box.setText("*");
		component4Box.setText("*");
	}
	
	private void setFilterEnableCondition(boolean enabled){
		filterBox.setEnabled(enabled);
		String s = enabled ? "*" : "";
		filterBox.setText(s);
	}
	
	/**
	 * The first empty view, e.g. if <New> has been selected
	 */
	private void setEmptyView(){
		patternType.setSelectedValue(PatternType.EPC_TAG);
		
		patternType.setEnabled(false);
		uintValueLabel.setVisible(false);
		valueBox.setVisible(false);
		filterLabel.setVisible(true);
		filterBox.setEnabled(false);
		
		component2Label.setVisible(false);
		component2Box.setVisible(false);
		component3Label.setVisible(false);
		component3Box.setVisible(false);
		component4Label.setVisible(false);
		component4Box.setVisible(false);
		
	}

}
