package havis.net.ui.middleware.client.cc.cmd.operation;

import java.util.Arrays;
import java.util.List;

public enum CCTagManufacturer {
	HARTING("HARTING", "76:x000B", Arrays.asList(CCTagModel.ETB)),
	RF_MICRON("RFMicron", "48:x0024", Arrays.asList(CCTagModel.RFM3254)),
	FARSENS("Farsens", "40:x0828", Arrays.asList(CCTagModel.ROCKY100));

	private String name;
	private String literal;
	private List<CCTagModel> models;

	public String getName() {
		return name;
	}

	public String getLiteral() {
		return literal;
	}

	public List<CCTagModel> getModels() {
		return models;
	}

	private CCTagManufacturer(String name, String literal, List<CCTagModel> models) {
		this.name = name;
		this.literal = literal;
		this.models = models;
	}
	
	public static CCTagManufacturer fromLiteral(String literal) {
		for (CCTagManufacturer tm : CCTagManufacturer.values()) {
			if (tm.literal.equals(literal)) {
				return tm;
			}
		}
		return null;
	}
}
