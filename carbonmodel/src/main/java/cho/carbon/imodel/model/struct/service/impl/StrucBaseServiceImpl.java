package cho.carbon.imodel.model.struct.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
	public List<StrucBase> getAllStruc() {
		return strucBaseDao.getAllStruc();
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
		List<StrucBase> allStruc = this.getAllStruc();	
		
		
		StrucBase pStrucBase = commService.get(StrucBase.class, sbPid);
			StrucElementType pType = StrucElementType.getType(pStrucBase.getType());
			
			StrucMiCode strucMiCode = null;
			StrucFieldSubenum strucFieldSubenum = null;
			StrucFieldValue strucFieldValue = null;
			StrucPointer strucPointer = null;
			if (strucBase == null) {
				strucBase = new StrucBase(null, strucElementType.getCode(), "", StrucOptType.WRITE.getIndex(), 1, sbPid);
				strucMiCode = new StrucMiCode(null, "");
				strucFieldSubenum = new StrucFieldSubenum(null, null);
				strucFieldValue = new StrucFieldValue(null, null);
				strucPointer = new StrucPointer();
			} else {
				strucMiCode = commService.get(StrucMiCode.class, strucBase.getId());
				strucFieldSubenum = commService.get(StrucFieldSubenum.class, strucBase.getId());
				strucFieldValue = commService.get(StrucFieldValue.class, strucBase.getId());
				strucPointer = commService.get(StrucPointer.class, strucBase.getId());
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
				 	
					getViewLabelToStrucMiCode(viewLabelList, strucMiCode, modelItemByType);
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
						ModelItemType[] pTypes = {ModelItemType.ONE_LINE_GROUP};
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
					
				getViewLabelToStrucMiCode(viewLabelList, strucMiCode, modelItemList );
				getViewLabelToStrucFieldValue(viewLabelList, strucMiCode, strucFieldValue);
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
						ModelItemType[] pTypes = {ModelItemType.ONE_LINE_GROUP};
						ModelItemType[] chilTypes = {ModelItemType.ENUM_ITEM,
								ModelItemType.PREENUM_STRING_ITEM,
								ModelItemType.MULTI_ENUM_ITEM,
								ModelItemType.CASCADE_ENUM_ITEM};
						
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
					
				// 这个对应的美剧子集
				
				// 获取子集下拉框
				MiEnum miEnum = commService.get(MiEnum.class, enumCode);
				// 获取他的子集 值域
				List<CascadedictSubsection> subSelectByParentId = casSubsectionService.getSubSelectByParentId(miEnum.getPid());
				//枚举对应的子集
				viewLabelList.add(new ViewLabel("input", "hidden", "strucFieldSubenum.sbId", strucFieldSubenum.getSbId()==null?"":strucFieldSubenum.getSbId()+"", null));
				ViewLabel subViewLabel = new ViewLabel("select", "text", "strucFieldSubenum.subenumId",strucFieldSubenum.getSubenumId() == null?"":strucFieldSubenum.getSubenumId()+"" , "选择子集");
				// 获取值域
				Map<String, String> valueDomain = new HashMap<String, String>();
				  for (CascadedictSubsection casCade : subSelectByParentId) {
					  valueDomain.put(casCade.getId() + "", casCade.getName()); 
				  }
				  
				 subViewLabel.setValueDomain(valueDomain);
				 subViewLabel.setViewClazz("subsetStrucFileldEnum");
				viewLabelList.add(subViewLabel);
				getViewLabelToStrucMiCode(viewLabelList, strucMiCode, modelItemList2);
				getViewLabelToStrucFieldValue(viewLabelList, strucMiCode, strucFieldValue);
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
					
					getViewLabelToStrucFieldValue(viewLabelList, strucMiCode, strucFieldValue);
					getViewLabelToStrucMiCode(viewLabelList, strucMiCode, modelItemList3);
					getVbRfieldStrucRelation(viewLabelList, strucRelation.isEmpty()? null:strucRelation.get(0), relationOne);
					break;
				case RREFFIELD:
					
				case REFFIELD:
					
					
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
				
					getViewLabelToStrucPointer(viewLabelList, strucPointer, allStruc);
						
					getViewLabelToStrucMiCode(viewLabelList, strucMiCode, modelItemList4);
					getViewLabelToStrucFieldValue(viewLabelList, strucMiCode, strucFieldValue);
					break;
					
				case RSTRUC:
					// 存在关系的右实体
					StrucMiCode  p3 = commService.get(StrucMiCode.class, sbPid);
					String leftModelCode = p3.getItemCode();
					List<ModelItem> exRelaRightMi = modelRelaService.getExRelaRightMi(leftModelCode);
						
					getViewLabelToStrucMiCode(viewLabelList, strucMiCode, exRelaRightMi);
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
					
					// 指向结构体
					getViewLabelToStrucPointer(viewLabelList, strucPointer, allStruc);
					
					break;
			}
			
			Collections.sort(viewLabelList);
			return viewLabelList;
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
		
		ViewLabel itemCodeVb = new ViewLabel("select", "text", "strucRelation.modelRelationType",strucRelation.getModelRelationType()==null?"":strucRelation.getModelRelationType() , "选择关系", 16);
		// 获取值域
		Map<String, String> valueDomain = new HashMap<String, String>();
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
		ViewLabel titleVb = new ViewLabel("input", "text", "strucBase.title", strucBase.getTitle(), "字段名称", 20);
		
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
	private void getViewLabelToStrucMiCode(List<ViewLabel> viewLabelList, StrucMiCode strucMiCode, List<ModelItem> modelItemList) {
		String sbId =  "";
		String itemCode = "";
		
		  if (strucMiCode !=null) { 
			  sbId = strucMiCode.getSbId() ==  null?"":strucMiCode.getSbId()+""; 
			  itemCode = strucMiCode.getItemCode() ==  null?"":strucMiCode.getItemCode(); 
		  
		  }
		
		viewLabelList.add(new ViewLabel("input", "hidden", "strucMiCode.id", sbId, null));

		ViewLabel itemCodeVb = new ViewLabel("select", "text", "strucMiCode.itemCode",itemCode , "选择模型", 15);
		// 获取值域
		Map<String, String> valueDomain = new HashMap<String, String>();
		  for (ModelItem modelItem : modelItemList) {
			  valueDomain.put(modelItem.getCode() + "", modelItem.getName()); 
		  }
		  itemCodeVb.setValueDomain(valueDomain);
		
		itemCodeVb.setViewClazz("strucMiCodeItemCode");
		viewLabelList.add(itemCodeVb);
	}
	
		//获取  StrucFieldValue 的必填字段
		private void getViewLabelToStrucFieldValue(List<ViewLabel> viewLabelList, StrucMiCode strucMiCode,StrucFieldValue strucFieldValue) {
			String sbId =  "";
			String valueType = "";
			
			  if (strucFieldValue !=null) { 
				  sbId = strucFieldValue.getSbId() ==  null?"":strucFieldValue.getSbId()+""; 
				  valueType = strucFieldValue.getValueType() ==  null?"":strucFieldValue.getValueType()+""; 
			  
			  }
			
			viewLabelList.add(new ViewLabel("input", "hidden", "strucFieldValue.sbId", sbId, null));
			ViewLabel valueTypeVb = new ViewLabel("select", "text", "strucFieldValue.valueType",valueType , "类型", 18);
			
			MiValue miValue = commService.get(MiValue.class, strucMiCode.getItemCode());
				ItemValueType itemType = ItemValueType.STRING;
				if (miValue != null) {
					itemType = ItemValueType.getValueType(Integer.parseInt(miValue.getDataType()));
				}
					
			Collection<AttributeValueType> canTransType = ValueTypeMapping.getCanTransType(itemType);
			
			// 获取值域
			Map<String, String> valueDomain = new HashMap<String, String>();
			  for (AttributeValueType attrValueType : canTransType) {
				  valueDomain.put(attrValueType.getIndex() + "", attrValueType.getCName()); 
			  }
			  valueTypeVb.setValueDomain(valueDomain);
			  valueTypeVb.setViewClazz("strucFieldValueType");
			viewLabelList.add(valueTypeVb);
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
		StrucBaseContainer sbc = new StrucBaseContainer();
		StrucBase strucBase = new StrucBase(null, StrucElementType.STRUC.getCode(), modelItem.getName(), StrucOptType.WRITE.getIndex(), 1, null);
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
		StrucFieldValue strucFieldValue = new StrucFieldValue(null, itemVType.getIndex());
		
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

}


