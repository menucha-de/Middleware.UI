package havis.net.ui.middleware.client.shared.pattern;

import havis.middleware.ale.service.mc.MCCacheSpec;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.ds.CachePatternEditorPlace;
import havis.net.ui.middleware.client.shared.EditorBaseActivity;
import havis.net.ui.middleware.client.shared.report.model.PatternModel;
import havis.net.ui.middleware.client.shared.report.model.PatternType;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;

import java.util.List;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;

public class CachePatternItemEditorActivity
		extends EditorBaseActivity<CachePatternEditorPlace, PatternModel, PatternItemEditor> {

	// accepted test pattern: urn:epc:pat:sgtin-96:1.0037000.000001.*

	interface Codec extends JsonEncoderDecoder<PatternModel> {}
	
	@Inject
	public CachePatternItemEditorActivity(Driver driver, PatternItemEditor editor) {
		super(driver, editor, ConstantsResource.INSTANCE.filterPattern(), (Codec) GWT.create(Codec.class));
	}

	interface Driver extends SimpleBeanEditorDriver<PatternModel, PatternItemEditor> {
	}

	private int currentIndex = -1;

	private List<String> currentPatternList;

	/**
	 * Creates an urn which indicates a new record
	 * 
	 * @param epc
	 * @return composed urn
	 */

	private String composeNewUrn(String epc) {
		String[] tmp = epc.split("-");
		if (tmp.length > 1) {
			if (tmp[0].startsWith("epc") && tmp[1].startsWith("pure")) {
				return ("new:" + epc + ":idpat:new:*.*.*.*");
			}
		}
		return ("new:" + epc + ":pat:new:*.*.*.*");
	}

	@Override
	public void onClose() {
		super.onClose();
		if (getPlace().isNew() && currentPatternList != null && currentPatternList.size() > currentIndex) {
			PopupPanel overlay = new PopupPanel(false, true);
			overlay.show();
			currentPatternList.remove(currentIndex);
			getPlace().updatePathId("" + (currentIndex - 1));
			overlay.hide();
		}

		goTo(new EditorPlace(getPlace().getListType(), getPlace().getEditorType(), getPlace().getPathList(),
				getPlace().getOpenWidgetIdList()));
	}

	@Override
	public void onAccept() {
		super.onAccept();
		if (!hasErrors()) {
			currentPatternList.set(currentIndex, getData().toUri());
			goTo(new EditorPlace(getPlace().getListType(), getPlace().getEditorType(), getPlace().getPathList(),
					getPlace().getOpenWidgetIdList()));
		}
	}

	@Override
	protected void onDataLoaded(Object data) {
		currentPatternList = ((MCCacheSpec) data).getPatterns().getPatterns().getPattern();

		if (currentPatternList == null) {
			showErrorMessage("MCCacheSpec: patterns.patterns.pattern is NULL!");
			return;
		}

		PatternModel filterPatternSpec;
		if (getPlace().isNew()) {
			String defaultFormat = PatternType.EPC_TAG.getScheme();
			filterPatternSpec = new PatternModel(composeNewUrn(defaultFormat));

			currentPatternList.add(filterPatternSpec.toString());
			currentIndex = currentPatternList.size() - 1;
			getPlace().setNew(currentIndex);
		} else {
			currentIndex = getPlace().getPathAsInt(1);
			filterPatternSpec = new PatternModel(currentPatternList.get(currentIndex));
		}
		setData(filterPatternSpec);
	}
}
