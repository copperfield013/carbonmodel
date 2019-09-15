package cho.carbon.imodel.model.struct.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cho.carbon.imodel.model.cascadedict.pojo.CascadedictSubsection;
import cho.carbon.imodel.model.cascadedict.service.CascadedictSubsectionService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.Constants;
import cho.carbon.imodel.model.modelitem.pojo.MiEnum;
import cho.carbon.imodel.model.modelitem.pojo.MiReference;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.pojo.ModelRelationType;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.service.ModelRelationTypeService;
import cho.carbon.imodel.model.modelitem.vo.ViewLabel;
import cho.carbon.imodel.model.struct.dao.StrucBaseDao;
import cho.carbon.imodel.model.struct.pojo.StrucBase;
import cho.carbon.imodel.model.struct.pojo.StrucFieldSubenum;
import cho.carbon.imodel.model.struct.pojo.StrucFieldValue;
import cho.carbon.imodel.model.struct.pojo.StrucMiCode;
import cho.carbon.imodel.model.struct.pojo.StrucPointer;
import cho.carbon.imodel.model.struct.pojo.StrucRRef;
import cho.carbon.imodel.model.struct.pojo.StrucRelation;
import cho.carbon.imodel.model.struct.service.StrucBaseService;
import cho.carbon.imodel.model.struct.strategy.StructStrategyContext;
import cho.carbon.imodel.model.struct.vo.StrucBaseContainer;
import cho.carbon.meta.enun.AttributeValueType;
import cho.carbon.meta.enun.ItemValueType;
import cho.carbon.meta.enun.ModelItemType;
import cho.carbon.meta.enun.RelationType;
import cho.carbon.meta.enun.StrucElementType;
import cho.carbon.meta.enun.StrucOptType;
import cho.carbon.meta.mapping.ValueTypeMapping;
import cn.sowell.copframe.dto.page.PageInfo;

@Service
public class StrucBaseServiceImpl implements StrucBaseService {

	@Resource
	StrucBaseDao strucBaseDao;
	
	@Resource
	CommService commService;
	
	@Resource
	ModelItemService modelItemService;
	@Resource
	CascadedictSubsectionService casSubsectionService;
	@Resource
	ModelRelationTypeService modelRelaService;
	
	@Override
	public List<StrucBase> queryList(StrucBase strucBase, PageInfo pageInfo) {
		return strucBaseDao.queryList(strucBase, pageInfo);
	}
	
	@Override
	public List<StrucBase> getAllStruc(String modelCode) {
		return strucBaseDao.getAllStruc(modelCode);
	}

	@Override
	public StrucBaseContainer saveOrUpdate(StrucBaseContainer strucBaseContainer) {
		//这里需要策略模式
		StructStrategyContext context = new StructStrategyContext(commService, this);
		context.saveOrUpdate(strucBaseContainer);
		return strucBaseContainer;
	}

	@Override
	public List<ViewLabel> getDefaultAttrByMType(Integer sbId, StrucElementType strucElementType, Integer sbPid) throws Exception {
		StrucBase strucBase = null;
		if (sbId != null) {
			strucBase = commService.get(StrucBase.class, sbId);
		}
		
		// 获取到底需要哪些属性
		List<ViewLabel> viewLabelList =  getDAByMType(strucBase, strucElementType, sbPid);
		
		return viewLabelList;
	}

