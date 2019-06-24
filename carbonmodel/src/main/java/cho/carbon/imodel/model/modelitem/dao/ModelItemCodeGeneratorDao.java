package cho.carbon.imodel.model.modelitem.dao;


public interface ModelItemCodeGeneratorDao {
	/**
	 * 对象插入到数据表中
	 * @param demo
	 */
	void insert(Object obj);
	
	
	/**
	 * 	从数据库中加载，获取实体和属性前缀
	 * @return
	 * @throws Exception
	 */
	public String getBasicItemFixByDB() throws Exception;
		
}
