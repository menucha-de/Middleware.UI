package havis.net.ui.middleware.client.place.ec;

import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.HasListType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.TriggerListType;
import havis.net.ui.middleware.client.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.History;

public class CommonEditorPlace  extends Place implements HasListType {

	private String prefix = "";
	private ListType listType = ListType.EC;
	private EditorType editorType = EditorType.EC;
	private TriggerListType triggerListType = null;
	private List<String> path;
	//private boolean isNew;
	private List<String> openWidgetIds;
	
	
	/**
	 * c'tor used by updateOpenWidgetId()
	 * @param splittedToken
	 */
	public CommonEditorPlace(String[] splittedToken){
		int ltIndex = splittedToken.length-4;
		int etIndex = splittedToken.length-3;
		int pathIndex = splittedToken.length-2;
		int owdgIndex = splittedToken.length-1;
		
//		splittedToken[0]:"repSpecItem"
//		1:"EC"
//		2:"EC"
//		3:"10c92856-7816-47e8-a696-a4213cf18744,1"
//		4:"4,0"
		
		listType = ListType.valueOf(splittedToken[ltIndex]);
		
		String[] splEditorTypeItems = splittedToken[etIndex].split("-");
		
		
		editorType = EditorType.valueOf(splEditorTypeItems[0]);
		if(splEditorTypeItems.length > 1){
			triggerListType = TriggerListType.valueOf(splEditorTypeItems[1]);
		}
		
		
		
		if(splittedToken.length == 5){
			prefix = splittedToken[0];
		}
		
		path = new ArrayList<String>();
		
		String[] splPathItems = splittedToken[pathIndex].split(",");
		for(String s: splPathItems){
			path.add(s);
		}
		
		openWidgetIds = new ArrayList<String>();
		String[] splWidgetIdItems = splittedToken[owdgIndex].split(",");
		for(String s: splWidgetIdItems){
			openWidgetIds.add(s);
		}
		
	}
	
	/**
	 * 'go back' constructor
	 * @param lt
	 * @param et
	 * @param pathList
	 * @param openWidgetIdList
	 */
	public CommonEditorPlace(ListType lt, EditorType et, List<String> pathList, List<String> openWidgetIdList){
		listType = lt;
		editorType = et;
		
		if(pathList.size()>0){
			pathList.remove(pathList.size()-1);
		}
		path = new ArrayList<String>(pathList);
		
		
		if(openWidgetIdList.size()>0){
			openWidgetIdList.remove(openWidgetIdList.size()-1);
		}
		openWidgetIds = new ArrayList<String>(openWidgetIdList);
	}
			
	
	
	public CommonEditorPlace(ListType lt, EditorType et, TriggerListType tlt, List<String> pathList, List<String> openWidgetIdList, boolean isNew, int index, String openWidgetId) {
		listType = lt;
		editorType = et;
		triggerListType = tlt;
//		if(triggerListType != null)
//			GWT.log("CommonEditorPlace 3; " + listType.toString() + ", " + editorType.toString() + ", " + triggerListType.toString());
//		else
//			GWT.log("CommonEditorPlace 3; " + listType.toString() + ", " + editorType.toString());
//		
//		int i = 0;
//		for(String s : pathList){
//			GWT.log("pathList[" + i + "]: " + s);
//			i++;
//		}
		
		path = new ArrayList<String>(pathList);
		
		if(isNew){
			index = 0;
			this.path.add("NEW"+new Integer(index).toString());
//			GWT.log("addNewID: " + index);
		}
		else{
			if(index >= 0){
				if(this.path.size() > 1 && this.path.get(1).startsWith("NEW")){
					this.path.set(1, "NEW" + index);
				}
				this.path.add(new Integer(index).toString());
//				GWT.log("addID: " + index);
			}
		}
			
		openWidgetIds = new ArrayList<String>();
		if(openWidgetIdList != null && !openWidgetIdList.isEmpty()){
			for(String s : openWidgetIdList){
				openWidgetIds.add(s);
//				GWT.log("owdADDoldList: " + s);
			}
		}
		openWidgetIds.add(openWidgetId);
//		GWT.log("owdADDnewId: " + openWidgetId);
		
//		i = 0;
//		for(String s : this.path){
//			GWT.log("path[" + i + "]: " + s);
//			i++;
//		}
	}
	
