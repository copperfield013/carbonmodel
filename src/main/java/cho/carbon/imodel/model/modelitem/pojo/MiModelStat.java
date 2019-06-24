package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_cc_mi_model_stat")
public class MiModelStat {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "code")
	private String code;

	@Column(name = "source_code")
	private String sourceCode;

	public MiModelStat() {}
	
	public MiModelStat(String code, String sourceCode) {
		super();
		this.code = code;
		this.sourceCode = sourceCode;
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
}
