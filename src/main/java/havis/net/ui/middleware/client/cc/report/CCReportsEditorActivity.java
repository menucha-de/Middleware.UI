package havis.net.ui.middleware.client.cc.report;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

import havis.middleware.ale.service.cc.CCReports;
import havis.net.rest.middleware.cc.CCAsync;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.shared.ReportsBaseActivity;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;

public class CCReportsEditorActivity extends ReportsBaseActivity<CCReports, CCReportsEditor> {

	CCReportsEditor editor;
	boolean hasParameters;
	interface Driver extends SimpleBeanEditorDriver<CCReports, CCReportsEditor> {
	}

	@Inject
	public CCReportsEditorActivity(Driver driver, final CCReportsEditor editor, final CCAsync service,
			final CommonStorage storage) {
		super(driver, editor, "Command Cycle Report", service, EditorType.CC,
				storage.getParameters() == null || storage.getParameters().size() == 0);
		hasParameters = storage.getParameters() != null && storage.getParameters().size() > 0; 
		if (hasParameters) {
			this.editor = editor;
			editor.setParameters(storage.getParameters());
			storage.clearParameters();
			editor.executeButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					service.getReport(getSpecId(), editor.getParameters(), callback);
				}
			});
		}
	}

	@Override
	protected void afterEdit() {
		super.afterEdit();
		if (hasParameters) {
			editor.focusFirst();
		}
	}

}
