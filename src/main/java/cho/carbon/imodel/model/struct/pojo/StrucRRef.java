package cho.carbon.imodel.model.struct.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "t_cc_struc_rref")
public class StrucRRef implements Cloneable {
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "sb_id")
	private Integer sbId;
	
	@Column(name = "ref_item_code")
	private String refItemCode;
	
	public StrucRRef() {}

	public StrucRRef(Integer sbId, String refItemCode) {
		super();
		this.sbId = sbId;
		this.refItemCode = refItemCode;
	}

	public Integer getSbId() {
		return sbId;
	}

	public void setSbId(Integer sbId) {
		this.sbId = sbId;
	}

	public String getRefItemCode() {
		return refItemCode;
	}

	public void setRefItemCode(String refItemCode) {
		this.refItemCode = refItemCode;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return (StrucRRef)super.clone();
	}
}
