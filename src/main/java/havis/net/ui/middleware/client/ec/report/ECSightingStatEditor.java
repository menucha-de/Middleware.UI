package havis.net.ui.middleware.client.ec.report;

import havis.middleware.ale.service.ECSightingStat;
import havis.middleware.ale.service.ec.ECSightingSignalStat;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.shared.client.table.CustomTable;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ECSightingStatEditor extends Composite implements ValueAwareEditor<ECSightingStat> {

	@Ignore
	@UiField
	CustomTable values;

	private ConstantsResource res = ConstantsResource.INSTANCE;

	private static ECSightingSignalStatEditorUiBinder uiBinder = GWT.create(ECSightingSignalStatEditorUiBinder.class);

	interface ECSightingSignalStatEditorUiBinder extends UiBinder<Widget, ECSightingStatEditor> {
	}

	public ECSightingStatEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		values.setHeader(Arrays.asList(new String[] { res.antenna(), res.strength() }));
		values.setColumnWidth(2, 0, Unit.EM);
	}

	@Override
	public void setDelegate(EditorDelegate<ECSightingStat> delegate) {
	}

	@Override
	public void flush() {
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(ECSightingStat value) {
		if (value != null) {
			ECSightingSignalStat ssig = (ECSightingSignalStat) value;
			values.addRow(new ValueWidgetRow(ssig.getAntenna() + "", ssig.getStrength() + ""));
		}
	}
}
