package havis.net.ui.middleware.client.subscriber;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.Callback;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import havis.middleware.ale.service.mc.MCConnectorSpec;
import havis.middleware.ale.service.mc.MCSubscriberSpec;
import havis.net.rest.middleware.connector.ConnectorAsync;
import havis.net.rest.middleware.shared.HasSubscribers;
import havis.net.rest.shared.data.SerializableValue;
import havis.net.ui.middleware.client.place.SubscriberPlace;
import havis.net.ui.middleware.client.shared.BaseActivity;
import havis.net.ui.middleware.client.shared.EditorDialogView;
import havis.net.ui.middleware.client.shared.event.SpecAddedEvent;
import havis.net.ui.middleware.client.shared.event.SpecChangedEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.utils.Utils;
import havis.transport.ui.client.TransportType;
import havis.transport.ui.client.event.SaveTransportEvent;

public class SubscriberEditorActivity extends BaseActivity implements EditorDialogView.Presenter {

	@Inject
	EditorDialogView view;

	@Inject
	CommonStorage commonStorage;

	@Inject
	PlaceController placeController;

	@Inject
	ConnectorAsync connectorService;

	@Inject
	private SubscriberEditor editor;

	private String id;
	private MCSubscriberSpec spec;
	private HasSubscribers service;

	public void setService(HasSubscribers service) {
		this.service = service;
	}

	@Inject
	private Driver driver;

	interface Driver extends SimpleBeanEditorDriver<MCSubscriberSpec, SubscriberEditor> {
	}

	@Override
	public void bind() {
		view.setPresenter(this);
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		setEventBus(eventBus);
		panel.setWidget(view.asWidget());
		bind();

		SubscriberPlace place = (SubscriberPlace) placeController.getWhere();
		id = place.getSpecId();
		final String subId = place.getSubscriberId();

		this.service = commonStorage.getSubscribersService(place.getListType());

		driver.initialize(editor);

		setSupportedSubscriberTypes(new Callback<List<TransportType>, Throwable>() {
			@Override
			public void onFailure(Throwable exception) {
				showException(exception);
			}
			@Override
			public void onSuccess(List<TransportType> result) {
				view.setEnabledAcceptButton(false);

				view.setEditorTitle(ConstantsResource.INSTANCE.subscriberEditorTitle());
				editor.setTransportTypes(result);
				editor.addSaveTransportHandler(new SaveTransportEvent.Handler() {
					
					@Override
					public void onSaveTransport(SaveTransportEvent arg0) {
						if (driver.hasErrors()) {
							String error = "";
							for (EditorError e : driver.getErrors()) {
								error += e.getMessage() + '\n';
							}
							showErrorMessage(error);
						} else {
							if (Utils.isNullOrEmpty(spec.getId())) {
								service.addSubscriber(id, spec, new MethodCallback<SerializableValue<String>>() {

									@Override
									public void onFailure(Method method, Throwable exception) {
										showException(exception);
									}

									@Override
									public void onSuccess(Method method, SerializableValue<String> response) {
										History.back();
										getEventBus().fireEvent(new SpecAddedEvent(id));
									}
								});
							} else {
								service.setSubscriber(id, spec.getId(), spec, new MethodCallback<Void>() {

									@Override
									public void onFailure(Method method, Throwable exception) {
										showException(exception);
									}

									@Override
									public void onSuccess(Method method, Void response) {
										History.back();
										getEventBus().fireEvent(new SpecChangedEvent(id));
									}
								});
							}
						}
					}
				});
				if (Utils.isNullOrEmpty(subId) || subId.equals("NEW")) {
					spec = new MCSubscriberSpec();
					spec.setEnable(false);
					onGetResponse(spec);
				} else {
					service.getSubscriber(id, subId, new MethodCallback<MCSubscriberSpec>() {

						@Override
						public void onFailure(Method method, Throwable exception) {
							spec = null;
							showException(exception);
						}

						@Override
						public void onSuccess(Method method, MCSubscriberSpec response) {
							onGetResponse(response);
						}
					});
				}
			}
		});
	}

	private void setSupportedSubscriberTypes(final Callback<List<TransportType>, Throwable> callback) {
		connectorService.getSubscribers(new MethodCallback<List<MCConnectorSpec>>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				callback.onFailure(exception);
			}

			@Override
			public void onSuccess(Method method, List<MCConnectorSpec> response) {
				List<TransportType> types = new ArrayList<>();
				types.add(TransportType.CUSTOM);
				if (response != null) {
					for (MCConnectorSpec spec : response) {
						TransportType type = TransportType.valueByScheme(spec.getType().toLowerCase());
						if (type != null && type != TransportType.CUSTOM) {
							types.add(type);
						}
					}
				}
				callback.onSuccess(types);
			}
		});
	}

	private void onGetResponse(MCSubscriberSpec response) {
		spec = response;
		if (spec.getUri() == null) {
			spec.setUri("");
		}
		if (spec.getProperties() == null) {
			spec.setProperties(new MCSubscriberSpec.Properties());
		}
		driver.edit(spec);
		view.setEditor(editor);
		view.setEnableButton(editor.enable);
		view.setEnabledAcceptButton(true);
	}

	@Override
	public void onClose() {
		History.back();
	}

	@Override
	public void onAccept() {
		driver.flush();
	}
}
