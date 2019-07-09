package cho.carbon.imodel.model.cascadedict.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "t_cc_casenum_item")
public class CascadedictBasicItem {

	 @Id
	  @Column(name="id")
	  @GenericGenerator(name = "system-uuid", strategy = "uuid")
	  private Integer id;
	
	@Column(name = "cas_pid")
	private String casPid;
	
	@Column(name = "parent_id")
	private Integer parentId;
	
	@Column(name = "corder")
	private Integer order;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "using_state")
	private String status;
	
	@Column(name="update_time")
	private String updateTime;
	
	@Transient
	private String enName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCasPid() {
		return casPid;
	}

	public void setCasPid(String casPid) {
		this.casPid = casPid;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		
		
		
		
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getEnName() {
		return "";
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}
}