	/**
	 * 
	 * @param strucBase
	 * @param strucElementType
	 * @param sbPid
	 * @return
	 * @throws Exception 
	 */
	private List<ViewLabel> getDAByMType(StrucBase strucBase, StrucElementType strucElementType, Integer sbPid) throws Exception {
		//获取所有结构体
		List<StrucBase> allStruc = this.getAllStruc(null);	
		
		
		StrucBase pStrucBase = commService.get(StrucBase.class, sbPid);
			StrucElementType pType = StrucElementType.getType(pStrucBase.getType());
			
			StrucMiCode strucMiCode = null;
			StrucFieldSubenum strucFieldSubenum = null;
			StrucFieldValue strucFieldValue = null;
			StrucPointer strucPointer = null;
			StrucRRef strucRRef = null;
			
			if (strucBase == null) {
				strucBase = new StrucBase(null, strucElementType.getCode(), "", StrucOptType.WRITE.getIndex(), 1, sbPid);
				strucMiCode = new StrucMiCode(null, "");
				strucFieldSubenum = new StrucFieldSubenum(null, null);
				strucFieldValue = new StrucFieldValue(null, null);
				strucPointer = new StrucPointer();
				strucRRef = new StrucRRef();
			} else {
				strucMiCode = commService.get(StrucMiCode.class, strucBase.getId());
				strucFieldSubenum = commService.get(StrucFieldSubenum.class, strucBase.getId());
				strucFieldValue = commService.get(StrucFieldValue.class, strucBase.getId());
				strucPointer = commService.get(StrucPointer.class, strucBase.getId());
				strucRRef = commService.get(StrucRRef.class, strucBase.getId());
			}
			
			List<ViewLabel> viewLabelList = new ArrayList();
			
			//获取通用必填字段
			getVbToStrucBase(strucBase, viewLabelList);
			  
			switch (strucElementType) {
				case GROUP1D:
					break;
				case GROUP2D:
					//配置文件对应的实体模型
					StrucMiCode pStrucMiCode = commService.get(StrucMiCode.class, sbPid);
					
					// 二维表可以选择多行属性
					ModelItemType[] existMiTypes = {ModelItemType.MULTI_LINE_GROUP};
					List<ModelItem> modelItemByType = modelItemService.getModelItemByType(pStrucMiCode.getItemCode(), existMiTypes, null, Constants.USING_STATE_USING);
				 	
					getViewLabelToStrucMiCode(viewLabelList, strucMiCode, modelItemByType, strucElementType);
					break;
				case FIELD:
					List<ModelItem> modelItemList = null;
					//配置文件对应的实体模型
					if (StrucElementType.GROUP1D.equals(pType)) {
						// 父亲是字段组， 则获取所有的普通属性
						// 输入实体code， 获取本实体下所有的普通属性
						StrucMiCode p1 = commService.get(StrucMiCode.class, pStrucBase.getParentId());
						ModelItem modelItem = commService.get(ModelItem.class, p1.getItemCode());
						//获取实体下， 所有普通分组下的孩子
						ModelItemType[] pTypes = {ModelItemType.ONE_LINE_GROUP, ModelItemType.DIMENSION_GROUP, ModelItemType.FACT_GROUP};
						ModelItemType[] chilTypes = {ModelItemType.VALUE_ITEM,
								ModelItemType.FILE_ITEM,
								ModelItemType.ENCRYPTION_ITEM,
								ModelItemType.CALCULATED_ITEM,
								ModelItemType.PASSWORD_ITEM,
								ModelItemType.DIMENSION_ITEM,
								ModelItemType.FACT_ITEM,
								ModelItemType.TWO_LEVEL_ITEM};
						modelItemList = modelItemService.getModelItemByBelongMode(modelItem.getBelongModel(), pTypes, chilTypes,  false);
						
					} else if (StrucElementType.GROUP2D.equals(pType)) {
						//父亲是二维表， 则获取二维表对应的孩子
						StrucMiCode p2 = commService.get(StrucMiCode.class, sbPid);
						modelItemList = modelItemService.getModelItemByType(p2.getItemCode(), null, null, Constants.USING_STATE_USING);
					}
					
				getViewLabelToStrucMiCode(viewLabelList, strucMiCode, modelItemList, strucElementType);
				getViewLabelToStrucFieldValue(viewLabelList, strucMiCode, strucFieldValue, strucElementType);
				break;
				case ENUMFIELD:
					
					List<ModelItem> modelItemList2 = null;
					//配置文件对应的实体模型
					if (StrucElementType.GROUP1D.equals(pType)) {
						// 父亲是字段组， 则获取所有的普通属性
						// 输入实体code， 获取本实体下所有的普通属性
						StrucMiCode p1 = commService.get(StrucMiCode.class, pStrucBase.getParentId());
						ModelItem modelItem = commService.get(ModelItem.class, p1.getItemCode());
						//获取实体下， 所有普通分组下的孩子
						ModelItemType[] pTypes = {ModelItemType.ONE_LINE_GROUP, ModelItemType.DIMENSION_GROUP};
						ModelItemType[] chilTypes = {ModelItemType.ENUM_ITEM,
								ModelItemType.PREENUM_STRING_ITEM,
								ModelItemType.MULTI_ENUM_ITEM,
								ModelItemType.CASCADE_ENUM_ITEM,
								ModelItemType.DIMENSION_ENUM_ITEM};
						
						modelItemList2 = modelItemService.getModelItemByBelongMode(modelItem.getBelongModel(), pTypes,chilTypes, false);
						
					} else if (StrucElementType.GROUP2D.equals(pType)) {
						//父亲是二维表， 则获取二维表对应的孩子
						StrucMiCode p2 = commService.get(StrucMiCode.class, sbPid);
						ModelItemType[] types2 = {ModelItemType.ENUM_ITEM, ModelItemType.PREENUM_STRING_ITEM, ModelItemType.MULTI_ENUM_ITEM, ModelItemType.CASCADE_ENUM_ITEM};
						modelItemList2 = modelItemService.getModelItemByType(p2.getItemCode(), types2, null, Constants.USING_STATE_USING);
					}
					String enumCode = "";
					
					if (!modelItemList2.isEmpty()) {
						ModelItem modelItem = modelItemList2.get(0);
						enumCode = modelItem.getCode();
					}
					
					if (strucMiCode !=null && strucMiCode.getItemCode() !="") {
						enumCode = strucMiCode.getItemCode();
					}
					
				// 这个对应的枚举子集
				
				// 获取子集下拉框
				MiEnum miEnum = commService.get(MiEnum.class, enumCode);
				// 获取他的子集 值域
				List<CascadedictSubsection> subSelectByParentId = casSubsectionService.getSubSelectByParentId(miEnum == null? null:miEnum.getPid());
				//枚举对应的子集
				viewLabelList.add(new ViewLabel("input", "hidden", "strucFieldSubenum.sbId", strucFieldSubenum.getSbId()==null?"":strucFieldSubenum.getSbId()+"", null));
				ViewLabel subViewLabel = new ViewLabel("select", "text", "strucFieldSubenum.subenumId",strucFieldSubenum.getSubenumId() == null?"":strucFieldSubenum.getSubenumId()+"" , "字典子集");
				// 获取值域
				Map<String, String> valueDomain = new HashMap<String, String>();
				  for (CascadedictSubsection casCade : subSelectByParentId) {
					  valueDomain.put(casCade.getId() + "", casCade.getName()); 
				  }
				  
				 subViewLabel.setValueDomain(valueDomain);
				 subViewLabel.setViewClazz("subsetStrucFileldEnum");
				viewLabelList.add(subViewLabel);
				getViewLabelToStrucMiCode(viewLabelList, strucMiCode, modelItemList2, strucElementType);
				getViewLabelToStrucFieldValue(viewLabelList, strucMiCode, strucFieldValue, strucElementType);
				break;
				case RFIELD:
					// 关系属性， 
					
					// 这个是结构体的id
					StrucMiCode strucModel= commService.get(StrucMiCode.class, pStrucBase.getParentId());
					List<StrucRelation> strucRelation = this.getStrucRelationBySbId(strucBase.getId());
					////////
					//这里要选择对一的关系
					List<ModelRelationType> relationOne = modelRelaService.getRelaByType(strucModel.getItemCode(), RelationType.ONE);
					
					///////////////
					// 关系对应右实体的普通孩子
					ModelItemType[] pTypes = {ModelItemType.ONE_LINE_GROUP};
					List<ModelItem> modelItemList3 = modelItemService.getModelItemByBelongMode(relationOne.isEmpty()?null:relationOne.get(0).getRightModelCode(), pTypes , null, false);
					
					getViewLabelToStrucFieldValue(viewLabelList, strucMiCode, strucFieldValue, strucElementType);
					getViewLabelToStrucMiCode(viewLabelList, strucMiCode, modelItemList3, strucElementType);
					getVbRfieldStrucRelation(viewLabelList, strucRelation.isEmpty()? null:strucRelation.get(0), relationOne);
					
					getViewLabelToStrucPointer(viewLabelList, strucPointer, allStruc);
					break;
				case RREFFIELD:
					
					List<ModelItem> modelItemList5 = getMiListByP(sbPid, pStrucBase, pType);
					
					// 这里获取引用属性， 引用的另一个实体下面的普通属性
					String modelCode = "";//我就是引用属性
					if (!modelItemList5.isEmpty()) {
						ModelItem modelItem = modelItemList5.get(0);
						modelCode = modelItem.getCode();
					}
					
					if (strucMiCode != null && strucMiCode.getItemCode()!="") {
						modelCode = strucMiCode.getItemCode();
					}
						
				// 根据引用属性， 获取它引用的实体， 进而获取此实体下的普通属性
					MiReference miReference = commService.get(MiReference.class, modelCode);
					ModelItemType[] pTypes1 = {ModelItemType.ONE_LINE_GROUP};
					//属性列表
					List<ModelItem> modelItemByBelongMode = modelItemService.getModelItemByBelongMode(miReference == null? null:miReference.getModelCode(), pTypes1 , null, false);
					
					// 
					ViewLabel itemCodeVb = new ViewLabel("select", "text", "strucRRef.refItemCode",strucRRef.getRefItemCode(), "关联属性", 13);
					// 获取值域
					Map<String, String> valueDomain4 = new HashMap<String, String>();
					  for (ModelItem modelItem : modelItemByBelongMode) {
						  valueDomain4.put(modelItem.getCode() + "", modelItem.getName()); 
					  }
					  itemCodeVb.setValueDomain(valueDomain4);
					  itemCodeVb.setViewClazz("strucRRef");
					  viewLabelList.add(itemCodeVb);
				case REFFIELD:
					
					List<ModelItem> modelItemList4 = getMiListByP(sbPid, pStrucBase, pType);
				
					getViewLabelToStrucPointer(viewLabelList, strucPointer, allStruc);
						
					getViewLabelToStrucMiCode(viewLabelList, strucMiCode, modelItemList4, strucElementType);
					getViewLabelToStrucFieldValue(viewLabelList, strucMiCode, strucFieldValue, strucElementType);
					break;
					
				case RSTRUC:
					// 存在关系的右实体
					StrucMiCode  p3 = commService.get(StrucMiCode.class, sbPid);
					String leftModelCode = p3.getItemCode();
					List<ModelItem> exRelaRightMi = modelRelaService.getExistRelaRightMi(leftModelCode);
						
					getViewLabelToStrucMiCode(viewLabelList, strucMiCode, exRelaRightMi, strucElementType);
					// 多选 和右实体存在的关系
					ModelItem rightModel= exRelaRightMi.isEmpty()?null:exRelaRightMi.get(0);
					String rightModelCode = null;
					if (rightModel != null) {
						rightModelCode = rightModel.getCode();
					}
					
					if (strucMiCode != null && strucMiCode.getItemCode()!="") {
						rightModelCode = strucMiCode.getItemCode();
					}
						
					// 左右实体共同的code
					List<ModelRelationType>  modelRelaList= modelRelaService.getEntityRelaByBitemId(leftModelCode, rightModelCode);
					
					
					
					List<StrucRelation> strucRelation1 = this.getStrucRelationBySbId(strucBase.getId());
					
					getVbMultRfieldStrucRelation(viewLabelList, strucRelation1, modelRelaList);
					
					//重新给指向结构体赋值
					
					allStruc =this.getAllStruc(rightModelCode); 
					
					// 指向结构体
					getViewLabelToStrucPointer(viewLabelList, strucPointer, allStruc);
					
					break;
			}
			
			Collections.sort(viewLabelList);
			return viewLabelList;
	}

