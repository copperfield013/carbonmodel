package cho.carbon.imodel.model.struct.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "t_cc_struc_micode")
public class StrucMiCode implements Cloneable {
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "sb_id")
	private Integer sbId;
	
	@Column(name = "item_code")
	private String itemCode;
	
	public StrucMiCode() {}

	public StrucMiCode(Integer sbId, String itemCode) {
		super();
		this.sbId = sbId;
		this.itemCode = itemCode;
	}

	public Integer getSbId() {
		return sbId;
	}

	public void setSbId(Integer sbId) {
		this.sbId = sbId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return (StrucMiCode)super.clone();
	}
}
