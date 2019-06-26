package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 具体过滤条件
 * @author so-well
 *
 */
@Entity
@Table(name = "t_cc_mi_filter_criterion")
public class MiFilterCriterion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;

	// 属性code
	@Column(name = "item_code")
	private String itemCode;
	
	//操作符
	@Column(name = "operator")
	private Integer operator;
	
	//值
	@Column(name = "value")
	private String value;
	
	//所属分组
	@Column(name = "group_id")
	private Integer group_id;
	
	@Column(name = "c_order")
	private Integer corder;

	public MiFilterCriterion() {}
	
	public MiFilterCriterion(Integer id, String name, String itemCode, Integer operator, String value, Integer group_id,
			Integer corder) {
		super();
		this.id = id;
		this.name = name;
		this.itemCode = itemCode;
		this.operator = operator;
		this.value = value;
		this.group_id = group_id;
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

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	public Integer getCorder() {
		return corder;
	}

	public void setCorder(Integer corder) {
		this.corder = corder;
	}
	
}