	/**
	 * 获取父亲下面的孩子
	 * @param sbPid
	 * @param pStrucBase
	 * @param pType
	 * @return
	 * @throws Exception
	 */
	private List<ModelItem> getMiListByP(Integer sbPid, StrucBase pStrucBase, StrucElementType pType) throws Exception {
		
		List<ModelItem> modelItemList4 = null;
		
		//配置文件对应的实体模型
		if (StrucElementType.GROUP1D.equals(pType)) {
			// 父亲是字段组， 则获取所有的普通属性
			// 输入实体code， 获取本实体下所有的普通属性
			StrucMiCode p1 = commService.get(StrucMiCode.class, pStrucBase.getParentId());
			ModelItem modelItem1 = commService.get(ModelItem.class, p1.getItemCode());
			//获取实体下， 所有普通分组下的孩子
			ModelItemType[] pTypes1 = {ModelItemType.ONE_LINE_GROUP};
			ModelItemType[] chilTypes = {ModelItemType.REFERENCE_ITEM, ModelItemType.CASCADE_REFERENCE_ITEM};
			modelItemList4 = modelItemService.getModelItemByBelongMode(modelItem1.getBelongModel(), pTypes1,chilTypes, false);
			
		} else if (StrucElementType.GROUP2D.equals(pType)) {
			//父亲是二维表， 则获取二维表对应的孩子
			StrucMiCode p2 = commService.get(StrucMiCode.class, sbPid);
			ModelItemType[] types2 = {ModelItemType.REFERENCE_ITEM, ModelItemType.CASCADE_REFERENCE_ITEM};
			modelItemList4 = modelItemService.getModelItemByType(p2.getItemCode(), types2, null, Constants.USING_STATE_USING);
		}
		return modelItemList4;
	}

