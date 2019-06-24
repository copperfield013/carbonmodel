package cho.carbon.imodel.model.modelitem.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.abc.model.enun.ItemValueType;
import com.abc.model.enun.ModelItemType;

import cho.carbon.imodel.model.modelitem.dao.ModelItemCodeGeneratorDao;
import cho.carbon.imodel.model.modelitem.pojo.ModelItemCodeGenerator;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;

@Service
public class ModelItemCodeGeneratorServiceImpl implements ModelItemCodeGeneratorService {
	@Resource
	ModelItemCodeGeneratorDao  btCodeGenDao;
	
	private static Map<String, String> map = new HashMap<String, String>();

	@Override
	public String getRelaCode(String belongModel) throws Exception {
		ModelItemCodeGenerator btNg = new ModelItemCodeGenerator();
		btCodeGenDao.insert(btNg);
		return btNg.getRelaCode(belongModel).toUpperCase();
	}

	@Override
	public String getBasicItemFix(ModelItemType dataType, String belongModel) throws Exception {
		//获取前缀信息
		String basicItemFix = null;
		if (ModelItemType.MODEL.equals(dataType) && (belongModel == null || belongModel == "")) {
			 basicItemFix = btCodeGenDao.getBasicItemFixByDB();
		} else {
			
			if(belongModel == null) {
				throw new Exception("模型编码不能为空");
			}
			
			basicItemFix = map.get(belongModel);
			if (basicItemFix == null || basicItemFix == "") {
				int indexOf = belongModel.lastIndexOf(ModelItemCodeGenerator.ENTITYINFIX);
				basicItemFix = belongModel.substring(0, indexOf);
				map.put(belongModel, basicItemFix);
			}
		} 
		return basicItemFix.toUpperCase();
	}

	@Override
	public String getBasicItemCode(ModelItemType itemType, String belongModel) throws Exception {
		String basicItemCode = null;
		ModelItemCodeGenerator btNg = new ModelItemCodeGenerator();
		btCodeGenDao.insert(btNg);

		String basicItemFix = getBasicItemFix(itemType, belongModel);
		
		switch (itemType) {
		case MODEL:
			basicItemCode =btNg.getEntityCode(basicItemFix);
			map.put(basicItemCode, basicItemFix);
			break;
		default:
			basicItemCode = btNg.getAttrCode(basicItemFix);
			break;
		}
		return basicItemCode.toUpperCase();
	}
	
}
