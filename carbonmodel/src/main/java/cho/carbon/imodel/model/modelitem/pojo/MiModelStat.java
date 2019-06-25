package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 统计实体， 来源实体， 过滤条件
 * @author so-well
 *
 */
@Entity
@Table(name = "t_cc_mi_model_stat")
public class MiModelStat {
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "code")
	private String code;

	//来源实体code
	@Column(name = "source_code")
	private String sourceCode;
	
	@Column(name="filter_id")
	private Integer filterId;

	public MiModelStat() {}
	
	public MiModelStat(String code, String sourceCode, Integer filterId) {
		super();
		this.code = code;
		this.sourceCode = sourceCode;
		this.filterId = filterId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public Integer getFilterId() {
		return filterId;
	}

	public void setFilterId(Integer filterId) {
		this.filterId = filterId;
	}
	
}
