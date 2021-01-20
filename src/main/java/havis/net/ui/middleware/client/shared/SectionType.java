package havis.net.ui.middleware.client.shared;

import havis.net.ui.middleware.client.place.ListType;

public enum SectionType {
	CM, LR, TM, EC, PC, CC, DS, DSAS, DSRN, DSCA;
	
	public static SectionType fromListType(ListType listType) {
		return valueOf(listType.name());
	}
}
