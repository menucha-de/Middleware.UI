package havis.net.ui.middleware.client.configuration;

import havis.middleware.ale.service.mc.MCVersionSpec;
import havis.net.rest.middleware.configuration.ConfigurationAsync;
import havis.net.rest.middleware.ec.ECAsync;
import havis.net.rest.shared.data.SerializableValue;
import havis.net.ui.middleware.client.shared.BaseActivity;
import havis.net.ui.middleware.client.shared.IsMainSection;
import havis.net.ui.middleware.client.shared.event.ServiceEvent;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class CommonActivity extends BaseActivity implements CommonView.Presenter {

	@Inject
	private CommonView view;

	@Inject
	private ConfigurationAsync service;

	@Inject
	private ECAsync versionService;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		bind();
		panel.setWidget(view.asWidget());
		setEventBus(eventBus);
		load();
	}

	@Override
	public void bind() {
		view.setPresenter(this);
	}

	private void load() {
		service.getAleId(new MethodCallback<SerializableValue<String>>() {

			@Override
			public void onSuccess(Method method, SerializableValue<String> response) {
				view.setAleId(response.getValue());
				service.getThreadMax(new MethodCallback<SerializableValue<String>>() {

					@Override
					public void onFailure(Method method, Throwable exception) {
						showException(exception);
					}

					@Override
					public void onSuccess(Method method, SerializableValue<String> response) {
						view.setMaxThreads(response.getValue());
					}
				});
				service.getReadercycleDuration(new MethodCallback<SerializableValue<String>>() {

					@Override
					public void onFailure(Method method, Throwable exception) {
						showException(exception);
					}

					@Override
					public void onSuccess(Method method, SerializableValue<String> response) {
						view.setDuration(response.getValue());
					}
				});
				service.getReadercycleCount(new MethodCallback<SerializableValue<String>>() {

					@Override
					public void onFailure(Method method, Throwable exception) {
						showException(exception);
					}

					@Override
					public void onSuccess(Method method, SerializableValue<String> response) {
						view.setCount(response.getValue());
					}
				});
				service.getReadercycleLifetime(new MethodCallback<SerializableValue<String>>() {

					@Override
					public void onFailure(Method method, Throwable exception) {
						showException(exception);
					}

					@Override
					public void onSuccess(Method method, SerializableValue<String> response) {
						view.setLifetime(response.getValue());
					}
				});
				
				service.getReadercycleExtendedMode(new MethodCallback<SerializableValue<Boolean>>() {
					
					@Override
					public void onSuccess(Method method, SerializableValue<Boolean> response) {
						view.setExtendedMode(response.getValue());
					}
					
					@Override
					public void onFailure(Method method, Throwable exception) {
						showException(exception);
					}
				});
				service.getName(new MethodCallback<SerializableValue<String>>() {

					@Override
					public void onFailure(Method method, Throwable exception) {
						showException(exception);
					}

					@Override
					public void onSuccess(Method method, SerializableValue<String> response) {
						view.setName(response.getValue());
					}
				});
				service.getSubscriberConnectTimeout(new MethodCallback<SerializableValue<String>>() {

					@Override
					public void onSuccess(Method method, SerializableValue<String> response) {
						view.setTimeout(response.getValue());
					}

					@Override
					public void onFailure(Method method, Throwable exception) {
						showException(exception);
					}
				});
				versionService.getVersion(new MethodCallback<MCVersionSpec>() {

					@Override
					public void onSuccess(Method method, MCVersionSpec response) {
						view.setVersion(response.getStandard());
					}

					@Override
					public void onFailure(Method method, Throwable exception) {
						showException(exception);
					}
				});
				
				service.getSoapService(new MethodCallback<SerializableValue<String>>() {
					@Override
					public void onSuccess(Method method, SerializableValue<String> response) {
						view.setSoapService(Boolean.valueOf(response.getValue()));
					}
					@Override
					public void onFailure(Method method, Throwable exception) {
						showException(exception);
					}
				});
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				if (method.getResponse().getStatusCode() == 404) {
					getEventBus().fireEventFromSource(new ServiceEvent(((IsMainSection) view).getSectionType()), this);
					showErrorMessage("The service was either stopped or is not yet fully started");
				} else {
					showException(exception);
				}
			}
		});
		view.setDocumentation(
				com.google.gwt.core.client.GWT.getHostPageBaseURL() + "rest/ale/doc/ALE_Vendor_Specification.pdf");
		view.setAuthor("Menucha Team");
		view.setHomepage("http://menucha.de");
	}

	@Override
	public void updateAleId(String aleId) {
		service.setAleId(new SerializableValue<String>(aleId), new MethodCallback<Void>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				showException(exception);
			}

			@Override
			public void onSuccess(Method method, Void response) {
				// GWT.log("CommonActivity/updateAleId/onSuccess");
			}
		});
	}

	@Override
	public void updateMaxThreads(String threads) {
		service.setThreadMax(new SerializableValue<String>(threads), new MethodCallback<Void>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				showException(exception);
				GWT.log("setThreadMax.onFailure, msg: " + exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, Void response) {
			}
		});
	}

	@Override
	public void updateDuration(String duration) {
		service.setReadercycleDuration(new SerializableValue<String>(duration), new MethodCallback<Void>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				showException(exception);
				GWT.log("setReadercycleDuration.onFailure, msg: " + exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, Void response) {
			}
		});
	}

	@Override
	public void updateCount(String count) {
		service.setReadercycleCount(new SerializableValue<String>(count), new MethodCallback<Void>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				showException(exception);
				GWT.log("setReadercycleCount.onFailure, msg: " + exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, Void response) {
			}
		});
	}

	@Override
	public void updateLifetime(String lifetime) {
		service.setReadercycleLifetime(new SerializableValue<String>(lifetime), new MethodCallback<Void>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				showException(exception);
				GWT.log("setReadercycleLifetime.onFailure, msg: " + exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, Void response) {
			}
		});
	}

	@Override
	public void updateTimeout(String timeout) {
		service.setSubscriberConnectTimeout(new SerializableValue<String>(timeout), new MethodCallback<Void>() {

			@Override
			public void onSuccess(Method method, Void response) {
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				showException(exception);
			}
		});
	}

	@Override
	public void updateExtendedMode(Boolean value) {
		service.setReadercycleExtendedMode(new SerializableValue<Boolean>(value), new MethodCallback<Void>() {
			@Override
			public void onSuccess(Method method, Void response) {
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				showException(exception);
			}
		});
	}
	
	@Override
	public void updateSoapService(Boolean value) {
		service.setSoapService(new SerializableValue<String>(value.toString()), new MethodCallback<Void>() {
			@Override
			public void onSuccess(Method method, Void response) {
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				showException(exception);
			}
		});
	}
}
