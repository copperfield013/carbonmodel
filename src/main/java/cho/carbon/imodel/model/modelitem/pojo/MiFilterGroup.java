package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 过滤条件分组
 * @author so-well
 *
 */
@Entity
@Table(name = "t_cc_mi_filter_group")
public class MiFilterGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	// 值域 ： 1. 普通组 2. 关系组
	@Column(name = "type")
	private Integer type;
	
	//值域为 1. OR 2.AND， 目前只支持AND
	@Column(name = "logical_operator")
	private Integer logicalOperator;

	@Column(name = "pid")
	private Integer pid;
	
	@Column(name = "c_order")
	private Integer corder;

	public MiFilterGroup() {}
	
	public MiFilterGroup(Integer id, String name, Integer type, Integer logicalOperator, Integer pid, Integer corder) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.logicalOperator = logicalOperator;
		this.pid = pid;
		this.corder = corder;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(Integer logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getCorder() {
		return corder;
	}

	public void setCorder(Integer corder) {
		this.corder = corder;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}
