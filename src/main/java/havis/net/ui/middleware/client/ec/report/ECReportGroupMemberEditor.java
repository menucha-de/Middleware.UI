package havis.net.ui.middleware.client.ec.report;

import havis.middleware.ale.service.ECReaderStat;
import havis.middleware.ale.service.ECSightingStat;
import havis.middleware.ale.service.ec.ECReportGroupListMember;
import havis.middleware.ale.service.ec.ECReportMemberField;
import havis.middleware.ale.service.ec.ECSightingSignalStat;
import havis.middleware.ale.service.ec.ECTagCountStat;
import havis.middleware.ale.service.ec.ECTagStat;
import havis.middleware.ale.service.ec.ECTagTimestampStat;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.utils.Utils;
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
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ECReportGroupMemberEditor extends ConfigurationSection implements ValueAwareEditor<ECReportGroupListMember> {

	private static ECReportGroupEditortUiBinder uiBinder = GWT.create(ECReportGroupEditortUiBinder.class);

	interface ECReportGroupEditortUiBinder extends UiBinder<Widget, ECReportGroupMemberEditor> {
	}

	@Ignore
	@UiField
	CustomTable sightings;

	@Ignore
	@UiField
	Label sightingsLabel;

	@Ignore
	@UiField
	CustomTable values;

	public ECReportGroupMemberEditor() {
		initWidget(uiBinder.createAndBindUi(this));

		setText("...");

		sightings.setHeader(Arrays.asList(new String[] { ConstantsResource.INSTANCE.reader(), ConstantsResource.INSTANCE.antennaShort(),
				ConstantsResource.INSTANCE.strengthShort(), ConstantsResource.INSTANCE.timestamp() }));
		sightings.setColumnWidth(0, 25, Unit.PCT);
		sightings.setColumnWidth(1, 10, Unit.PCT);
		sightings.setColumnWidth(2, 10, Unit.PCT);
		sightings.setColumnWidth(3, 55, Unit.PCT);
		sightings.setColumnWidth(4, 0, Unit.EM);

//		values.setHeader(Arrays.asList(new String[] { "Key", "Value" }));
		values.setColumnWidth(0, 6, Unit.EM);
		values.setColumnWidth(1, 70, Unit.PCT);
		values.setColumnWidth(2, 0, Unit.EM);
	}

	@Override
	public void setDelegate(EditorDelegate<ECReportGroupListMember> delegate) {
	}

	@Override
	public void flush() {
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(ECReportGroupListMember value) {
		if (value != null) {
			if (value.getEpc() != null)
				values.addRow(new ValueWidgetRow(ConstantsResource.INSTANCE.capsEpc(), value.getEpc().getValue()));
			if (value.getTag() != null)
				values.addRow(new ValueWidgetRow(ConstantsResource.INSTANCE.capsTag(), value.getTag().getValue()));
			if (value.getRawDecimal() != null)
				values.addRow(new ValueWidgetRow(ConstantsResource.INSTANCE.capsRawDec(), value.getRawDecimal().getValue()));
			if (value.getRawHex() != null)
				values.addRow(new ValueWidgetRow(ConstantsResource.INSTANCE.capsRawHex(), value.getRawHex().getValue()));

			if (value.getEpc() != null && !Utils.isNullOrEmpty(value.getEpc().getValue())) {
				setText(value.getEpc().getValue());
			} else if (value.getTag() != null && !Utils.isNullOrEmpty(value.getTag().getValue())) {
				setText(value.getTag().getValue());
			} else if (value.getRawHex() != null && !Utils.isNullOrEmpty(value.getRawHex().getValue())) {
				setText(value.getRawHex().getValue());
			} else if (value.getRawDecimal() != null && !Utils.isNullOrEmpty(value.getRawDecimal().getValue())) {
				setText(value.getRawDecimal().getValue());
			}

			if (value.getExtension() != null && value.getExtension().getFieldList() != null) {
				for (ECReportMemberField f : value.getExtension().getFieldList().getField()) {
					values.addRow(new ValueWidgetRow("Field: " + f.getName(), f.getValue()));
				}
			}

			if (value.getExtension() != null && value.getExtension().getStats() != null) {
				for (ECTagStat stat : value.getExtension().getStats().getStat()) {
					if (stat instanceof ECTagTimestampStat) {
						values.addRow(new ValueWidgetRow(ConstantsResource.INSTANCE.first() + " seen", DateTimeFormat.getFormat(
								Defaults.getDateFormat()).format(((ECTagTimestampStat) stat).getFirstSightingTime())));
						values.addRow(new ValueWidgetRow(ConstantsResource.INSTANCE.last() + " seen", DateTimeFormat.getFormat(
								Defaults.getDateFormat()).format(((ECTagTimestampStat) stat).getLastSightingTime())));
					} else if (stat instanceof ECTagCountStat) {
						values.addRow(new ValueWidgetRow(stat.getProfile(), ((ECTagCountStat) stat).getCount() + ""));
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
			ValueWidgetRow row = (ValueWidgetRow) values.getRow(values.getCustomWidgetCount() - 1);
			values.insertRow(row, 0);
			List<ValueWidgetRow> rows = new ArrayList<ValueWidgetRow>();
			rows.add((ValueWidgetRow)values.getRow(values.getCustomWidgetCount() - 1));
			for(int i = 1; i < values.getCustomWidgetCount(); i++){
				rows.add((ValueWidgetRow)values.getRow(i));
			}
			values.clear();
			values.addRow(rows.get(rows.size() - 1));
			for(int i = 0; i < rows.size() - 1; i++){
				values.addRow(rows.get(i));
			}
			values.deleteRow(values.getRowCount() - 1);
		}
	}

}
