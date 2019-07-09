package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 过滤条件关系分组
 * @author so-well
 *
 */
@Entity
@Table(name = "t_cc_mi_filter_rgroup")
public class MiFilterRgroup {
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "group_id")
	private Integer groupId;
	
	//值域为  or and
	@Column(name = "logical_operator")
	private Integer logicalOperator;

	@Column(name = "relation_type")
	private String relationType;
	
	@Column(name = "in_left_code")
	private String inLeftCode;
	
	@Column(name = "ex_left_code")
	private String exLeftCode;
	
	@Column(name = "in_right_code")
	private String inRightCode;
	
	@Column(name = "ex_right_code")
	private String exRightCode;

	public MiFilterRgroup() {}
	
	public MiFilterRgroup(Integer groupId, Integer logicalOperator, String relationType, String inLeftCode,
			String exLeftCode, String inRightCode, String exRightCode) {
		
		this.groupId = groupId;
		this.logicalOperator = logicalOperator;
		this.relationType = relationType;
		this.inLeftCode = inLeftCode;
		this.exLeftCode = exLeftCode;
		this.inRightCode = inRightCode;
		this.exRightCode = exRightCode;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(Integer logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getInLeftCode() {
		return inLeftCode;
	}

	public void setInLeftCode(String inLeftCode) {
		this.inLeftCode = inLeftCode;
	}

	public String getExLeftCode() {
		return exLeftCode;
	}

	public void setExLeftCode(String exLeftCode) {
		this.exLeftCode = exLeftCode;
	}

	public String getInRightCode() {
		return inRightCode;
	}

	public void setInRightCode(String inRightCode) {
		this.inRightCode = inRightCode;
	}

	public String getExRightCode() {
		return exRightCode;
	}

	public void setExRightCode(String exRightCode) {
		this.exRightCode = exRightCode;
	}
	
}
