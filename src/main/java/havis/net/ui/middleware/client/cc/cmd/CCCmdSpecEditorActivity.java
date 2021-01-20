package havis.net.ui.middleware.client.cc.cmd;

import havis.middleware.ale.service.cc.CCCmdSpec;
import havis.middleware.ale.service.cc.CCCmdSpec.OpSpecs;
import havis.middleware.ale.service.cc.CCCmdSpec.StatProfileNames;
import havis.middleware.ale.service.cc.CCFilterSpec;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.cc.CCCmdSpecEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterItemEditorPlace;
import havis.net.ui.middleware.client.place.pc.OpSpecEditorPlace;
import havis.net.ui.middleware.client.shared.EditorBaseActivity;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.CCSpecStorage;
import havis.net.ui.shared.client.SectionExpandedCallback;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.inject.Inject;

public class CCCmdSpecEditorActivity extends EditorBaseActivity<CCCmdSpecEditorPlace, CCCmdSpec, CCCmdSpecEditor> {

	interface Codec extends JsonEncoderDecoder<CCCmdSpec> {}
	
	@Inject
	public CCCmdSpecEditorActivity(Driver driver, CCCmdSpecEditor editor) {
		super(driver, editor, "Command", (Codec) GWT.create(Codec.class));
		this.editor = editor;
	}

	private ConstantsResource res = ConstantsResource.INSTANCE;

	@Inject
	private CCSpecStorage storage;

	interface Driver extends SimpleBeanEditorDriver<CCCmdSpec, CCCmdSpecEditor> {
	}

	private CCCmdSpecEditor editor;

	private List<CCCmdSpec> ccCmdSpecs;

	private int currentIndex = -1;

	private void initializeListWidgetHandlers() {
		editor.configSecFilterSpecs.addNewItemHandler(new NewItemEvent.Handler() {
			@Override
			public void onNewItem(NewItemEvent event) {
				flushChanges();
				updatePathId();
				goTo(new FilterItemEditorPlace(getPlace(), true, -1, "0")); // goTo...FilterItemEditorActivity()

			}
		});
		editor.configSecFilterSpecs.setOpenItemHandler(new OpenItemEvent.Handler() {
			@Override
			public void onOpenItem(OpenItemEvent event) {
				flushChanges();
				updatePathId();
				goTo(new FilterItemEditorPlace(getPlace(), false, event.getIndex(),
						selectOpenWidgetId(event.getColumn()))); // goTo...FilterItemEditorActivity()
			}
		});
		editor.configSecOperations.addNewItemHandler(new NewItemEvent.Handler() {
			@Override
			public void onNewItem(NewItemEvent event) {
				flushChanges();
				updatePathId();
				goTo(new OpSpecEditorPlace(getPlace(), true, -1, "0")); // goTo...OpSpecItemEditorActivity()

			}
		});
		editor.configSecOperations.setOpenItemHandler(new OpenItemEvent.Handler() {
			@Override
			public void onOpenItem(OpenItemEvent event) {
				flushChanges();
				updatePathId();
				goTo(new OpSpecEditorPlace(getPlace(), false, event.getIndex(), "0"));
			}
		});
	}

	/**
	 * update the path and mark it as 'FormerNew' if necessary
	 */
	private void updatePathId() {
		if (getPlace().isNew()) {
			getPlace().setFormerNew(currentIndex);
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		if ((getPlace().isNew() || getPlace().isFormerNew()) && ccCmdSpecs != null
				&& ccCmdSpecs.size() > currentIndex) {
			if (ccCmdSpecs != null)
				ccCmdSpecs.remove(currentIndex);
			getPlace().updatePathId("" + (currentIndex - 1));

		}
		storage.removeEmptyLists(getPlace(), ccCmdSpecs);
		goBack();
	}

	@Override
	public void onAccept() {
		super.onAccept();
		if (!hasErrors()) {
			// Check for name doublets
			List<String> nameList = new ArrayList<String>();
			for (CCCmdSpec spec : ccCmdSpecs) {
				if (!nameList.contains(spec.getName())) {
					nameList.add(spec.getName());
				} else {
					showErrorMessage(res.errorAlreadyExists("report", spec.getName()));
					return;
				}
			}

			storage.removeEmptyLists(getPlace(), ccCmdSpecs);
			goBack();
		}
	}

	private void goBack() {
		goTo(new EditorPlace(getPlace().getListType(), getPlace().getEditorType(), getPlace().getPathList(),
				getPlace().getOpenWidgetIdList()));
	}

	/**
	 * Selects the appropriate openWidgetId
	 * 
	 * @param column
	 * @return openWidgetId
	 */
	private String selectOpenWidgetId(int column) {
		String openWidgetId = "0";
		switch (column) {
		case 1:
			openWidgetId = "1";
			break;
		case 2:
			openWidgetId = "2";
			break;
		default:
			break;
		}
		return openWidgetId;
	}

	/**
	 * OpenWidgetHandling stores information about opened/closed config sections
	 * utilizing the place history mechanism
	 */
	private void initializeOpenWidgetHandling() {
		// opens the specified section
		editor.getConfigSections().setOpen(getPlace().getOpenWidgetIdAsArr(1));

		// adds the ExpandedHandler using the specified place level
		editor.getConfigSections().setSectionExpandedLevel(new SectionExpandedCallback<String>() {
			@Override
			public void onExpandedChanged(String response) {
				getPlace().updateOpenWidgetId(response);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onDataLoaded(Object data) {
		ccCmdSpecs = (List<CCCmdSpec>) data;

		if (ccCmdSpecs == null) {
			showErrorMessage("CCCmdSpec: spec.CmdSpecs is NULL!");
			return;
		}

		CCCmdSpec ccCmdSpec;
		if (getPlace().isNew()) {
			ccCmdSpec = new CCCmdSpec();
			ccCmdSpec.setName("");
			ccCmdSpecs.add(ccCmdSpec);
			currentIndex = ccCmdSpecs.size() - 1;
			getPlace().setNew(currentIndex);
		} else {
			currentIndex = getPlace().getPathAsInt(1);
			ccCmdSpec = ccCmdSpecs.get(currentIndex);
		}

		if (ccCmdSpec.getFilterSpec() == null) {
			ccCmdSpec.setFilterSpec(new CCFilterSpec());
		}
		if (ccCmdSpec.getOpSpecs() == null) {
			ccCmdSpec.setOpSpecs(new OpSpecs());
		}

		if (ccCmdSpec.getStatProfileNames() == null) {
			ccCmdSpec.setStatProfileNames(new StatProfileNames());
			ccCmdSpec.getStatProfileNames().getStatProfileName();
		}
		setData(ccCmdSpec);
	}

	@Override
	protected void initializeEditorComponents() {
		initializeListWidgetHandlers();
		initializeOpenWidgetHandling();
	}

}
