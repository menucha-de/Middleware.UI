package havis.net.ui.middleware.client.ds;

import com.google.gwt.user.client.ui.SimplePanel;

import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.shared.IsMainSection;
import havis.net.ui.middleware.client.shared.SectionType;

public class DataSourceSelection extends SimplePanel implements DataSourceSelectionView, IsMainSection {

	@Override
	public SectionType getSectionType() {
		return SectionType.fromListType(ListType.DS);
	}
}
