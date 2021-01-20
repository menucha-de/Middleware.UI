package havis.net.ui.middleware.client.shared;

import havis.middleware.ale.service.mc.MCSpec;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.ListPlace;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.shared.event.SpecAddedEvent;
import havis.net.ui.middleware.client.shared.event.SpecChangedEvent;
import havis.net.ui.middleware.client.shared.event.SpecLoadedEvent;
import havis.net.ui.middleware.client.shared.event.SpecSavedEvent;
import havis.net.ui.middleware.client.shared.storage.BaseSpecStorage;
import havis.net.ui.middleware.client.utils.Utils;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public abstract class MCSpecBaseActivity<M extends MCSpec, E extends MCSpecEditor<? super M>>
		extends BaseActivity implements EditorDialogView.Presenter {

	@Inject
	private EditorDialogView view;

	@Inject
	private PlaceController placeController;

	private PopupPanel overlay;
	private String editorTitle;
	private EditorPlace place;
	private M spec;
	private E editor;
	private SimpleBeanEditorDriver<M, E> driver;
	private BaseSpecStorage<M> storage;
	private ListType listType;

	public MCSpecBaseActivity(SimpleBeanEditorDriver<M, E> driver, String editorTitle, BaseSpecStorage<M> storage,
			E editor, ListType listType) {
		this.editorTitle = editorTitle;
		this.driver = driver;
		this.storage = storage;
		this.editor = editor;
		this.listType = listType;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		setEventBus(eventBus);

		panel.setWidget(view.asWidget());
		bind();

		overlay = new PopupPanel(false, true);
		Utils.blockUi(overlay, 300);

		view.setEditorTitle(editorTitle);
		view.setEnabledAcceptButton(false);
		view.setEditor(new LoadingSpinner());

		place = (EditorPlace) placeController.getWhere();

		driver.initialize(editor);

		initializeEditorComponents();

		SpecLoadedEvent.register(eventBus, this, new SpecLoadedEvent.Handler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSpecLoaded(SpecLoadedEvent event) {
				spec = (M) event.getSpec();

				prepareEditor();
				view.setEditor((Widget) editor);
				driver.edit(spec);
				view.setEnableButton(editor.enable());
				view.setEnabledAcceptButton(true);
			}

			@Override
			public void onFailure(SpecLoadedEvent event, String message) {
				view.setEditor(null);
				showErrorMessage(message);
			}

		});

		SpecSavedEvent.register(eventBus, this, new SpecSavedEvent.Handler() {

			@Override
			public void onSpecSaved(SpecSavedEvent event) {
				overlay.hide();
				storage.resetClone();
				placeController.goTo(new ListPlace(listType));
				if (event.isNewSpec()) {
					eventBus.fireEvent(new SpecAddedEvent(event.getId()));
				} else {
					eventBus.fireEvent(new SpecChangedEvent(event.getId()));
				}
			}

			@Override
			public void onFailure(SpecSavedEvent event, String message) {
				showErrorMessage(message);
				if (overlay != null)
					overlay.hide();
				// Current spec contains null values. 
				// The result is that the UI elements are not connected to any objects. 
				storage.setSpec(place.getSpecId(), storage.getClone());
				storage.loadSpec(place, this);
				// Null values has been replaced by empty Objects.
				spec = storage.getSpec(place.getSpecId());
				// Update view to connect new objects with the UI elements
				driver.edit(spec);
			}
		});

		storage.loadSpec(place, this);
	}

	@Override
	public void bind() {
		view.setPresenter(this);
	}

	@Override
	public void onClose() {
		placeController.goTo(new ListPlace(listType));
	}

	@Override
	public void onAccept() {
		// Lock UI
		overlay.show();
		
		driver.flush();

		if (driver.hasErrors()) {
			StringBuilder builder = new StringBuilder();
			for (EditorError e : driver.getErrors()) {
				builder.append(e.getMessage());
				builder.append('\n');
			}
			if (overlay != null)
				overlay.hide();
			showErrorMessage(builder.toString());
			return;
		}

		storage.clone(spec);
		storage.removeEmptyLists(place, spec);

		overlay.show();
		storage.saveSpec(spec, this);
	}

	protected EditorPlace getPlace() {
		return place;
	}
	
	protected M getMCSpec() {
		return spec;
	}

	protected void flushChanges() {
		driver.flush();
	}
	
	protected void goTo(Place place) {
		placeController.goTo(place);
	}
	
	protected void updateCache() {
		storage.setSpec(place.getSpecId(), spec);
	}
	
	protected void prepareEditor() {
		
	}
	
	protected abstract void initializeEditorComponents();
	
	

}
