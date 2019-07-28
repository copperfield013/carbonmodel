package cho.carbon.imodel.model.modelitem.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.Constants;
import cho.carbon.imodel.model.modelitem.dao.ModelRelationTypeDao;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.pojo.ModelRelationType;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelRelationTypeService;
import cho.carbon.meta.enun.RelationType;
import cn.sowell.copframe.dto.page.PageInfo;

@Service
public class ModelRelationTypeServiceImpl implements ModelRelationTypeService {

	@Resource
	ModelRelationTypeDao modelRelationTypeDao;
	
	@Resource
	ModelItemCodeGeneratorService miCodeGenService;
	
	@Resource
	CommService commService;
	
//	@Resource
//	BasicChangeService basicChangeService;
	
	@Override
	public List<ModelRelationType> queryList(ModelRelationType criteria, PageInfo pageInfo) {
		return modelRelationTypeDao.queryList(criteria, pageInfo);
	}

	@Override
	public void create(ModelRelationType dictParentItem) {
		commService.insert(dictParentItem);
	}

	@Override
	public ModelRelationType getRecordRelationType(String id) {
		return commService.get(ModelRelationType.class, id);
	}

	@Override
	public void update(ModelRelationType modelRelationType) {
		commService.update(modelRelationType);
		
	}

	@Override
	public void delete(String typeCode) {
		ModelRelationType criteria = commService.get(ModelRelationType.class, typeCode);
		commService.delete(criteria);
		if (!criteria.getTypeCode().equals(criteria.getReverseCode())) {
			ModelRelationType reverseObj = commService.get(ModelRelationType.class, criteria.getReverseCode());
			commService.delete(reverseObj);
		}
	}

	@Override
	public List<ModelRelationType> getEntityRelaByBitemId(String recordType) {
		return modelRelationTypeDao.getEntityRelaByBitemId(recordType);
	}

	@Override
	public void saveRelation(ModelRelationType lefRrecordType, ModelRelationType rightRrecordType, String symmetry) throws Exception {
		
			if ("symmetry".equals(symmetry)) {//添加对称关系
				
				
				String recordRelaCode = miCodeGenService.getRelaCode(lefRrecordType.getLeftModelCode());
				
				lefRrecordType.setTypeCode(recordRelaCode);
				lefRrecordType.setReverseCode(recordRelaCode);
				lefRrecordType.setUsingState(Constants.USING_STATE_USING);
				commService.insert(lefRrecordType);
			} else {//添加不对称关系
				String LeftRecordRelaCode = "";
				String rightRecordRelaCode = "";
				
				LeftRecordRelaCode = miCodeGenService.getRelaCode(lefRrecordType.getLeftModelCode());
				rightRecordRelaCode = miCodeGenService.getRelaCode(rightRrecordType.getLeftModelCode());
				
				lefRrecordType.setTypeCode(LeftRecordRelaCode);
				lefRrecordType.setReverseCode(rightRecordRelaCode);
				rightRrecordType.setTypeCode(rightRecordRelaCode);
				rightRrecordType.setReverseCode(LeftRecordRelaCode);
				
				lefRrecordType.setUsingState(Constants.USING_STATE_MAP.get("using"));
				rightRrecordType.setUsingState(Constants.USING_STATE_MAP.get("using"));
				
				commService.insert(lefRrecordType);
				commService.insert(rightRrecordType);
			}
			
		
	}

	@Override
	public List<ModelRelationType> getEntityRelaByBitemId(String leftRecordType, String rightRecordType) {
		return modelRelationTypeDao.getEntityRelaByBitemId(leftRecordType, rightRecordType);
	}

	@Override
	public void saveStatus(String typeCode, Integer usingState) throws Exception {
		ModelRelationType leftRlea = commService.get(ModelRelationType.class, typeCode);
		ModelRelationType rightRlea = commService.get(ModelRelationType.class, leftRlea.getReverseCode());
		if (!Constants.USING_STATE_PASTDUE.equals(usingState)) {
			//过期实体
			leftRlea.setUsingState(Constants.USING_STATE_PASTDUE);
			rightRlea.setUsingState(Constants.USING_STATE_PASTDUE);
		} else {
			//解除过期
			leftRlea.setUsingState(Constants.USING_STATE_USING);
			rightRlea.setUsingState(Constants.USING_STATE_USING);
		}
		
		if (leftRlea.equals(rightRlea)) {
			commService.update(leftRlea);
		} else {
			commService.update(leftRlea);
			commService.update(rightRlea);
		}
	}

	@Override
	public List<ModelRelationType> getRelaByType(String leftRecordType, RelationType relationType) {
		return modelRelationTypeDao.getRelaByType(leftRecordType, relationType);
	}

	@Override
	public List<ModelItem> getExistRelaRightMi(String leftModelCode) {
		return modelRelationTypeDao.getExistRelaRightMi(leftModelCode);
	}

}
