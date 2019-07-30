package cho.carbon.imodel.admin.controller.modelitem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cho.carbon.imodel.admin.controller.AdminConstants;
import cho.carbon.imodel.model.cascadedict.pojo.CascadedictBasicItem;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiCascade;
import cho.carbon.imodel.model.modelitem.pojo.MiEnum;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterGroup;
import cho.carbon.imodel.model.modelitem.pojo.MiModel;
import cho.carbon.imodel.model.modelitem.pojo.MiModelStat;
import cho.carbon.imodel.model.modelitem.pojo.MiReference;
import cho.carbon.imodel.model.modelitem.pojo.MiStatFact;
import cho.carbon.imodel.model.modelitem.pojo.MiTwolevelMapping;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.MiTableDBService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;
import cho.carbon.imodel.model.modelitem.vo.ViewLabel;
import cho.carbon.imodel.model.struct.pojo.StrucMiCode;
import cho.carbon.meta.enun.FilterCriteriaOperator2;
import cho.carbon.meta.enun.ModelItemType;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.NoticeType;
import cn.sowell.copframe.dto.ajax.PageAction;
import cn.sowell.copframe.dto.page.PageInfo;

@Controller
@RequestMapping(AdminConstants.URI_BASE + "/modelItem")
public class ModelItemController {

	Logger logger = Logger.getLogger(ModelItemController.class);

	@Resource
	CommService commService;
	
	@Resource
	ModelItemService miService;
	@Resource
	MiTableDBService miTableDBService;
	
	//创建表
	@ResponseBody
	@RequestMapping(value="/createDBTable", method=RequestMethod.POST)
	public AjaxPageResponse createDBTable(){
		try {
			miTableDBService.createDBTable();
			return AjaxPageResponse.REFRESH_LOCAL("操作成功");
		} catch (Exception e) {
		 return AjaxPageResponse.FAILD("操作失败");
		}
	}

