package havis.net.ui.middleware.client.lr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import havis.middleware.ale.service.lr.LRSpec;
import havis.middleware.ale.service.mc.MCLogicalReaderSpec;
import havis.net.rest.middleware.lr.LRAsync;
import havis.net.rest.shared.data.SerializableValue;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.shared.BaseActivity;
import havis.net.ui.middleware.client.shared.EditorDialogView;
import havis.net.ui.middleware.client.shared.LoadingSpinner;
import havis.net.ui.middleware.client.shared.event.ItemsLoadedEvent;
import havis.net.ui.middleware.client.shared.event.NewItemEvent;
import havis.net.ui.middleware.client.shared.event.SpecAddedEvent;
import havis.net.ui.middleware.client.shared.event.SpecChangedEvent;
import havis.net.ui.middleware.client.shared.event.SpecLoadedEvent;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.shared.storage.LRSpecStorage;
import havis.net.ui.middleware.client.utils.Utils;

public class ReaderEditorActivity extends BaseActivity implements EditorDialogView.Presenter {

	@Inject
	private EditorDialogView view;

	@Inject
	private PlaceController placeController;

	@Inject
	private LRAsync service;

	@Inject
	private CommonStorage commonStorage;
	
	@Inject
	private LRSpecStorage storage;

	@Inject
	private Driver driver;
	
	interface Driver extends SimpleBeanEditorDriver<MCLogicalReaderSpec, ReaderEditor> {
	}

	@Inject
	private ReaderEditor editor;

	private MCLogicalReaderSpec spec;
	
	private PopupPanel overlay;

	private boolean hasError;

	@Override
	public void bind() {
		view.setPresenter(this);
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		setEventBus(eventBus);
		panel.setWidget(view.asWidget());
		bind();
		overlay = new PopupPanel(false, true);
		Utils.blockUi(overlay, 300);
		
		EditorPlace place = (EditorPlace) placeController.getWhere();
		final String id = place.getSpecId();
		view.setEditorTitle("Logical Reader");
		view.setEnableButton(editor.enable);
		view.setEnabledAcceptButton(false);
		
		driver.initialize(editor);
		view.setEditor(new LoadingSpinner());

		SpecLoadedEvent.register(eventBus, editor, new SpecLoadedEvent.Handler() {

			@Override
			public void onSpecLoaded(SpecLoadedEvent event) {
				spec = (MCLogicalReaderSpec) event.getSpec();
				if (spec == null) {
					spec = new MCLogicalReaderSpec();
					spec.setEnable(false);
				}
				storage.clone(spec);
				loadSpec();
				view.setEditor(editor);

				checkForNewLR();
				
				driver.edit(spec);
				
				editor.reader.setVisible(spec.getSpec().isIsComposite());
				view.setEnabledAcceptButton(true);
			}

			private void checkForNewLR() {
				String newLRSpecName = commonStorage.getNewLRSpecName();
				if (!Utils.isNullOrEmpty(newLRSpecName)) {
					spec.getSpec().getReaders().getReader().add(commonStorage.getNewLRSpecName());
					commonStorage.setNewLRSpecName(null);
				}
			}

			@Override
			public void onFailure(SpecLoadedEvent event, String message) {
				view.setEditor(null);
				showErrorMessage(message);
			}
		});

		ItemsLoadedEvent.register(eventBus, editor.reader, new ItemsLoadedEvent.Handler() {

			@Override
			public void onItemsLoaded(ItemsLoadedEvent event) {
				Collection<MCLogicalReaderSpec> lrSpecs = storage.getList();
				List<String> values = new ArrayList<String>();
				if (lrSpecs != null) {
					for (MCLogicalReaderSpec spec : lrSpecs) {
						if (!Utils.isNullOrEmpty(spec.getId()))
							values.add(spec.getName());
					}
				}
				editor.reader.setAcceptableValues(values);
			}

			@Override
			public void onFailure(ItemsLoadedEvent event, String message) {
				showErrorMessage(message);
			}
		});

		editor.reader.setNewItemHandler(new NewItemEvent.Handler() {

			@Override
			public void onNewItem(NewItemEvent event) {
				driver.flush();
				storage.setSpec(id, spec);
				commonStorage.setNewLRSpecName("");
				placeController.goTo(new EditorPlace(ListType.LR, EditorType.LR, Utils.getNewId(),true, "0"));
			}
		});

		spec = null;
				
		storage.loadList(editor.reader);
		storage.loadSpec(place, editor);
	}

	@Override
	public void onClose() {
		if (hasError) {
			storage.setSpec(spec.getId(), storage.getClone());
			storage.resetClone();
			hasError = false;
		}
		History.back();
	}

	private void loadSpec() {
		LRSpec lrSpec = spec.getSpec();
		if (spec.getSpec() == null) {
			lrSpec = new LRSpec();
			spec.setSpec(lrSpec);
		}

		if (lrSpec.isIsComposite() == null) {
			lrSpec.setIsComposite(false);
		}

		if (lrSpec.getProperties() == null)
			lrSpec.setProperties(new LRSpec.Properties());

		if (lrSpec.getReaders() == null)
			lrSpec.setReaders(new LRSpec.Readers());

//		if (value != null && value.getSpec() != null && value.getSpec().isIsComposite() != null)
//			reader.setVisible(value.getSpec().isIsComposite());
	}
	
	private void saveSpec() {
		if (spec.getSpec() != null) {
			if (spec.getSpec().getProperties() != null && spec.getSpec().getProperties().getProperty().isEmpty())
				spec.getSpec().setProperties(null);
			if (!spec.getSpec().isIsComposite() && spec.getSpec().getReaders() != null) {
				spec.getSpec().getReaders().getReader().clear();
			}
			if (spec.getSpec().getReaders() != null && spec.getSpec().getReaders().getReader().isEmpty()) {
				spec.getSpec().setReaders(null);
			}
		}
	}
	
	@Override
	public void onAccept() {
		driver.flush();
		if (driver.hasErrors()) {
			String error = "";
			for (EditorError e : driver.getErrors()) {
				error += e.getMessage() + '\n';
			}
			showErrorMessage(error);
		} else {
			saveSpec();
			if (Utils.isNullOrEmpty(spec.getId())) {
				addSpec();
			} else {
				updateSpec();
			}
		}
	}

	private void updateSpec() {
		service.getSpecs().set(spec.getId(), spec, new MethodCallback<Void>() {

			@Override
			public void onSuccess(Method method, Void response) {
				storage.setSpec(spec.getId(), spec);
				
				getEventBus().fireEvent(new SpecChangedEvent(spec.getId()));
				History.back();
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				showException(exception);
				loadSpec();
				driver.edit(spec);
				hasError = true;
			}
		});
	}

	private void addSpec() {
		service.getSpecs().add(spec, new MethodCallback<SerializableValue<String>>() {

			@Override
			public void onSuccess(Method method, SerializableValue<String> response) {
				spec.setId(response.getValue());
				storage.setSpec(response.getValue(), spec);
				if (commonStorage.getNewLRSpecName() != null) {
					commonStorage.setNewLRSpecName(spec.getName());
				}
				
				getEventBus().fireEvent(new SpecAddedEvent(response.getValue()));
				History.back();
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				showException(exception);
				loadSpec();
				driver.edit(spec);
			}
		});
	}
}
