package havis.net.ui.middleware.client.ec.report;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.inject.Inject;

import havis.middleware.ale.service.ec.ECReports;
import havis.net.rest.middleware.ec.ECAsync;
import havis.net.ui.middleware.client.place.EditorType;
import havis.net.ui.middleware.client.shared.ReportsBaseActivity;

public class ECReportsEditorActivity extends ReportsBaseActivity<ECReports, ECReportsEditor> {

	interface Driver extends SimpleBeanEditorDriver<ECReports, ECReportsEditor> {
	}

	@Inject
	public ECReportsEditorActivity(Driver driver, ECReportsEditor editor, ECAsync service) {
		super(driver, editor, "Event Cycle Report", service, EditorType.EC, true);
	}
}
