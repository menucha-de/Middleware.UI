package havis.net.ui.middleware.client.shared.storage;

import java.util.List;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;

import havis.middleware.ale.service.ECFieldSpec;
import havis.middleware.ale.service.ECFilterListMember;
import havis.middleware.ale.service.ec.ECBoundarySpec;
import havis.middleware.ale.service.ec.ECBoundarySpecExtension;
import havis.middleware.ale.service.ec.ECFilterSpec;
import havis.middleware.ale.service.ec.ECFilterSpecExtension;
import havis.middleware.ale.service.ec.ECGroupSpec;
import havis.middleware.ale.service.ec.ECGroupSpecExtension;
import havis.middleware.ale.service.ec.ECReportOutputFieldSpec;
import havis.middleware.ale.service.ec.ECReportOutputSpec;
import havis.middleware.ale.service.ec.ECReportOutputSpecExtension;
import havis.middleware.ale.service.ec.ECReportOutputSpecExtension.FieldList;
import havis.middleware.ale.service.ec.ECReportSpec;
import havis.middleware.ale.service.ec.ECSpec;
import havis.middleware.ale.service.ec.ECSpecExtension;
import havis.middleware.ale.service.mc.MCEventCycleSpec;
import havis.net.rest.middleware.ec.ECAsync;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.TriggerPlace;
import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;
import havis.net.ui.middleware.client.place.ec.ECReportSpecEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterItemEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterPatternItemEditorPlace;
import havis.net.ui.middleware.client.place.ec.GroupPatternItemEditorPlace;
import havis.net.ui.middleware.client.place.ec.OutputFieldItemEditorPlace;
import havis.net.ui.middleware.client.utils.Utils;

public class ECSpecStorage extends CycleSpecStorage<MCEventCycleSpec> {

	interface Codec extends JsonEncoderDecoder<MCEventCycleSpec> {
	}

	@Inject
	public ECSpecStorage(ECAsync service) {
		super((Codec) GWT.create(Codec.class));
		setSubscribersService(service);
		setSpecs(service.getSpecs());
	}

	@Override
	protected Object getInitializedSpec(CommonEditorPlace place, MCEventCycleSpec spec) {
		MCEventCycleSpec mcEventCycleSpec = initializeECSpec(spec);

		if (spec == null) {
			setSpec(place.getSpecId(), mcEventCycleSpec);
		}

		if (place instanceof EditorPlace) {
			if (place.getEditorType() == EditorType.EC) {
				return (Object) mcEventCycleSpec;
			}
		}

		if (place instanceof TriggerPlace) {
			return (Object) mcEventCycleSpec;
		}

		List<ECReportSpec> ecReportSpecs = initializeECReportSpecs(mcEventCycleSpec);
		if (place instanceof ECReportSpecEditorPlace) {
			return (Object) ecReportSpecs;
		}

		ECReportSpec ecReportSpec = getECReportSpecItem(place, ecReportSpecs);
		if (place instanceof OutputFieldItemEditorPlace) {
			return (Object) initializeECReportOutputFields(place, ecReportSpec);
		} else if (place instanceof GroupPatternItemEditorPlace) {
			return (Object) initializeECReportGroupPattern(place, ecReportSpec);
		} else if (place instanceof FilterItemEditorPlace) {
			return (Object) initializeFilterListMembers(ecReportSpec);
		} else if (place instanceof FilterPatternItemEditorPlace) {
			List<ECFilterListMember> filterListMembers = initializeFilterListMembers(ecReportSpec);
			// return (Object) initializePatList(place, filterListMembers);
			return (Object) filterListMembers;
		}
		return mcEventCycleSpec;
	}

