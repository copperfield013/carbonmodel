package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 枚举属性对应的枚举类
 * @author so-well
 *
 */

@Entity
@Table(name = "t_cc_mi_enum")
public class MiEnum {
	  @Id
	  @Column(name="code")
	  @GenericGenerator(name = "system-uuid", strategy = "uuid")
	  private String code;//单独生成规则
	
	 @Column(name="pid")
	  private Integer pid;

	 public MiEnum() {}
	 
	public MiEnum(String code, Integer pid) {
		super();
		this.code = code;
		this.pid = pid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}
	 
}