	//构建引用属性指向节点
	private void getViewLabelToStrucPointer(List<ViewLabel> viewLabelList, StrucPointer strucPointer,
			List<StrucBase> allStruc) {
		String sbId =  "";
		String pointer = "";
		
		  if (strucPointer !=null) { 
			  sbId = strucPointer.getSbId() ==  null?"":strucPointer.getSbId()+""; 
			  pointer = strucPointer.getPointer() ==  null?"":""+strucPointer.getPointer(); 
		  
		  }
		
		viewLabelList.add(new ViewLabel("input", "hidden", "strucPointer.id", sbId, null));

		ViewLabel itemCodeVb = new ViewLabel("select", "text", "strucPointer.pointer",pointer , "指向结构体", 1);
		// 获取值域
		Map<String, String> valueDomain = new HashMap<String, String>();
		  for (StrucBase strucBase : allStruc) {
			  valueDomain.put(strucBase.getId() + "", strucBase.getTitle()); 
		  }
		  itemCodeVb.setValueDomain(valueDomain);
		  itemCodeVb.setViewClazz("strucPointer");
		viewLabelList.add(itemCodeVb);
	}

	/**
	 * 获取 单选关系字段的结构
	 * @param viewLabelList
	 * @param strucRelation
	 * @param relationOne
	 */
	private void getVbRfieldStrucRelation(List<ViewLabel> viewLabelList, StrucRelation strucRelation,
			List<ModelRelationType> relationOne) {
		
		if(strucRelation == null) {
			strucRelation = new StrucRelation();
		}
		
		viewLabelList.add(new ViewLabel("input", "hidden", "strucRelation.id", strucRelation.getId()==null?"":"" +strucRelation.getId(), null));
		viewLabelList.add(new ViewLabel("input", "hidden", "strucRelation.sbId", strucRelation.getSbId()==null?"":strucRelation.getSbId()+"", null));
		
		ViewLabel itemCodeVb = new ViewLabel("select", "text", "strucRelation.modelRelationType",strucRelation.getModelRelationType()==null?"":strucRelation.getModelRelationType() , "对一关系", 16);
		// 获取值域
		Map<String, String> valueDomain = new LinkedHashMap<String, String>();
		  for (ModelRelationType modelRelation : relationOne) {
			  valueDomain.put(modelRelation.getTypeCode() + "", modelRelation.getName()); 
		  }
		  itemCodeVb.setValueDomain(valueDomain);
		  itemCodeVb.setViewClazz("radioStrucRela");
		  
		viewLabelList.add(itemCodeVb);
	}
	