	private MCEventCycleSpec initializeECSpec(MCEventCycleSpec mcEventCycleSpec) {

		if (mcEventCycleSpec == null) {
			mcEventCycleSpec = new MCEventCycleSpec();
			mcEventCycleSpec.setEnable(false);
			mcEventCycleSpec.setId(null);
		}

		ECSpec spec = mcEventCycleSpec.getSpec();
		if (spec == null) {
			spec = new ECSpec();
			mcEventCycleSpec.setSpec(spec);
		}

		if (spec.getLogicalReaders() == null)
			spec.setLogicalReaders(new ECSpec.LogicalReaders());

		if (spec.getReportSpecs() == null)
			spec.setReportSpecs(new ECSpec.ReportSpecs());

		// Primary Keys
		ECSpecExtension se = spec.getExtension();
		if (se == null) {
			se = new ECSpecExtension();
			spec.setExtension(se);
		}

		if (se.getPrimaryKeyFields() == null) {
			se.setPrimaryKeyFields(new ECSpecExtension.PrimaryKeyFields());
			se.getPrimaryKeyFields().getPrimaryKeyField();
		}

		// ECBoundarySpec
		ECBoundarySpec bs = spec.getBoundarySpec();
		if (bs == null) {
			bs = new ECBoundarySpec();
			spec.setBoundarySpec(bs);
		}

		if (bs.getDuration() == null)
			bs.setDuration(Utils.getTime());
		if (bs.getStableSetInterval() == null)
			bs.setStableSetInterval(Utils.getTime());
		if (bs.getRepeatPeriod() == null)
			bs.setRepeatPeriod(Utils.getTime());

		ECBoundarySpecExtension bse = bs.getExtension();
		if (bse == null) {
			bse = new ECBoundarySpecExtension();
			bs.setExtension(bse);
		}
		ECBoundarySpecExtension.StartTriggerList startTriggerList = bse.getStartTriggerList();
		if (startTriggerList == null) {
			startTriggerList = new ECBoundarySpecExtension.StartTriggerList();
			bse.setStartTriggerList(startTriggerList);
		}
		ECBoundarySpecExtension.StopTriggerList stopTriggerList = bse.getStopTriggerList();
		if (stopTriggerList == null) {
			stopTriggerList = new ECBoundarySpecExtension.StopTriggerList();
			bse.setStopTriggerList(stopTriggerList);
		}

		return mcEventCycleSpec;
	}

	private List<ECReportOutputFieldSpec> initializeECReportOutputFields(CommonEditorPlace place,
			ECReportSpec ecReportSpec) {

		if (ecReportSpec.getOutput() == null) {
			ecReportSpec.setOutput(new ECReportOutputSpec());
		}
		if (ecReportSpec.getOutput().getExtension() == null) {
			ecReportSpec.getOutput().setExtension(new ECReportOutputSpecExtension());
		}

		if (ecReportSpec.getOutput().getExtension().getFieldList() == null) {
			ecReportSpec.getOutput().getExtension().setFieldList(new FieldList());
		}

		return ecReportSpec.getOutput().getExtension().getFieldList().getField();
	}

	private ECGroupSpec initializeECReportGroupPattern(CommonEditorPlace place, ECReportSpec ecReportSpec) {

		if (ecReportSpec.getGroupSpec() == null) {
			ecReportSpec.setGroupSpec(new ECGroupSpec());
		}

		if (ecReportSpec.getGroupSpec().getExtension() == null) {
			ecReportSpec.getGroupSpec().setExtension(new ECGroupSpecExtension());
		}

		if (ecReportSpec.getGroupSpec().getExtension().getFieldspec() == null) {
			ecReportSpec.getGroupSpec().getExtension().setFieldspec(new ECFieldSpec());
		}

		return ecReportSpec.getGroupSpec();
	}

	private List<ECFilterListMember> initializeFilterListMembers(ECReportSpec ecReportSpec) {
		if (ecReportSpec == null)
			return null;

		if (ecReportSpec.getFilterSpec() == null) {
			ecReportSpec.setFilterSpec(new ECFilterSpec());
		}
		if (ecReportSpec.getFilterSpec().getExtension() == null) {
			ecReportSpec.getFilterSpec().setExtension(new ECFilterSpecExtension());
		}
		if (ecReportSpec.getFilterSpec().getExtension().getFilterList() == null) {
			ecReportSpec.getFilterSpec().getExtension().setFilterList(new ECFilterSpecExtension.FilterList());
		}
		return ecReportSpec.getFilterSpec().getExtension().getFilterList().getFilter();
	}

	/**
	 * Initializes ECReportSpecs of MCEventCycleSpec
	 * 
	 * @param place
	 * @param mcPortCycleSpec
	 * @return pcReportSpecs
	 */
	private List<ECReportSpec> initializeECReportSpecs(MCEventCycleSpec mcEventCycleSpec) {

		if (mcEventCycleSpec.getSpec() == null) {
			mcEventCycleSpec.setSpec(new ECSpec());
		}
		if (mcEventCycleSpec.getSpec().getReportSpecs() == null) {
			mcEventCycleSpec.getSpec().setReportSpecs(new ECSpec.ReportSpecs());
		}
		return mcEventCycleSpec.getSpec().getReportSpecs().getReportSpec();
	}

