package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import cho.carbon.meta.enun.ModelItemType;

/**
 * 全sql统计实体， 存储sql语句
 * @author chuyin
 *
 */

@Entity
@Table(name = "t_cc_mi_model_sql")
public class MiModelSql {
	
	 @Id
	  @Column(name="code")
	  @GenericGenerator(name = "system-uuid", strategy = "uuid")
	  private String code;//单独生成规则
	  
	  @Column(name="sql_txt")
	  private String sqlTxt;
	  
	  public MiModelSql() {}

	public MiModelSql(String code, String sqlTxt) {
		this.code = code;
		this.sqlTxt = sqlTxt;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSqlTxt() {
		return sqlTxt;
	}

	public void setSqlTxt(String sqlTxt) {
		this.sqlTxt = sqlTxt;
	}
	 
}
