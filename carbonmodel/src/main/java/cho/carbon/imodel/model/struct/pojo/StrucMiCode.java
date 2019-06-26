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
public class StrucMiCode {
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "sb_id")
	private Integer sbId;
	
	@Column(name = "item_code")
	private String item_code;
	
	public StrucMiCode() {}

	public StrucMiCode(Integer sbId, String item_code) {
		super();
		this.sbId = sbId;
		this.item_code = item_code;
	}

	public Integer getSbId() {
		return sbId;
	}

	public void setSbId(Integer sbId) {
		this.sbId = sbId;
	}

	public String getItem_code() {
		return item_code;
	}

	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	
}