	/**
	 * 	获取 多选关系字段的结构
	 * @param viewLabelList
	 * @param strucRelation
	 * @param relationOne
	 */
	private void getVbMultRfieldStrucRelation(List<ViewLabel> viewLabelList, List<StrucRelation> strucRelation,
			List<ModelRelationType> relationOne) {
		
		String value = "";
		 for (StrucRelation sr: strucRelation) {
			 value +=  sr.getModelRelationType()+",";
		  }
		
		 if (!value.isEmpty()) {
			 value = value.substring(0, value.length()-1);
		 }
		
		ViewLabel itemCodeVb = new ViewLabel("multSelect", "text", "strucRelation.modelRelationType",value , "多选关系", 10);
		
		// 获取可选值域
		Map<String, String> valueDomain = new HashMap<String, String>();
		  for (ModelRelationType modelRelation : relationOne) {
			  valueDomain.put(modelRelation.getTypeCode() + "", modelRelation.getName()); 
		  }
		  itemCodeVb.setValueDomain(valueDomain);
		  itemCodeVb.setViewClazz("multStrucRela");
		viewLabelList.add(itemCodeVb);
	}

	private void getVbToStrucBase(StrucBase strucBase, List<ViewLabel> viewLabelList) {
		// 这里设置通用属性
		viewLabelList.add(new ViewLabel("input", "hidden", "strucBase.id", strucBase.getId() ==null?"":strucBase.getId()+"", null));
		viewLabelList.add(new ViewLabel("input", "hidden", "strucBase.type", "" + strucBase.getType(), null));
		viewLabelList.add(new ViewLabel("input", "hidden", "strucBase.parentId", strucBase.getParentId()+"", null));
		ViewLabel titleVb = new ViewLabel("input", "text", "strucBase.title", strucBase.getTitle(), "名称", 20);
		
		titleVb.setViewClazz("strucBaseTitle");
		viewLabelList.add(titleVb);
		 
		ViewLabel optVb = new ViewLabel("select", "text", "strucBase.opt",strucBase.getOpt()+"" , "权限", 19);
		// 获取值域
		Map<String, String> optValue = new HashMap<String, String>();
		StrucOptType[] strucOptTypes = StrucOptType.values();
		  for (StrucOptType strucOptType : strucOptTypes) {
			  optValue.put(strucOptType.getIndex() + "", strucOptType.getName()); 
		  }
		  optVb.setValueDomain(optValue);
		  viewLabelList.add(optVb);
	}

