package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 	计算属性对应的表达式和过滤条件
 * @author so-well
 *
 */
@Entity
@Table(name = "t_cc_mi_calculated")
public class MiCalculated {
	
	 @Id
	  @Column(name="code")
	  @GenericGenerator(name = "system-uuid", strategy = "uuid")
	  private String code;//单独生成规则

	 @Column(name="express_id")
	  private Integer expressId;
	 
	 /**
	  * 过滤条件id
	  */
	 @Column(name="filter_id")
	 private Integer filterId;
	 
	 @Column(name="corder")
	 private Integer corder;
	 
	 /**
	  * 值域： CalculatedItemType.PRESERVE
	 		CalculatedItemType.FETCH
	  */
	 @Column(name="type")
	 private Integer type;
	 
	 public MiCalculated() {}
	 
	public MiCalculated(String code, Integer expressId, Integer filterId, Integer corder) {
		super();
		this.code = code;
		this.expressId = expressId;
		this.filterId = filterId;
		this.corder = corder;
	}

	public MiCalculated(String code, Integer expressId, Integer filterId, Integer corder, Integer type) {
		super();
		this.code = code;
		this.expressId = expressId;
		this.filterId = filterId;
		this.corder = corder;
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getExpressId() {
		return expressId;
	}

	public void setExpressId(Integer expressId) {
		this.expressId = expressId;
	}

	public Integer getFilterId() {
		return filterId;
	}

	public void setFilterId(Integer filterId) {
		this.filterId = filterId;
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
