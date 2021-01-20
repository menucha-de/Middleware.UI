package havis.net.ui.middleware.client.shared.storage;

import java.util.List;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;

import havis.middleware.ale.service.ECFilterListMember;
import havis.middleware.ale.service.cc.CCBoundarySpec;
import havis.middleware.ale.service.cc.CCCmdSpec;
import havis.middleware.ale.service.cc.CCFilterSpec;
import havis.middleware.ale.service.cc.CCOpSpec;
import havis.middleware.ale.service.cc.CCSpec;
import havis.middleware.ale.service.mc.MCCommandCycleSpec;
import havis.net.rest.middleware.cc.CCAsync;
import havis.net.ui.middleware.client.cc.cmd.operation.CCOpType;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.TriggerPlace;
import havis.net.ui.middleware.client.place.cc.CCCmdSpecEditorPlace;
import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterItemEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterPatternItemEditorPlace;
import havis.net.ui.middleware.client.place.pc.OpSpecEditorPlace;
import havis.net.ui.middleware.client.utils.Utils;

public class CCSpecStorage extends CycleSpecStorage<MCCommandCycleSpec> {
	
	interface Codec extends JsonEncoderDecoder<MCCommandCycleSpec> {}

	@Inject
	public CCSpecStorage(CCAsync service) {
		super((Codec) GWT.create(Codec.class));
		setSubscribersService(service);
		setSpecs(service.getSpecs());
	}

	@Override
	protected Object getInitializedSpec(CommonEditorPlace place, MCCommandCycleSpec spec) {
		MCCommandCycleSpec mcCommandCycleSpec = initializeCCSpec((MCCommandCycleSpec) spec);

		if (spec == null) {
			setSpec(place.getSpecId(), mcCommandCycleSpec);
		}

		if (place instanceof EditorPlace) {
			if (place.getEditorType() == EditorType.CC) {
				return (Object) mcCommandCycleSpec;
			}
		}

		if (place instanceof TriggerPlace) {
			return (Object) mcCommandCycleSpec;
		}

		List<CCCmdSpec> ccCmdSpecs = initializeCCCmdSpecs(mcCommandCycleSpec);
		if (place instanceof CCCmdSpecEditorPlace) {
			return (Object) ccCmdSpecs;
		}

		CCCmdSpec ccCmdSpec = getCCCmdSpecItem(place, ccCmdSpecs);
		if (place instanceof FilterItemEditorPlace) {
			return (Object) initializeFilterListMembers(ccCmdSpec);
		} else if (place instanceof FilterPatternItemEditorPlace) {
			List<ECFilterListMember> filterListMembers = initializeFilterListMembers(ccCmdSpec);
			return (Object) filterListMembers;
		}
		if (place instanceof OpSpecEditorPlace) {
			return (Object) initializeCcOpSpecs(ccCmdSpec);
		}

		return mcCommandCycleSpec;

	}

	/**
	 * Initializes PCReportSpecs of MCPortCycleSpec
	 * 
	 * @param place
	 * @param mcPortCycleSpec
	 * @return pcReportSpecs
	 */
	private List<CCCmdSpec> initializeCCCmdSpecs(MCCommandCycleSpec mcCommandCycleSpec) {

		if (mcCommandCycleSpec.getSpec() == null) {
			mcCommandCycleSpec.setSpec(new CCSpec());
		}
		if (mcCommandCycleSpec.getSpec().getCmdSpecs() == null) {
			mcCommandCycleSpec.getSpec().setCmdSpecs(new CCSpec.CmdSpecs());
		}

		return mcCommandCycleSpec.getSpec().getCmdSpecs().getCmdSpec();
	}

	private CCCmdSpec getCCCmdSpecItem(CommonEditorPlace place, List<CCCmdSpec> ccCmdSpec) {

		if (ccCmdSpec == null || ccCmdSpec.isEmpty() || place.getPathAsInt(1) >= ccCmdSpec.size()) {
			return null;
		}

		return ccCmdSpec.get(place.getPathAsInt(1));
	}

