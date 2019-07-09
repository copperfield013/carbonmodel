package cho.carbon.imodel.model.admin.dao;

import cho.carbon.imodel.model.system.pojo.SystemAdmin;

public interface SystemAdminDao {

	
	SystemAdmin getSystemAdminByUserId(long userId);
}
