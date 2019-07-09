package cho.carbon.imodel.model.struct.vo;

import cho.carbon.imodel.model.struct.pojo.StrucBase;
import cho.carbon.imodel.model.struct.pojo.StrucFieldSubenum;
import cho.carbon.imodel.model.struct.pojo.StrucFieldValue;
import cho.carbon.imodel.model.struct.pojo.StrucMiCode;
import cho.carbon.imodel.model.struct.pojo.StrucPointer;
import cho.carbon.imodel.model.struct.pojo.StrucRRef;
import cho.carbon.imodel.model.struct.pojo.StrucRelation;

public class StrucBaseContainer {
	private StrucBase strucBase;
	private StrucMiCode strucMiCode;
	private StrucFieldSubenum strucFieldSubenum;
	private StrucFieldValue strucFieldValue;
	private StrucRelation strucRelation;
	private StrucPointer strucPointer;
	private StrucRRef strucRRef;
	
	
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
	public StrucRelation getStrucRelation() {
		return strucRelation;
	}
	public void setStrucRelation(StrucRelation strucRelation) {
		this.strucRelation = strucRelation;
	}
	public StrucPointer getStrucPointer() {
		return strucPointer;
	}
	public void setStrucPointer(StrucPointer strucPointer) {
		this.strucPointer = strucPointer;
	}
	public StrucRRef getStrucRRef() {
		return strucRRef;
	}
	public void setStrucRRef(StrucRRef strucRRef) {
		this.strucRRef = strucRRef;
	}
	
}
