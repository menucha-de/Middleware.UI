package havis.net.ui.middleware.client.lr;

import havis.middleware.ale.service.lr.LRProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Property extends LRProperty {

	public enum Group {
		MANDATORY,
		OPTIONAL,
		ADVANCED;
	}

	public final static Map<String, Property> PROPERTIES;

	static {
		Map<String, Property> properties = new HashMap<String, Property>();
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(Group.MANDATORY, "ReaderType",
				Arrays.asList(new String[] { "RF-R400", "RF-R400|Stream", "RF-R500", "RF-R500|Stream", "LLRP", "FEIG.LRU1002", "FEIG.LRU3x00", "FEIG.HyWEAR|Scan" })));
		list.add(new Property(Group.MANDATORY, "Connector.Host", "Host"));
		list.add(new Property(Group.OPTIONAL, "Connector.ConnectionType", "ConnectionType", Arrays.asList(new String[] { "TCP" })));
		list.add(new Property(Group.OPTIONAL, "Connector.Port", "Port"));
		list.add(new Property(Group.OPTIONAL, "Connector.DeviceID", "DeviceID"));
		list.add(new Property("Connector.Inventory.Antennas", "InventoryAntennas"));
		list.add(new Property("AntennaID", "AntennaID (Composite only)"));
		for (Property p : list) {
			properties.put(p.getName(), p);
		}
		PROPERTIES = Collections.unmodifiableMap(properties);
	}

	public static Map<String, Property> getProperties(Group group) {
		Map<String, Property> properties = new HashMap<String, Property>();
		for (Map.Entry<String, Property> entry : PROPERTIES.entrySet()) {
			if (Objects.equals(entry.getValue().getGroup(), group)) {
				properties.put(entry.getKey(), entry.getValue());
			} else if (Objects.equals(Group.ADVANCED, group) && entry.getValue().getGroup() == null) {
				properties.put(entry.getKey(), entry.getValue());
			}
		}
		return Collections.unmodifiableMap(properties);
	}

	private String label;
	private Group group;
	private List<String> values;

	public Property(String name) {
		this.name = name;
	}

	public Property(String name, String label) {
		this.name = name;
		this.label = label;
	}

	public Property(Group group, String name) {
		this.name = name;
		this.group = group;
	}

	public Property(Group group, String name, String label) {
		this.name = name;
		this.label = label;
		this.group = group;
	}

	public Property(String name, List<String> values) {
		this.name = name;
		this.values = values;
	}

	public Property(String name, String label, List<String> values) {
		this.name = name;
		this.label = label;
		this.values = values;
	}

	public Property(Group group, String name, List<String> values) {
		this.name = name;
		this.group = group;
		this.values = values;
	}

	public Property(Group group, String name, String label, List<String> values) {
		this.name = name;
		this.label = label;
		this.group = group;
		this.values = values;
	}

	public String getLabel() {
		if (label == null) {
			return name;
		}
		return label;
	}

	public Group getGroup() {
		return group;
	}

	public List<String> getValues() {
		return values;
	}
}