	public CommonEditorPlace(ListType lt, EditorType et, EditorPlace place, boolean isNew, int index, String openWidgetId) {
		listType = lt;
		editorType = et;
		
		path = new ArrayList<String>();
		path.add(place.getSpecId());
		
		if(isNew){
			this.path.add("NEW"+new Integer(index).toString());
		}
		else{
			this.path.add(new Integer(index).toString());
		}
			
		openWidgetIds = new ArrayList<String>();
		
		if(!Utils.isNullOrEmpty(place.getOpenWidgetId(0)))
			openWidgetIds.add(place.getOpenWidgetId(0));
		
		if(!Utils.isNullOrEmpty(openWidgetId))
			openWidgetIds.add(openWidgetId);
		
		//GWT.log("CommonEditorPlace: " + lt + ", " + et + ", place: " + place.toString() +  ", " + isNew + ", " + index + ", owId: " + openWidgetId);
	}
	
	
	
	public EditorType getEditorType() {  
		return editorType;
	}
	
	public TriggerListType getTriggerListType() {
		return triggerListType;
	}
	
	public String getSpecId() {
		if(path.size()>0)
			return path.get(0);
		return null;
	}
	
	
	public List<String> getPathList() {
		return path;
	}
	
	public String getPath() {
		if(path == null || path.isEmpty())return null;
		String ret = "";
		for(String s : path){
			ret += (s + ",");
		}
		if(ret.endsWith(","))ret = ret.substring(0, ret.length()-1);
		return ret;
	}
	
	public String getPath(int index) {
		if(index < path.size())
			return checkAndRemoveNew(path.get(index));
		GWT.log("CommonEditorPlace.getPath: index does not exist!");
		return null;
	}
	
	public Integer getPathAsInt(int index) {
		if(index < path.size())
			return Integer.valueOf(getPath(index));
		GWT.log("CommonEditorPlace.getPathAsInt: index does not exist!");
		return -1;
	}
	
	private String setPathId(int index, String element) {
		if(index < path.size())
			return path.set(index, element);
		GWT.log("CommonEditorPlace.setPathId: index does not exist!");
		return null;
	}
	
	public Integer getPathSize(){
		return path.size();
	}
	
	
	public List<String> getOpenWidgetIdList() {
		return openWidgetIds;
	}
	
	public String getOpenWidgetIds() {
		if(openWidgetIds == null || openWidgetIds.isEmpty())return "0";
		
		String ret = "";
		for(String s : openWidgetIds){
			ret += (s + ",");
		}
		if(ret.endsWith(","))ret = ret.substring(0, ret.length()-1);
		return ret;
	}
		
	
	public String getOpenWidgetId(int index) {
		if(openWidgetIds == null || openWidgetIds.isEmpty() || openWidgetIds.size() <= index)return "0";
		return openWidgetIds.get(index);
	}
	
	public String setOpenWidgetId(int index, String element) {
		return openWidgetIds.set(index, element);
	}
	
	
	public String[] getOpenWidgetIdAsArr(int index) {
		if(openWidgetIds == null || openWidgetIds.isEmpty())
			return (String[]) Arrays.asList("0").toArray();
		
		return openWidgetIds.get(index).split("-");
	}
	
	public boolean isNew(){
		if(path == null || path.isEmpty())return false;
		return path.get(path.size()-1).startsWith("NEW");
	}
	
	public boolean isFormerNew(){
		if(path == null || path.isEmpty())return false;
		return path.get(path.size()-1).startsWith("FOW");
	}
	
