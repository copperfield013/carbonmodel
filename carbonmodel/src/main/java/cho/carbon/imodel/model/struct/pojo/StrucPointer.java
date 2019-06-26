package cho.carbon.imodel.model.struct.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "t_cc_struc_pointer")
public class StrucPointer {
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "sb_id")
	private Integer sbId;
	
	@Column(name = "pointer")
	private Integer pointer;
	
	public StrucPointer() {}

	public StrucPointer(Integer sbId, Integer pointer) {
		super();
		this.sbId = sbId;
		this.pointer = pointer;
	}

	public Integer getSbId() {
		return sbId;
	}

	public void setSbId(Integer sbId) {
		this.sbId = sbId;
	}

	public Integer getPointer() {
		return pointer;
	}

	public void setPointer(Integer pointer) {
		this.pointer = pointer;
	}
	
}
