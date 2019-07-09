package cho.carbon.imodel.model.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cho.carbon.imodel.model.admin.dao.AdminUserDao;
import cho.carbon.imodel.model.admin.service.AdminUserService;


@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService{

	@Resource
	AdminUserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		return userDao.getUser(username);
	}

}
