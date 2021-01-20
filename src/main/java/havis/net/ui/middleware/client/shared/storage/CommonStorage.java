package havis.net.ui.middleware.client.shared.storage;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import havis.middleware.ale.service.cc.CCParameterListEntry;
import havis.middleware.ale.service.mc.MCAssociationSpec;
import havis.middleware.ale.service.mc.MCCacheSpec;
import havis.middleware.ale.service.mc.MCCommandCycleSpec;
import havis.middleware.ale.service.mc.MCEventCycleSpec;
import havis.middleware.ale.service.mc.MCLogicalReaderSpec;
import havis.middleware.ale.service.mc.MCPortCycleSpec;
import havis.middleware.ale.service.mc.MCRandomSpec;
import havis.middleware.ale.service.mc.MCSpec;
import havis.middleware.ale.service.mc.MCTagMemorySpec;
import havis.middleware.tdt.PackedObjectInvestigator;
import havis.middleware.tdt.TdtResources;
import havis.middleware.tdt.PackedObjectInvestigator.ColumnName;
import havis.net.rest.middleware.shared.HasSubscribers;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.TriggerPlace;
import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;
import havis.net.ui.middleware.client.utils.Utils;

public class CommonStorage {

	private String newLRSpecName;
	private String newTMSpecName;
	private String newTMFieldName;
	private List<CCParameterListEntry> parameters;

	@Inject
	private LRSpecStorage lrStorage;

	@Inject
	private TMSpecStorage tmStorage;

	@Inject
	private CCSpecStorage ccStorage;

	@Inject
	private PCSpecStorage pcStorage;

	@Inject
	private ECSpecStorage ecStorage;

	@Inject
	private DRSpecStorage drStorage;

	@Inject
	private DCSpecStorage dcStorage;

	@Inject
	private DASpecStorage daStorage;

	public void clear() {
		lrStorage.clear();
		tmStorage.clear();
		ccStorage.clear();
		pcStorage.clear();
		ecStorage.clear();
		drStorage.clear();
		dcStorage.clear();
		daStorage.clear();
	}

	public void loadSpec(CommonEditorPlace place, Object source) {
		switch (place.getListType()) {
		case EC:
			ecStorage.loadSpec(place, source);
			break;
		case CC:
			ccStorage.loadSpec(place, source);
			break;
		case PC:
			pcStorage.loadSpec(place, source);
			break;
		case DSCA:
			dcStorage.loadSpec(place, source);
			break;
		case DSAS:
			daStorage.loadSpec(place, source);
			break;
		case DSRN:
			drStorage.loadSpec(place, source);
			break;
		default:
		}
	}

	public String getString(ListType listType, MCSpec source) {
		switch (listType) {
		case LR:
			return lrStorage.getJSONString((MCLogicalReaderSpec) source);
		case EC:
			return ecStorage.getJSONString((MCEventCycleSpec) source);
		case CC:
			return ccStorage.getJSONString((MCCommandCycleSpec) source);
		case PC:
			return pcStorage.getJSONString((MCPortCycleSpec) source);
		case TM:
			return tmStorage.getJSONString((MCTagMemorySpec) source);
		case DSCA:
			return dcStorage.getJSONString((MCCacheSpec) source);
		case DSAS:
			return daStorage.getJSONString((MCAssociationSpec) source);
		case DSRN:
			return drStorage.getJSONString((MCRandomSpec) source);
		default:
			return null;
		}
	}

	public List<String> getTriggerList(CommonEditorPlace place, MCSpec spec) {
		switch (place.getListType()) {
		case EC:
			return ecStorage.getTriggerList(place, (MCEventCycleSpec) spec);
		case PC:
			return pcStorage.getTriggerList(place, (MCPortCycleSpec) spec);
		case CC:
			return ccStorage.getTriggerList(place, (MCCommandCycleSpec) spec);
		default:
			return null;
		}
	}

	public void resetEmptyListObjects(CommonEditorPlace place, Object specObj) {

		if (place.getEditorType() == EditorType.EC) {
			ecStorage.removeEmptyLists(place, specObj);
		} else if (place.getEditorType() == EditorType.PC) {
			pcStorage.removeEmptyLists(place, specObj);
		} else if (place.getEditorType() == EditorType.CC) {
			ccStorage.removeEmptyLists(place, specObj);
		} else if (place.getEditorType() == EditorType.TR) {
			if (place instanceof TriggerPlace) {
				if (place.getListType() == ListType.EC) {
					ecStorage.resetTriggerList(place, place.getSpecId());
				} else if (place.getListType() == ListType.PC) {
					pcStorage.resetTriggerList(place, place.getSpecId());
				} else if (place.getListType() == ListType.CC) {
					ccStorage.resetTriggerList(place, place.getSpecId());
				}
			}
		} else if (place.getEditorType() == EditorType.DSCA) {
			MCCacheSpec cs = (MCCacheSpec) specObj;
			if (cs.getPatterns() == null || cs.getPatterns().getPatterns() == null || cs.getPatterns().getPatterns().getPattern().isEmpty()) {
				cs.setPatterns(null);
			}
			cs.setSpec(null);
		} else if (place.getEditorType() == EditorType.DSAS) {
			MCAssociationSpec as = (MCAssociationSpec) specObj;
			if (as.getEntries() == null || as.getEntries().getEntries() == null || as.getEntries().getEntries().getEntry().isEmpty()) {
				as.setEntries(null);
			}
			if (as.getSpec() == null || Utils.isNullOrEmpty(as.getSpec().getDatatype()) || Utils.isNullOrEmpty(as.getSpec().getFormat())) {
				as.setSpec(null);
			}
		} else if (place.getEditorType() == EditorType.DSRN) {
			// MCRandomSpec rs = (MCRandomSpec)specObj;
		}

		return;
	}

	public String getNewLRSpecName() {
		return newLRSpecName;
	}

	public void setNewLRSpecName(String newSpecName) {
		this.newLRSpecName = newSpecName;
	}

	public String getNewTMSpecName() {
		return newTMSpecName;
	}

	public void setNewTMSpecName(String newTMSpecName) {
		this.newTMSpecName = newTMSpecName;
	}

	public String getNewTMFieldName() {
		return newTMFieldName;
	}

	public void setNewTMFieldName(String newTMFieldName) {
		this.newTMFieldName = newTMFieldName;
	}

	public HasSubscribers getSubscribersService(ListType type) {
		switch (type) {
		case EC:
			return ecStorage.getSubscribersService();
		case CC:
			return ccStorage.getSubscribersService();
		case PC:
			return pcStorage.getSubscribersService();
		default:
			return null;
		}
	}

	/**
	 * Stores the OID DATA FORMAT 9 table
	 */
	private static Map<String, Map<ColumnName, String>> OIDS;

	/**
	 * @return DATA FORMAT 9 Table
	 */
	public static Map<String, Map<ColumnName, String>> getOids() {
		if (OIDS == null) {
			OIDS = Collections.unmodifiableMap(new PackedObjectInvestigator(TdtResources.INSTANCE.tableForData().getText()).getOids(
					"urn:oid:1.0.15961.9", ColumnName.FORMAT_9_NAME, ColumnName.FORMAT_9_FORMAT_STRING));
		}
		return OIDS;
	}
	
	public void setParameters(List<CCParameterListEntry> parameters) {
		this.parameters = parameters;
	}
	
	public List<CCParameterListEntry> getParameters() {
		return parameters;
	}
	
	public void clearParameters() {
		parameters.clear();
	}
}
