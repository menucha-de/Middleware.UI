package havis.net.ui.middleware.client.tm.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * See 6.1 of ALE Specification, Version 1.1.1
 */
public enum Fieldname {
	/**
	 * See 6.1.1 of ALE Specification, Version 1.1.1
	 */
	EPC("epc", DataType.EPC, DataType.UINT, DataType.BITS),
	/**
	 * See 6.1.2 of ALE Specification, Version 1.1.1
	 */
	KILL_PWD("killPwd", DataType.UINT),
	/**
	 * See 6.1.3 of ALE Specification, Version 1.1.1
	 */
	ACCESS_PWD("accessPwd", DataType.UINT),
	/**
	 * See 6.1.4 of ALE Specification, Version 1.1.1
	 */
	EPC_BANK("epcBank", DataType.BITS),
	/**
	 * See 6.1.5 of ALE Specification, Version 1.1.1
	 */
	TID_BANK("tidBank", DataType.BITS),
	/**
	 * See 6.1.6 of ALE Specification, Version 1.1.1
	 */
	USER_BANK("userBank", DataType.BITS),
	/**
	 * See 6.1.7 of ALE Specification, Version 1.1.1
	 */
	AFI("afi", DataType.UINT),
	/**
	 * See 6.1.8 of ALE Specification, Version 1.1.1
	 */
	NSI("nsi", DataType.UINT);

	/**
	 * {@link Fieldname} representation
	 */
	private String fieldname;
	
	/**
	 * Valid data types of the field
	 */
	private DataType[] dataTypes;

	private Fieldname(String fieldname, DataType... dataTypes) {
		this.fieldname = fieldname;
		this.dataTypes = dataTypes;
	}

	/**
	 * Return the {@link #fieldname}
	 * 
	 * @return {@link #fieldname}
	 */
	public String getFieldname() {
		return fieldname;
	}
	
	/**
	 * Return the {@link #dataTypes}
	 * 
	 * @return {@link #dataTypes}
	 */
	public List<DataType> getValidDataTypes() {
		return Arrays.asList(dataTypes);
	}

	/**
	 * Returns the corresponding {@link Fieldname}
	 * 
	 * @param fieldname
	 * @return the corresponding {@link Fieldname}
	 */
	public static Fieldname getFieldname(String fieldname) {
		for (Fieldname f : Fieldname.values()) {
			if (f.fieldname.equals(fieldname))
				return f;
		}
		return null;
	}

	@Override
	public String toString() {
		return fieldname;
	}
	
	public static Map<String, List<String>> asPreDefGroup(){
		Map<String, List<String>> values = new HashMap<String, List<String>>();
		List<String> predefined = new ArrayList<String>();
		for (Fieldname f : Fieldname.values())
			predefined.add(f.getFieldname());
		values.put("<Predefined>", predefined);
		return values;
	}
	
}
