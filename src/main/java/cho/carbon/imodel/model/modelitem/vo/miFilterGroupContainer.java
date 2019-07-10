package cho.carbon.imodel.model.modelitem.vo;


public class miFilterGroupContainer {
	
	private Integer id;
	
	private String name;
	
	// 值域 ： 1. 普通组 2. 关系组
	private Integer type;
	
	private Integer logicalOperator;

	private Integer pid;
	
	private Integer corder;
	
	private Integer groupId;
	
	private String relationType;
	
	private String inLeftCode;
	
	private String exLeftCode;
	
	private String inRightCode;
	
	private String exRightCode;

	public miFilterGroupContainer(Integer id, String name, Integer type, Integer logicalOperator, Integer pid,
			Integer corder, Integer groupId, String relationType, String inLeftCode, String exLeftCode,
			String inRightCode, String exRightCode) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.logicalOperator = logicalOperator;
		this.pid = pid;
		this.corder = corder;
		this.groupId = groupId;
		this.relationType = relationType;
		this.inLeftCode = inLeftCode;
		this.exLeftCode = exLeftCode;
		this.inRightCode = inRightCode;
		this.exRightCode = exRightCode;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(Integer logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getCorder() {
		return corder;
	}

	public void setCorder(Integer corder) {
		this.corder = corder;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getInLeftCode() {
		return inLeftCode;
	}

	public void setInLeftCode(String inLeftCode) {
		this.inLeftCode = inLeftCode;
	}

	public String getExLeftCode() {
		return exLeftCode;
	}

	public void setExLeftCode(String exLeftCode) {
		this.exLeftCode = exLeftCode;
	}

	public String getInRightCode() {
		return inRightCode;
	}

	public void setInRightCode(String inRightCode) {
		this.inRightCode = inRightCode;
	}

	public String getExRightCode() {
		return exRightCode;
	}

	public void setExRightCode(String exRightCode) {
		this.exRightCode = exRightCode;
	}

}
