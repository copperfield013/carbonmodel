package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_cc_mi_twolevel")
public class MiTowlevel {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "code")
	private String code;

	@Column(name = "mapping_id")
	private String mappingId;

	@Column(name = "enum_id")
	private String enumId;

	public MiTowlevel() {}
	
	public MiTowlevel(String code, String mappingId, String enumId) {
		super();
		this.code = code;
		this.mappingId = mappingId;
		this.enumId = enumId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMappingId() {
		return mappingId;
	}

	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}

	public String getEnumId() {
		return enumId;
	}

	public void setEnumId(String enumId) {
		this.enumId = enumId;
	}
}