	//获取  StrucMiCode 的必填字段
	private void getViewLabelToStrucMiCode(List<ViewLabel> viewLabelList, StrucMiCode strucMiCode, List<ModelItem> modelItemList, StrucElementType strucElementType) {
		String sbId =  "";
		String itemCode = "";
		
		  if (strucMiCode !=null) { 
			  sbId = strucMiCode.getSbId() ==  null?"":strucMiCode.getSbId()+""; 
			  itemCode = strucMiCode.getItemCode() ==  null?"":strucMiCode.getItemCode(); 
		  
		  }
		
		viewLabelList.add(new ViewLabel("input", "hidden", "strucMiCode.id", sbId, null));
		
		String itemName = "模型属性";
		String viewClazz = "strucMiCodeItemCode";
		if (StrucElementType.RSTRUC.equals(strucElementType)) {
			itemName = "选择右模型";
		} else if (StrucElementType.GROUP2D.equals(strucElementType)) {
			itemName = "多行分组";
		} else if (StrucElementType.RREFFIELD.equals(strucElementType)) {
			itemName = "引用属性";
			viewClazz = "rRefFieldClazz";
		}else if (StrucElementType.REFFIELD.equals(strucElementType)) {
			itemName = "引用属性";
			viewClazz = "refFieldClazz";
		}

		ViewLabel itemCodeVb = new ViewLabel("select", "text", "strucMiCode.itemCode",itemCode , itemName, 15);
		// 获取值域
		Map<String, String> valueDomain = new HashMap<String, String>();
		  for (ModelItem modelItem : modelItemList) {
			  valueDomain.put(modelItem.getCode() + "", modelItem.getName()); 
		  }
		  itemCodeVb.setValueDomain(valueDomain);
		
		itemCodeVb.setViewClazz(viewClazz);
		viewLabelList.add(itemCodeVb);
	}
	
