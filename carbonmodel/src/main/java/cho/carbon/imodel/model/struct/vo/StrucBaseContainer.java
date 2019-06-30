package cho.carbon.imodel.model.struct.vo;

import cho.carbon.imodel.model.struct.pojo.StrucBase;
import cho.carbon.imodel.model.struct.pojo.StrucFieldSubenum;
import cho.carbon.imodel.model.struct.pojo.StrucFieldValue;
import cho.carbon.imodel.model.struct.pojo.StrucMiCode;

public class StrucBaseContainer {
	private StrucBase strucBase;
	private StrucMiCode strucMiCode;
	private StrucFieldSubenum strucFieldSubenum;
	private StrucFieldValue strucFieldValue;
	
	
	public StrucBase getStrucBase() {
		return strucBase;
	}
	public void setStrucBase(StrucBase strucBase) {
		this.strucBase = strucBase;
	}
	public StrucMiCode getStrucMiCode() {
		return strucMiCode;
	}
	public void setStrucMiCode(StrucMiCode strucMiCode) {
		this.strucMiCode = strucMiCode;
	}
	public StrucFieldSubenum getStrucFieldSubenum() {
		return strucFieldSubenum;
	}
	public void setStrucFieldSubenum(StrucFieldSubenum strucFieldSubenum) {
		this.strucFieldSubenum = strucFieldSubenum;
	}
	public StrucFieldValue getStrucFieldValue() {
		return strucFieldValue;
	}
	public void setStrucFieldValue(StrucFieldValue strucFieldValue) {
		this.strucFieldValue = strucFieldValue;
	}
}
