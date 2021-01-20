package havis.net.ui.middleware.client.pc.report;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.inject.Inject;

import havis.middleware.ale.service.pc.PCReports;
import havis.net.rest.middleware.pc.PCAsync;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.shared.ReportsBaseActivity;

public class PCReportsEditorActivity extends ReportsBaseActivity<PCReports, PCReportsEditor> {

	interface Driver extends SimpleBeanEditorDriver<PCReports, PCReportsEditor> {
	}

	@Inject
	public PCReportsEditorActivity(Driver driver, PCReportsEditor editor, PCAsync service) {
		super(driver, editor, "Port Cycle Report", service, EditorType.PC, true);
	}
}
