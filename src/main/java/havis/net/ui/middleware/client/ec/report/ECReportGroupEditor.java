package havis.net.ui.middleware.client.ec.report;

import havis.middleware.ale.service.ec.ECReportGroup;
import havis.middleware.ale.service.ec.ECReportGroupListMember;
import havis.net.ui.middleware.client.shared.resourcebundle.ConstantsResource;
import havis.net.ui.middleware.client.utils.Utils;
import havis.net.ui.shared.client.ConfigurationSection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Widget;

public class ECReportGroupEditor extends ConfigurationSection implements ValueAwareEditor<ECReportGroup> {

	@UiField
	FlowPanel member;

	@Path("groupCount.count")
	@UiField
	IntegerBox count;

	@Ignore
	@UiField
	FlowPanel countGroup;

	private ConstantsResource res = ConstantsResource.INSTANCE;

	private static ECReportGroupEditortUiBinder uiBinder = GWT.create(ECReportGroupEditortUiBinder.class);

	interface ECReportGroupEditortUiBinder extends UiBinder<Widget, ECReportGroupEditor> {
	}

	@Path("groupList.member")
	ListEditor<ECReportGroupListMember, ECReportGroupMemberEditor> membersEditor;

	private class ECReportMemberSource extends EditorSource<ECReportGroupMemberEditor> {

		@Override
		public ECReportGroupMemberEditor create(int index) {
			final ECReportGroupMemberEditor editor = new ECReportGroupMemberEditor();
			member.insert(editor, index);
			return editor;
		}

		@Override
		public void dispose(ECReportGroupMemberEditor subEditor) {
			member.remove(subEditor);
		}
	}

	public ECReportGroupEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		setText(res.group() + ": " + res.defaultGroupName());
		membersEditor = ListEditor.of(new ECReportMemberSource());
	}

	@Override
	public void setDelegate(EditorDelegate<ECReportGroup> delegate) {
	}

	@Override
	public void flush() {
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@Override
	public void setValue(ECReportGroup value) {
		if (value != null) {
			if (value.getGroupCount() != null)
				countGroup.setVisible(true);

			if (!Utils.isNullOrEmpty(value.getGroupName())) {
				setText(res.group() + ": " + value.getGroupName());
			}
		}
	}

}
