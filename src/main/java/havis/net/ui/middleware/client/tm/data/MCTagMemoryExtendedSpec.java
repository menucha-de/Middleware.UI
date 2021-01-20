package havis.net.ui.middleware.client.tm.data;

import havis.middleware.ale.service.mc.MCTagMemorySpec;
import havis.middleware.ale.service.tm.TMFixedFieldListSpec;
import havis.middleware.ale.service.tm.TMVariableFieldListSpec;

public class MCTagMemoryExtendedSpec extends MCTagMemorySpec {

	public TMFixedFieldListSpec getFixedFieldListSpec() {
		
		if(!(super.getSpec() instanceof TMFixedFieldListSpec))return null;
		
		return (TMFixedFieldListSpec) super.getSpec();
	}
	
	public void setFixedFieldListSpec(TMFixedFieldListSpec value) {
		super.setSpec(value);
	}
	
	public TMVariableFieldListSpec getVariableFieldListSpec() {
		
		if(!(super.getSpec() instanceof TMVariableFieldListSpec))return null;
		
		return (TMVariableFieldListSpec) super.getSpec();
	}
	
	public void setVariableFieldListSpec(TMVariableFieldListSpec value) {
		super.setSpec(value);
	}
	
	public MCTagMemoryExtendedSpec(MCTagMemorySpec mcSpec) {
		setId(mcSpec.getId());
		setSpec(mcSpec.getSpec());
		setEnable(mcSpec.isEnable());
		setName(mcSpec.getName());
		setBaseExtension(mcSpec.getBaseExtension());
		setCreationDate(mcSpec.getCreationDate());
		setExtension(mcSpec.getExtension());		
	}
	
	public MCTagMemoryExtendedSpec() {
		super();
	}
	
	public MCTagMemorySpec getMCTagMemorySpec(){
		MCTagMemorySpec spec = new MCTagMemorySpec();
		spec.setBaseExtension(getBaseExtension());
		spec.setCreationDate(getCreationDate());
		spec.setEnable(isEnable());
		spec.setExtension(getExtension());
		spec.setId(getId());
		spec.setName(getName());
		spec.setSchemaVersion(getSchemaVersion());
		spec.setSpec(getSpec());
		return spec;
	}

}
