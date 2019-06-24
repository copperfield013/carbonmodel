package cho.carbon.imodel.model.modelitem.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_cc_mi_cascade")
public class MiCascade implements Serializable {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "code")
	private String code;

	@Column(name = "level")
	private Integer level;
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "sub_code")
	private String subCode;
	
	public MiCascade() {}

	public MiCascade(String code, Integer level, String subCode) {
		super();
		this.code = code;
		this.level = level;
		this.subCode = subCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getSubCode() {
		return subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}
	
}
