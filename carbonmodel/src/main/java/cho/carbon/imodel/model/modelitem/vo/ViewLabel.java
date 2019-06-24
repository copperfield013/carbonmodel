package cho.carbon.imodel.model.modelitem.vo;

import java.util.HashMap;
import java.util.Map;

public class ViewLabel {
	
	/* <input name="name" type="text" class="edit-input" value="${btNode.name}"> */
	
	private String label;
	private String type;
	private String name;
	//值
	private String value;
	//显示名称
	private String showName;
	// 值域   value 属性的取值范围
	private Map<String, String> valueDomain = null;
	
	private String viewClazz;//页面class标记
	
	public ViewLabel() {}
	
	public ViewLabel(String label, String type, String name, String value, String showName) {
		super();
		this.label = label;
		this.type = type;
		this.name = name;
		this.value = value;
		this.showName = showName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public Map<String, String> getValueDomain() {
		return valueDomain;
	}

	public void setValueDomain(Map<String, String> valueDomain) {
		this.valueDomain = valueDomain;
	}

	public String getViewClazz() {
		return viewClazz;
	}

	public void setViewClazz(String viewClazz) {
		this.viewClazz = viewClazz;
	}
	
}
