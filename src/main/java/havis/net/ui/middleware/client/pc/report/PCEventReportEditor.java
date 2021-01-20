package havis.net.ui.middleware.client.pc.report;

import havis.middleware.ale.service.ECReaderStat;
import havis.middleware.ale.service.ECSightingStat;
import havis.middleware.ale.service.ec.ECSightingSignalStat;
import havis.middleware.ale.service.pc.PCEventCountStat;
import havis.middleware.ale.service.pc.PCEventReport;
import havis.middleware.ale.service.pc.PCEventStat;
import havis.middleware.ale.service.pc.PCEventTimestampStat;
import havis.net.ui.middleware.client.ec.report.SightingsWidgetRow;
import havis.net.ui.middleware.client.ec.report.ValueWidgetRow;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.shared.client.ConfigurationSection;
import havis.net.ui.shared.client.table.CustomTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fusesource.restygwt.client.Defaults;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PCEventReportEditor extends ConfigurationSection implements ValueAwareEditor<PCEventReport> {

	@Ignore
	@UiField
	FlowPanel opReports;

	@Ignore
	@UiField
	CustomTable values;

	@Ignore
	@UiField
	CustomTable sightings;

	@Ignore
	@UiField
	Label sightingsLabel;

	private static PCEventReportEditorUiBinder uiBinder = GWT.create(PCEventReportEditorUiBinder.class);

	interface PCEventReportEditorUiBinder extends UiBinder<Widget, PCEventReportEditor> {
	}

	@Path("opReports.opReport")
	ListEditor<havis.middleware.ale.service.pc.PCOpReport, PCOpReportEditor> pcOpReportEditor;

	private class PCOpReportSource extends EditorSource<PCOpReportEditor> {

		@Override
		public PCOpReportEditor create(int index) {
			PCOpReportEditor editor = new PCOpReportEditor();
			opReports.insert(editor, index);
			return editor;
		}

		@Override
		public void dispose(PCOpReportEditor subEditor) {
			opReports.remove(subEditor);
		}
	}

	public PCEventReportEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		// setText(res.events());
		pcOpReportEditor = ListEditor.of(new PCOpReportSource());

		sightings.setHeader(Arrays.asList(new String[] { ConstantsResource.INSTANCE.reader(), ConstantsResource.INSTANCE.antennaShort(),
				ConstantsResource.INSTANCE.strengthShort(), ConstantsResource.INSTANCE.timestamp() }));
		sightings.setColumnWidth(0, 25, Unit.PCT);
		sightings.setColumnWidth(1, 10, Unit.PCT);
		sightings.setColumnWidth(2, 10, Unit.PCT);
		sightings.setColumnWidth(3, 55, Unit.PCT);
		sightings.setColumnWidth(4, 0, Unit.EM);

		values.setColumnWidth(0, 6, Unit.EM);
		values.setColumnWidth(1, 70, Unit.PCT);
		values.setColumnWidth(2, 0, Unit.EM);
	}

	@Override
	public void setValue(PCEventReport value) {
		if (value != null) {
			setText(value.getId());
			if (value.getStats() != null) {
				for (PCEventStat stat : value.getStats().getStat()) {
					if (stat instanceof PCEventTimestampStat) {
						values.setVisible(true);
						values.addRow(new ValueWidgetRow(ConstantsResource.INSTANCE.first() + " occurred",
								DateTimeFormat.getFormat(Defaults.getDateFormat())
										.format(((PCEventTimestampStat) stat).getFirstOccurringTime())));
						values.addRow(new ValueWidgetRow(ConstantsResource.INSTANCE.last() + " occurred",
								DateTimeFormat.getFormat(Defaults.getDateFormat())
										.format(((PCEventTimestampStat) stat).getLastOccurringTime())));
					} else if (stat instanceof PCEventCountStat) {
						values.setVisible(true);
						values.addRow(new ValueWidgetRow(stat.getProfile(), ((PCEventCountStat) stat).getCount() + ""));
					} else {
						if (stat.getStatBlockList() != null) {
							for (ECReaderStat s : stat.getStatBlockList()) {
								if (s.getSightings() == null) {
									ValueWidgetRow row = null;
									for (int i = 0; i < values.getCustomWidgetCount(); i++) {
										row = (ValueWidgetRow) values.getRow(i);
										if (row.getKey().equals("Readers")) {
											row.addValue(s.getReaderName());
											break;
										}
										row = null;
									}
									if (row == null) {
										values.addRow(new ValueWidgetRow("Readers", s.getReaderName()));
									}
								} else {
									sightingsLabel.setVisible(true);
									sightings.setVisible(true);
									for (ECSightingStat sigh : s.getSightings().getSighting()) {
										if (sigh instanceof ECSightingSignalStat)
											sightings.addRow(new SightingsWidgetRow(s.getReaderName(), ((ECSightingSignalStat) sigh).getAntenna(),
													((ECSightingSignalStat) sigh).getStrength(), ((ECSightingSignalStat) sigh).getTimestamp()));
									}
								}
							}
						}
					}
				}

				// move first row to the top
				if (values.getCustomWidgetCount() > 0) {
					ValueWidgetRow row = (ValueWidgetRow) values.getRow(values.getCustomWidgetCount() - 1);
					values.insertRow(row, 0);
					List<ValueWidgetRow> rows = new ArrayList<ValueWidgetRow>();
					rows.add((ValueWidgetRow) values.getRow(values.getCustomWidgetCount() - 1));
					for (int i = 1; i < values.getCustomWidgetCount(); i++) {
						rows.add((ValueWidgetRow) values.getRow(i));
					}
					values.clear();
					values.addRow(rows.get(rows.size() - 1));
					for (int i = 0; i < rows.size() - 1; i++) {
						values.addRow(rows.get(i));
					}
					values.deleteRow(values.getRowCount() - 1);
				}
			}
		}
	}

	@Override
	public void setDelegate(EditorDelegate<PCEventReport> delegate) {
	}

	@Override
	public void flush() {
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

}