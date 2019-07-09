package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_cc_mi_value")
public class MiValue {
	
	  @Id
	  @Column(name="code")
	  @GenericGenerator(name = "system-uuid", strategy = "uuid")
	  private String code;//单独生成规则
	
	 @Column(name="data_type")
	  private String dataType;

	 
	 @Column(name="data_length")
	  private String dataLength;
	 
	 @Column(name="belong_table")
	  private String belongTable;
	 
	 @Column(name="using_state")
	  private Integer usingState;

	 public MiValue() {}
	 
	public MiValue(String code, String dataType, String dataLength, String belongTable, Integer usingState) {
		this.code = code;
		this.dataType = dataType;
		this.dataLength = dataLength;
		this.belongTable = belongTable;
		this.usingState = usingState;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataLength() {
		return dataLength;
	}

	public void setDataLength(String dataLength) {
		this.dataLength = dataLength;
	}

	public String getBelongTable() {
		return belongTable;
	}

	public void setBelongTable(String belongTable) {
		this.belongTable = belongTable;
	}

	public Integer getUsingState() {
		return usingState;
	}

	public void setUsingState(Integer usingState) {
		this.usingState = usingState;
	}
	 
}
