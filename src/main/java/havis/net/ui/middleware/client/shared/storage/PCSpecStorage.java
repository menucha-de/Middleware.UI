package havis.net.ui.middleware.client.shared.storage;

import java.util.List;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;

import havis.middleware.ale.service.ECFilterListMember;
import havis.middleware.ale.service.mc.MCPortCycleSpec;
import havis.middleware.ale.service.pc.PCBoundarySpec;
import havis.middleware.ale.service.pc.PCBoundarySpec.StartTriggerList;
import havis.middleware.ale.service.pc.PCBoundarySpec.StopTriggerList;
import havis.middleware.ale.service.pc.PCFilterSpec;
import havis.middleware.ale.service.pc.PCOpSpec;
import havis.middleware.ale.service.pc.PCOpSpecs;
import havis.middleware.ale.service.pc.PCReportSpec;
import havis.middleware.ale.service.pc.PCReportSpec.TriggerList;
import havis.middleware.ale.service.pc.PCSpec;
import havis.net.rest.middleware.pc.PCAsync;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.TriggerPlace;
import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterItemEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterPatternItemEditorPlace;
import havis.net.ui.middleware.client.place.pc.OpSpecEditorPlace;
import havis.net.ui.middleware.client.place.pc.PcReportSpecItemEditorPlace;
import havis.net.ui.middleware.client.utils.Utils;

public class PCSpecStorage extends CycleSpecStorage<MCPortCycleSpec> {

	interface Codec extends JsonEncoderDecoder<MCPortCycleSpec> {}
	
	@Inject
	public PCSpecStorage(PCAsync service) {
		super((Codec) GWT.create(Codec.class));
		setSubscribersService(service);
		setSpecs(service.getSpecs());
	}

	@Override
	protected Object getInitializedSpec(CommonEditorPlace place, MCPortCycleSpec spec) {
		MCPortCycleSpec mcPortCycleSpec = initializePCSpec((MCPortCycleSpec) spec);

		if (spec == null) {
			setSpec(place.getSpecId(), mcPortCycleSpec);
		}

		if (place instanceof EditorPlace) {

			if (place.getEditorType() == EditorType.PC) {
				return (Object) mcPortCycleSpec;
			}
		}

		if (place instanceof TriggerPlace) {
			return (Object) mcPortCycleSpec;
		}

		List<PCReportSpec> pcReportSpecs = initializePCReportSpecs(mcPortCycleSpec);
		if (place instanceof PcReportSpecItemEditorPlace) {
			return (Object) pcReportSpecs;
		}

		PCReportSpec pcReportSpec = getPCReportSpecItem(place, pcReportSpecs);
		if (place instanceof FilterItemEditorPlace) {
			return (Object) initializeFilterListMembers(pcReportSpec);
		} else if (place instanceof FilterPatternItemEditorPlace) {
			List<ECFilterListMember> filterListMembers = initializeFilterListMembers(pcReportSpec);
			return (Object) filterListMembers;
		} else if (place instanceof OpSpecEditorPlace) {
			return (Object) initializePortOperationSpecs(pcReportSpec);
		}

		return mcPortCycleSpec;
	}

	/**
	 * Initializes PCReportSpecs of MCPortCycleSpec
	 * 
	 * @param mcPortCycleSpec
	 * @return pcReportSpecs
	 */
	private List<PCReportSpec> initializePCReportSpecs(MCPortCycleSpec mcPortCycleSpec) {

		if (mcPortCycleSpec.getSpec() == null) {
			mcPortCycleSpec.setSpec(new PCSpec());
		}
		if (mcPortCycleSpec.getSpec().getReportSpecs() == null) {
			mcPortCycleSpec.getSpec().setReportSpecs(new PCSpec.ReportSpecs());
		}

		List<PCReportSpec> pcReportSpecs = mcPortCycleSpec.getSpec().getReportSpecs().getReportSpec(); // auto
																										// creates
																										// a
																										// new
																										// ArrayList()...

		return pcReportSpecs;
	}

	private PCReportSpec getPCReportSpecItem(CommonEditorPlace place, List<PCReportSpec> pcReportSpecs) {

		if (pcReportSpecs == null || pcReportSpecs.isEmpty() || place.getPathAsInt(1) >= pcReportSpecs.size()) {
			return null;
		}

		return pcReportSpecs.get(place.getPathAsInt(1));
	}

	private List<ECFilterListMember> initializeFilterListMembers(PCReportSpec pcReportSpec) {
		if (pcReportSpec == null)
			return null;

		if (pcReportSpec.getFilterSpec() == null) {
			pcReportSpec.setFilterSpec(new PCFilterSpec());
		}
		if (pcReportSpec.getFilterSpec().getFilterList() == null) {
			pcReportSpec.getFilterSpec().setFilterList(new PCFilterSpec.FilterList());
		}

		List<ECFilterListMember> filterListMembers = pcReportSpec.getFilterSpec().getFilterList().getFilter();

		return filterListMembers;
	}

