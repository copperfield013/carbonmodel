package cho.carbon.imodel.model.struct.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 	结构体基础类
 * @author so-well
 *
 */
@Entity
@Table(name = "t_cc_struc_base")
public class StrucBase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "opt")
	private String opt;
	
	@Column(name = "corder")
	private Integer corder;
	
	@Column(name = "parent_id")
	private Integer parent_id;
	
	public StrucBase() {}

	public StrucBase(Integer id, String type, String title, String opt, Integer corder, Integer parent_id) {
		super();
		this.id = id;
		this.type = type;
		this.title = title;
		this.opt = opt;
		this.corder = corder;
		this.parent_id = parent_id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public Integer getCorder() {
		return corder;
	}

	public void setCorder(Integer corder) {
		this.corder = corder;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}
}
