package cho.carbon.imodel.model.cascadedict.criteria;

import cho.carbon.imodel.model.cascadedict.pojo.DictionaryMappingAlias;

public class DictionaryMappingAliasCriteria extends DictionaryMappingAlias {
	private String btItemParentName;
	private String basicItemName;
	public String getBtItemParentName() {
		return btItemParentName;
	}
	public String getBasicItemName() {
		return basicItemName;
	}
	public void setBtItemParentName(String btItemParentName) {
		this.btItemParentName = btItemParentName;
	}
	public void setBasicItemName(String basicItemName) {
		this.basicItemName = basicItemName;
	}
	
}
