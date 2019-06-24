package cho.carbon.imodel.model.modelitem.dao;

import java.util.List;

/**
 * 数据表结构生成
 * @author so-well
 *
 */
public interface MiTableDBDao {
	/**
	 * 查询需要创建的表
	 * @return
	 */
	List queryCreTab(String dataBaseName);
	
	/**
	 * 新增的字段
	 * @return
	 */
	List queryNewAddCol(String dataBaseName);
	
	/**
	 * 需要更新的字段
	 * @return
	 */
	List queryEditCol(String dataBaseName);
	
	/**
	 * 关系表函数
	 * @return
	 */
	List queryCreRelaTabFun(String dataBaseName);
	
	/**
	 * 关键关系表
	 * @return
	 */
	List queryCreRelaTab(String dataBaseName);
	
	/**
	 * 给多值属性表， 添加索引
	 * @return
	 */
	List queryCreRepeatTabIndex(String dataBaseName);
	
	/**
	 * 创建实体文件表F1
	 * @return
	 */
	List queryCreEntityFileTbaF1(String dataBaseName);
	
	/**
	 * 创建实体文件表F2
	 * @return
	 */
	List queryCreEntityFileTbaF2(String dataBaseName);
	
	/**
	 * 创建实体文件表F3
	 * @return
	 */
	List queryCreEntityFileTbaF3(String dataBaseName);
	
	/**
	 * 创建实体历史表H1
	 * @return
	 */
	List queryCreEntityTabH1(String dataBaseName);
	/**
	 * 创建实体历史表H2
	 * @return
	 */
	List queryCreEntityTabH2(String dataBaseName);
	
	/**
	 * 创建删除实体D1表
	 * @return
	 */
	List queryCreEntityTabD1(String dataBaseName);
	
	/**
	 * 创建实体c1表
	 * @return
	 */
	List queryCreEntityTabc1(String dataBaseName);
	
	/**
	 * 创建实体c2表
	 * @return
	 */
	List queryCreEntityTabc2(String dataBaseName);
}
