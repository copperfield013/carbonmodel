package cho.carbon.imodel.model.modelitem.pojo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_cc_mi_twolevel_mapping")
public class MiTwolevelMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "mlgroup_code")
	private String mlgroupCode;// 对应t_cc_model_item的code-多行类型

	@Column(name = "enum_item_code")
	private String enumItemCode;// 重复类型包含的枚举类型的属性

	@Column(name = "value_item_code")
	private String valueItemCode;// 重复类型下面的其中一个属性

	@Column(name = "description")
	private String description;
	
	public MiTwolevelMapping() {}

	public MiTwolevelMapping(Integer id, String name, String mlgroupCode, String enumItemCode, String valueItemCode,
			String description) {
		super();
		this.id = id;
		this.name = name;
		this.mlgroupCode = mlgroupCode;
		this.enumItemCode = enumItemCode;
		this.valueItemCode = valueItemCode;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMlgroupCode() {
		return mlgroupCode;
	}

	public void setMlgroupCode(String mlgroupCode) {
		this.mlgroupCode = mlgroupCode;
	}

	public String getEnumItemCode() {
		return enumItemCode;
	}

	public void setEnumItemCode(String enumItemCode) {
		this.enumItemCode = enumItemCode;
	}

	public String getValueItemCode() {
		return valueItemCode;
	}

	public void setValueItemCode(String valueItemCode) {
		this.valueItemCode = valueItemCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