		//获取  StrucFieldValue 的必填字段
		private void getViewLabelToStrucFieldValue(List<ViewLabel> viewLabelList, StrucMiCode strucMiCode,StrucFieldValue strucFieldValue, StrucElementType strucElementType) {			
			String sbId =  "";
			viewLabelList.add(new ViewLabel("input", "hidden", "strucFieldValue.sbId", sbId, null));
			
			String valueType = "";
			
			  if (strucFieldValue !=null) { 
				  sbId = strucFieldValue.getSbId() ==  null?"":strucFieldValue.getSbId()+""; 
				  valueType = strucFieldValue.getValueType() ==  null?"":strucFieldValue.getValueType()+""; 
			  }
			  
			  if (!StrucElementType.ENUMFIELD.equals(strucElementType)) {
				  
				  ViewLabel valueTypeVb = new ViewLabel("select", "text", "strucFieldValue.valueType",valueType , "值类型", 14);
			
			MiValue miValue = commService.get(MiValue.class, strucMiCode.getItemCode());
				ItemValueType itemType = ItemValueType.STRING;
				if (miValue != null) {
					itemType = ItemValueType.getValueType(Integer.parseInt(miValue.getDataType()));
				}
				
				ModelItem modelItem = commService.get(ModelItem.class, strucMiCode.getItemCode());
				
				Integer aa = modelItem == null? null:modelItem.getType();
				ModelItemType miType = ModelItemType.getItemType(aa==null?-1:aa);
				
			Collection<AttributeValueType> canTransType = ValueTypeMapping.getCanTransType(itemType, miType);
			
			// 获取值域
			Map<String, String> valueDomain = new HashMap<String, String>();
			  for (AttributeValueType attrValueType : canTransType) {
				  valueDomain.put(attrValueType.getIndex() + "", attrValueType.getCName()); 
			  }
			  valueTypeVb.setValueDomain(valueDomain);
			  valueTypeVb.setViewClazz("strucFieldValueType");
			  viewLabelList.add(valueTypeVb);
			}
		}

	@Override
	public List<StrucBase> getStructStairChild(Integer sbPid) {
		
		return strucBaseDao.getStructStairChild(sbPid);
	}

	@Override
	public void createStrucBaseQuick(String belongModel) throws Exception {
		
		//获取MODEL
		ModelItem modelItem = commService.get(ModelItem.class, belongModel);
		
		//创建结构体根节点
		
		String stucTitle = modelItem.getName() + System.currentTimeMillis();
		
		StrucBaseContainer sbc = new StrucBaseContainer();
		StrucBase strucBase = new StrucBase(null, StrucElementType.STRUC.getCode(), stucTitle, StrucOptType.WRITE.getIndex(), 1, null);
		StrucMiCode strucMiCode = new StrucMiCode(null, modelItem.getCode());
		sbc.setStrucBase(strucBase);
		sbc.setStrucMiCode(strucMiCode);
		
		//保存跟节点
		this.saveOrUpdate(sbc);
		
		//构建字段组和二维组， 维度组， 事实组
		List<ModelItem> modelItemGroup = modelItemService.getModelItemStairChild(modelItem.getCode());
		quickCreateStrucGroup(strucBase, modelItemGroup);
	}

