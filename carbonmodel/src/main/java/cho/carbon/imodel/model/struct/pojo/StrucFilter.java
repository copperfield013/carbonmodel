package cho.carbon.imodel.model.struct.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "t_cc_struc_filter")
public class StrucFilter {
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "sb_id")
	private Integer sbId;
	
	@Column(name = "filter_group_id")
	private Integer filterGroupId;
	
	public StrucFilter() {}

	public StrucFilter(Integer sbId, Integer filterGroupId) {
		super();
		this.sbId = sbId;
		this.filterGroupId = filterGroupId;
	}

	public Integer getSbId() {
		return sbId;
	}

	public void setSbId(Integer sbId) {
		this.sbId = sbId;
	}

	public Integer getFilterGroupId() {
		return filterGroupId;
	}

	public void setFilterGroupId(Integer filterGroupId) {
		this.filterGroupId = filterGroupId;
	}

}