	private List<PCOpSpec> initializePortOperationSpecs(PCReportSpec pcReportSpec) {
		if (pcReportSpec == null)
			return null;

		if (pcReportSpec.getOpSpecs() == null) {
			pcReportSpec.setOpSpecs(new PCOpSpecs());
		}

		List<PCOpSpec> opSpecs = pcReportSpec.getOpSpecs().getOpSpec();
		return opSpecs;
	}

	/**
	 * Create probably needed MCPortCycleSpec objects
	 */
	private MCPortCycleSpec initializePCSpec(MCPortCycleSpec mcPortCycleSpec) {

		if (mcPortCycleSpec == null) {
			mcPortCycleSpec = new MCPortCycleSpec();
			mcPortCycleSpec.setEnable(false);
			mcPortCycleSpec.setId(null);
		}

		PCSpec spec = mcPortCycleSpec.getSpec();
		if (spec == null) {
			spec = new PCSpec();
			mcPortCycleSpec.setSpec(spec);
		}

		if (spec.getLogicalReaders() == null)
			spec.setLogicalReaders(new PCSpec.LogicalReaders());

		PCBoundarySpec bs = spec.getBoundarySpec();
		if (bs == null) {
			bs = new PCBoundarySpec();
			spec.setBoundarySpec(bs);
		}

		if (spec.getReportSpecs() == null)
			spec.setReportSpecs(new PCSpec.ReportSpecs());

		if (bs.getDuration() == null)
			bs.setDuration(Utils.getTime());
		if (bs.getNoNewEventsInterval() == null)
			bs.setNoNewEventsInterval(Utils.getTime());
		if (bs.getRepeatPeriod() == null)
			bs.setRepeatPeriod(Utils.getTime());

		StartTriggerList startTriggerList = bs.getStartTriggerList();
		if (bs.getStartTriggerList() == null) {
			startTriggerList = new StartTriggerList();
			bs.setStartTriggerList(startTriggerList);
		}
		StopTriggerList stopTriggerList = bs.getStopTriggerList();
		if (stopTriggerList == null) {
			stopTriggerList = new StopTriggerList();
			bs.setStopTriggerList(stopTriggerList);
		}

		return mcPortCycleSpec;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeEmptyLists(CommonEditorPlace place, Object spec) {

		if (place instanceof EditorPlace) {
			cleanSpec((MCPortCycleSpec) spec);
			return;
		}
		MCPortCycleSpec mcPortCycleSpec = getSpec(place.getSpecId());

		if (place instanceof PcReportSpecItemEditorPlace) {
			List<PCReportSpec> lst = (List<PCReportSpec>) spec;
			int pathIdx = place.getPathAsInt(1);

			if (lst == null || lst.isEmpty()) {
				mcPortCycleSpec.getSpec().setReportSpecs(null);
			} else if (lst.size() > pathIdx && lst.get(pathIdx) != null) {
				PCReportSpec pcReportSpec = lst.get(pathIdx);

				// filterSpec.filterList.filter
				if (pcReportSpec.getFilterSpec() != null) {

					if (pcReportSpec.getFilterSpec().getFilterList() == null
							|| pcReportSpec.getFilterSpec().getFilterList().getFilter().isEmpty()) {
						// No filters found
						pcReportSpec.setFilterSpec(null);
					}
					// FilterList exists, but may be incomplete...
					else if (canResetFilterSpec(pcReportSpec.getFilterSpec().getFilterList().getFilter()) == true) {
						pcReportSpec.setFilterSpec(null);
					}
				}
				// opSpecs
				if (pcReportSpec.getOpSpecs() != null && (pcReportSpec.getOpSpecs().getOpSpec() == null
						|| pcReportSpec.getOpSpecs().getOpSpec().isEmpty())) {
					// No opSpecs found
					pcReportSpec.setOpSpecs(null);
				}

				// statProfileNames
				if (pcReportSpec.getStatProfileNames() != null
						&& (pcReportSpec.getStatProfileNames().getStatProfileName() == null
								|| pcReportSpec.getStatProfileNames().getStatProfileName().isEmpty())) {
					// No statProfileNames found
					pcReportSpec.setStatProfileNames(null);
				}

			}
		} else if (place instanceof FilterItemEditorPlace) {
			List<ECFilterListMember> ecFilterListMembers = (List<ECFilterListMember>) spec;
			if (ecFilterListMembers == null || ecFilterListMembers.isEmpty()) {
				PCReportSpec pcReportSpec = getPCReportSpec(place, mcPortCycleSpec);
				pcReportSpec.getFilterSpec().setFilterList(null);
			}
		} else if (place instanceof FilterPatternItemEditorPlace) {
			List<String> currentPatternList = (List<String>) spec;
			if (currentPatternList == null || currentPatternList.isEmpty()) {
				PCReportSpec pcReportSpec = getPCReportSpec(place, mcPortCycleSpec);
				pcReportSpec.getFilterSpec().getFilterList().getFilter().get(place.getPathAsInt(2)).setPatList(null);
			}
		} else if (place instanceof OpSpecEditorPlace) {
			List<PCOpSpec> pcOpSpecs = (List<PCOpSpec>) spec;
			if (pcOpSpecs != null && pcOpSpecs.isEmpty()) {
				if (place.getPathList().size() > 1) {
					PCReportSpec pcReportSpec = getPCReportSpec(place, mcPortCycleSpec);
					pcReportSpec.setOpSpecs(null);
				}
			}
		}

	}

	@Override
	public void cleanSpec(MCPortCycleSpec spec) {
		PCSpec pcSpec = spec.getSpec();

		if (pcSpec.getLogicalReaders() != null) {
			if (pcSpec.getLogicalReaders().getLogicalReader().isEmpty())
				pcSpec.setLogicalReaders(null);
		}

		if (pcSpec.getReportSpecs() != null) {
			if (pcSpec.getReportSpecs().getReportSpec().isEmpty())
				pcSpec.setReportSpecs(null);
		}

		PCBoundarySpec bs = pcSpec.getBoundarySpec();
		if (bs != null) {

			if (!Utils.isTimeSet(bs.getRepeatPeriod())) {
				bs.setRepeatPeriod(null);
			}
			if (!Utils.isTimeSet(bs.getDuration())) {
				bs.setDuration(null);
			}
			if (!Utils.isTimeSet(bs.getNoNewEventsInterval())) {
				bs.setNoNewEventsInterval(null);
			}

			if (bs.getStopTriggerList() != null && bs.getStopTriggerList().getStopTrigger().isEmpty())
				bs.setStopTriggerList(null);
			if (bs.getStartTriggerList() != null && bs.getStartTriggerList().getStartTrigger().isEmpty())
				bs.setStartTriggerList(null);
			if (!bs.isWhenDataAvailable() && bs.getStartTriggerList() == null && bs.getStopTriggerList() == null
					&& bs.getRepeatPeriod() == null && bs.getNoNewEventsInterval() == null
					&& bs.getDuration() == null) {
				pcSpec.setBoundarySpec(null);
			}
		}
	}

	@Override
	public List<String> getTriggerList(CommonEditorPlace place, MCPortCycleSpec spec) {
		PCBoundarySpec pcbs = ((MCPortCycleSpec) spec).getSpec().getBoundarySpec();
		switch (place.getTriggerListType()) {
		case START:
			return pcbs.getStartTriggerList().getStartTrigger();
		case STOP:
			return pcbs.getStopTriggerList().getStopTrigger();
		case REPORT:
			int reportIndex = place.getPathAsInt(1);
			List<PCReportSpec> list = initializePCReportSpecs((MCPortCycleSpec) spec);
			if (list == null || list.size() <= reportIndex) {
				return null;
			}
			if (list.get(reportIndex).getTriggerList() == null) {
				list.get(reportIndex).setTriggerList(new TriggerList());
			}
			return list.get(reportIndex).getTriggerList().getTrigger();
		default:
			return null;
		}
	}

	/**
	 * Get the current PCReportSpec object
	 * 
	 * @param place
	 * @param spec
	 * @return the object
	 */
	
	// TODO: change visibility!
	public PCReportSpec getPCReportSpec(CommonEditorPlace place, MCPortCycleSpec spec) {
		return spec.getSpec().getReportSpecs().getReportSpec().get(place.getPathAsInt(1));
	}

	@Override
	public void resetTriggerList(CommonEditorPlace place, String specId) {
		PCBoundarySpec pcbs = getSpec(specId).getSpec().getBoundarySpec();
		switch (place.getTriggerListType()) {
		case START:
			if (pcbs.getStartTriggerList().getStartTrigger().isEmpty()) {
				pcbs.setStartTriggerList(null);
			}
			break;
		case STOP:
			if (pcbs.getStopTriggerList().getStopTrigger().isEmpty()) {
				pcbs.setStopTriggerList(null);
			}
			break;
		case REPORT:
			PCReportSpec pcReportSpec = getPCReportSpec(place, getSpec(specId));
			if (pcReportSpec.getTriggerList() != null && pcReportSpec.getTriggerList().getTrigger().isEmpty()) {
				pcReportSpec.setTriggerList(null);
			}
			break;
		default:
		}
	}
}