	/**
	 * 快速生成结构组
	 * @param modelItemGroup
	 * @throws Exception 
	 */
	private void quickCreateStrucGroup(StrucBase pStrucBase, List<ModelItem> modelItemGroup) throws Exception {
		StrucBaseContainer sbc = null;
		for (ModelItem modelItem : modelItemGroup) {
			
			ModelItemType itemType = ModelItemType.getItemType(modelItem.getType());
			
			switch (itemType) {
			case ONE_LINE_GROUP:
			case DIMENSION_GROUP:
			case FACT_GROUP:
				
				sbc = new StrucBaseContainer();
				StrucBase strucBaseGroup = new StrucBase(null, StrucElementType.GROUP1D.getCode(), modelItem.getName(), StrucOptType.WRITE.getIndex(), pStrucBase.getCorder()+1, pStrucBase.getId());
				sbc.setStrucBase(strucBaseGroup);
				// 保存组
				this.saveOrUpdate(sbc);
				
				createGroupChil(modelItem, strucBaseGroup);
				break;
			case MULTI_LINE_GROUP:
			case GIANT_LINE_GROUP:
				sbc = new StrucBaseContainer();
				StrucBase strucBaseGroup2D = new StrucBase(null, StrucElementType.GROUP2D.getCode(), modelItem.getName(), StrucOptType.WRITE.getIndex(), pStrucBase.getCorder()+1, pStrucBase.getId());
				StrucMiCode strucMiCode = new StrucMiCode(null, modelItem.getCode());
				sbc.setStrucBase(strucBaseGroup2D);
				sbc.setStrucMiCode(strucMiCode);
				// 保存二维组
				this.saveOrUpdate(sbc);
				
				// 生成组的孩子的结构体
				// 除了引用属性，其他都可以生成
				createGroupChil(modelItem, strucBaseGroup2D);
			break;
		
			case ENUM_ITEM:
			case DIMENSION_ENUM_ITEM:
			case PREENUM_STRING_ITEM:
			case MULTI_ENUM_ITEM:
			case CASCADE_ENUM_ITEM:
				sbc = createFieldSb(pStrucBase, modelItem, StrucElementType.ENUMFIELD);
				// 保存属性
				this.saveOrUpdate(sbc);
				
				break;
				
		default:
			sbc = createFieldSb(pStrucBase, modelItem, StrucElementType.FIELD);
			// 保存属性
			this.saveOrUpdate(sbc);
			break;
			
		}
		
	}
	}

	private StrucBaseContainer createFieldSb(StrucBase pStrucBase, ModelItem modelItem, StrucElementType strucElementType) {
		StrucBaseContainer	sbc = new StrucBaseContainer();
		StrucBase strucBaseEnum = new StrucBase(null, strucElementType.getCode(), modelItem.getName(), StrucOptType.WRITE.getIndex(), pStrucBase.getCorder()+1, pStrucBase.getId());
		StrucMiCode strucMiCodeEnum = new StrucMiCode(null, modelItem.getCode());
		
		MiValue miValue = commService.get(MiValue.class, modelItem.getCode());
		ItemValueType itemVType = ItemValueType.STRING;
		if (miValue != null) {
			itemVType = ItemValueType.getValueType(Integer.parseInt(miValue.getDataType()));
		}
		
		Integer aa = modelItem == null? null:modelItem.getType();
		ModelItemType miType = ModelItemType.getItemType(aa==null?-1:aa);
		
		List<AttributeValueType> valueTypeCo = (List<AttributeValueType>)ValueTypeMapping.getCanTransType(itemVType, miType);
		StrucFieldValue strucFieldValue = new StrucFieldValue(null, valueTypeCo.get(0).getIndex());
		
		sbc.setStrucBase(strucBaseEnum);
		sbc.setStrucMiCode(strucMiCodeEnum);
		sbc.setStrucFieldValue(strucFieldValue);
		return sbc;
	}
	
	private void createGroupChil(ModelItem modelItem, StrucBase strucBaseGroup) throws Exception {
		// 生成组的孩子的结构体
		// 除了引用属性，其他都可以生成
		ModelItemType[] noNiTypes = {ModelItemType.REFERENCE_ITEM,ModelItemType.CASCADE_REFERENCE_ITEM};
		List<ModelItem> modelItemGroupChild = modelItemService.getModelItemByType(modelItem.getCode(), null, noNiTypes , Constants.USING_STATE_USING);
		// 生成分组的孩子
		quickCreateStrucGroup(strucBaseGroup, modelItemGroupChild);
	}

	@Override
	public List<StrucBase> getGroup1DChild(Integer sbId) {
		return strucBaseDao.getGroup1DChild(sbId);
	}

	@Override
	public List<StrucRelation> getStrucRelationBySbId(Integer sbId) {
		return strucBaseDao.getStrucRelationBySbId(sbId);
	}

	@Override
	public void deleteStruct(Integer sbId) {
		StructStrategyContext context = new StructStrategyContext(commService, this);
		context.delStruct(sbId);
	}

	@Override
	public void createCopyStruct(Integer sbId) throws Exception {
		StructStrategyContext context = new StructStrategyContext(commService, this);
		context.copyStruct(sbId);
	}

}


