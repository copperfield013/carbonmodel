package cho.carbon.imodel.model.modelitem.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cho.carbon.imodel.model.cascadedict.pojo.CascadedictBasicItem;
import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.Constants;
import cho.carbon.imodel.model.modelitem.dao.ModelItemDao;
import cho.carbon.imodel.model.modelitem.pojo.MiCalExpress;
import cho.carbon.imodel.model.modelitem.pojo.MiCalculated;
import cho.carbon.imodel.model.modelitem.pojo.MiCascade;
import cho.carbon.imodel.model.modelitem.pojo.MiEnum;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterCriterion;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterGroup;
import cho.carbon.imodel.model.modelitem.pojo.MiModelStat;
import cho.carbon.imodel.model.modelitem.pojo.MiReference;
import cho.carbon.imodel.model.modelitem.pojo.MiStatDimension;
import cho.carbon.imodel.model.modelitem.pojo.MiStatFact;
import cho.carbon.imodel.model.modelitem.pojo.MiTwolevelMapping;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.strategy.MiStrategyContext;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;
import cho.carbon.imodel.model.modelitem.vo.ViewLabel;
import cho.carbon.imodel.model.struct.pojo.StrucFilter;
import cho.carbon.meta.enun.AggregateFunctionType;
import cho.carbon.meta.enun.CalculatedItemType;
import cho.carbon.meta.enun.ItemValueType;
import cho.carbon.meta.enun.ModelItemType;
import cn.sowell.copframe.dto.page.PageInfo;

@Service
public class ModelItemServiceImpl implements ModelItemService {

	@Resource
	CommService commService;

	@Resource
	ModelItemDao modelItemDao;
	@Resource
	ModelItemCodeGeneratorService miCodeGService;
	
	@Resource
	CascadedictBasicItemService casenumItemService;
	
	@Override
	public List<ModelItem> queryList(ModelItem modelItem, PageInfo pageInfo) throws Exception {
		return modelItemDao.queryList(modelItem, pageInfo);
	}

	@Override
	public ModelItemContainer saveOrUpdate(ModelItemContainer modelItemContainer) throws Exception {
		ModelItem modelItem = modelItemContainer.getModelItem();

		String flag = "";
		// 首先生成code
		if (modelItem.getCode() == null || modelItem.getCode().trim().length() < 1) {
			flag = "add";
			ModelItemType itemType = ModelItemType.getItemType(modelItem.getType());
			if (!Constants.isModel(itemType)) {
				ModelItem modelItem2 = commService.get(ModelItem.class, modelItem.getParent());
				modelItem.setBelongModel(modelItem2.getBelongModel());
			}
			String itemCode = miCodeGService.getBasicItemCode(itemType, modelItem.getBelongModel());
			modelItem.setCode(itemCode);
			if (Constants.isModel(itemType)) {
				modelItem.setBelongModel(itemCode);
			}
		}
		//这里要弄一个设计模式， 
		MiStrategyContext miSContext = new MiStrategyContext(commService, casenumItemService, this, miCodeGService);
		miSContext.saveOrUpdateMi(flag, modelItemContainer);

		return modelItemContainer;
	}

	@Override
	public List<ViewLabel> getDefaultAttrByMType(String modelItemCode, ModelItemType modelItemType, String mipCode) throws Exception {
		
		ModelItem modelItem = commService.get(ModelItem.class, modelItemCode);
		
		// 获取到底需要哪些属性
		List<ViewLabel> viewLabelList =  getDAByMType(modelItem, modelItemType, mipCode);
		return viewLabelList;
	}

