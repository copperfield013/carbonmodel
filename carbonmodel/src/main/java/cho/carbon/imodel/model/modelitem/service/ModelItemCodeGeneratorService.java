package cho.carbon.imodel.model.modelitem.service;

import cho.carbon.meta.enun.ModelItemType;

public interface ModelItemCodeGeneratorService {
	
	
	/**
	 *   获取 BasicItem 模型code
	 * @param dataType    ValueType.RECORD.getIndex()
	 * @return
	 * @throws Exception
	 */
	public String getBasicItemCode(ModelItemType dataType, String belongModel) throws Exception;
	
	/**
	 * 	输入实体code 和 中缀，组合关系code
	 * @param belongModel
	 * @param infix
	 * @return
	 * @throws Exception 
	 */
	public String getRelaCode(String belongModel) throws Exception;
	
	/**
	 * 获取实体和属性前缀
	 * @param dataType    实体和属性本身的类型
	 * @param belongModel    实体的code , 例如：XFJDE001 ，实体进来可传  null
	 * @return
	 * @throws Exception
	 */
	public String getBasicItemFix(ModelItemType dataType, String belongModel) throws Exception;
	
}
