package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 模型   字典   关系  数据改变时
 * @author so-well
 *
 */
@Entity
@Table(name = "t_cc_mi_change")
public class BasicChange {

	public static final String  RECORDRELATION = "recordrelation";
	public static final String  CASCADEDICT = "cascadedict";
	
	  @Id
	  @Column(name="c_code")
	  @GenericGenerator(name = "system-uuid", strategy = "uuid")
	  private String code;//单独生成规则
	  
	  public BasicChange() {
		 
	  }
	  
	  public BasicChange(String code) {
		  this.code = code;
	  }
	  

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
