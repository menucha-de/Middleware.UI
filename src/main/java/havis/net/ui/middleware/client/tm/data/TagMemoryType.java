package havis.net.ui.middleware.client.tm.data;

import havis.middleware.ale.service.tm.TMFixedFieldListSpec;
import havis.middleware.ale.service.tm.TMSpec;
import havis.middleware.ale.service.tm.TMVariableFieldListSpec;

public enum TagMemoryType {
	FIXED, VARIABLE;

	public static TagMemoryType getTagMemoryType(TMSpec spec) {
		if (spec != null) {
			if (TMFixedFieldListSpec.class.equals(spec.getClass()))
				return FIXED;
			else if (TMVariableFieldListSpec.class.equals(spec.getClass()))
				return VARIABLE;
		}
		return null;
	}
}
