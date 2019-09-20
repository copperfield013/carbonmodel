package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模型前缀表
 * @author so-well
 *
 */
@Entity
@Table(name = "t_cc_model_item_fix")
public class ModelItemFix {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "prefix")
	private String prefix;
	/**
	 * 1: 启用
	 * 0： 废弃
	 */
	@Column(name = "using_state")
	private Integer usingState;

	@Column(name="describe_txt")
	private String describeTxt;

	public ModelItemFix() {}

	public ModelItemFix(Integer id, String prefix, Integer usingState, String describeTxt) {
		super();
		this.id = id;
		this.prefix = prefix;
		this.usingState = usingState;
		this.describeTxt = describeTxt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Integer getUsingState() {
		return usingState;
	}

	public void setUsingState(Integer usingState) {
		this.usingState = usingState;
	}

	public String getDescribeTxt() {
		return describeTxt;
	}

	public void setDescribeTxt(String describeTxt) {
		this.describeTxt = describeTxt;
	}
	
}
