package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 维度属性
 * @author so-well
 *
 */
@Entity
@Table(name = "t_cc_mi_stat_dimension")
public class MiStatDimension {
	
	  @Id
	  @Column(name="code")
	  @GenericGenerator(name = "system-uuid", strategy = "uuid")
	  private String code;//单独生成规则
	
	  /**
	   * 表达式id
	   */
	 @Column(name="express_id")
	  private Integer expressId;
	 
	 @Column(name="corder")
	  private Integer corder;

	 public MiStatDimension() {
		 
	 }
	 
	public MiStatDimension(String code, Integer expressId, Integer corder) {
		super();
		this.code = code;
		this.expressId = expressId;
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

	public Integer getCorder() {
		return corder;
	}

	public void setCorder(Integer corder) {
		this.corder = corder;
	}
	 
}