	/**
	 * jsp 页面 分页 显示实体列表
	 * 
	 * @param pageInfo
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(ModelItem modelItem, PageInfo pageInfo) {
		try {
			List<ModelItem> modelItemList = miService.queryList(modelItem, pageInfo);
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("modelItemList", modelItemList);
			modelAndView.addObject("modelItem", modelItem);
			modelAndView.addObject("pageInfo", pageInfo);
			modelAndView.setViewName(AdminConstants.JSP_BASE + "/modelitem/list.jsp");
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @return 获取所有MODEL
	 */
	@ResponseBody
	@RequestMapping("/getModelList")
	public String getModelList(){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
			List<ModelItem> modelList = miService.getModelList();
			map.put("modelList", modelList);
			map.put("code", 200);
			map.put("msg", "成功！");
			return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
		} catch (Exception e) {
			logger.error("操作失败！", e);
			e.printStackTrace();
			map.put("code", 400);
			map.put("msg", "操作失败！");
			return jobj.toString();
		}
	}
	

	@RequestMapping("/add")
	public String add(Model model) {
		return AdminConstants.JSP_BASE + "/modelitem/addModel.jsp";
	}
	
	@RequestMapping("/update")
	public String update(String itemCode, Model model) {
		ModelItem modelItem = commService.get(ModelItem.class, itemCode);
		
		ModelItemType itemType = ModelItemType.getItemType(modelItem.getType());
		if (ModelItemType.STAT_MODEL.equals(itemType) || ModelItemType.SQL_MODEL.equals(itemType) ) {
			MiModelStat miModelStat = commService.get(MiModelStat.class, itemCode);
			ModelItem sourceModelItem = commService.get(ModelItem.class, miModelStat.getSourceCode());
			model.addAttribute("sourceModelItem", sourceModelItem);
		}
		
		MiModel miModel = commService.get(MiModel.class, itemCode);
		
		model.addAttribute("modelItem", modelItem);
		model.addAttribute("miModel", miModel);
		return AdminConstants.JSP_BASE + "/modelitem/updateModel.jsp";
	}

	@ResponseBody
	@RequestMapping("/createModelItem")
	public AjaxPageResponse createModelItem(ModelItemContainer modelItemContainer) {
		try {
			ModelItemContainer itemContainer = miService.saveOrUpdate(modelItemContainer);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("添加成功", "modelitem_list");
		} catch (Exception e) {
			logger.error("添加失败", e);
			String message = e.getMessage();
			
			if (!message.contains("t_cc_model_item_fix")) {
				message = "操作失败！";
			}
			
			return AjaxPageResponse.FAILD(message);
		}
	}
	
	@ResponseBody
	@RequestMapping("/createModelItemAttr")
	public String createModelItemAttr(ModelItemContainer modelItemContainer){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
			ModelItemContainer itemContainer = miService.saveOrUpdate(modelItemContainer);
			map.put("modelItem", itemContainer.getModelItem());
			map.put("code", 200);
			map.put("msg", "成功！");
			return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
		} catch (Exception e) {
			logger.error("添加失败", e);
			e.printStackTrace();
			map.put("code", 400);
			map.put("msg", "操作失败！");
			return jobj.toString();
		}
	}
	
    @RequestMapping(value = "/itemtree")
	public ModelAndView itemtree(String itemCode) {
		ModelAndView mv = new ModelAndView();
		ModelItem modelItem = commService.get(ModelItem.class, itemCode);
		
		mv.addObject("modelItem", modelItem);
		mv.setViewName(AdminConstants.JSP_BASE + "/modelitem/itemtree.jsp");
		return mv;
	}
    
   /**
    *  ModelItem 的type
    *  * @param modelItemCode   本身code
    * @param modelItemType      本身类型
    * @param mipCode  父code   
    * @param model
    * @return
    */
    @RequestMapping("/addAttr")
	public String addAttr(String modelItemCode, Integer modelItemType,String mipCode,  Model model) {
    	// 根据 type 类型， 获取本类型到底需要哪些属性以及默认属性
    	model.addAttribute("modelItemCode", modelItemCode);
    	model.addAttribute("modelItemType", modelItemType);
    	model.addAttribute("mipCode", mipCode);
		return AdminConstants.JSP_BASE + "/modelitem/addAttr.jsp";
	}
    
    /**
     * 	根据 ModelItem type  获取此类型必填的字段属性	
     * @param modelItemType
     * @param model
     * @return
     */
    @ResponseBody
	@RequestMapping("/getDefaultAttrByMType")
	public String getDefaultAttrByMType(String modelItemCode, Integer modelItemType, String mipCode){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
			List<ViewLabel> viewLabelList = miService.getDefaultAttrByMType(modelItemCode, ModelItemType.getItemType(modelItemType), mipCode);
	    	map.put("viewLabelList", viewLabelList);
			map.put("code", 200);
			map.put("msg", "成功！");
			return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
		} catch (Exception e) {
			logger.error("添加失败", e);
			e.printStackTrace();
			map.put("code", 400);
			map.put("msg", "操作失败！");
			return jobj.toString();
		}
	}
    
    
    	
		 /**
		  *	 获取可选的展示属性
		  * @param pmiCode
		  * @return
		  */
		 @ResponseBody
		@RequestMapping("/getShowCode")
		public String getShowCode(String miCode){
			Map<String, Object> map = new HashMap<String, Object>();
			JSONObject jobj = new JSONObject(map);
			try {
				//获取所有实体模型
		  		ModelItemType[] types2 = {ModelItemType.ONE_LINE_GROUP};
		  		//获取所有模型下的属性， 单行下的孩子
		  		 List<ModelItem> modelItemList = miService.getModelItemByBelongMode(miCode, types2,null, false);
		    	map.put("modelItemList", modelItemList);
				map.put("code", 200);
				map.put("msg", "成功！");
				return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
			} catch (Exception e) {
				logger.error("操作失败", e);
				e.printStackTrace();
				map.put("code", 400);
				map.put("msg", "操作失败！");
				return jobj.toString();
			}
		}
		 /**
		  *	 获取可选的识别属性
		  * @param pmiCode
		  * @return
		  */
		 @ResponseBody
		@RequestMapping("/getRecognitionCode")
		public String getRecognitionCode(String miCode){
			Map<String, Object> map = new HashMap<String, Object>();
			JSONObject jobj = new JSONObject(map);
			try {
				ModelItemType[] types = {ModelItemType.ONE_LINE_GROUP, ModelItemType.MULTI_LINE_GROUP, ModelItemType.GIANT_LINE_GROUP};
				//获取所有模型下的属性， 包括多行和单行下的孩子
				 List<ModelItem> modelItemList = miService.getModelItemByBelongMode(miCode, types ,null,  false);
		    	map.put("modelItemList", modelItemList);
				map.put("code", 200);
				map.put("msg", "成功！");
				return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
			} catch (Exception e) {
				logger.error("操作失败", e);
				e.printStackTrace();
				map.put("code", 400);
				map.put("msg", "操作失败！");
				return jobj.toString();
			}
		}   
		 
    
    /**
     * 	根据父code, 获取下一阶梯的孩子
     * @param pmiCode
     * @return
     */
    @ResponseBody
	@RequestMapping("/getModelItemStairChild")
	public String getModelItemStairChild(String pmiCode){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
			List<ModelItem> modelItemList = miService.getModelItemStairChild(pmiCode);
	    	map.put("modelItemList", modelItemList);
			map.put("code", 200);
			map.put("msg", "成功！");
			return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
		} catch (Exception e) {
			logger.error("操作失败", e);
			e.printStackTrace();
			map.put("code", 400);
			map.put("msg", "操作失败！");
			return jobj.toString();
		}
	}
    
    @ResponseBody
   	@RequestMapping("/delModelItem")
   	public String delModelItem(String miCode){
   		Map<String, Object> map = new HashMap<String, Object>();
   		JSONObject jobj = new JSONObject(map);
   		try {
   			
   			List<ModelItem> modelItemStairChild = miService.getModelItemStairChild(miCode);
   			if (!modelItemStairChild.isEmpty()) {
   				map.put("code", 400);
   	   			map.put("msg", "请先删除孩子！");
   	   			return jobj.toString();
   			}  
   			
   			ModelItem modelItem = commService.get(ModelItem.class, miCode);
   			ModelItemType itemType = ModelItemType.getItemType(modelItem.getType());

   			if (ModelItemType.MULTI_LINE_GROUP.equals(itemType)) {
   				List<MiTwolevelMapping> miTwoMapping = miService.getMiTwoMapping(modelItem.getCode());
   				if (!miTwoMapping.isEmpty()) {
   					map.put("code", 400);
   	   	   			map.put("msg", "请先删除二级属性组！");
   	   	   			return jobj.toString();
   				}
   			}
   			
   			if (ModelItemType.CASCADE_REFERENCE_ITEM.equals(itemType)) {
   				List<MiCascade> miCascadeList = miService.getMiCascadeList(modelItem.getCode());
   				if (!miCascadeList.isEmpty()) {
   					map.put("code", 400);
   	   	   			map.put("msg", "请先删除级联引用的孩子！");
   	   	   			return jobj.toString();
   				}
   			}
   			
   			if (ModelItemType.FACT_ITEM.equals(itemType)) {
   				MiStatFact miStatFact = commService.get(MiStatFact.class, modelItem.getCode());		
   				
   				MiFilterGroup miFilterGroup = null;
   				if (miStatFact != null && miStatFact.getFilterId() !=null) {
   					miFilterGroup = commService.get(MiFilterGroup.class, miStatFact.getFilterId());		
   				}
   				
   				if (miFilterGroup!=null) {
   					map.put("code", 400);
   	   	   			map.put("msg", "请先删除事实属性的过滤条件！");
   	   	   			return jobj.toString();
   				}
   			}
   			
   			miService.deleteModelItem(miCode);
   			map.put("code", 200);
   			map.put("msg", "操作成功！");
   			return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
   		} catch (Exception e) {
   			logger.error("操作失败", e);
   			e.printStackTrace();
   			map.put("code", 400);
   			map.put("msg", "操作失败！");
   			return jobj.toString();
   		}
   	}
    
    
    /**
	 * jsp 获取级联枚举的孩子
	 * @param pageInfo
	 * @return
	 */
	@RequestMapping(value = "/getCasEnumChild")
	public ModelAndView getCasEnumChild(String pmiCode) {
		try {
			List<MiCascade> miCascadeList = miService.getMiCascadeList(pmiCode);
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("miCascadeList", miCascadeList);
			modelAndView.addObject("pmiCode", pmiCode);
			modelAndView.setViewName(AdminConstants.JSP_BASE + "/modelitem/casEnumChild/casEnumAttrList.jsp");
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 /**
		 * jsp 级联引用属性的孩子
		 * @param pageInfo
		 * @return
		 */
		@RequestMapping(value = "/getCasRefChild")
		public ModelAndView getCasRefChild(String pmiCode) {
			try {
				List<MiCascade> miCascadeList = miService.getMiCascadeList(pmiCode);
				ModelAndView modelAndView = new ModelAndView();
				modelAndView.addObject("miCascadeList", miCascadeList);
				modelAndView.addObject("pmiCode", pmiCode);
				modelAndView.setViewName(AdminConstants.JSP_BASE + "/modelitem/casReferenceChild/casReferenceChild.jsp");
				return modelAndView;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * 跳转到添加级联属性孩子的页面
		 * @param model
		 * @return
		 */
		@RequestMapping("/addCasRefChild")
		public String addCasRefChild(Model model, String pmiCode) {
			try {
				
				ModelItem modelItem = commService.get(ModelItem.class, pmiCode);
				//获取引用属性， 
				ModelItemType[] existMiTypes = { ModelItemType.REFERENCE_ITEM};
				List<ModelItem> modelItemList = miService.getModelItemByType(modelItem.getParent(),existMiTypes, null, null);
				model.addAttribute("pmiCode", pmiCode);
				model.addAttribute("modelItemList", modelItemList);
				return AdminConstants.JSP_BASE + "/modelitem/casReferenceChild/addCasRefChild.jsp";
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@ResponseBody
		@RequestMapping(value = "/doAddCasRefChild")
		public AjaxPageResponse doAddCasRefChild(MiCascade miCascade) {
			try {
				List<MiCascade> miCascadeList = miService.getMiCascadeList(miCascade.getCode());
				
				Integer level = miCascadeList == null?0:miCascadeList.size();
				miCascade.setLevel(level+1);
				commService.insert(miCascade);

				return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("添加成功", "modelitem_list");
			} catch (Exception e) {
				logger.error("添加失败", e);
				return AjaxPageResponse.FAILD("添加失败");
			}
		}
		
		
		@ResponseBody
		@RequestMapping(value = "/delCasRefChild")
		public AjaxPageResponse delCasRefChild(MiCascade miCascade) {
			try {
				commService.delete(miCascade);
				return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("删除成功", "modelitem_list");
			} catch (Exception e) {
				logger.error("删除失败", e);
				return AjaxPageResponse.FAILD("删除失败");
			}
		}
		
		//下面是二级属性处理逻辑
		/**
		 * @param moreLineCode  多行组的code
		 * @return   	获取多行属性对应的二级属性
		 */
		 @ResponseBody
			@RequestMapping("/getMiTwoMapping")
			public String getMiTwoMapping(String moreLineCode){
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject jobj = new JSONObject(map);
				try {
					List<MiTwolevelMapping> miTwoMappingList = miService.getMiTwoMapping(moreLineCode);
			    	map.put("miTwoMappingList", miTwoMappingList);
					map.put("code", 200);
					map.put("msg", "成功！");
					return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
				} catch (Exception e) {
					logger.error("操作失败", e);
					e.printStackTrace();
					map.put("code", 400);
					map.put("msg", "操作失败！");
					return jobj.toString();
				}
			}
		 
		 @RequestMapping("/addTwoLevelMaping")
		public String add(Model model, String moreLineCode) {
			 ModelItem modelItem = commService.get(ModelItem.class, moreLineCode);
			 model.addAttribute("modelItem", modelItem);
			return AdminConstants.JSP_BASE + "/modelitem/twoLevel/twoLevelTree.jsp";
		}
		 
		 
		 /**
		  * 	保存更新二级组
		  * @param miTwolevelMapping
		  * @return
		  */
		 	@ResponseBody
			@RequestMapping("/miTwoMappSaveOrUP")
			public String miTwoMappSaveOrUP(MiTwolevelMapping miTwolevelMapping){
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject jobj = new JSONObject(map);
				try {
					
					if (miTwolevelMapping.getId().SIZE <=0 || miTwolevelMapping.getId() == null) {
						commService.insert(miTwolevelMapping);
					} else {
						commService.update(miTwolevelMapping);
					}

					map.put("miTwolevelMapping", miTwolevelMapping);
					map.put("code", 200);
					map.put("msg", "成功！");
					return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
				} catch (Exception e) {
					logger.error("操作失败", e);
					e.printStackTrace();
					map.put("code", 400);
					map.put("msg", "操作失败！");
					return jobj.toString();
				}
			}
		 
		 	/**
		 	 * 	根据MiEnum 的code， 获取pid下的孩子
		 	 * @param miCode
		 	 * @return
		 	 */
		 	@ResponseBody
			@RequestMapping("/getMiEnumChild")
			public String getMiEnumChild(String miCode){
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject jobj = new JSONObject(map);
				try {
					List<CascadedictBasicItem> casEnumChild = miService.getCasEnumChild(miCode);
					map.put("casEnumChild", casEnumChild);
					map.put("code", 200);
					map.put("msg", "成功！");
					return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
				} catch (Exception e) {
					logger.error("操作失败", e);
					e.printStackTrace();
					map.put("code", 400);
					map.put("msg", "操作失败！");
					return jobj.toString();
				}
			}
		 	
		 /**
		  	* 获取 父节点下  所有枚举属性
		  * @param moreLineCode
		  * @return
		  */
		 	@ResponseBody
			@RequestMapping("/getMiEnumList")
			public String getMiEnumList(String pmiCode){
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject jobj = new JSONObject(map);
				try {
					
					ModelItemType[] existMiTypes = {ModelItemType.ENUM_ITEM};
					List<ModelItem> modelItemEnumList = miService.getModelItemByType(pmiCode, existMiTypes,null, null);
			    	map.put("modelItemEnumList", modelItemEnumList);
					map.put("code", 200);
					map.put("msg", "成功！");
					return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
				} catch (Exception e) {
					logger.error("操作失败", e);
					e.printStackTrace();
					map.put("code", 400);
					map.put("msg", "操作失败！");
					return jobj.toString();
				}
			}
		 	
		 	/**
		  	* 获取父节点下的除了枚举属性之外的所有属性
		  * @param moreLineCode
		  * @return
		  */
		 	@ResponseBody
			@RequestMapping("/getMiNoEnumList")
			public String getMiNoEnumList(String pmiCode){
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject jobj = new JSONObject(map);
				try {
					ModelItemType[] noNiTypes = {ModelItemType.ENUM_ITEM};
					List<ModelItem> modelItemNoEnumList = miService.getModelItemByType(pmiCode, null, noNiTypes, null);
			    	map.put("modelItemNoEnumList", modelItemNoEnumList);
					map.put("code", 200);
					map.put("msg", "成功！");
					return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
				} catch (Exception e) {
					logger.error("操作失败", e);
					e.printStackTrace();
					map.put("code", 400);
					map.put("msg", "操作失败！");
					return jobj.toString();
				}
			}
		 	
		 	/**
		 	 * 根据mappingId, 获取二级组对应的所有二级属性
		 	 * @param mappingId
		 	 * @return
		 	 */
		 	@ResponseBody
			@RequestMapping("/getTwoAttrByMappingId")
			public String getTwoAttrByMappingId(Integer mappingId){
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject jobj = new JSONObject(map);
				try {
					 List twoAttrList = miService.getTwoAttrByMappingId(mappingId);
			    	map.put("twoAttrList", twoAttrList);
					map.put("code", 200);
					map.put("msg", "成功！");
					return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
				} catch (Exception e) {
					logger.error("操作失败", e);
					e.printStackTrace();
					map.put("code", 400);
					map.put("msg", "操作失败！");
					return jobj.toString();
				}
		 
		 	} 	
		 	
		 	
		 	/**
		 	 * 删除二级属性组
		 	 * @param mappingId
		 	 * @return
		 	 */
		 	@ResponseBody
			@RequestMapping("/delTwoAttrMapping")
			public String delTwoAttrMapping(Integer mappingId){
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject jobj = new JSONObject(map);
				try {
					
					List twoAttrList = miService.getTwoAttrByMappingId(mappingId);
					if(!twoAttrList.isEmpty()) {
						map.put("code", 400);
						map.put("msg", "请先删除二级属性组的孩子！");
						return jobj.toString();
					}
					
					MiTwolevelMapping mapp = new MiTwolevelMapping();
					mapp.setId(mappingId);
					commService.delete(mapp);
					
					map.put("code", 200);
					map.put("msg", "成功！");
					return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
				} catch (Exception e) {
					logger.error("操作失败", e);
					e.printStackTrace();
					map.put("code", 400);
					map.put("msg", "操作失败！");
					return jobj.toString();
				}
		 
		 	} 	
		 	
		 	
		 	/**
		 	 * 根据miCode , 获取 MiEnum
		 	 * @param miCode
		 	 * @return
		 	 */
		    @ResponseBody
			@RequestMapping("/getMiEnum")
			public String getMiEnum(String miCode){
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject jobj = new JSONObject(map);
				try {
					MiEnum miEnum = commService.get(MiEnum.class, miCode);
					map.put("miEnum", miEnum);
					map.put("code", 200);
					map.put("msg", "成功！");
					return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
				} catch (Exception e) {
					logger.error("添加失败", e);
					e.printStackTrace();
					map.put("code", 400);
					map.put("msg", "操作失败！");
					return jobj.toString();
				}
			}
		    
		    @ResponseBody
			@RequestMapping("/getMiReference")
			public String getMiReference(String miCode){
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject jobj = new JSONObject(map);
				try {
					MiReference miReference = commService.get(MiReference.class, miCode);
					map.put("miReference", miReference);
					map.put("code", 200);
					map.put("msg", "成功！");
					return jobj.toJSONString(map, SerializerFeature.WriteMapNullValue);
				} catch (Exception e) {
					logger.error("添加失败", e);
					e.printStackTrace();
					map.put("code", 400);
					map.put("msg", "操作失败！");
					return jobj.toString();
				}
			}
		    
		    @ResponseBody
			@RequestMapping("/getFilOperator")
			public String getFilOperator(){
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject jobj = new JSONObject(map);
				try {
					
					FilterCriteriaOperator2[] values = FilterCriteriaOperator2.values();
					
					Map<String, Object> operatorMap = new HashMap<String, Object>();
					for (FilterCriteriaOperator2 fo : values) {
						operatorMap.put(fo.getCode()+"", fo.getName());
					}
					
					map.put("operatorMap", operatorMap);
					map.put("code", 200);
					map.put("msg", "成功！");
					return jobj.toString();
				} catch (Exception e) {
					logger.error("添加失败", e);
					e.printStackTrace();
					map.put("code", 400);
					map.put("msg", "操作失败！");
					return jobj.toString();
			}
	}
		    
}