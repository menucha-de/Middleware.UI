package havis.net.ui.middleware.client.shared.report.model;

import havis.net.ui.middleware.client.utils.Utils;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;



public class PatternModel {
	//TODO regEx may have to be extended...
	private final static String pattern = "([a-z]+):([a-z-]+):([a-z]+):([a-z0-9-]+):([0-9*]+).([0-9*]+).{0,1}([0-9*xX]*).{0,1}([0-9*-\\[\\]]*)";
	private final static String numberPattern = "([0-9xX])([a-fA-F0-9]+)";
	
	private String urn;
	private String epc;
	private String id;
	private String scheme;
	private String component1;
	private String component2;
	private String component3;
	private String component4;
	
	private String length;
	
//	The EPC URI is a string having the following form:
//	urn:epc:id:scheme:component1.component2.component3.
	
	private final static int URI_URN = 1;
	private final static int URI_EPC = 2;
	private final static int URI_ID = 3;
	private final static int URI_SCHEME = 4;
	private final static int URI_CMP1 = 5;
	private final static int URI_CMP2 = 6;
	private final static int URI_CMP3 = 7;
	private final static int URI_CMP4 = 8;
	
	protected PatternModel() {
		
	}
	
	public PatternModel(String uri) {
		//e.g. uri: "urn:epc:pat:sgtin-96:3.123456789.*.[0-100]"
		
		RegExp regExp = RegExp.compile(pattern);
		MatchResult result0 = regExp.exec(uri);
		
		if (result0 != null) {
			//GWT.log("### PatternModel: " + uri);
			if(result0.getGroupCount() > URI_URN)
				this.urn = result0.getGroup(URI_URN);
			if(result0.getGroupCount() > URI_EPC)
				this.epc = result0.getGroup(URI_EPC);
			if(result0.getGroupCount() > URI_ID)
				this.id = result0.getGroup(URI_ID);
			
			if(result0.getGroupCount() > URI_SCHEME){
				this.scheme = result0.getGroup(URI_SCHEME);
				if(scheme.contains("-")){
					String[] tmp = scheme.split("-");
					if (tmp.length > 1) {
						scheme = tmp[0];
						length = tmp[1];
					}
					else{
						length = "";
					}
				}
				if(scheme.startsWith("new")){
					scheme = "";
				}
			}
			if(result0.getGroupCount() > URI_CMP1)
				this.component1 = result0.getGroup(URI_CMP1);
			if(result0.getGroupCount() > URI_CMP2)
				this.component2 = result0.getGroup(URI_CMP2);
			if(result0.getGroupCount() > URI_CMP3)
				this.component3 = result0.getGroup(URI_CMP3);
			if(result0.getGroupCount() > URI_CMP4)
				this.component4 = result0.getGroup(URI_CMP4);
			
			
			if(uri.startsWith("urn:epc:idpat:sscc")){
				component3 = component2;
				component2 = component1;
				component1 = "";
			}
			
			return;
		}
		
		urn = "urn";
		id = "pat";
		scheme = "";
		component2 = "";
		component3 = "";
		component4 = "";
		
		RegExp regExp2 = RegExp.compile(numberPattern);
		MatchResult result2 = regExp2.exec(uri);
		if(result2  != null){
			if(uri.startsWith("x") || uri.startsWith("X")){
				epc = "hex";
			}
			else{
				epc = "decimal";
			}
			component1 = uri;
		}
		else{
			epc = "epc";
			component1 = "";
		}
		
	}
	
	
	public String getUrn() {
		return urn;
	}
	
	public String getEpc() {
		return epc;
	}
	
	public String getId() {
		return id;
	}
	
	public String getScheme() {
		return scheme;
	}
	
	public SchemeType getSchemeBox() {
		return SchemeType.getValue(scheme);
	}
	
	public String getLength() {
		return length;
	}
	
	public String getComponent1() {
		return component1;
	}
	
	public String getComponent2() {
		return component2;
	}
	
	public String getComponent3() {
		return component3;
	}
	
	public String getComponent4() {
		return component4;
	}
	
	public void setUrn(String value) {
		urn = value;
	}
	
	public void setEpc(String value) {
		epc = value;
	}
	
	public void setId(String value) {
		id = value;
	}
	
	public void setScheme(String value) {
		scheme = value;
	}
	
	public void setLength(String value) {
		length = value;
	}
	
	public void setComponent1(String value) {
		component1 = value;
	}
	
	public void setComponent2(String value) {
		component2 = value;
	}
	
	public void setComponent3(String value) {
		component3 = value;
	}
	
	public void setComponent4(String value) {
		component4 = value;
	}
	
	public String toUri(){
		
		if(Utils.isNullOrEmpty(scheme)){
			return component1;
		}
		
		
		String res = urn + ":" +  epc + ":";
		//e.g. uri: "urn:epc:pat:sgtin-96:3.123456789.*.[0-100]"
		String tmpScheme = (length != null && length.length() > 0) ? (scheme + "-" + length) : scheme;
		
		res += (id + ":");
		res += (tmpScheme + ":");
		
		
		if(!Utils.isNullOrEmpty(component1)){
			res += component1;
		}
		
		if(!Utils.isNullOrEmpty(component2)){
			if(!res.endsWith(".") && !res.endsWith(":"))res += (".");
			res += component2;
		}
		
		if(!Utils.isNullOrEmpty(component3)){
			if(!res.endsWith(".") && !res.endsWith(":"))res += (".");
			res += component3;
		}
			
		if(!Utils.isNullOrEmpty(component4)){
			if(!res.endsWith(".") && !res.endsWith(":"))res += (".");
			res += component4;
		}
			
		return  res;
	}
	
}
