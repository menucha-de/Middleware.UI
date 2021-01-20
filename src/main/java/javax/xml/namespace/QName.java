package javax.xml.namespace;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public class QName {

	public QName() {
		// TODO Auto-generated constructor stub
	}

	public QName(String arg0, String arg1) {
		// TODO Auto-generated constructor stub
	}
	String qname;

	public String getQname() {
		return qname;
	}

	public void setQname(String qname) {
		this.qname = qname;
	}

}