	private MCCommandCycleSpec initializeCCSpec(MCCommandCycleSpec mcCommandCycleSpec) {

		if (mcCommandCycleSpec == null) {
			mcCommandCycleSpec = new MCCommandCycleSpec();
			mcCommandCycleSpec.setEnable(false);
			mcCommandCycleSpec.setId(null);
		}

		CCSpec spec = mcCommandCycleSpec.getSpec();
		if (spec == null) {
			spec = new CCSpec();
			mcCommandCycleSpec.setSpec(spec);
		}

		if (spec.getLogicalReaders() == null)
			spec.setLogicalReaders(new CCSpec.LogicalReaders());

		if (spec.getCmdSpecs() == null)
			spec.setCmdSpecs(new CCSpec.CmdSpecs());

		CCBoundarySpec bs = spec.getBoundarySpec();
		if (bs == null) {
			bs = new CCBoundarySpec();
			spec.setBoundarySpec(bs);
		}

		if (bs.getDuration() == null)
			bs.setDuration(Utils.getTime());
		if (bs.getNoNewTagsInterval() == null)
			bs.setNoNewTagsInterval(Utils.getTime());
		if (bs.getRepeatPeriod() == null)
			bs.setRepeatPeriod(Utils.getTime());

		CCBoundarySpec.StartTriggerList startTriggerList = bs.getStartTriggerList();
		if (startTriggerList == null) {
			startTriggerList = new CCBoundarySpec.StartTriggerList();
			bs.setStartTriggerList(startTriggerList);
		}
		CCBoundarySpec.StopTriggerList stopTriggerList = bs.getStopTriggerList();
		if (stopTriggerList == null) {
			stopTriggerList = new CCBoundarySpec.StopTriggerList();
			bs.setStopTriggerList(stopTriggerList);
		}

		return mcCommandCycleSpec;
	}

	private List<ECFilterListMember> initializeFilterListMembers(CCCmdSpec ccCmdSpec) {
		if (ccCmdSpec == null)
			return null;

		if (ccCmdSpec.getFilterSpec() == null) {
			ccCmdSpec.setFilterSpec(new CCFilterSpec());
		}
		if (ccCmdSpec.getFilterSpec().getFilterList() == null) {
			ccCmdSpec.getFilterSpec().setFilterList(new CCFilterSpec.FilterList());
		}

		List<ECFilterListMember> filterListMembers = ccCmdSpec.getFilterSpec().getFilterList().getFilter();

		return filterListMembers;
	}

