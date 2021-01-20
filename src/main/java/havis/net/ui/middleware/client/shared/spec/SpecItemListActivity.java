package havis.net.ui.middleware.client.shared.spec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.XmlCallback;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;

import elemental.client.Browser;
import elemental.events.Event;
import elemental.events.EventListener;
import elemental.html.FileReader;
import havis.middleware.ale.service.cc.CCSpec;
import havis.middleware.ale.service.mc.MCCommandCycleSpec;
import havis.middleware.ale.service.mc.MCLogicalReaderSpec;
import havis.middleware.ale.service.mc.MCSpec;
import havis.net.rest.middleware.shared.HasSpecs;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.place.HasListType;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.place.MainPlace;
import havis.net.ui.middleware.client.shared.BaseActivity;
import havis.net.ui.middleware.client.shared.IsMainSection;
import havis.net.ui.middleware.client.shared.event.EnableSpecEvent;
import havis.net.ui.middleware.client.shared.event.ServiceEvent;
import havis.net.ui.middleware.client.shared.event.SpecAddedEvent;
import havis.net.ui.middleware.client.shared.event.SpecChangedEvent;
import havis.net.ui.middleware.client.shared.event.SpecEvent;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.shared.spec.SpecItemListView.Presenter;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.shared.storage.LRSpecStorage;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.table.CustomTable;
import havis.net.ui.shared.client.table.CustomWidgetRow;
import havis.net.ui.shared.client.upload.File;
import havis.net.ui.shared.client.upload.FileList;
import havis.net.ui.shared.client.widgets.LoadingSpinner;

public abstract class SpecItemListActivity<T extends MCSpec> extends BaseActivity implements Presenter {

	private static final int OVERLAY_HEIGHT = 500;
	
	@Inject
	Provider<SpecItemView.Presenter> items;
	
	@Inject
	protected SpecItemListView view;
	
	@Inject
	private PlaceController placeController;
	
	@Inject
	private CommonStorage commonStorage;
	
	@Inject
	private LRSpecStorage lrs;
	
	private HasSpecs<T> service;
	
	private EnableSpecEvent.Handler enableHandler;
	private SpecEvent.Handler specHandler;
	
	private Driver driver = GWT.create(Driver.class);
	interface Driver extends SimpleBeanEditorDriver<List<MCSpec>, ListEditor<MCSpec, SpecItemView>> {}
	
	private List<MCSpec> editorList = new ArrayList<>();
	private ListEditor<MCSpec, SpecItemView> specListEditor;

	private ListType listType;
	private boolean hasSubscribers;
	
	private LoadingSpinner spinner = new LoadingSpinner();
	
	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	PopupPanel.PositionCallback posCallback = new PopupPanel.PositionCallback() {

		@Override
		public void setPosition(int offsetWidth, int offsetHeight) {
			int top = (OVERLAY_HEIGHT - offsetHeight) / 2;
			int left = (Window.getClientWidth() - offsetWidth) / 2;
			spinner.setPopupPosition(left, top);
		}
	};

	private class SpecItemSource extends EditorSource<SpecItemView> {

		private Object parentSource;
		public SpecItemSource(Object src){
			parentSource = src;
		}
		
		@Override
		public void setIndex(SpecItemView editor, int index) {
			view.getList().insert(editor.asWidget(), index);
		}
		
		@Override
		public SpecItemView create(int index) {
			SpecItemView.Presenter itemPresenter = items.get();
			//pass the wanted source to SpecEditorSource making fireEventFromSource() happy
			itemPresenter.setSource(parentSource);
			
			SpecItemView editor = itemPresenter.getView();
			editor.addEnableSpecHandler(enableHandler);
			editor.addSpecEventHandler(specHandler);
			editor.setHasSubscribers(hasSubscribers);
			view.getList().insert(editor.asWidget(), index);
			return editor;
		}
		
		@Override
		public void dispose(SpecItemView subEditor) {
			subEditor.asWidget().removeFromParent();
			subEditor.setPresenter(null);
		}
	}

	protected SpecItemListActivity() {
		specListEditor = ListEditor.of(new SpecItemSource(this));
		driver.initialize(specListEditor);
		driver.edit(editorList);
		
		
	}
	
	public SpecItemListActivity(HasSpecs<T> service, ListType listType) {
		this();
		this.service = service;
		this.listType = listType;
	}
	
	@Override
	public void onLoad() {
		onRefresh();
	}
	
	public void onDelete(final String id) {
		service.delete(id, new MethodCallback<Void>() {
			
			@Override
			public void onSuccess(Method method, Void response) {
				removeItem(id);
				commonStorage.clear(); //force spec reload from back end
				refreshSubscribers();
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				showException(exception);
			}
		});
	}

