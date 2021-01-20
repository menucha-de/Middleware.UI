package havis.net.ui.middleware.client.ds.association;

import havis.middleware.ale.service.cc.AssocTableEntry;
import havis.middleware.ale.service.mc.MCAssociationSpec;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.ds.AssociationEntriesEditorPlace;
import havis.net.ui.middleware.client.shared.EditorBaseActivity;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;

import java.util.List;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;

public class AssociationEntriesEditorActivity
		extends EditorBaseActivity<AssociationEntriesEditorPlace, AssocTableEntry, AssociationEntryEditor> {

	interface Codec extends JsonEncoderDecoder<AssocTableEntry> {}
	
	@Inject
	public AssociationEntriesEditorActivity(Driver driver, AssociationEntryEditor editor) {
		super(driver, editor, ConstantsResource.INSTANCE.filterPattern(), (Codec) GWT.create(Codec.class));
	}

	interface Driver extends SimpleBeanEditorDriver<AssocTableEntry, AssociationEntryEditor> {
	}

	private int currentIndex = -1;

	private List<AssocTableEntry> currentEntriesList;

	@Override
	public void onClose() {
		super.onClose();
		if (getPlace().isNew()) {
			PopupPanel overlay = new PopupPanel(false, true);
			overlay.show();
			if (currentEntriesList != null)
				currentEntriesList.remove(currentIndex);
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
			goTo(new EditorPlace(getPlace().getListType(), getPlace().getEditorType(), getPlace().getPathList(),
					getPlace().getOpenWidgetIdList()));
		}
	}

	@Override
	protected void onDataLoaded(Object data) {
		currentEntriesList = ((MCAssociationSpec) data).getEntries().getEntries().getEntry();

		if (currentEntriesList == null) {
			showErrorMessage("MCAssociationSpec: entries.entries.entry is NULL!");
			return;
		}

		if (getPlace().isNew()) {
			AssocTableEntry assocTableEntry = new AssocTableEntry();
			assocTableEntry.setKey("");
			assocTableEntry.setValue("");

			currentEntriesList.add(assocTableEntry);
			currentIndex = currentEntriesList.size() - 1;
			getPlace().setNew(currentIndex);
		} else {
			int lastIdx = getPlace().getPathSize() - 1;
			currentIndex = getPlace().getPathAsInt(lastIdx);
		}
		setData(currentEntriesList.get(currentIndex));
	}

}
