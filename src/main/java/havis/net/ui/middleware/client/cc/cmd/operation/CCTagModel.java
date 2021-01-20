package havis.net.ui.middleware.client.cc.cmd.operation;

import java.util.Arrays;
import java.util.List;

public enum CCTagModel {
	ETB("ETB", "0040", Arrays.asList(CCTagCommand.GET_SENSOR_VALUE)),
	RFM3254("RFM3254", "0403", Arrays.asList(
			CCTagCommand.GET_SENSOR_CODE,
			CCTagCommand.GET_RSSI_CODE,
			CCTagCommand.GET_TEMPERATURE_VALUE
	)),
	ROCKY100("ROCKY100", "0001", Arrays.asList(
			CCTagCommand.BLINK,
			CCTagCommand.GET_SHORT_VALUE,
			CCTagCommand.GET_THREE_SHORT_VALUES,
			CCTagCommand.GET_FLOAT_VALUE,
			CCTagCommand.GET_TWO_FLOAT_VALUES
	));

	private String name;
	private String literal;
	private List<CCTagCommand> commands;

	public String getName() {
		return name;
	}

	public String getLiteral() {
		return literal;
	}

	public List<CCTagCommand> getCommands() {
		return commands;
	}

	private CCTagModel(String name, String literal, List<CCTagCommand> commands) {
		this.name = name;
		this.literal = literal;
		this.commands = commands;
	}
	public static CCTagModel fromLiteral(String literal) {
		for (CCTagModel tm : CCTagModel.values()) {
			if (tm.literal.equals(literal)) {
				return tm;
			}
		}
		return null;
	}
}
