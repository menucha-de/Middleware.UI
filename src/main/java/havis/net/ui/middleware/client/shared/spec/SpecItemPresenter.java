package havis.net.ui.middleware.client.shared.spec;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

import havis.middleware.ale.service.mc.MCSubscriberSpec;
import havis.net.rest.middleware.shared.HasSubscribers;
import havis.net.ui.middleware.client.place.HasListType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.SubscriberPlace;
import havis.net.ui.middleware.client.shared.event.EnableSpecEvent;
import havis.net.ui.middleware.client.shared.event.MessageEvent;
import havis.net.ui.middleware.client.shared.event.SpecAddedEvent;
import havis.net.ui.middleware.client.shared.event.SpecChangedEvent;
import havis.net.ui.middleware.client.shared.event.SpecEvent;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.utils.Utils;

public class SpecItemPresenter implements SpecItemView.Presenter {

	private EventBus eventBus;

	private SpecItemView view;
	private PlaceController placeController;
	private HasSubscribers service;
	private ListEditor<MCSubscriberSpec, SubscriberListItemEditor> editor;
	private Driver driver = GWT.create(Driver.class);
	private ListType type;
	private Object parentSource;
	
	List<HandlerRegistration> handlers = new ArrayList<>();
	
	interface Driver extends SimpleBeanEditorDriver<List<MCSubscriberSpec>, ListEditor<MCSubscriberSpec, SubscriberListItemEditor>> {
	}

	private class SpecEditorSource extends EditorSource<SubscriberListItemEditor> {

		private Object source;
		
		public SpecEditorSource(Object src){
			this.source = src;
		}
		
		
		@Override
		public SubscriberListItemEditor create(int index) {
			final SubscriberListItemEditor editor = new SubscriberListItemEditor();
			editor.addEnableSpecHandler(new EnableSpecEvent.Handler() {
				
				@Override
				public void onEnableSpec(EnableSpecEvent event) {
					editor.setLoading(true);
					for (final MCSubscriberSpec spec: SpecItemPresenter.this.editor.getList()) {
						if (spec.getId().equals(event.getId())) {
							spec.setEnable(event.getEnable());
							service.setSubscriber(view.id().getValue(), event.getId(), spec, new MethodCallback<Void>() {
								
								@Override
								public void onSuccess(Method method, Void response) {
									// Subscriber has been updated
									editor.setLoading(false);
								}
								
								@Override
								public void onFailure(Method method, Throwable exception) {
									editor.setLoading(false);
									//GWT.log("service.setSubscriber.onFailure: " + exception.getMessage());
									eventBus.fireEventFromSource(new MessageEvent(MessageEvent.MessageType.ERROR, Utils.getReason(exception)), source);
//									set state!!!
									editor.enable.setValue(false);
								}
							});
							break;
						}
					}
				}
			});
			
			editor.addSpecEventHandler(new SpecEvent.Handler() {
				
				@Override
				public void onSpecEvent(SpecEvent event) {
					if (event.getAction() == SpecEvent.Action.EDIT) {
						placeController.goTo(new SubscriberPlace(type, view.id().getValue(), event.getId()));
					}
				}
			});
			view.getTable().addRow(editor);
			return editor;
		}

		@Override
		public void dispose(SubscriberListItemEditor subEditor) {
			view.getTable().deleteRow(subEditor);
		}
	}
	
	@Override
	public void bind() {
		view.setPresenter(this);
	}
	
	@Inject
	public SpecItemPresenter(final SpecItemView view, PlaceController placeController, CommonStorage commonStorage, final EventBus eventBus) {
		this.view = view;
		this.placeController = placeController;
		this.eventBus = eventBus;
		this.type = ((HasListType) placeController.getWhere()).getListType();
		this.service = commonStorage.getSubscribersService(type);
		
		
		if (service != null) {
			handlers.add(SpecAddedEvent.register(eventBus, new SpecAddedEvent.Handler() {
				
				@Override
				public void onSpecAdded(SpecAddedEvent event) {
					onShowSubscribers();
				}
			}));
			
			handlers.add(SpecChangedEvent.register(eventBus, new SpecChangedEvent.Handler() {
				
				@Override
				public void onSpecChanged(SpecChangedEvent event) {
					onShowSubscribers();
				}
			}));
		}
		bind();
		
		//part of the workaround, scheduler must be used.
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				editor = ListEditor.of(new SpecEditorSource(parentSource));
				driver.initialize(editor);
			}
		});
	}
	
	@Override
	public SpecItemView getView() {
		return view;
	}

	@Override
	public void onDeleteSubscriber(final int index) {
		service.deleteSubscriber(view.id().getValue(), editor.getList().get(index).getId(), new MethodCallback<Void>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				GWT.log("onDeleteSubscriber: " + exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, Void response) {
				editor.getList().remove(index);
			}
		});
	}

	@Override
	public void onAddSubscriber() {
		placeController.goTo(new SubscriberPlace(type, view.id().getValue(), "NEW"));
	}

	public void onShowSubscribers() {
		service.getSubscribers(view.id().getValue(), new MethodCallback<List<MCSubscriberSpec>>() {
			
			@Override
			public void onSuccess(Method method, List<MCSubscriberSpec> response) {
				driver.edit(response);
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				GWT.log("getSubscribers: " + exception.getMessage());
			}
		});
	}
	
	@Override
	public void onEnable(boolean value) {
		view.enable().setValue(value);
		onShowSubscribers();
	}

	@Override
	public void setSource(Object src) {
		parentSource = src;
	}

	@Override
	public String getBaseExportURL() {
		if(type == null){
			return null;
		} else {
			String baseURL = com.google.gwt.core.client.GWT.getHostPageBaseURL() + "rest/ale/";
			switch (type) {
			case DSCA:
				return baseURL + "cc/caches/";
			case DSRN:
				return baseURL + "cc/randoms/";
			case DSAS:
				return baseURL + "cc/associations/";
			default:
				return baseURL + type.toString().toLowerCase() + "/specs/";
			}
		}
	}
	
	public void onDispose() {
		for (HandlerRegistration h : handlers) {
			h.removeHandler();
		}
	}
}
