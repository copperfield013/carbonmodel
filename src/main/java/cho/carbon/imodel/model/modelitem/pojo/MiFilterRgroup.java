package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	
	//存在这个关系，  关系可以多选
	@Column(name = "in_relation_type")
	private String inRelationType;
		
	//不存在这个关系
	@Column(name = "ex_relation_type")
	private String exRelationType;
	
	//这个先不用
	@Column(name = "in_left_code")
	private String inLeftCode;
	
	//这个先不用
	@Column(name = "ex_left_code")
	private String exLeftCode;
	
	// 存在一个具体的数据
	@Column(name = "in_right_code")
	private String inRightCode;
	
	// 不存在一个具体的数据
	@Column(name = "ex_right_code")
	private String exRightCode;

	public MiFilterRgroup() {}

	public MiFilterRgroup(Integer groupId, String inRelationType, String exRelationType, String inLeftCode,
			String exLeftCode, String inRightCode, String exRightCode) {
		super();
		this.groupId = groupId;
		this.inRelationType = inRelationType;
		this.exRelationType = exRelationType;
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

	public String getInRelationType() {
		return inRelationType;
	}

	public void setInRelationType(String inRelationType) {
		this.inRelationType = inRelationType;
	}

	public String getExRelationType() {
		return exRelationType;
	}

	public void setExRelationType(String exRelationType) {
		this.exRelationType = exRelationType;
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