	private void onEnable(final EnableSpecEvent event) {		
		final SpecItemView source = (SpecItemView) event.getSource();
		source.setLoading(true);
		final String id = event.getId();
		// load spec
		service.get(id, new MethodCallback<T>() {
			
			@Override
			public void onSuccess(Method method, T response) {
				// loading spec was successful.
				// set new enable state
				response.setEnable(event.getEnable());
				// send enable/disable request to the back-end
				service.set(event.getId(), response, new MethodCallback<Void>() {

					@Override
					public void onFailure(Method method, Throwable exception) {
						// An error occurred						
						showException(exception);
						// get current state of spec
						service.get(id, new MethodCallback<T>() {

							@Override
							public void onFailure(Method method, Throwable exception) {								
								source.setLoading(false);
								// set previous state
								source.enable().setValue(!event.getEnable());
							}

							@Override
							public void onSuccess(Method method, T response) {
								source.setLoading(false);
								source.enable().setValue(response.isEnable());
							}							
						});
					}

					@Override
					public void onSuccess(Method method, Void response) {
						source.setLoading(false);
						MCSpec spec = findMCMcSpec(event.getId());
						spec.setEnable(event.getEnable());
						driver.edit(editorList);
						
						if(event.getEnable() == false){
							setSubscribers(id, false);
						}
					}
				});
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				// connection failed or spec does not exist.
				source.setLoading(false);
				showException(exception);
			}
		});
	}

	
	/**
	 * Sets all subscribers of 'id' to 'setVal'
	 * 
	 * @param id
	 * @param setVal
	 */
	private void setSubscribers(String id, boolean setVal) {

		for (int x = 0; x < view.getList().getWidgetCount(); x++) {
			SpecItemView editor = (SpecItemView) view.getList().getWidget(x);
			if (editor != null) {
				if (((SpecItem) editor).id.getValue().startsWith(id)) {
					GWT.log("ID found: " + id);
					CustomTable ct = editor.getTable();
					if (ct != null) {
						for (int y = 0; y < ct.getCustomWidgetCount(); y++) {
							CustomWidgetRow row = ct.getRow(y);
							if (row != null) {
								if (row instanceof SubscriberListItemEditor) {
									SubscriberListItemEditor slie = (SubscriberListItemEditor) row;
									if (slie != null) {
										GWT.log("SET " + slie.getUri() + " to: " + setVal);
										slie.setValue(setVal);
									}
								}
							}
						}
						return;
					}
				}
			}
		}
	}	
	