	/**
	 * 
	 * @param modelItemType
	 * @param mipCode       父亲的code
	 * @return
	 * @throws Exception 
	 */
	private List<ViewLabel> getDAByMType(ModelItem modelItem, ModelItemType modelItemType, String mipCode) throws Exception {
		MiValue miValue = null;
		MiEnum miEnum = null;
		MiReference miReference = null;
		MiStatFact miStatFact = null;
		MiCalculated miCalculated = null;
		if (modelItem == null) {
			modelItem = new ModelItem("", "", modelItemType.getIndex(), "", "", 0, "");
			miValue = new MiValue("", ItemValueType.STRING.getIndex() + "", "32", "", 0);
			miEnum = new MiEnum("", null);
			miReference = new MiReference("", "", "", "", "0");
			miStatFact = new MiStatFact("", null, null, null, 0);
			miCalculated = new MiCalculated();
		} else {
			miValue = commService.get(MiValue.class, modelItem.getCode());
			miEnum = commService.get(MiEnum.class, modelItem.getCode());
			miReference = commService.get(MiReference.class, modelItem.getCode());
			miStatFact = commService.get(MiStatFact.class, modelItem.getCode());
			miCalculated = commService.get(MiCalculated.class, modelItem.getCode());
		}
		List<ViewLabel> viewLabelList = new ArrayList();
		// 这里设置通用属性
		viewLabelList.add(new ViewLabel("textarea", "text", "modelItem.description", modelItem.getDescription(), "描述"));
		viewLabelList.add(new ViewLabel("input", "hidden", "modelItem.code", modelItem.getCode(), null));
		
		ViewLabel viewLabel = new ViewLabel("input", "text", "modelItem.name", modelItem.getName(), "名称", 20);
		viewLabel.setViewClazz("required");
		viewLabelList.add(viewLabel);
		viewLabelList.add(new ViewLabel("input", "hidden", "modelItem.type", "" + modelItem.getType(), null));
		viewLabelList.add(new ViewLabel("input", "hidden", "modelItem.parent", mipCode, null));
		viewLabelList.add(new ViewLabel("input", "hidden", "modelItem.belongModel", modelItem.getBelongModel(), null));
		viewLabelList.add(new ViewLabel("input", "hidden", "modelItem.usingState", modelItem.getUsingState() + "", null));

		switch (modelItemType) {
		case MODEL:
		case ONE_LINE_GROUP:
		case FACT_GROUP:
		case DIMENSION_GROUP:
		case FILE_ITEM:
			break;
		case GIANT_LINE_GROUP:
		case MULTI_LINE_GROUP:
			break;
		case FACT_ITEM:
			getViewLabelToMiValue(miValue, viewLabelList, modelItemType);
			
			//MiStatFact 事实属性
			ViewLabel updrillFuncType = new ViewLabel("select", "text", "miStatFact.updrillFuncType",miStatFact==null?"": miStatFact.getUpdrillFuncType()+"", "上钻类型", 0);
			
			// 获取值域
			Map<String, String> valueMap2 = new HashMap<String, String>();
			AggregateFunctionType[] aggrs = AggregateFunctionType.values();
			for (AggregateFunctionType aggr : aggrs) {
				valueMap2.put(aggr.getIndex() + "", aggr.getName());
			}
			updrillFuncType.setValueDomain(valueMap2);
			viewLabelList.add(updrillFuncType);
			
			break;
		case VALUE_ITEM:
		case DIMENSION_ITEM:
		case CASCADE_REFERENCE_ITEM:
			getViewLabelToMiValue(miValue, viewLabelList, modelItemType);
			break;
		case CALCULATED_ITEM:
			getViewLabelToMiValue(miValue, viewLabelList, modelItemType);
			// miCalculated
			ViewLabel viewLabel2 = new ViewLabel("select", "text", "miCalculated.type", "" +miCalculated.getType(), "计算类型", 10);
			
			Map<String, String> valueDomain10 = new HashMap<String, String>();
			valueDomain10.put(CalculatedItemType.PRESERVE.getIndex()+"", CalculatedItemType.PRESERVE.getName());
			valueDomain10.put(CalculatedItemType.FETCH.getIndex()+"", CalculatedItemType.FETCH.getName());
			viewLabel2.setValueDomain(valueDomain10);
			
			viewLabelList.add(viewLabel2);
			
			break;
		case ENUM_ITEM:
		case DIMENSION_ENUM_ITEM:
		case PREENUM_STRING_ITEM:
			//miValue
			getViewLabelToMiValue(miValue, viewLabelList, modelItemType);
			//miEnum
			getViewLabelToMiEnum(miEnum, viewLabelList);
			break;
		case CASCADE_ENUM_ITEM:
			//miValue
			getViewLabelToMiValue(miValue, viewLabelList, modelItemType);
			//miEnum
			ViewLabel viewEnum = new ViewLabel("select", "text", "miEnum.pid", miEnum.getPid() == null?"":miEnum.getPid()+"", "枚举字典");	
			//获取值域
			Map<String, String> valueDomain = new HashMap<String, String>();
			List<CascadedictBasicItem> casenumItemList = casenumItemService.getCascaseDictPitem();
			for (CascadedictBasicItem casenumItem : casenumItemList) {
				valueDomain.put(casenumItem.getId()+"", casenumItem.getName());
			}
			viewEnum.setValueDomain(valueDomain);
			viewLabelList.add(viewEnum);
			
			viewLabelList.add(new ViewLabel("input", "hidden", "miEnum.code", miEnum.getCode(), null));
			
			//级联枚举孩子数量
			List<MiCascade> miCascadeList = null;
			if (modelItem.getCode() != null) {
				miCascadeList = this.getMiCascadeList(modelItem.getCode());
			}
			viewLabelList.add(new ViewLabel("input", "number", "modelItem.casEnumChildCount", miCascadeList==null?"0":miCascadeList.size() +"", "级联级数"));
			
			
			break;
		case MULTI_ENUM_ITEM:
			//miEnum
			getViewLabelToMiEnum(miEnum, viewLabelList);
			break;
		case REFERENCE_ITEM:
			//miValue
			getViewLabelToMiValue(miValue, viewLabelList, modelItemType);
			//miReference
			getViewLabelToMiReference(miReference, viewLabelList);
			
			break;
		case ENCRYPTION_ITEM:

			break;
		case PASSWORD_ITEM:

			break;
		case TWO_LEVEL_ITEM:

			break;
		}
		
		Collections.sort(viewLabelList);
		
		return viewLabelList;
	}