	private List<CCOpSpec> initializeCcOpSpecs(CCCmdSpec ccCmdSpec) {
		if (ccCmdSpec == null)
			return null;

		if (ccCmdSpec.getFilterSpec() == null) {
			ccCmdSpec.setFilterSpec(new CCFilterSpec());
		}
		if (ccCmdSpec.getOpSpecs() == null) {
			ccCmdSpec.setOpSpecs(new CCCmdSpec.OpSpecs());
		}

		return ccCmdSpec.getOpSpecs().getOpSpec();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeEmptyLists(CommonEditorPlace place, Object specObj) {

		if (place instanceof EditorPlace) {
			cleanSpec((MCCommandCycleSpec) specObj);
			return;
		}

		MCCommandCycleSpec mcCommandCycleSpec = getSpec(place.getSpecId());

		if (place instanceof CCCmdSpecEditorPlace) {
			List<CCCmdSpec> lst = (List<CCCmdSpec>) specObj;
			int pathIdx = place.getPathAsInt(1);

			if (lst == null || lst.isEmpty()) {
				mcCommandCycleSpec.getSpec().setCmdSpecs(null);
			} else if (lst.size() > pathIdx && lst.get(pathIdx) != null) {
				CCCmdSpec ccCmdSpec = lst.get(pathIdx);

				// Filter
				if (ccCmdSpec.getFilterSpec() != null) {

					if (ccCmdSpec.getFilterSpec().getFilterList() == null
							|| ccCmdSpec.getFilterSpec().getFilterList().getFilter().isEmpty()) {
						// No filterlist found
						ccCmdSpec.setFilterSpec(null);
						return;
					}
					// FilterList exists, but may be incomplete...
					if (ccCmdSpec.getFilterSpec().getFilterList() == null) {
						ccCmdSpec.setFilterSpec(null);
						return;
					}
					if (canResetFilterSpec(ccCmdSpec.getFilterSpec().getFilterList().getFilter()) == true) {
						ccCmdSpec.setFilterSpec(null);
					}
				}
			}

		} else if (place instanceof FilterItemEditorPlace) {
			List<ECFilterListMember> ecFilterListMembers = (List<ECFilterListMember>) specObj;
			if (ecFilterListMembers == null || ecFilterListMembers.isEmpty()) {
				CCCmdSpec ccCmdSpec = getCCCmdSpec(place, mcCommandCycleSpec);
				ccCmdSpec.getFilterSpec().setFilterList(null);
			}
		} else if (place instanceof FilterPatternItemEditorPlace) {
			List<String> currentPatternList = (List<String>) specObj;
			if (currentPatternList == null || currentPatternList.isEmpty()) {
				CCCmdSpec ccCmdSpec = getCCCmdSpec(place, mcCommandCycleSpec);
				ccCmdSpec.getFilterSpec().getFilterList().getFilter().get(place.getPathAsInt(2)).setPatList(null);
			}
		} else if (place instanceof OpSpecEditorPlace) {
			List<CCOpSpec> ccOpSpecs = (List<CCOpSpec>) specObj;

			if (ccOpSpecs != null && !ccOpSpecs.isEmpty()) {
				for (CCOpSpec ops : ccOpSpecs) {
					// always setDataSpec(null) on READ
					if (!Utils.isNullOrEmpty(ops.getOpType())
							&& ops.getOpType().startsWith(CCOpType.READ.getCCOpType())) {
						ops.setDataSpec(null);
					} else if (ops.getDataSpec() != null && Utils.isNullOrEmpty(ops.getDataSpec().getData())) {
						ops.setDataSpec(null);
					}

					if (ops.getFieldspec() != null && Utils.isNullOrEmpty(ops.getFieldspec().getFieldname())) {
						ops.setFieldspec(null);
					}
				}
			}
		}
	}

	@Override
	public void cleanSpec(MCCommandCycleSpec spec) {
		CCSpec ccSpec = spec.getSpec();

		if (ccSpec.getLogicalReaders() != null) {
			if (ccSpec.getLogicalReaders().getLogicalReader().isEmpty())
				ccSpec.setLogicalReaders(null);
		}

		if (ccSpec.getCmdSpecs() != null) {
			if (ccSpec.getCmdSpecs().getCmdSpec().isEmpty())
				ccSpec.setCmdSpecs(null);
		}

		CCBoundarySpec bs = ccSpec.getBoundarySpec();
		if (bs != null) {

			if (!Utils.isTimeSet(bs.getRepeatPeriod())) {
				bs.setRepeatPeriod(null);
			}
			if (!Utils.isTimeSet(bs.getDuration())) {
				bs.setDuration(null);
			}
			if (!Utils.isTimeSet(bs.getNoNewTagsInterval())) {
				bs.setNoNewTagsInterval(null);
			}

			if (bs.getStopTriggerList() != null && bs.getStopTriggerList().getStopTrigger().isEmpty())
				bs.setStopTriggerList(null);
			if (bs.getStartTriggerList() != null && bs.getStartTriggerList().getStartTrigger().isEmpty())
				bs.setStartTriggerList(null);
			if (!bs.isAfterError()
					&& bs.getStartTriggerList() == null
					&& bs.getStopTriggerList() == null
					&& bs.getRepeatPeriod() == null
					&& bs.getNoNewTagsInterval() == null
					&& bs.getDuration() == null
					&& bs.getTagsProcessedCount() == null) {
				ccSpec.setBoundarySpec(null);
			}
		}
	}

	@Override
	public List<String> getTriggerList(CommonEditorPlace place, MCCommandCycleSpec spec) {
		CCBoundarySpec ccbs = ((MCCommandCycleSpec) spec).getSpec().getBoundarySpec();
		switch (place.getTriggerListType()) {
		case START:
			return ccbs.getStartTriggerList().getStartTrigger();
		case STOP:
			return ccbs.getStopTriggerList().getStopTrigger();
		default:
			return null;
		}
	}

	@Override
	public void resetTriggerList(CommonEditorPlace place, String specId) {
		CCBoundarySpec ccbs = getSpec(specId).getSpec().getBoundarySpec();
		switch (place.getTriggerListType()) {
		case START:
			if (ccbs.getStartTriggerList().getStartTrigger().isEmpty()) {
				ccbs.setStartTriggerList(null);
			}
			break;
		case STOP:
			if (ccbs.getStopTriggerList().getStopTrigger().isEmpty()) {
				ccbs.setStopTriggerList(null);
			}
			break;
		default:
		}
	}

	/**
	 * Get the current CCCmdSpec object
	 * 
	 * @param place
	 * @param spec
	 * @return the object
	 */
	private CCCmdSpec getCCCmdSpec(CommonEditorPlace place, MCCommandCycleSpec spec) {
		return spec.getSpec().getCmdSpecs().getCmdSpec().get(place.getPathAsInt(1));
	}
}
