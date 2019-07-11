package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import cho.carbon.meta.enun.ModelItemType;


@Entity
@Table(name = "t_cc_model_item")
public class ModelItem {
	
	 @Id
	  @Column(name="code")
	  @GenericGenerator(name = "system-uuid", strategy = "uuid")
	  private String code;//单独生成规则
	  
	  @Column(name="name")
	  private String name;
	  
	  @Column(name="type")
	  private Integer type;

	  @Column(name="parent")
	  private String parent;
	  
	  @Column(name="belong_model")
	  private String belongModel;
	  
	  @Column(name="using_state")
	  private Integer usingState;
	  
	  @Column(name="description")
	  private String description;
	  
	  @Transient
	  private String showType;
	  
	  @Transient
	  private String showUsingState;
	  
	  //级联枚举孩子的数量
	  @Transient
	  private Integer casEnumChildCount;
	  
	  public ModelItem() {}
	  
	public ModelItem(String code, String name, Integer type, String parent, String belongModel, Integer usingState,
			String description) {
		super();
		this.code = code;
		this.name = name;
		this.type = type;
		this.parent = parent;
		this.belongModel = belongModel;
		this.usingState = usingState;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getBelongModel() {
		return belongModel;
	}

	public void setBelongModel(String belongModel) {
		this.belongModel = belongModel;
	}
	
	public Integer getUsingState() {
		return usingState;
	}
	
	

	public String getShowUsingState() {
		
		  switch (this.usingState) {
			  case 0: 
				  return "新增"; 
			  case 1: return "正常使用";
			  case 2: 
				  return "已过期"; 
			  case -1: 
				return "错误"; 
		  }
		
		return showUsingState;
	}

	public void setUsingState(Integer usingState) {
		this.usingState = usingState;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setCasEnumChildCount(Integer casEnumChildCount) {
		this.casEnumChildCount = casEnumChildCount;
	}

	public Integer getCasEnumChildCount() {
		return casEnumChildCount;
	}

	public String getShowType() {
		
		switch (this.type) {
		case 1:
			return "普通实体";
		case 101:
			return ModelItemType.STAT_MODEL.getCName();
		case 102:
			return ModelItemType.SQL_MODEL.getCName();
		case 2:
			return "普通属性";
		case 201:
			return "枚举属性";
		case 202:
			return "预设枚举";
		case 203:
			return "文件属性";
		case 204:
			return "多选枚举";
		case 205:
			return "引用属性";
		case 206:
			return "级联枚举";
		case 207:
			return "级联引用";
		case 208:
			return "加密属性";
		case 209:
			return "计算属性";
		case 210:
			return "密码属性";
		case 211:
			return ModelItemType.DIMENSION_ITEM.getCName();
		case 212:
			return ModelItemType.FACT_ITEM.getCName();
		case 4:
			return "二级属性";
		case 5:
			return "单行分组";
		case 501:
			return  ModelItemType.DIMENSION_GROUP.getCName();
		case 502:
			return ModelItemType.FACT_GROUP.getCName();
		case 6:
			return "多行分组";
		case 7:
			return "巨行属性";
		default:
			return "未知属性";
		}
	}
}