	private void getViewLabelToMiEnum(MiEnum miEnum, List<ViewLabel> viewLabelList) throws Exception {
			ViewLabel viewEnum = new ViewLabel("select", "text", "miEnum.pid", miEnum.getPid()==null?"":miEnum.getPid()+"", "枚举字典");	
			//获取值域
			Map<String, String> valueDomain = new HashMap<String, String>();
			List<CascadedictBasicItem> casenumItemList = casenumItemService.getParentAll();
			for (CascadedictBasicItem casenumItem : casenumItemList) {
				valueDomain.put(casenumItem.getId()+"", casenumItem.getName());
			}
			viewEnum.setValueDomain(valueDomain);
			viewLabelList.add(viewEnum);
			
			viewLabelList.add(new ViewLabel("input", "hidden", "miEnum.code", miEnum.getCode(), null));
	}

	/*
	 * 获取MiValue 需要在页面显示的值
	 */
	private void getViewLabelToMiValue(MiValue miValue, List<ViewLabel> viewLabelList, ModelItemType modelItemType) {
		viewLabelList.add(new ViewLabel("input", "hidden", "miValue.code", miValue.getCode(), null));
		if (!(ModelItemType.CASCADE_ENUM_ITEM.equals(modelItemType) || ModelItemType.PREENUM_STRING_ITEM.equals(modelItemType) || ModelItemType.ENUM_ITEM.equals(modelItemType) || ModelItemType.REFERENCE_ITEM.equals(modelItemType))) {
			ViewLabel dataType = new ViewLabel("select", "text", "miValue.dataType", miValue.getDataType(), "数据类型", 19);
			dataType.setViewClazz("miValueDataType");
			
			// 获取值域
			Map<String, String> valueDomain = new HashMap<String, String>();
			ItemValueType[] itemValueTypes = ItemValueType.values();
			for (ItemValueType itemValueType : itemValueTypes) {
				valueDomain.put(itemValueType.getIndex() + "", itemValueType.getName());
			}
			dataType.setValueDomain(valueDomain);
			viewLabelList.add(dataType);
		}
		
		if (!(ModelItemType.ENUM_ITEM.equals(modelItemType) || ModelItemType.CASCADE_ENUM_ITEM.equals(modelItemType) || ModelItemType.REFERENCE_ITEM.equals(modelItemType) || ModelItemType.CASCADE_REFERENCE_ITEM.equals(modelItemType))) {
			ViewLabel dataLength = new ViewLabel("input", "text", "miValue.dataLength", miValue.getDataLength(), "数据长度");
			dataLength.setViewClazz("miValueDataLength");
			viewLabelList.add(dataLength);
		}
		viewLabelList.add(new ViewLabel("input", "hidden", "miValue.usingState", miValue.getUsingState() + "", null));
		viewLabelList.add(new ViewLabel("input", "hidden", "miValue.belongTable", miValue.getBelongTable() + "", null));
	}
	
