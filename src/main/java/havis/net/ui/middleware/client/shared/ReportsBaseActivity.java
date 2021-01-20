package havis.net.ui.middleware.client.shared;

import java.util.Date;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.XmlCallback;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import havis.middleware.ale.service.IReports;
import havis.net.rest.middleware.shared.HasReports;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.utils.Utils;

public class ReportsBaseActivity<R extends IReports, E extends Editor<? super R>> extends BaseActivity implements EditorDialogView.Presenter {

	@Inject
	private EditorDialogView view;

	@Inject
	private PlaceController placeController;

	private E editor;
	private SimpleBeanEditorDriver<R, E> driver;
	private String title;
	private HasReports<R> service;
	private EditorType type;
	private PopupPanel overlay;
	private boolean autoRun;
	private EditorPlace place;

	@Override
	public void bind() {
		view.setPresenter(this);
	}

	public ReportsBaseActivity(SimpleBeanEditorDriver<R, E> driver, E editor, String title, HasReports<R> service, EditorType type, boolean autoRun) {
		this.driver = driver;
		this.editor = editor;
		this.title = title;
		this.service = service;
		this.type = type;
		this.autoRun = autoRun;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		setEventBus(eventBus);
		panel.setWidget(view.asWidget());
		bind();
		overlay = new PopupPanel(false, true);
		Utils.blockUi(overlay, 300);

		place = (EditorPlace) placeController.getWhere();
		driver.initialize(editor);
		view.setEditorTitle(title);

		view.setEnableButton(null);// no button needed
		view.setEnabledExportButton(false);

		if (autoRun) {
			view.setEditor(new LoadingSpinner());
			getReport();
		} else {
			view.setEditor((Widget) editor);
		}
	}

	protected MethodCallback<R> callback = new MethodCallback<R>() {

		@Override
		public void onFailure(Method method, Throwable exception) {
//			view.setEditor(null);
			showException(exception);
		}

		@Override
		public void onSuccess(Method method, R response) {
			view.setEditor((Widget) editor);
			driver.edit(response);
			afterEdit();
			Method m = new Resource(com.google.gwt.core.client.GWT.getHostPageBaseURL() + "rest/ale/" + type.getName().toLowerCase() + "/report").post();
			m.json(JSONParser.parseStrict(method.getResponse().getText()));
			final String filename = type.getName() + "Reports_" + DateTimeFormat.getFormat("yyyyMMddHHmmss").format(new Date()) + ".xml";
			m.send(new XmlCallback() {

				@Override
				public void onSuccess(Method method, Document response) {
					view.setExportButton("data:application/xml;charset=utf-8,"
							+ URL.encodePathSegment(method.getResponse().getText()), filename);
					view.setEnabledExportButton(true);
				}

				@Override
				public void onFailure(Method method, Throwable exception) {
					showException(exception);
				}
			});

		}
	};
	
	protected void getReport() {
		service.getReport(place.getSpecId(), callback);
	}
	
	protected String getSpecId() {
		return place.getSpecId();
	}

	@Override
	public void onClose() {
		History.back();
	}

	@Override
	public void onAccept() {
	}

	protected void afterEdit() {
	}
}
