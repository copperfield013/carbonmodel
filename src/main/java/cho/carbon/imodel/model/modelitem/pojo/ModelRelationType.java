package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_cc_model_relation_type")
public class ModelRelationType {

	// 关系code
	@Id
	@Column(name = "type_code")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String typeCode;// 主键规则待定
	// 关系名称
	@Column(name = "name")
	private String name;

	// 左模型code
	@Column(name = "left_model_code")
	private String leftModelCode;

	// 右模型code
	@Column(name = "right_model_code")
	private String rightModelCode;

	// 逆向关系code
	@Column(name = "reverse_code")
	private String reverseCode;
	
	// 我的状态
	@Column(name="using_state")
	private Integer usingState;
	
	// 对一、 对多
	@Column(name="relation_type")
	private Integer relationType;
	
	@Column(name="giant")
	private Integer giant; //  关系是巨型的
	
	
	@Transient
	private String leftModelName;
	
	@Transient
	private String rightModelName;

	public ModelRelationType() {}
	
	public ModelRelationType(String typeCode, String name, String leftModelCode, String rightModelCode,
			String reverseCode, Integer usingState, Integer relationType, Integer giant) {
		this.typeCode = typeCode;
		this.name = name;
		this.leftModelCode = leftModelCode;
		this.rightModelCode = rightModelCode;
		this.reverseCode = reverseCode;
		this.usingState = usingState;
		this.relationType = relationType;
		this.giant = giant;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLeftModelCode() {
		return leftModelCode;
	}

	public void setLeftModelCode(String leftModelCode) {
		this.leftModelCode = leftModelCode;
	}

	public String getRightModelCode() {
		return rightModelCode;
	}

	public void setRightModelCode(String rightModelCode) {
		this.rightModelCode = rightModelCode;
	}

	public String getReverseCode() {
		return reverseCode;
	}

	public void setReverseCode(String reverseCode) {
		this.reverseCode = reverseCode;
	}

	public Integer getUsingState() {
		return usingState;
	}

	public void setUsingState(Integer usingState) {
		this.usingState = usingState;
	}

	public Integer getRelationType() {
		return relationType;
	}

	public void setRelationType(Integer relationType) {
		this.relationType = relationType;
	}

	public Integer getGiant() {
		return giant;
	}

	public void setGiant(Integer giant) {
		this.giant = giant;
	}

	public String getLeftModelName() {
		return leftModelName;
	}

	public String getRightModelName() {
		return rightModelName;
	}

	public void setLeftModelName(String leftModelName) {
		this.leftModelName = leftModelName;
	}

	public void setRightModelName(String rightModelName) {
		this.rightModelName = rightModelName;
	}
	
}