	/**
	 * 
	 */
	public String toString(){
		String token = prefix ;
		token += ":" + getListType().toString();
		token += ":" + combineEditorTypeAndTriggerListType();
		token += ":" + getPath();
		token += ":" + getOpenWidgetIds();
		return token;
	}
	
	
	public String combineEditorTypeAndTriggerListType(){
		
		if(getEditorType() == EditorType.TR){
			return (getEditorType().toString() + "-" + getTriggerListType().toString());
		}
		return getEditorType().toString();
	}
		
	
	/**
	 * Removes the 'NEW' string inside the id 
	 * @param in
	 * @return the number only
	 */
	private String checkAndRemoveNew(String in){
		if(in.startsWith("NEW") || in.startsWith("FOW")){
			String[] sTmp = in.split("W");
			return sTmp[1];
		}
		return in;
	}
	
	/**
	 * Registers the ConfigChangedEvent to listen to fired events from the editor when the user opens or closes
	 * different ConfigSections
	 */
	public void updateOpenWidgetId(String newOpenWidgetId){
		
		setOpenWidgetId(getOpenWidgetIdList().size()-1, newOpenWidgetId);
		
		//updates the current 'OpenWidgetId' containing the id of the current expanded widget.
		//sample content of token, e.g.: "editor:EC:EC:35d8e332-c4c9-4a99-95d7-301efc809d63:3"
		
		//token does contain the prefix
		String token = History.getToken();
		if(Utils.isNullOrEmpty(token)){
			GWT.log("CommonEcEditorPlace.updateOpenWidgetId.token is empty!");
			return;
		}
		
		
		String[] splitted = token.split(":");
		
		if (splitted.length == 5) {
			CommonEditorPlace pl = new CommonEditorPlace(splitted);
			pl.setOpenWidgetId(pl.getOpenWidgetIdList().size()-1, newOpenWidgetId);
			History.replaceItem(pl.toString(), false);
		}
		else{
			GWT.log("CommonEcEditorPlace.updateOpenWidgetId splitted.length does not fit.");
		}
	}
	
	/**
	 * Marks the path with FOW (former new), which means that a temporarily saved object was newly created.
	 * So when the activity is reloaded from a deeper level it should not create a new object as with 'NEW'
	 * nevertheless the object should be removeable at cancel anyway
	 */
	public void setFormerNew(int newIndex){
		updatePathId("FOW"+(newIndex));
	}
	
	
	public void setNew(int newIndex){
		updatePathId("NEW"+(newIndex));
	}
	
	public void removeLastPlace(){
		int index = path.size()-1;
		if(index > 0){
			path.remove(index);
			if(index < openWidgetIds.size())
				openWidgetIds.remove(index);
		}
	}
	
	
	public void updatePathId(String newPathId){
		
		int lastIndex = getPathList().size()-1;
		if(lastIndex > 0){
			setPathId(lastIndex, newPathId);
			
			//updates the current 'OpenWidgetId' containing the id of the current expanded widget.
			//sample content of token, e.g.: "editor:EC:EC:35d8e332-c4c9-4a99-95d7-301efc809d63:3"
			
			//token does contain the prefix
			String token = History.getToken();
			if(Utils.isNullOrEmpty(token)){
				GWT.log("CommonEcEditorPlace.updatePathId.token is empty!");
				return;
			}
			
			
			String[] splitted = token.split(":");
			
			if (splitted.length == 5) {
				CommonEditorPlace pl = new CommonEditorPlace(splitted);
				int lastIndex1 = pl.getPathSize()-1;
				if(lastIndex1 > 0){
					pl.setPathId(lastIndex1, newPathId);
					History.replaceItem(pl.toString(), false);
				}
				else{
					GWT.log("lastIndex1 <= 0: " + token);
				}
			}
			else{
				GWT.log("CommonEcEditorPlace.updatePathId splitted.length does not fit.");
			}
		}
		else{
			GWT.log("updatePathId.lastIndex <= 0: " + toString());
		}
		
	}
	
	
	@Override
	public ListType getListType() {
		return listType;
	}

}
