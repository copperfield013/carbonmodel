package cho.carbon.imodel.model.buildproject.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cho.carbon.imodel.model.buildproject.dao.BuildProjectDao;


/**
 * 	构建项目接口
 * @author so-well
 *
 */
@Repository
public class BuildProjectDaoImpl implements BuildProjectDao {
	
	@Resource
	SessionFactory sFactory;
	
	/**
	   * 	获取枚举数据
	 * @return
	 */
	 public List<String> getEnumData() {
		 StringBuffer sb = new StringBuffer(100);
		 sb.append("SELECT ")
		 .append("  CONCAT('public static final Integer ENUM_',")
		 .append(" replace(replace(replace(replace(b.name, '（', '_'), '）', '_'), '(', '_'), ')', '_'), ")
		 .append(" '_', ")
		 .append(" replace(replace(replace(replace(a.name, '（', '_'), '）', '_'), '(', '_'), ')', '_'), ")
		 .append(" '=', a.id, ';')")
		 .append(" FROM")
		 .append(" t_cc_casenum_item a")
		 .append(" LEFT JOIN")
		 .append("  t_cc_casenum_item b ON a.parent_id = b.id")
		 .append(" WHERE")
		 .append(" a.parent_id != 0")
		 .append(" ORDER BY b.id , a.id");
		 
		 return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	 }
	
	/**
	 * 	获取关系数据
	 * @return
	 */
	 public List<String> getRelationData() {
		 StringBuffer sb = new StringBuffer(200);
		 sb.append(" SELECT ")
		 .append(" CONCAT('public static final String RR_',")
		 .append("  b.name, '_', a.name, '_', c.name, '=\"', a.type_code, '\";')")
		 .append(" FROM")
		 .append(" t_cc_model_relation_type a")
		 .append("  LEFT JOIN")
		 .append(" t_cc_model_item b ON a.left_model_code = b.code")
		 .append(" LEFT JOIN")
		 .append(" t_cc_model_item c ON a.right_model_code = c.code ")
		 .append(" 	WHERE b.type=1 AND c.type=1")
		 .append(" order by a.type_code");
		
		 return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	 }
	
	 /**
	  * 	获取实体的item 属性字段
	  * @param entityCode    实体code
	  * @param entityPrefix   实体前缀
	  * @return
	  */
	 public List<String> getItemData(String entityCode, String entityPrefix) {
		 
		 StringBuffer sb = new StringBuffer(400);
		 
		 sb.append(" SELECT")
		 .append(" CONCAT(	'public static final String ',p.name,'_',")
		 .append(" REPLACE ( REPLACE ( d.name, '）', '_' ), '（', '_' ),")
		 .append(" '=\"',	d.code,	'\";' ) AS item ")
		 .append(" FROM	t_cc_model_item d ")
		 .append(" left join t_cc_model_item p on d.parent=p.code")
		 .append(" WHERE	d.code LIKE '%"+entityPrefix+"%' 	AND d.belong_model = '"+entityCode+"'")
		 .append(" AND d.code <>'"+entityCode+"'");
		 return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	 }
	 
	 /**
	  * 建立项目
	  * @return
	  */
	 public  String buildProject() {
		 
		 
		 return null;
	 }
}