	private ECReportSpec getECReportSpecItem(CommonEditorPlace place, List<ECReportSpec> ecReportSpecs) {

		if (ecReportSpecs == null || ecReportSpecs.isEmpty() || place.getPathAsInt(1) >= ecReportSpecs.size()) {
			return null;
		}
		return ecReportSpecs.get(place.getPathAsInt(1));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeEmptyLists(CommonEditorPlace place, Object spec) {
		if (place.getEditorType() == EditorType.EC) {
			if (place instanceof EditorPlace) {
				cleanSpec((MCEventCycleSpec) spec);
				return;
			}

			MCEventCycleSpec mcEventCycleSpec = getSpec(place.getSpecId());

			if (place instanceof ECReportSpecEditorPlace) {
				List<ECReportSpec> lst = (List<ECReportSpec>) spec;
				int pathIdx = place.getPathAsInt(1);

				if (lst == null || lst.isEmpty()) {
					mcEventCycleSpec.getSpec().setReportSpecs(null);
				} else if (lst.size() > pathIdx && lst.get(pathIdx) != null) {
					ECReportSpec ecReportSpec = lst.get(pathIdx);
					ecReportSpec.setReportIfEmpty(ecReportSpec.isReportIfEmpty() ? true : null);
					ecReportSpec.setReportOnlyOnChange(ecReportSpec.isReportOnlyOnChange() ? true : null);
					
					// Group
					if (ecReportSpec.getGroupSpec() != null && ecReportSpec.getGroupSpec().getExtension() != null) {
						ECGroupSpec ecGroupSpec = ecReportSpec.getGroupSpec();
						// No pattern found
						if (ecGroupSpec.getPattern() == null || ecGroupSpec.getPattern().isEmpty()) {
							ecGroupSpec.getExtension().setFieldspec(null);
						}
						// No fieldspec.fieldname found
						else if (ecGroupSpec.getExtension().getFieldspec() != null
								&& Utils.isNullOrEmpty(ecGroupSpec.getExtension().getFieldspec().getFieldname())) {
							ecGroupSpec.getExtension().setFieldspec(null);
						}

						if (ecGroupSpec.getExtension().getFieldspec() == null) {
							ecGroupSpec.setExtension(null);
						}

						if (ecGroupSpec.getExtension() == null && ecGroupSpec.getPattern().isEmpty()) {
							ecReportSpec.setGroupSpec(null);
						}
					}

					// Filter
					ECFilterSpec ecFilterSpec = ecReportSpec.getFilterSpec();
					if (ecFilterSpec != null && (ecFilterSpec.getExtension() == null
							|| ecFilterSpec.getExtension().getFilterList() == null
							|| ecFilterSpec.getExtension().getFilterList().getFilter().isEmpty())) {
						// No filterlist found
						ecReportSpec.setFilterSpec(null);
						return;
					}

					// FilterList exists, but may be incomplete...
					if (ecReportSpec.getFilterSpec() != null 
							&& ecReportSpec.getFilterSpec().getExtension() != null 
							&& ecReportSpec.getFilterSpec().getExtension().getFilterList() != null
							&& ecReportSpec.getFilterSpec().getExtension().getFilterList().getFilter() != null
							&& canResetFilterSpec(
							ecReportSpec.getFilterSpec().getExtension().getFilterList().getFilter()) == true) {
						ecReportSpec.setFilterSpec(null);
					}
				}
			} else if (place instanceof OutputFieldItemEditorPlace) {
				List<ECReportOutputFieldSpec> lst = (List<ECReportOutputFieldSpec>) spec;
				if (lst == null || lst.isEmpty()) {

					ECReportOutputSpec ecOut = mcEventCycleSpec.getSpec().getReportSpecs().getReportSpec()
							.get(place.getPathAsInt(1)).getOutput();

					if (ecOut != null) {
						ecOut.setIncludeCount(ecOut.isIncludeCount() ? true : null);
						ecOut.setIncludeEPC(ecOut.isIncludeEPC() ? true : null);
						ecOut.setIncludeRawDecimal(ecOut.isIncludeRawDecimal() ? true : null);
						ecOut.setIncludeRawHex(ecOut.isIncludeRawHex() ? true : null);
						ecOut.setIncludeTag(ecOut.isIncludeTag() ? true : null);
					}
					if (ecOut == null || ecOut.getExtension() == null || ecOut.getExtension().getFieldList() == null) {
						return;
					}

					if (ecOut.getExtension().getFieldList().getField().isEmpty()) {
						ecOut.getExtension().setFieldList(null);
					}
					return;
				}
				for (ECReportOutputFieldSpec ofspec : lst) {
					if (ofspec.getFieldspec() != null && Utils.isNullOrEmpty(ofspec.getFieldspec().getFieldname())) {
						ofspec.setFieldspec(null);
					}
				}
			} else if (place instanceof GroupPatternItemEditorPlace) {
				List<String> lst = (List<String>) spec;
				if (lst == null || lst.isEmpty()) {
					mcEventCycleSpec.getSpec().getReportSpecs().getReportSpec().get(place.getPathAsInt(1))
							.setGroupSpec(null);
				}
			}
		}
	}

	@Override
	public void cleanSpec(MCEventCycleSpec spec) {
		ECSpec ecSpec = spec.getSpec();
		if (!ecSpec.isIncludeSpecInReports()) {
			ecSpec.setIncludeSpecInReports(null);
		}

		if (ecSpec.getExtension() != null) {
			if (ecSpec.getExtension().getPrimaryKeyFields().getPrimaryKeyField().isEmpty()) {
				ecSpec.setExtension(null);
			}
		}
		if (ecSpec.getLogicalReaders() != null) {
			if (ecSpec.getLogicalReaders().getLogicalReader().isEmpty())
				ecSpec.setLogicalReaders(null);
		}

		if (ecSpec.getReportSpecs() != null) {
			if (ecSpec.getReportSpecs().getReportSpec().isEmpty())
				ecSpec.setReportSpecs(null);
		}

		ECBoundarySpec bs = ecSpec.getBoundarySpec();
		if (bs != null) {

			if (!Utils.isTimeSet(bs.getRepeatPeriod())) {
				bs.setRepeatPeriod(null);
			}
			if (!Utils.isTimeSet(bs.getDuration())) {
				bs.setDuration(null);
			}
			if (!Utils.isTimeSet(bs.getStableSetInterval())) {
				bs.setStableSetInterval(null);
			}

			ECBoundarySpecExtension bse = bs.getExtension();
			if (bse != null) {
				if (bse.getStopTriggerList() != null && bse.getStopTriggerList().getStopTrigger().isEmpty())
					bse.setStopTriggerList(null);
				if (bse.getStartTriggerList() != null && bse.getStartTriggerList().getStartTrigger().isEmpty())
					bse.setStartTriggerList(null);
				if (!bse.isWhenDataAvailable() && bse.getStartTriggerList() == null
						&& bse.getStopTriggerList() == null) {
					bs.setExtension(null);
				}
			}

			if (bs.getExtension() == null && bs.getRepeatPeriod() == null && bs.getStableSetInterval() == null
					&& bs.getDuration() == null) {
				ecSpec.setBoundarySpec(null);
			}
		}
	}

	@Override
	public List<String> getTriggerList(CommonEditorPlace place, MCEventCycleSpec spec) {
		ECBoundarySpecExtension ecbse = spec.getSpec().getBoundarySpec().getExtension();
		switch (place.getTriggerListType()) {
		case START:
			return ecbse.getStartTriggerList().getStartTrigger();
		case STOP:
			return ecbse.getStopTriggerList().getStopTrigger();
		default:
			return null;
		}
	}

	@Override
	public void resetTriggerList(CommonEditorPlace place, String specId) {
		ECBoundarySpecExtension ecbse = getSpec(specId).getSpec().getBoundarySpec().getExtension();
		switch (place.getTriggerListType()) {
		case START:
			if (ecbse.getStartTriggerList().getStartTrigger().isEmpty()) {
				ecbse.setStartTriggerList(null);
			}
			break;
		case STOP:
			if (ecbse.getStopTriggerList().getStopTrigger().isEmpty()) {
				ecbse.setStopTriggerList(null);
			}
			break;
		default:
		}
	}

}
