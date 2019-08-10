package cho.carbon.imodel.model.modelitem.service.impl;


import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;


import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.Constants;
import cho.carbon.imodel.model.modelitem.dao.MiTableDBDao;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.MiTableDBService;


@Service
public class MiTableDBServiceImpl implements MiTableDBService {

	@Resource
	SessionFactory sFactory;
	
	@Resource
	CommService commService;
	
	@Resource
	MiTableDBDao miTableDBDao;
	
	private void updateMiUsingState(String code, Integer usingState) {
		Session openSession = null;
		Transaction tx = null;
		try {
			openSession = sFactory.openSession();
			tx = openSession.beginTransaction();
			
			ModelItem modelItem = commService.get(ModelItem.class, code);
			MiValue miValue = commService.get(MiValue.class, code);
			if (modelItem !=null) {
				modelItem.setUsingState(usingState);
				openSession.update(modelItem);
			}
			if (miValue!= null) {
				miValue.setUsingState(usingState);
				openSession.update(miValue);
			}
			
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}finally {
			openSession.close();
		}
	}
	
	@Override
	public void createDBTable() {
		
		try{
			commService.excuteBySql("SET FOREIGN_KEY_CHECKS=0;");
			
			//获取数据库名称
			String dataBaseName = commService.getDataBaseName();
			
			//初始化表结构
			intiTable(dataBaseName);
			
			//初始化表字段
			initTableColumn(dataBaseName);
			
			//创建关系表
			initRelationTab(dataBaseName);
			
			//关系表函数
			initRelationFun(dataBaseName);
			
			//创建实体文件表
			initFileTab(dataBaseName);
			
			//创建实体历史表
			initHistoryTab(dataBaseName);
			
			//创建删除表
			initDelTab(dataBaseName);
			
			//创建实体C表
			initTabC(dataBaseName);
			
			//给多行属性添加索引
			initMoreLingTabIndex(dataBaseName);
			
			
			//以上程序执行完比， 应确保只有状态为1  和-1， 下面程序把所有状态为0的改为1
			commService.excuteBySql("UPDATE t_cc_model_item SET using_state=1 WHERE using_state=0");
			commService.excuteBySql("UPDATE t_cc_mi_value SET using_state=1 WHERE using_state=0");
			
		} finally {
			commService.excuteBySql("SET FOREIGN_KEY_CHECKS=1;");
		}
	}
	//给多行属性添加索引
	private void initMoreLingTabIndex(String dataBaseName) {
		//给多值属性添加索引
		List repeatIndex = miTableDBDao.queryCreRepeatTabIndex(dataBaseName);
		for (Object object : repeatIndex) {
			try {
				commService.excuteBySql(object.toString());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
	}

	//创建实体C表
	private void initTabC(String dataBaseName) {
		List tabc1 = miTableDBDao.queryCreEntityTabc1(dataBaseName);
		for (Object object : tabc1) {
			try {
				commService.excuteBySql(object.toString());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
		List tabc2 = miTableDBDao.queryCreEntityTabc2(dataBaseName);
		for (Object object : tabc2) {
			try {
				commService.excuteBySql(object.toString());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
	}

	//创建删除表
	private void initDelTab(String dataBaseName) {
		List tabD1 = miTableDBDao.queryCreEntityTabD1(dataBaseName);
		for (Object object : tabD1) {
			try {
				commService.excuteBySql(object.toString());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
	}

	//创建实体历史表
	private void initHistoryTab(String dataBaseName) {
		
		List tabH1 = miTableDBDao.queryCreEntityTabH1(dataBaseName);
		for (Object object : tabH1) {
			try {
				commService.excuteBySql(object.toString());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
		List tabH2 = miTableDBDao.queryCreEntityTabH2(dataBaseName);
		for (Object object : tabH2) {
			try {
				commService.excuteBySql(object.toString());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	//创建实体文件表
	private void initFileTab(String dataBaseName) {
		List creF1 = miTableDBDao.queryCreEntityFileTbaF1(dataBaseName);
		for (Object object : creF1) {
			try {
				commService.excuteBySql(object.toString());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		List creF2 = miTableDBDao.queryCreEntityFileTbaF2(dataBaseName);
		for (Object object : creF2) {
			try {
				commService.excuteBySql(object.toString());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
		List creF3 = miTableDBDao.queryCreEntityFileTbaF3(dataBaseName);
		for (Object object : creF3) {
			try {
				commService.excuteBySql(object.toString());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	//创建关系表
	private void initRelationTab(String dataBaseName) {
		List creRelaTab = miTableDBDao.queryCreRelaTab(dataBaseName);
		for (Object object : creRelaTab) {
			try {
				commService.excuteBySql(object.toString());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
	}

	//关系表函数
	private void initRelationFun(String dataBaseName) {
		List creRelaTabFun = miTableDBDao.queryCreRelaTabFun(dataBaseName);
		for (Object object : creRelaTabFun) {
			try {
				commService.excuteBySql(object.toString());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
	}

	// 新增表字段
	private void initTableColumn(String dataBaseName) {
		// 新增表字段
		List columnList = miTableDBDao.queryNewAddCol(dataBaseName);
		Iterator iterator = columnList.iterator();
		while (iterator.hasNext()) {
			Object[] cur = (Object[]) iterator.next();
			String code = (String) cur[0];
			String value = (String) cur[1];
			try {
				commService.excuteBySql(value);
				updateMiUsingState(code, Constants.USING_STATE_USING);
			} catch (Exception e) {
				e.printStackTrace();
				updateMiUsingState(code, Constants.USING_STATE_ERROR);
				continue;
			}
		}
		
		//需要更新的表字段
		List queryEditCol = miTableDBDao.queryEditCol(dataBaseName);
		Iterator iteratorEdit = queryEditCol.iterator();
		while (iteratorEdit.hasNext()) {
			Object[] cur = (Object[]) iteratorEdit.next();
			String code = (String) cur[0];
			String value = (String) cur[1];
			try {
				commService.excuteBySql(value);
				updateMiUsingState(code, Constants.USING_STATE_USING);
			} catch (Exception e) {
				e.printStackTrace();
				updateMiUsingState(code, Constants.USING_STATE_ERROR);
				continue;
			}
		}
		
	}

	/**
	 * 初始化表结构
	 */
	private void intiTable(String dataBaseName) {
		
		// 查询需要创建的表
		List tableList = miTableDBDao.queryCreTab(dataBaseName);
		Iterator iterator = tableList.iterator();
		while (iterator.hasNext()) {
			try {
				Object[] cur = (Object[]) iterator.next();
				String code = (String) cur[0];
				String value = (String) cur[1];
				
				commService.excuteBySql(value);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
	}

}
