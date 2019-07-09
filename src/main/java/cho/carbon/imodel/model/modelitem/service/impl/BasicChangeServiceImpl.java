package cho.carbon.imodel.model.modelitem.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cho.carbon.imodel.model.modelitem.dao.BasicChangeDao;
import cho.carbon.imodel.model.modelitem.pojo.BasicChange;
import cho.carbon.imodel.model.modelitem.service.BasicChangeService;

@Service
public class BasicChangeServiceImpl implements BasicChangeService {

	@Resource
	BasicChangeDao basicChangeDao;

	@Override
	public List queryList() {
		return basicChangeDao.queryList();
	}

	@Override
	public void create(BasicChange basicChange) {
		BasicChange basicChange2 = basicChangeDao.get(BasicChange.class, basicChange.getCode());
		if (basicChange2 == null) {
			basicChangeDao.insert(basicChange);
		}
	}

	@Override
	public void update(BasicChange basicChange) {
		basicChangeDao.update(basicChange);
	}

	@Override
	public void delete(String code) {
		BasicChange basicChange2 = basicChangeDao.get(BasicChange.class, code);
		if (basicChange2 != null) {
			basicChangeDao.delete(basicChange2);
		}
	}

	@Override
	public BasicChange getOne(String code) {
		return basicChangeDao.get(BasicChange.class, code);
	}
	


}
