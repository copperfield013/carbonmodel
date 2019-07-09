package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 引用属性类
 * @author so-well
 *
 */
@Entity
@Table(name = "t_cc_mi_reference")
public class MiReference {

	  @Id
	  @Column(name="code")
	  @GenericGenerator(name = "system-uuid", strategy = "uuid")
	  private String code;//单独生成规则   存放t_sc_basic_item中引用属性的code
	
	  //引用模型Code
	 @Column(name="model_code")
	  private String modelCode;
	 
	 //识别属性  除了伴生的，枚举也不选，  都可以选择
	 @Column(name="recognition_code")
	  private String recognitionCode;
	 
	 //展示属性   只能选择普通的
	 @Column(name="show_code")
	  private String showCode;
	 
	 @Column(name="add_new_ref")
	  private String addNewRef;//填 是：1  否：0
	 
	 public MiReference() {}

	public MiReference(String code, String modelCode, String recognitionCode, String showCode, String addNewRef) {
		super();
		this.code = code;
		this.modelCode = modelCode;
		this.recognitionCode = recognitionCode;
		this.showCode = showCode;
		this.addNewRef = addNewRef;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getRecognitionCode() {
		return recognitionCode;
	}

	public void setRecognitionCode(String recognitionCode) {
		this.recognitionCode = recognitionCode;
	}

	public String getShowCode() {
		return showCode;
	}

	public void setShowCode(String showCode) {
		this.showCode = showCode;
	}

	public String getAddNewRef() {
		return addNewRef;
	}

	public void setAddNewRef(String addNewRef) {
		this.addNewRef = addNewRef;
	}
}
