package havis.net.ui.middleware.client.shared;

import havis.net.ui.middleware.client.place.ec.CommonEditorPlace;
import havis.net.ui.middleware.client.shared.event.SpecLoadedEvent;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.utils.Utils;

import java.util.HashMap;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public abstract class EditorBaseActivity<P extends CommonEditorPlace, D, E extends Editor<? super D>> extends BaseActivity implements EditorDialogView.Presenter {
	
	private P place;
	private E editor;
	private SimpleBeanEditorDriver<D, E> driver;
	private String editorTitle;
	private D data;
	/**
	 * Storage for clones of the data object. 
	 */
	private static HashMap<String, Object> clone = new HashMap<String, Object>();
	
	@Inject
	private EditorDialogView view;

	@Inject
	private PlaceController placeController;
	
	@Inject
	private CommonStorage storage;
	
	protected JsonEncoderDecoder<D> codec;

	public EditorBaseActivity(SimpleBeanEditorDriver<D, E> driver, E editor, String editorTitle, JsonEncoderDecoder<D> codec) {
		this.driver = driver;
		this.editor = editor;
		this.editorTitle = editorTitle;
		this.codec = codec;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		setEventBus(eventBus);
		panel.setWidget(view.asWidget());
		bind();

		place = (P) placeController.getWhere();

		view.setEditorTitle(editorTitle);
		view.setEnableButton(null);
		view.setEnabledAcceptButton(false);
		view.setEditor(new LoadingSpinner());

		driver.initialize(editor);
		
		initializeEditorComponents();
		
		SpecLoadedEvent.register(eventBus, this, new SpecLoadedEvent.Handler() {
			
			@Override
			public void onSpecLoaded(SpecLoadedEvent event) {
				onDataLoaded(event.getSpec());
				driver.edit(data);
				view.setEnabledAcceptButton(true);
				view.setEditor((Widget) editor);
			}
			
			@Override
			public void onFailure(SpecLoadedEvent event, String message) {
				view.setEditor(null);
				showErrorMessage(message);
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
		Utils.flush(this.data, clone.get(getPlace().getPath()));
		clone.remove(getPlace().getPath());
	}

	@Override
	public void onAccept() {
		PopupPanel overlay = new PopupPanel(false, true);
		overlay.show();

		driver.flush();

		if (driver.hasErrors()) {
			StringBuilder builder = new StringBuilder();
			for (EditorError e : driver.getErrors()) {
				builder.append(e.getMessage());
				builder.append('\n');
			}
			showErrorMessage(builder.toString());
		} else {
			clone.remove(getPlace().getPath());
		}
		overlay.hide();
	}
	
	protected P getPlace() {
		return place;
	}
	
	protected D getData() {
		return data;
	}
	
	protected void setData(D data) {
		this.data = data;
		if(!clone.containsKey(getPlace().getPath())) {
			clone.put(getPlace().getPath(), Utils.clone(codec, data));
		}
	}

	protected void flushChanges() {
		driver.flush();
	}

	protected boolean hasErrors() {
		return driver.hasErrors();
	}
	
	protected void initializeEditorComponents() {
		
	}

	protected abstract void onDataLoaded(Object data);
}
