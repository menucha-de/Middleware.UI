package havis.net.ui.middleware.client.cc.report;

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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import havis.middleware.ale.service.ECReaderStat;
import havis.middleware.ale.service.ECSightingStat;
import havis.middleware.ale.service.cc.CCTagCountStat;
import havis.middleware.ale.service.cc.CCTagReport;
import havis.middleware.ale.service.cc.CCTagStat;
import havis.middleware.ale.service.cc.CCTagTimestampStat;
import havis.middleware.ale.service.ec.ECSightingSignalStat;
import havis.net.ui.middleware.client.ec.report.SightingsWidgetRow;
import havis.net.ui.middleware.client.ec.report.ValueWidgetRow;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.shared.client.ConfigurationSection;
import havis.net.ui.shared.client.ConfigurationSections;
import havis.net.ui.shared.client.table.CustomTable;

public class CCTagReportEditor extends ConfigurationSection
		implements ValueAwareEditor<havis.middleware.ale.service.cc.CCTagReport> {

	@Ignore
	@UiField
	ConfigurationSections opReports;

	@Ignore
	@UiField
	CustomTable values;

	@Ignore
	@UiField
	CustomTable sightings;

	@Ignore
	@UiField
	Label sightingsLabel;

	private static CCTagReportUiBinder uiBinder = GWT.create(CCTagReportUiBinder.class);

	interface CCTagReportUiBinder extends UiBinder<Widget, CCTagReportEditor> {
	}

	@Path("opReports.opReport")
	ListEditor<havis.middleware.ale.service.cc.CCOpReport, CCOpReportEditor> ccOpReportEditor;

	private class CCOpReportSource extends EditorSource<CCOpReportEditor> {

		@Override
		public CCOpReportEditor create(int index) {
			final CCOpReportEditor editor = new CCOpReportEditor(index);
			editor.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					editor.calculateHeight();
				}
			});
			opReports.insert(editor, index);
			editor.setMultiOpen(true);
			editor.setOpen(true);
			return editor;
		}

		@Override
		public void dispose(CCOpReportEditor subEditor) {
			opReports.remove(subEditor);
		}
	}

	public CCTagReportEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		ccOpReportEditor = ListEditor.of(new CCOpReportSource());

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
	public void setDelegate(EditorDelegate<CCTagReport> delegate) {
	}

	@Override
	public void flush() {
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(CCTagReport value) {
		if (value != null) {
			setText(value.getId());

			if (value.getStats() != null) {
				for (CCTagStat stat : value.getStats().getStat()) {
					if (stat instanceof CCTagTimestampStat) {
						values.setVisible(true);
						values.addRow(new ValueWidgetRow(ConstantsResource.INSTANCE.first() + " seen",
								DateTimeFormat.getFormat(Defaults.getDateFormat())
										.format(((CCTagTimestampStat) stat).getFirstSightingTime())));
						values.addRow(new ValueWidgetRow(ConstantsResource.INSTANCE.last() + " seen",
								DateTimeFormat.getFormat(Defaults.getDateFormat())
										.format(((CCTagTimestampStat) stat).getLastSightingTime())));
					} else if (stat instanceof CCTagCountStat) {
						values.setVisible(true);
						values.addRow(new ValueWidgetRow(stat.getProfile(), ((CCTagCountStat) stat).getCount() + ""));
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
			} else {
				
			}
		}
	}

}
