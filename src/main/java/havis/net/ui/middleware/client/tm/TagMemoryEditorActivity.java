package havis.net.ui.middleware.client.tm;

import havis.middleware.ale.service.mc.MCTagMemorySpec;
import havis.middleware.ale.service.tm.TMFixedFieldListSpec;
import havis.middleware.ale.service.tm.TMFixedFieldListSpec.FixedFields;
import havis.middleware.ale.service.tm.TMFixedFieldSpec;
import havis.middleware.ale.service.tm.TMVariableFieldListSpec;
import havis.middleware.ale.service.tm.TMVariableFieldListSpec.VariableFields;
import havis.net.rest.middleware.tm.TMAsync;
import havis.net.rest.shared.data.SerializableValue;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.shared.BaseActivity;
import havis.net.ui.middleware.client.shared.EditorDialogView;
import havis.net.ui.middleware.client.shared.LoadingSpinner;
import havis.net.ui.middleware.client.shared.event.SpecAddedEvent;
import havis.net.ui.middleware.client.shared.event.SpecChangedEvent;
import havis.net.ui.middleware.client.shared.event.SpecLoadedEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.shared.storage.TMSpecStorage;
import havis.net.ui.middleware.client.tm.data.MCTagMemoryExtendedSpec;
import havis.net.ui.middleware.client.tm.data.TagMemoryType;
import havis.net.ui.middleware.client.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class TagMemoryEditorActivity extends BaseActivity implements EditorDialogView.Presenter {

	@Inject
	private EditorDialogView view;

	@Inject
	private PlaceController placeController;

	@Inject
	private CommonStorage commonStorage;

	@Inject
	private TMSpecStorage storage;

	@Inject
	TMAsync service;

	@Inject
	private FixedFieldDriver fixedFieldDriver;

	interface FixedFieldDriver extends SimpleBeanEditorDriver<MCTagMemoryExtendedSpec, TMFixedFieldListEditor> {
	}

	@Inject
	private TMFixedFieldListEditor fixedTagMemory;

	@Inject
	private VariableFieldDriver variableFieldDriver;

	interface VariableFieldDriver extends SimpleBeanEditorDriver<MCTagMemoryExtendedSpec, TMVariableFieldListEditor> {
	}

	@Inject
	private TMVariableFieldListEditor variableTagMemory;

	private MCTagMemoryExtendedSpec spec;

	private ConstantsResource res = ConstantsResource.INSTANCE;

	private List<String> dblList = new ArrayList<String>();

	@Override
	public void bind() {
		view.setPresenter(this);
	}

	private ChangeHandler typeChangeHanlder = new ChangeHandler() {

		@Override
		public void onChange(ChangeEvent event) {
			ListBox src = (ListBox) event.getSource();
			storage.setSpec(spec.getId(), spec);
			if (TagMemoryType.VARIABLE.toString().equals(src.getSelectedValue())) {
				TMVariableFieldListSpec tmVariableFieldListSpec = new TMVariableFieldListSpec();
				VariableFields variableFields = new VariableFields();
				tmVariableFieldListSpec.setVariableFields(variableFields);
				spec.setSpec(tmVariableFieldListSpec);
			} else {
				TMFixedFieldListSpec tmFixedFieldListSpec = new TMFixedFieldListSpec();
				FixedFields fixedFields = new FixedFields();
				tmFixedFieldListSpec.setFixedFields(fixedFields);
				spec.setSpec(tmFixedFieldListSpec);
			}
			String name = storage.getSpec(spec.getId()).getName();
			spec.setName(name);

			initView();
		}
	};

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		setEventBus(eventBus);
		panel.setWidget(view.asWidget());
		bind();

		EditorPlace place = (EditorPlace) placeController.getWhere();

		fixedFieldDriver.initialize(fixedTagMemory);
		variableFieldDriver.initialize(variableTagMemory);

		view.setEnabledAcceptButton(false);
		view.setEditorTitle(ConstantsResource.INSTANCE.tagMemory());
		view.setEditor(new LoadingSpinner());

		SpecLoadedEvent.register(eventBus, this, new SpecLoadedEvent.Handler() {

			@Override
			public void onSpecLoaded(SpecLoadedEvent event) {
				if (event.getSpec() == null) {
					spec = new MCTagMemoryExtendedSpec();
					spec.setEnable(false);
					TMFixedFieldListSpec tmFixedFieldListSpec = new TMFixedFieldListSpec();
					FixedFields fixedFields = new FixedFields();
					tmFixedFieldListSpec.setFixedFields(fixedFields);
					spec.setSpec(tmFixedFieldListSpec);
				} else {
					spec = new MCTagMemoryExtendedSpec((MCTagMemorySpec) event.getSpec());
				}

				initView();

				view.setEnabledAcceptButton(true);
			}

			@Override
			public void onFailure(SpecLoadedEvent event, String message) {
				view.setEditor(null);
				showErrorMessage(message);
			}
		});

		fixedTagMemory.type.addChangeHandler(typeChangeHanlder);
		variableTagMemory.type.addChangeHandler(typeChangeHanlder);

		storage.loadSpec(place, this);
	}

	private void initView() {
		if (spec.getSpec() instanceof TMFixedFieldListSpec) {
			fixedTagMemory.type.setValue(TagMemoryType.FIXED, false);
			fixedFieldDriver.edit(spec);
			view.setEditor(fixedTagMemory);
			view.setEnableButton(fixedTagMemory.enable);
		} else {
			variableTagMemory.type.setValue(TagMemoryType.VARIABLE, false);
			variableFieldDriver.edit(spec);
			view.setEditor(variableTagMemory);
			view.setEnableButton(variableTagMemory.enable);
		}
	}

	@Override
	public void onClose() {
		History.back();
	}

	@Override
	public void onAccept() {
		SimpleBeanEditorDriver<?, ?> driver;
		if (spec.getFixedFieldListSpec() != null) {
			driver = fixedFieldDriver;
		} else {
			driver = variableFieldDriver;
		}
		// flush() will be called in a depth-first order by the EditorDriver
		driver.flush();
		if (driver.hasErrors()) {
			String error = "";
			// so show last error only
			for (EditorError e : driver.getErrors()) {
				error = e.getMessage() + '\n';
			}
			showErrorMessage(error);
			return;
		}

		final MCTagMemorySpec tmSpec = spec.getMCTagMemorySpec();

		if (tmSpec != null && tmSpec.getSpec() instanceof TMFixedFieldListSpec) {
			TMFixedFieldListSpec fixedSpec = (TMFixedFieldListSpec) tmSpec.getSpec();
			if (fixedSpec != null) {
				// check if name exists in that TM
				dblList.clear();

				// if it starts with <New>, the last added name is stored in
				// NewTMFieldName
				// the appearing screen after 'Apply' will show that name as
				// selected name.

				boolean isNewEntry = commonStorage.getNewTMFieldName() != null
						&& commonStorage.getNewTMFieldName().startsWith(res.newEntry());
				commonStorage.setNewTMFieldName(null);

				for (TMFixedFieldSpec s : fixedSpec.getFixedFields().getFixedField()) {
					if (!dblList.contains(s.getFieldname())) {
						dblList.add(s.getFieldname());
						if (isNewEntry)
							commonStorage.setNewTMFieldName(s.getFieldname());
					} else {
						showErrorMessage(res.errorAlreadyDefinedHere(s.getFieldname()));
						return;
					}

				}
			}
		}
		if (Utils.isNullOrEmpty(spec.getId())) {
			service.getSpecs().add(tmSpec, new MethodCallback<SerializableValue<String>>() {

				@Override
				public void onFailure(Method method, Throwable exception) {
					showException(exception);
				}

				@Override
				public void onSuccess(Method method, SerializableValue<String> response) {
					tmSpec.setId(response.getValue());
					storage.setSpec(response.getValue(), tmSpec);

					History.back();
					getEventBus().fireEvent(new SpecAddedEvent(response.getValue()));
				}
			});
		} else {
			service.getSpecs().set(spec.getId(), tmSpec, new MethodCallback<Void>() {

				@Override
				public void onSuccess(Method method, Void response) {
					storage.setSpec(spec.getId(), tmSpec);
					History.back();
					getEventBus().fireEvent(new SpecChangedEvent(spec.getId()));
				}

				@Override
				public void onFailure(Method method, Throwable exception) {
					showException(exception);
				}
			});
		}

	}
}
