package havis.net.ui.middleware.client.pc.rep;

import havis.middleware.ale.service.pc.PCFilterSpec;
import havis.middleware.ale.service.pc.PCFilterSpec.FilterList;
import havis.middleware.ale.service.pc.PCOpSpecs;
import havis.middleware.ale.service.pc.PCReportSpec;
import havis.middleware.ale.service.pc.PCReportSpec.StatProfileNames;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.TriggerListType;
import havis.net.ui.middleware.client.place.TriggerPlace;
import havis.net.ui.middleware.client.place.ec.FilterItemEditorPlace;
import havis.net.ui.middleware.client.place.pc.OpSpecEditorPlace;
import havis.net.ui.middleware.client.place.pc.PcReportSpecItemEditorPlace;
import havis.net.ui.middleware.client.shared.BaseActivity;
import havis.net.ui.middleware.client.shared.EditorDialogView;
import havis.net.ui.middleware.client.shared.LoadingSpinner;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.OpenItemEvent;
import havis.net.ui.middleware.client.shared.event.SpecLoadedEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.PCSpecStorage;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.SectionExpandedCallback;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class PCReportSpecEditorActivity extends BaseActivity implements EditorDialogView.Presenter {

	private ConstantsResource res = ConstantsResource.INSTANCE;

	@Inject
	PCSpecStorage storage;

	@Inject
	EditorDialogView view;

	@Inject
	PlaceController placeController;

	@Inject
	private Driver driver;

	interface Driver extends SimpleBeanEditorDriver<PCReportSpec, PCReportSpecEditor> {
	}

	@Inject
	PCReportSpecEditor editor;

	private List<PCReportSpec> pcReportSpecs;

	private PCReportSpec pcReportSpec;

	private int currentIndex = -1;

	private PcReportSpecItemEditorPlace place;

	private PopupPanel overlay;

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {

		overlay = new PopupPanel(false, true);
		Utils.blockUi(overlay, 300);

		setEventBus(eventBus);
		panel.setWidget(view.asWidget());
		bind();

		place = (PcReportSpecItemEditorPlace) placeController.getWhere();

		view.setEditorTitle(res.report());
		view.setEnableButton(null);
		view.setEnabledAcceptButton(false);
		view.setEditor(new LoadingSpinner());
		driver.initialize(editor);

		initializeListWidgetHandlers();

		initializeOpenWidgetHandling();

		SpecLoadedEvent.register(eventBus, this, new SpecLoadedEvent.Handler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSpecLoaded(SpecLoadedEvent event) {

				if (event.getSpec() instanceof List<?>) {
					pcReportSpecs = (List<PCReportSpec>) event.getSpec();

					if (pcReportSpecs == null) {
						showErrorMessage("MCPortCycleSpec: spec.reportSpecs is NULL!");
						return;
					}

					if (place.isNew()) {
						pcReportSpec = new PCReportSpec();
						pcReportSpecs.add(pcReportSpec);
						currentIndex = pcReportSpecs.size() - 1;
						place.setNew(currentIndex);
					} else {
						currentIndex = place.getPathAsInt(1);
						pcReportSpec = pcReportSpecs.get(currentIndex);
					}

					if (pcReportSpec.getFilterSpec() == null) {
						pcReportSpec.setFilterSpec(new PCFilterSpec());
					}
					if (pcReportSpec.getFilterSpec().getFilterList() == null) {
						FilterList fl = new FilterList();
						fl.getFilter();
						pcReportSpec.getFilterSpec().setFilterList(fl);
					}

					if (pcReportSpec.getOpSpecs() == null) {
						PCOpSpecs ops = new PCOpSpecs();
						ops.getOpSpec();
						pcReportSpec.setOpSpecs(ops);
					}
					if (pcReportSpec.getStatProfileNames() == null) {
						StatProfileNames spn = new StatProfileNames();
						spn.getStatProfileName();
						pcReportSpec.setStatProfileNames(spn);
					}

					driver.edit(pcReportSpec);
					view.setEnabledAcceptButton(true);
					view.setEditor(editor);
				} else {
					view.setEditor(null);
					showErrorMessage("MCSpec error.");
				}
			}

			@Override
			public void onFailure(SpecLoadedEvent event, String message) {
				view.setEditor(null);
				showErrorMessage(message);
			}
		});

		storage.loadSpec(place, this); // triggers SpecLoadedEvent...
	}

	private void initializeListWidgetHandlers() {

		editor.configSecFilter.addNewItemHandler(new NewItemEvent.Handler() {
			@Override
			public void onNewItem(NewItemEvent event) {
				driver.flush();
				updatePathId();
				placeController.goTo(new FilterItemEditorPlace(place, true, -1, "0")); // goTo...FilterItemEditorActivity()
			}
		});
		editor.configSecFilter.setOpenItemHandler(new OpenItemEvent.Handler() {
			@Override
			public void onOpenItem(OpenItemEvent event) {
				driver.flush();
				updatePathId();
				placeController.goTo(new FilterItemEditorPlace(place, false, event.getIndex(),
						selectOpenWidgetId(event.getColumn()))); // goTo...FilterItemEditorActivity()
			}
		});

		editor.configSecTrigger.addNewItemHandler(new NewItemEvent.Handler() {
			@Override
			public void onNewItem(NewItemEvent event) {
				driver.flush();
				updatePathId();
				placeController.goTo(new TriggerPlace(place, TriggerListType.REPORT, true, -1, "0")); // goTo...TriggerEditorActivity()
			}
		});
		editor.configSecTrigger.setOpenItemHandler(new OpenItemEvent.Handler() {
			@Override
			public void onOpenItem(OpenItemEvent event) {
				driver.flush();
				updatePathId();
				placeController.goTo(new TriggerPlace(place, TriggerListType.REPORT, false, event.getIndex(), "0")); // goTo...TriggerEditorActivity()
			}
		});

		editor.configSecOperation.addNewItemHandler(new NewItemEvent.Handler() {
			@Override
			public void onNewItem(NewItemEvent event) {
				driver.flush();
				updatePathId();
				placeController.goTo(new OpSpecEditorPlace(place, true, -1, "0")); // goTo...OpSpecItemEditorActivity()
			}
		});
		editor.configSecOperation.setOpenItemHandler(new OpenItemEvent.Handler() {
			@Override
			public void onOpenItem(OpenItemEvent event) {
				driver.flush();
				updatePathId();
				placeController.goTo(new OpSpecEditorPlace(place, false, event.getIndex(), "0"));
			}
		});

	}

	/**
	 * update the path and mark it as 'FormerNew' if necessary
	 */
	private void updatePathId() {
		if (place.isNew()) {
			place.setFormerNew(currentIndex);
		}
	}

	@Override
	public void bind() {
		view.setPresenter(this);
	}

	@Override
	public void onClose() {
		if ((place.isNew() || place.isFormerNew()) && pcReportSpecs != null && pcReportSpecs.size() > currentIndex) {
			overlay.show();
			pcReportSpecs.remove(currentIndex);
			place.updatePathId("" + (currentIndex - 1));
			overlay.hide();
		}
		storage.removeEmptyLists(place, pcReportSpecs);
		goBack();
	}

	@Override
	public void onAccept() {
		overlay.show();
		driver.flush(); // acts like save as well

		if (driver.hasErrors()) {
			StringBuilder builder = new StringBuilder();
			for (EditorError e : driver.getErrors()) {
				builder.append(e.getMessage());
				builder.append('\n');
			}
			showErrorMessage(builder.toString());
			overlay.hide();
			return;
		}

		// Check for name doublets
		List<String> nameList = new ArrayList<String>();
		for (PCReportSpec spec : pcReportSpecs) {
			if (!nameList.contains(spec.getName())) {
				nameList.add(spec.getName());
			} else {
				showErrorMessage(res.errorAlreadyExists("report", spec.getName()));
				overlay.hide();
				return;
			}
		}

		// pcReportSpecs.set(currentIndex, pcReportSpec); - not necessary,
		// driver.flush() did it already.
		storage.removeEmptyLists(place, pcReportSpecs);
		overlay.hide();
		goBack();
	}

	/**
	 * OpenWidgetHandling stores information about opened/closed config sections
	 * utilizing the place history mechanism
	 */
	private void initializeOpenWidgetHandling() {

		// opens the specified section
		editor.getConfigSections().setOpen(place.getOpenWidgetIdAsArr(1));

		// adds the ExpandedHandler using the specified place level
		editor.getConfigSections().setSectionExpandedLevel(new SectionExpandedCallback<String>() {
			@Override
			public void onExpandedChanged(String response) {
				place.updateOpenWidgetId(response);
			}
		});
	}

	/**
	 * Selects the appropriate openWidgetId, used to jump column related to the
	 * next view
	 * 
	 * @param column
	 * @return openWidgetId
	 */
	private String selectOpenWidgetId(int column) {
		if (column > 0 && column < 3) {
			return Integer.toString(column);
		}
		return "0";
	}

	private void goBack() {
		placeController.goTo(new EditorPlace(place.getListType(), place.getEditorType(), place.getPathList(),
				place.getOpenWidgetIdList()));
	}
}
