package cho.carbon.imodel.model.struct.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "t_cc_struc_field_value")
public class StrucFieldValue {
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "sb_id")
	private Integer sbId;
	
	@Column(name = "value_type")
	private Integer valueType;
	
	public StrucFieldValue() {}

	public StrucFieldValue(Integer sbId, Integer valueType) {
		super();
		this.sbId = sbId;
		this.valueType = valueType;
	}

	public Integer getSbId() {
		return sbId;
	}

	public void setSbId(Integer sbId) {
		this.sbId = sbId;
	}

	public Integer getValueType() {
		return valueType;
	}

	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}

}
