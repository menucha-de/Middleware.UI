package havis.net.ui.middleware.client.cc.cmd.operation;

public enum CCTagCommand {
	GET_SENSOR_VALUE("GetSensorValue", "E0024800000"),
	GET_SENSOR_CODE("GetSensorCode", "0100"),
	GET_RSSI_CODE("GetRSSICode", "0200"),
	GET_TEMPERATURE_VALUE("GetTemperatureValue", "0400"),
	BLINK("Blink", "01"),
	GET_SHORT_VALUE("GetShortValue", "02"),
	GET_FLOAT_VALUE("GetFloatValue", "04"),
	GET_THREE_SHORT_VALUES("GetThreeShortValues", "06"),
	GET_TWO_FLOAT_VALUES("GetTwoFloatValues", "08");

	private String name;
	private String literal;
	private CCTagCommand(String name, String literal) {
		this.name = name;
		this.literal = literal;
	}
	public String getName() {
		return name;
	}
	public String getLiteral() {
		return literal;
	}

	public static CCTagCommand fromLiteral(String literal) {
		for (CCTagCommand tc : CCTagCommand.values()) {
			if (tc.literal.equals(literal)) {
				return tc;
			}
		}
		return null;
	}
}
