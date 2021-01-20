package havis.net.ui.middleware.client.place;


public enum EditorType {
	LR("LR",0), TM("TM",1), EC("EC",2), PC("PC",3), CC("CC",4), DSAS("DSAS",5), DSCA("DSCA",6),
	DSRN("DSRN",7), SUB("SUB",8), ECREP("ECREP",9), PCREP("PCREP",10),  CCREP("CCREP",11), INFO("INFO",12), TR("TR",13), DS("DS",14);
	
	private String name;
	private Integer index;

	EditorType(String name, Integer index) {
		this.name = name;
		this.index = index;
	}
	
	public Integer getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
}
