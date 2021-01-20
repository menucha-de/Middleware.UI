package havis.net.ui.middleware.client.shared.report.filter;

import havis.middleware.ale.service.ECFieldSpec;
import havis.middleware.ale.service.ECFilterListMember;
import havis.middleware.ale.service.ECFilterListMember.PatList;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.cc.CCCmdSpecEditorPlace;
import havis.net.ui.middleware.client.place.ec.ECReportSpecEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterItemEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterPatternItemEditorPlace;
import havis.net.ui.middleware.client.place.pc.PcReportSpecItemEditorPlace;
import havis.net.ui.middleware.client.shared.EditorBaseActivity;
import havis.net.ui.middleware.client.shared.TMFieldNamesCallback;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.shared.storage.TMSpecStorage;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.SectionExpandedCallback;

import java.util.List;
import java.util.Map;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.History;
import com.google.inject.Inject;

public class ECFilterListMemberEditorActivity
		extends EditorBaseActivity<FilterItemEditorPlace, ECFilterListMember, ECFilterListMemberEditor> {
	
	interface Driver extends SimpleBeanEditorDriver<ECFilterListMember, ECFilterListMemberEditor> {
	}
	
	interface Codec extends JsonEncoderDecoder<ECFilterListMember> {}

	@Inject
	public ECFilterListMemberEditorActivity(Driver driver, ECFilterListMemberEditor editor) {
		super(driver, editor, ConstantsResource.INSTANCE.filter(), (Codec) GWT.create(Codec.class));
		this.editor = editor;
	}

	private ConstantsResource res = ConstantsResource.INSTANCE;

	@Inject
	private CommonStorage commonStorage;

	@Inject
	private TMSpecStorage tms;

	private ECFilterListMemberEditor editor;

	private int currentIndex = -1;

	private List<ECFilterListMember> ecFilterListMembers;

	private ECFilterListMember filterListMember;

	private void initializeListWidgetHandlers() {

		editor.patternListEditor.setText("Filter Pattern");

		editor.patternListEditor.addNewItemHandler(new NewItemEvent.Handler() {
			@Override
			public void onNewItem(NewItemEvent event) {
				flushChanges();
				updatePathId();
				goTo(new FilterPatternItemEditorPlace(getPlace(), true, 0, "0"));
			}
		});

		editor.patternListEditor.setOpenItemHandler(new OpenItemEvent.Handler() {

			@Override
			public void onOpenItem(OpenItemEvent event) {
				flushChanges();
				updatePathId();
				goTo(new FilterPatternItemEditorPlace(getPlace(), false, event.getIndex(), "0"));
			}
		});
	}

	/**
	 * update the path and mark it as 'FormerNew' if necessary
	 */
	private void updatePathId() {
		if (getPlace().isNew()) {
			getPlace().updatePathId("FOW" + (currentIndex));
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		if ((getPlace().isNew() || getPlace().isFormerNew()) && ecFilterListMembers != null
				&& ecFilterListMembers.size() > currentIndex) {
			ecFilterListMembers.remove(currentIndex);
			getPlace().updatePathId("NEW" + (currentIndex - 1));
		}
		commonStorage.resetEmptyListObjects(getPlace(), ecFilterListMembers);
		goBack();
	}

	@Override
	public void onAccept() {
		super.onAccept();
		if (!hasErrors()) {
			commonStorage.resetEmptyListObjects(getPlace(), ecFilterListMembers);
			goBack();
		}
	}

	/**
	 * OpenWidgetHandling stores information about opened/closed config sections
	 * utilizing the place history mechanism
	 */
	private void initializeOpenWidgetHandling() {
		editor.getConfigSections().setOpen(getPlace().getOpenWidgetIdAsArr(2));

		editor.getConfigSections().setSectionExpandedLevel(new SectionExpandedCallback<String>() {

			@Override
			public void onExpandedChanged(String response) {
				getPlace().updateOpenWidgetId(response);
			}
		});
	}

	private void goBack() {

		if (getPlace().getListType() == ListType.EC) {
			goTo(new ECReportSpecEditorPlace(getPlace().getListType(), getPlace().getEditorType(),
					getPlace().getPathList(), getPlace().getOpenWidgetIdList()));
		} else if (getPlace().getListType() == ListType.PC) {
			goTo(new PcReportSpecItemEditorPlace(getPlace().getListType(), getPlace().getEditorType(),
					getPlace().getPathList(), getPlace().getOpenWidgetIdList()));
		} else if (getPlace().getListType() == ListType.CC) {
			goTo(new CCCmdSpecEditorPlace(getPlace().getListType(), getPlace().getEditorType(),
					getPlace().getPathList(), getPlace().getOpenWidgetIdList()));
		} else {
			History.back();
		}
	}

	private void checkForNewTM() {
		String newTMFieldName = commonStorage.getNewTMFieldName();
		if (!Utils.isNullOrEmpty(newTMFieldName)) {

			if (filterListMember.getFieldspec() == null) {
				filterListMember.setFieldspec(new ECFieldSpec());
			}

			// commonStorage.getNewTMSpecName(); - not used
			commonStorage.setNewTMSpecName(null);

			filterListMember.getFieldspec().setFieldname(newTMFieldName);
			filterListMember.getFieldspec().setDatatype(null);
			filterListMember.getFieldspec().setFormat(null);
			commonStorage.setNewTMFieldName(null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onDataLoaded(Object data) {
		ecFilterListMembers = (List<ECFilterListMember>) data;

		if (ecFilterListMembers == null) {
			showErrorMessage("PCReportSpec: filterSpec.filterList.filter is NULL!");
			return;
		}

		if (getPlace().isNew() && Utils.isNullOrEmpty(commonStorage.getNewTMFieldName())) {
			filterListMember = new ECFilterListMember();
			ECFieldSpec fsp = new ECFieldSpec();
			filterListMember.setFieldspec(fsp);
			PatList pl = new PatList();
			pl.getPat();
			filterListMember.setPatList(pl);

			ecFilterListMembers.add(filterListMember);
			currentIndex = ecFilterListMembers.size() - 1;
			getPlace().updatePathId("NEW" + currentIndex);
		} else {
			currentIndex = getPlace().getPathAsInt(2);
			filterListMember = ecFilterListMembers.get(currentIndex);
		}

		checkForNewTM();
		setData(filterListMember);
	}

	@Override
	protected void initializeEditorComponents() {
		initializeListWidgetHandlers();

		initializeOpenWidgetHandling();

		tms.fetchFieldNames(getEventBus(), new TMFieldNamesCallback() {
			@Override
			public void onGotTMFieldNames(Map<String, List<String>> map) {
				editor.setTMFields(tms.getFixedFields(), tms.getVariableFields());
			}
		});

		editor.filterListMemberFieldEditor.fieldSpecEditor.addNewItemHandler(new NewItemEvent.Handler() {

			@Override
			public void onNewItem(NewItemEvent event) {
				commonStorage.setNewTMFieldName(res.newEntry());
				goTo(new EditorPlace(ListType.EC, EditorType.TM, Utils.getNewId(), false, "0"));
			}
		});
	}
}
