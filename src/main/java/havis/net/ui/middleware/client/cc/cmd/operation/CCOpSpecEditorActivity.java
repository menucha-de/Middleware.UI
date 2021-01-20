package havis.net.ui.middleware.client.cc.cmd.operation;

import havis.middleware.ale.service.ECFieldSpec;
import havis.middleware.ale.service.cc.CCOpDataSpec;
import havis.middleware.ale.service.cc.CCOpSpec;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.cc.CCCmdSpecEditorPlace;
import havis.net.ui.middleware.client.place.pc.OpSpecEditorPlace;
import havis.net.ui.middleware.client.shared.DataSourceNamesCallback;
import havis.net.ui.middleware.client.shared.EditorBaseActivity;
import havis.net.ui.middleware.client.shared.TMFieldNamesCallback;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.CCSpecStorage;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.shared.storage.DASpecStorage;
import havis.net.ui.middleware.client.shared.storage.DCSpecStorage;
import havis.net.ui.middleware.client.shared.storage.DRSpecStorage;
import havis.net.ui.middleware.client.shared.storage.TMSpecStorage;
import havis.net.ui.middleware.client.utils.Utils;

import java.util.List;
import java.util.Map;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.inject.Inject;

public class CCOpSpecEditorActivity extends EditorBaseActivity<OpSpecEditorPlace, CCOpSpec, CCOpSpecEditor> {

	interface Driver extends SimpleBeanEditorDriver<CCOpSpec, CCOpSpecEditor> {
	}

	interface Codec extends JsonEncoderDecoder<CCOpSpec> {}

	@Inject
	public CCOpSpecEditorActivity(Driver driver, CCOpSpecEditor editor) {
		super(driver, editor, ConstantsResource.INSTANCE.cmdOperation(), (Codec) GWT.create(Codec.class));
		this.editor = editor;
	}

	private ConstantsResource res = ConstantsResource.INSTANCE;

	@Inject
	private CommonStorage commonStorage;

	@Inject
	private CCSpecStorage storage;

	@Inject
	private TMSpecStorage tms;
	
	@Inject
	private DASpecStorage das;
	
	@Inject
	private DCSpecStorage dcs;
	
	@Inject
	private DRSpecStorage drs;

	private CCOpSpecEditor editor;

	private List<CCOpSpec> ccOpSpecs;

	private CCOpSpec ccOpSpec;

	private int currentIndex = -1;

	@Override
	public void onClose() {
		super.onClose();
		if (getPlace().isNew() && ccOpSpecs != null && ccOpSpecs.size() > currentIndex) {
			if (ccOpSpecs != null)
				ccOpSpecs.remove(currentIndex);
			getPlace().updatePathId("" + (currentIndex - 1));
		}
		storage.removeEmptyLists(getPlace(), ccOpSpecs);
		goTo(new CCCmdSpecEditorPlace(ListType.CC, EditorType.CC, getPlace().getPathList(),
				getPlace().getOpenWidgetIdList()));
	}

	@Override
	public void onAccept() {
		super.onAccept();
		if (!hasErrors()) {
			storage.removeEmptyLists(getPlace(), ccOpSpecs);
			goTo(new CCCmdSpecEditorPlace(ListType.CC, EditorType.CC, getPlace().getPathList(),
					getPlace().getOpenWidgetIdList()));
		}
	}

	private void checkForNewTM() {
		String newTMFieldName = commonStorage.getNewTMFieldName();
		if (!Utils.isNullOrEmpty(newTMFieldName)) {
			if (ccOpSpec.getFieldspec() == null) {
				ccOpSpec.setFieldspec(new ECFieldSpec());
			}

			// TODO: better use Object getNewTMSpec();
			// ..and pass name and type
			String specName = commonStorage.getNewTMSpecName();
			if (!Utils.isNullOrEmpty(specName)) {
				ccOpSpec.setOpName(specName);
				ccOpSpec.setOpType(CCOpType.READ.toString()); // workaround
			}
			commonStorage.setNewTMSpecName(null);

			ccOpSpec.getFieldspec().setFieldname(newTMFieldName);
			ccOpSpec.getFieldspec().setDatatype(null);
			ccOpSpec.getFieldspec().setFormat(null);
			commonStorage.setNewTMFieldName(null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onDataLoaded(Object data) {
		ccOpSpecs = (List<CCOpSpec>) data;

		if (ccOpSpecs == null) {
			showErrorMessage("MCCommandCycleSpec: opSpecs.opSpec is NULL!");
			return;
		}

		if (getPlace().isNew() && Utils.isNullOrEmpty(commonStorage.getNewTMFieldName())) {
			ccOpSpec = new CCOpSpec();
			ccOpSpecs.add(ccOpSpec);
			currentIndex = ccOpSpecs.size() - 1;
			getPlace().setNew(currentIndex);
		} else {
			currentIndex = getPlace().getPathAsInt(2);
			ccOpSpec = ccOpSpecs.get(currentIndex);
		}

		checkForNewTM();

		if (ccOpSpec.getFieldspec() == null) {
			ccOpSpec.setFieldspec(new ECFieldSpec());
		}

		if (ccOpSpec.getDataSpec() == null) {
			ccOpSpec.setDataSpec(new CCOpDataSpec());
		}
		setData(ccOpSpec);
	}

	@Override
	protected void initializeEditorComponents() {
		editor.fieldSpecEditor.addNewItemHandler(new NewItemEvent.Handler() {
			@Override
			public void onNewItem(NewItemEvent event) {
				commonStorage.setNewTMSpecName(event.getName());
				commonStorage.setNewTMFieldName(res.newEntry());
				goTo(new EditorPlace(ListType.EC, EditorType.TM, Utils.getNewId(), false, "0"));
			}
		});

		tms.fetchFieldNames(getEventBus(), new TMFieldNamesCallback() {
			@Override
			public void onGotTMFieldNames(Map<String, List<String>> map) {
				editor.fieldSpecEditor.setTMFields(tms.getFixedFields(), tms.getVariableFields());				
			}
		});
		
		das.fetchDataSourceNames(getEventBus(), new DataSourceNamesCallback(){

			@Override
			public void onGotDataSourceNames(List<String> names) {
				editor.setAssociationNames(names);
			}
			
		});
		
		dcs.fetchDataSourceNames(getEventBus(), new DataSourceNamesCallback(){

			@Override
			public void onGotDataSourceNames(List<String> names) {
				editor.setCacheNames(names);
			}
			
		});
		
		drs.fetchDataSourceNames(getEventBus(), new DataSourceNamesCallback(){

			@Override
			public void onGotDataSourceNames(List<String> names) {
				editor.setRandomNames(names);
			}
			
		});
	}
}
