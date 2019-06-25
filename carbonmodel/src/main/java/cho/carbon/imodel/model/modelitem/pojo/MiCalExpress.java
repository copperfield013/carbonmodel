package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 表达式类
 * @author so-well
 *
 */
@Entity
@Table(name = "t_cc_mi_cal_express")
public class MiCalExpress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "code_txt")
	private String codeTxt;
	
	@Column(name = "name_txt")
	private String nameTxt;

	public MiCalExpress() {}
	
	public MiCalExpress(Integer id, String codeTxt, String nameTxt) {
		super();
		this.id = id;
		this.codeTxt = codeTxt;
		this.nameTxt = nameTxt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodeTxt() {
		return codeTxt;
	}

	public void setCodeTxt(String codeTxt) {
		this.codeTxt = codeTxt;
	}

	public String getNameTxt() {
		return nameTxt;
	}

	public void setNameTxt(String nameTxt) {
		this.nameTxt = nameTxt;
	}
	
}
