package cho.carbon.imodel.model.struct.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "t_cc_struc_field_subenum")
public class StrucFieldSubenum {
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "sb_id")
	private Integer sbId;
	
	@Column(name = "subenum_id")
	private Integer subenumId;
	
	public StrucFieldSubenum() {}

	public StrucFieldSubenum(Integer sbId, Integer subenumId) {
		super();
		this.sbId = sbId;
		this.subenumId = subenumId;
	}

	public Integer getSbId() {
		return sbId;
	}

	public void setSbId(Integer sbId) {
		this.sbId = sbId;
	}

	public Integer getSubenumId() {
		return subenumId;
	}

	public void setSubenumId(Integer subenumId) {
		this.subenumId = subenumId;
	}
	
}
