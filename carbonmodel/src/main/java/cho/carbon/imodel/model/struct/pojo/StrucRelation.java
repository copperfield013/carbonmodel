package cho.carbon.imodel.model.struct.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "t_cc_struc_relation")
public class StrucRelation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "sb_id")
	private Integer sbId;
	
	@Column(name = "model_relation_type")
	private String modelRelationType;

	public StrucRelation() {}
	
	public StrucRelation(Integer id, Integer sbId, String modelRelationType) {
		super();
		this.id = id;
		this.sbId = sbId;
		this.modelRelationType = modelRelationType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSbId() {
		return sbId;
	}

	public void setSbId(Integer sbId) {
		this.sbId = sbId;
	}

	public String getModelRelationType() {
		return modelRelationType;
	}

	public void setModelRelationType(String modelRelationType) {
		this.modelRelationType = modelRelationType;
	}
	
}