	private void refreshSubscribers() {
		for (int i = 0; i < view.getList().getWidgetCount(); ++i) {
			SpecItemView editor = (SpecItemView) view.getList().getWidget(i);
			if (editor.getTable().isVisible()) {
				editor.getPresenter().onShowSubscribers();
			}
		}
	}
	
	
	@Override
	public void onRefresh() {
		setItems(new ArrayList<MCSpec>());
		service.getList(new MethodCallback<List<T>>() {
			
			@Override
			public void onSuccess(Method method, List<T> response) {
				setItems(response);
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
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		setEventBus(eventBus);
		bind();
		panel.setWidget(view.asWidget());
		onLoad();
		
		final Place place = placeController.getWhere();
		if (place instanceof HasListType) {
			hasSubscribers = ((HasListType) place).getListType().isHasSubscribers();
		} else {
			hasSubscribers = false;
		}

		SpecAddedEvent.register(getEventBus(), new SpecAddedEvent.Handler() {
			
			@Override
			public void onSpecAdded(final SpecAddedEvent event) {
				service.getList(new MethodCallback<List<T>>() {
					
					@Override
					public void onSuccess(Method method, List<T> response) {
						for (MCSpec spec : response) {
							if (findMCMcSpec(spec.getId()) == null) {
								if (spec.getId().equals(event.getId())) {
									editorList.add(spec);
									driver.edit(editorList);
									
									if(spec instanceof MCLogicalReaderSpec){
										lrs.loadList(this, true);
									}
									break;
								}
							}
						}
					}
					
					@Override
					public void onFailure(Method method, Throwable exception) {
						showException(exception);
					}
				});
			}
		});
		
		SpecChangedEvent.register(getEventBus(), new SpecChangedEvent.Handler() {
			
			@Override
			public void onSpecChanged(final SpecChangedEvent event) {
				service.getList(new MethodCallback<List<T>>() {
					
					@Override
					public void onSuccess(Method method, List<T> response) {
						for (MCSpec spec : response) {
							if (spec.getId().equals(event.getId())) {
								MCSpec s = findMCMcSpec(event.getId());
								if (s != null) {
									s.setName(spec.getName());
									s.setEnable(spec.isEnable());
								}
								if (spec instanceof MCCommandCycleSpec && s instanceof MCCommandCycleSpec) {
									CCSpec existing = ((MCCommandCycleSpec) s).getSpec();
									CCSpec changed = ((MCCommandCycleSpec) spec).getSpec();
									existing.setCmdSpecs(changed.getCmdSpecs());
								}
								driver.edit(editorList);
								
								if(spec instanceof MCLogicalReaderSpec){
									lrs.loadList(this, true);
								}
								break;
							}
						}
					}
					
					@Override
					public void onFailure(Method method, Throwable exception) {
						showException(exception);
					}
				});
			}
		});
		
		enableHandler = new EnableSpecEvent.Handler() {
			@Override
			public void onEnableSpec(final EnableSpecEvent event) {
				onEnable(event);
			}
		};
		
		specHandler = new SpecEvent.Handler() {
			@Override
			public void onSpecEvent(SpecEvent event) {
				switch (event.getAction()) {
				case DELETE:
					onDelete(event.getId());
					break;
				case EDIT:
					onEdit(event.getId());
					break;
				case RUN:
					onRun(event.getId());
					break;
				case EXPORT:
					onExport(event.getId());
					break;
				}
			}
		};
	}
	
	protected void onExport(String id) {
		service.get(id, new XmlCallback() {
			
			@Override
			public void onSuccess(Method method, Document document) {
				String name = "spec";
				NodeList list = document.getElementsByTagName("name");
				if (list.getLength() > 0) {
					name = list.item(0).getFirstChild().toString();
				}
				view.getExportLink().setHref("data:application/json;charset=utf-8," + URL.encodePathSegment(document.toString()));
				view.getExportLink().getElement().setAttribute("download", listType.name() + "_" + name + ".xml");
				Utils.clickElement(view.getExportLink().getElement());
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				showException(exception);
			}
		});
	}

	@Override
	public void bind() {
		view.setPresenter(this);
	}
	
	private void reset() {
		editorList.clear();
		driver.edit(editorList);
		view.reset();
	}
	
	protected void setItems(Collection<? extends MCSpec> items) {
		reset();
		for (MCSpec spec : items) {
			editorList.add(spec);
		}
		driver.edit(editorList);
	}
	
	protected void insertItem(MCSpec spec) {
		editorList.add(spec);
		driver.edit(editorList);
	}
	
	protected void removeItem(MCSpec spec) {
		editorList.remove(spec);
		driver.edit(editorList);
	}
	
	protected void removeItem(String id) {
		editorList.remove(findMCMcSpec(id));
		driver.edit(editorList);
	}
	
	protected MCSpec findMCMcSpec(String id) {
		for (MCSpec spec : editorList) {
			if (spec != null && spec.getId().equals(id)) {
				return spec;
			}
		}
		return null;
	}
	
	public void onAdd() {
		ListType type = ((HasListType) placeController.getWhere()).getListType();
		placeController.goTo(new EditorPlace(type, EditorType.valueOf(type.name()), Utils.getNewId(), true));
	}
	
	public void onEdit(String id) {
		commonStorage.clear();
		
		ListType type = ((HasListType) placeController.getWhere()).getListType();
		placeController.goTo(new EditorPlace(type, EditorType.valueOf(type.name()), id, false));
	}
	
	@Override
	public void onClose() {
		placeController.goTo(new MainPlace(""));
	}

	@Override
	public void onImport() {
		FileList fileList = view.getImportField().getFileList();
		final File file = fileList.html5Item(0);
		elemental.html.Window window = Browser.getWindow();
		final FileReader reader = window.newFileReader();
		reader.setOnload(new EventListener() {
			
			@Override
			public void handleEvent(Event evt) {
				spinner.setPopupPositionAndShow(posCallback);
				String xml = reader.getResult().toString();
				try {
					Document doc = XMLParser.parse(xml);
					
					// disable spec before the import!
					doc.getDocumentElement().setAttribute("enable", "false");
					
					Method m = new Resource(com.google.gwt.core.client.GWT.getHostPageBaseURL() + listType.getResourceURL()).post();
					m.xml(doc);
					m.send(new JsonCallback() {
						@Override
						public void onSuccess(Method method, JSONValue response) {
							JSONObject jo = response.isObject();
							if (jo != null) {
								JSONValue val = jo.get("value");
								if (val != null) {
									JSONString sval = val.isString();
									if (sval != null) {
										getEventBus().fireEvent(new SpecAddedEvent(sval.stringValue()));
										spinner.hide();
									} else {
										showErrorMessage(res.errorUnableToImportSpec());
										spinner.hide();
									}
								} 
							}
						}
						
						@Override
						public void onFailure(Method method, Throwable exception) {
							spinner.hide();
							showErrorMessage(res.errorUnableToImportSpec());
							GWT.log(Utils.getReason(exception));
						}
					});					
				} catch (Exception e){
					showErrorMessage(res.errorUnableToImportSpec());
					spinner.hide();
				}
			}
		});
		reader.readAsText(file);
	}

	@Override
	public ListType getListType() {
		return listType;
	}
	
	public void onRun(final String id) { }
	
}