	/*
	 * 获取MiValue 需要在页面显示的值
	 */
	private void getViewLabelToMiReference(MiReference miReference, List<ViewLabel> viewLabelList) throws Exception {
		viewLabelList.add(new ViewLabel("input", "hidden", "miReference.code", miReference.getCode(), null));
		
		//引用的实体模型
		ViewLabel modelItemRef = new ViewLabel("select", "text", "miReference.modelCode", miReference.getModelCode(), "引用实体");
		modelItemRef.setViewClazz("miReferenceModelCode");
		// 获取值域
		Map<String, String> valueDomain = new LinkedHashMap<String, String>();
		//获取所有实体模型
		ModelItemType[] existMiTypes = {ModelItemType.MODEL};
		List<ModelItem> modelItemList = this.getModelItemByType(null, existMiTypes, null, null);
	
		for (ModelItem modelItem : modelItemList) {
			valueDomain.put(modelItem.getCode(), modelItem.getName());
		}
		modelItemRef.setValueDomain(valueDomain);
		
		// 获取值域
		Map<String, String> valueDomain3= new LinkedHashMap<String, String>();
		//获取所有实体模型
		ModelItemType[] types2 = {ModelItemType.ONE_LINE_GROUP};
		//获取所有模型下的属性， 单行下的孩子
		 List<ModelItem> modelItemStairChild2 = this.getModelItemByBelongMode(modelItemList.get(0).getBelongModel(), types2,null, false);
		
		for (ModelItem modelItem : modelItemStairChild2) {
			valueDomain3.put(modelItem.getCode(), modelItem.getName());
		}
		// 引用的识别属性
		ViewLabel recognitionCode = new ViewLabel("select", "text", "miReference.recognitionCode", miReference.getRecognitionCode(), "识别属性");
		recognitionCode.setViewClazz("miReferenceRecognitionCode");
		recognitionCode.setValueDomain(valueDomain3);
		
		//引用的展示属性
		ViewLabel showCode = new ViewLabel("select", "text", "miReference.showCode", miReference.getShowCode(), "展示属性");
		showCode.setViewClazz("miReferenceShowCode");
		showCode.setValueDomain(valueDomain3);
		
		viewLabelList.add(new ViewLabel("input", "radio", "miReference.addNewRef", miReference.getAddNewRef(), "创建吗"));
		
		viewLabelList.add(showCode);
		viewLabelList.add(recognitionCode);
		viewLabelList.add(modelItemRef);
	}

	@Override
	public List<ModelItem> getModelItemStairChild(String pmiCode) {
		return modelItemDao.getModelItemStairChild(pmiCode);
	}

	@Override
	public List<ModelItem> getModelItemByType(String parentCode, ModelItemType[] existMiTypes, ModelItemType[] noNiTypes, Integer miUsingState) throws Exception {
		return modelItemDao.getModelItemByType(parentCode, existMiTypes, noNiTypes, miUsingState);
	}

	@Override
	public List<MiCascade> getMiCascadeList(String miCasEnumCode) {
		return modelItemDao.getMiCascadeList(miCasEnumCode);
	}

	@Override
	public List<ModelItem> getModelItemByBelongMode(String belongMode, ModelItemType[] pTypes,ModelItemType[] chilTypes, boolean needCorrelation) {
		return modelItemDao.getModelItemByBelongMode(belongMode, pTypes, chilTypes, needCorrelation);
	}

	@Override
	public List<MiTwolevelMapping> getMiTwoMapping(String moreLineCode) {
		return modelItemDao.getMiTwoMapping(moreLineCode);
	}

	@Override
	public List<CascadedictBasicItem> getCasEnumChild(String enumCode) throws Exception {
		MiEnum miEnum = commService.get(MiEnum.class, enumCode);
		return casenumItemService.getChildByParentId(miEnum.getPid());
	}

	@Override
	public List getTwoAttrByMappingId(Integer mappingId) {
		return modelItemDao.getTwoAttrByMappingId(mappingId);
	}

	@Override
	public String deleteModelItem(String miCode) {
		MiStrategyContext miStrategyContext = new MiStrategyContext(commService, casenumItemService, this, miCodeGService);
		miStrategyContext.delModelItem(miCode);
		return null;
	}

	@Override
	public List<ModelItem> getModelList() {
		
		return modelItemDao.getModelList();
	}
	
}
