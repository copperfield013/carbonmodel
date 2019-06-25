package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 事实属性
 * @author so-well
 *
 */
@Entity
@Table(name = "t_cc_mi_stat_fact")
public class MiStatFact {
	
	  @Id
	  @Column(name="code")
	  @GenericGenerator(name = "system-uuid", strategy = "uuid")
	  private String code;//单独生成规则
	
	  /**
	   * 表达式id
	   */
	 @Column(name="express_id")
	  private Integer expressId;
	 
	 /**
	  * 过滤条件id
	  */
	 @Column(name="filter_id")
	 private Integer filterId;
	 
	 /**
	  * 事实属性上钻时用   枚举值域： AggregateFunctionType
	  */
	 @Column(name="updrill_func_type")
	 private Integer updrillFuncType;
	 
	 @Column(name="corder")
	  private Integer corder;

	 public MiStatFact() {}
	 
	public MiStatFact(String code, Integer expressId, Integer filterId, Integer updrillFuncType, Integer corder) {
		super();
		this.code = code;
		this.expressId = expressId;
		this.filterId = filterId;
		this.updrillFuncType = updrillFuncType;
		this.corder = corder;
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

	public Integer getUpdrillFuncType() {
		return updrillFuncType;
	}

	public void setUpdrillFuncType(Integer updrillFuncType) {
		this.updrillFuncType = updrillFuncType;
	}

	public Integer getCorder() {
		return corder;
	}

	public void setCorder(Integer corder) {
		this.corder = corder;
	}
	 
}
