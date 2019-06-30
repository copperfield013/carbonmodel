package cho.carbon.imodel.model.struct.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import cho.carbon.meta.enun.StrucElementType;

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
	private Integer type;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "opt")
	private Integer opt;
	
	@Column(name = "corder")
	private Integer corder;
	
	@Column(name = "parent_id")
	private Integer parentId;
	
	 @Transient
	private String showType;
	
	public StrucBase() {}

	public StrucBase(Integer id, Integer type, String title, Integer opt, Integer corder, Integer parentId) {
		super();
		this.id = id;
		this.type = type;
		this.title = title;
		this.opt = opt;
		this.corder = corder;
		this.parentId = parentId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getOpt() {
		return opt;
	}

	public void setOpt(Integer opt) {
		this.opt = opt;
	}

	public Integer getCorder() {
		return corder;
	}

	public void setCorder(Integer corder) {
		this.corder = corder;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getShowType() {
		StrucElementType elementType = StrucElementType.getType(this.type);
		
		switch (elementType) {
		case STRUC:
			return StrucElementType.STRUC.getCnName();
		case GROUP1D:
			return StrucElementType.GROUP1D.getCnName();
		case GROUP2D:
			return StrucElementType.GROUP2D.getCnName();
		case RSTRUC:
			return StrucElementType.RSTRUC.getCnName();
		case FIELD:
			return StrucElementType.FIELD.getCnName();
		case ENUMFIELD:
			return StrucElementType.ENUMFIELD.getCnName();
		case RFIELD:
			return StrucElementType.RFIELD.getCnName();
		case REFFIELD:
			return StrucElementType.REFFIELD.getCnName();
		case RREFFIELD:
			return "R引用字段";
		}
		return "未知字段";
	}
	
	
	
}
