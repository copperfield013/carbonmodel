package cho.carbon.imodel.model.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cho.carbon.imodel.model.admin.dao.SystemAdminDao;
import cho.carbon.imodel.model.admin.service.SystemAdminService;
import cho.carbon.imodel.model.system.pojo.SystemAdmin;

@Service
public class SystemAdminServiceImpl implements SystemAdminService{

	@Resource
	SystemAdminDao aDao;
	
	@Override
	public SystemAdmin getSystemAdminByUserId(long userId) {
		return aDao.getSystemAdminByUserId(userId);
	}
	
}	
