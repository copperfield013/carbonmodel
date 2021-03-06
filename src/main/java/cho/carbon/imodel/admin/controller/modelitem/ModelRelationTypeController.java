package cho.carbon.imodel.admin.controller.modelitem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import cho.carbon.hc.copframe.dto.ajax.AjaxPageResponse;
import cho.carbon.hc.copframe.dto.page.PageInfo;
import cho.carbon.imodel.admin.controller.AdminConstants;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.Constants;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.pojo.ModelRelationType;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.service.ModelRelationTypeService;
import cho.carbon.meta.enun.ModelItemType;
import cho.carbon.meta.enun.RelationType;

@Controller
@RequestMapping(AdminConstants.URI_BASE + "/modelRelationType")
public class ModelRelationTypeController {
	
	@Resource
	ModelRelationTypeService modelRelationTypeService;
	
	@Resource
	ModelItemService miService;
	
	@Resource
	CommService commService;
	
	
	Logger logger = Logger.getLogger(ModelRelationTypeController.class);
	@org.springframework.web.bind.annotation.InitBinder
	public void InitBinder(ServletRequestDataBinder binder) {
		System.out.println("执行了InitBinder方法");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}
	
	//这个有用
	@RequestMapping(value = "/list")
	public ModelAndView list(ModelRelationType modelRelationType,  PageInfo pageInfo) {
		try {
			
			List<ModelRelationType> modelRelaList = modelRelationTypeService.queryList(modelRelationType, pageInfo);
			
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("modelRelaList", modelRelaList);
			modelAndView.addObject("modelRelationType", modelRelationType);
			modelAndView.addObject("pageInfo", pageInfo);
			modelAndView.setViewName(AdminConstants.JSP_BASE + "/modelitem/confrelation/confRelationList.jsp");
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 	 跳转到关系添加页面
	 * @param model
	 * @param leftModelCode
	 * @return
	 */
	@RequestMapping("/add")
	public String add(Model model, String leftModelCode) {
		try {
			ModelItemType[] existMiTypes = {ModelItemType.MODEL, ModelItemType.STAT_MODEL, ModelItemType.SQL_MODEL, ModelItemType.MQL_MODEL};
			List<ModelItem> modelItemList = miService.getModelItemByType(null, existMiTypes, null, Constants.USING_STATE_USING);
			ModelItem modelItem = commService.get(ModelItem.class, leftModelCode);
			model.addAttribute("leftModelName", modelItem.getName());
			model.addAttribute("leftModelCode", leftModelCode);
			model.addAttribute("modelItemList", modelItemList);
			return AdminConstants.JSP_BASE + "/modelitem/confrelation/addRelation.jsp";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/doAdd")
	public AjaxPageResponse doAdd(String leftModelCode,String rightModelCode, String leftName, String rightName,Integer leftRelationType,Integer rightRelationType, Integer leftgiant, Integer rightgiant, String symmetry){
		ModelRelationType leftObj = new ModelRelationType();
		ModelRelationType rightObj = new ModelRelationType();
		
		leftObj.setName(leftName);
		leftObj.setLeftModelCode(leftModelCode);
		leftObj.setGiant(leftgiant);
		
		if ("symmetry".equals(symmetry)) {//添加对称关系
			leftObj.setRightModelCode(leftModelCode);
			leftObj.setRelationType(leftRelationType);
		} else {
			leftObj.setRightModelCode(rightModelCode);
			leftObj.setRelationType(leftRelationType);
			
			//生成右关系
			rightObj.setName(rightName);
			rightObj.setLeftModelCode(rightModelCode);
			rightObj.setRightModelCode(leftModelCode);
			rightObj.setRelationType(rightRelationType);
			rightObj.setGiant(rightgiant);
		}
		
			try {
				modelRelationTypeService.saveRelation(leftObj, rightObj, symmetry);
				return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("添加成功", "modelItem_confRelationList");
			} catch (DataIntegrityViolationException e) {
				e.printStackTrace();
				return AjaxPageResponse.FAILD("主键重复或者是关系名重复, 请重新添加");
			} catch (Exception e) {
				logger.error("操作失败", e);
				e.printStackTrace();
				return AjaxPageResponse.FAILD("前缀表没有可用数据");
			}
	}

	@ResponseBody
	@RequestMapping("/delete")
	public AjaxPageResponse delete(String typeCode){
		try {
			modelRelationTypeService.delete(typeCode);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			logger.error("删除失败", e);
			e.printStackTrace();
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
	@ResponseBody
	@RequestMapping("/saveStatus")
	public AjaxPageResponse saveStatus(String typeCode, Integer usingState){
		try {
			modelRelationTypeService.saveStatus(typeCode, usingState);
			return AjaxPageResponse.REFRESH_LOCAL("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxPageResponse.FAILD("操作失败");
		}
	}
	
	/**
	 * 	编辑关系名称， 获取关系记录
	 * @param typeCode
	 * @param isPastDue
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(Model model,String typeCode){
		try {
     		//  正向关系 
			 ModelRelationType relation= modelRelationTypeService.getRecordRelationType(typeCode);
			 // 逆向关系
			
			  ModelRelationType reverseRela = null; 
			 //  是否为对称关系 
			  String symmetry = "symmetry";
			  if (!relation.getTypeCode().equals(relation.getReverseCode())) {
				  reverseRela =	  modelRelationTypeService.getRecordRelationType(relation.getReverseCode());
				  symmetry = "";
			  }
					
			  //正向模型名称
			  ModelItem modelItem = commService.get(ModelItem.class,  relation.getLeftModelCode());
			 //逆向模型名称
			  ModelItem reverseModelItem = commService.get(ModelItem.class,  relation.getRightModelCode());
			 
			 
			 //TODO......
			  model.addAttribute("leftModelName", modelItem.getName());
			 model.addAttribute("symmetry", symmetry);
			 model.addAttribute("relation", relation);
			 model.addAttribute("reverseRela", reverseRela);
			 model.addAttribute("reverseModelItem", reverseModelItem);
			 
			return AdminConstants.JSP_BASE + "/modelitem/confrelation/editRelation.jsp";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 根据关系code， 获取本关系对象
	 * @param typeCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getModelRelation")
	public String getModelRelation(String typeCode){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
			ModelRelationType modelRelationType = commService.get(ModelRelationType.class, typeCode);
			map.put("modelRelationType", modelRelationType);
			map.put("code", 200);
			map.put("msg", "成功！");
			return jobj.toJSONString();
		} catch (Exception e) {
			map.put("code", 400);
			map.put("msg", "失败！");
			return jobj.toJSONString();
		}
	}
	
	/**
	 * 保存编辑后的 关系名称
	 * @param typeCode
	 * @param isPastDue
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/do_edit")
	public AjaxPageResponse doEdit( String typeCode, String leftName,Integer leftRelationType, Integer leftgiant,
			String symmetry,
			String reverseCode, String rightName, Integer rightRelationType, Integer rightgiant){
		try {
			ModelRelationType recordRelationType = modelRelationTypeService.getRecordRelationType(typeCode);
			recordRelationType.setName(leftName);
			recordRelationType.setRelationType(leftRelationType);
			recordRelationType.setGiant(leftgiant);
			if (!"symmetry".equals(symmetry) ) {
				// 不对称关系
				 ModelRelationType reverseRela = modelRelationTypeService.getRecordRelationType(reverseCode);
				 reverseRela.setName(rightName);
				 reverseRela.setRelationType(rightRelationType);
				 reverseRela.setGiant(rightgiant);
				 
				 modelRelationTypeService.update(reverseRela);
			} 
			
			
			modelRelationTypeService.update(recordRelationType);
			
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("添加成功", "modelItem_confRelationList");
		} catch (Exception e) {
			return AjaxPageResponse.FAILD("操作失败！");
		}
	}
	
	 //这里获取关系类型
    @ResponseBody
	@RequestMapping("/getRelationType")
	public String getRelationType(){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
			Map<String, Object> reTypeMap = new HashMap<String, Object>();
			reTypeMap.put(String.valueOf(RelationType.MANY.getIndex()), RelationType.MANY.getCName());
			reTypeMap.put(String.valueOf(RelationType.ONE.getIndex()), RelationType.ONE.getCName());
			map.put("reTypeMap", reTypeMap);
			map.put("code", 200);
			map.put("msg", "操作成功");
			return jobj.toString();
		} catch (Exception e) {
			logger.error("操作失败", e);
			map.put("code", 400);
			map.put("msg", "操作失败");
			return jobj.toString();
		}
	}
    
    
    //这里获取本实体下特定类型的关系
    @ResponseBody
	@RequestMapping("/getRelation")
	public String getRelation(String leftRecordType, Integer relationType){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
			
			RelationType relationType2 = RelationType.getRelationType(relationType);
			
			List<ModelRelationType> relationList = modelRelationTypeService.getRelaByType(leftRecordType, relationType2);
			map.put("relationList", relationList);
			map.put("code", 200);
			map.put("msg", "操作成功");
			return jobj.toString();
		} catch (Exception e) {
			logger.error("操作失败", e);
			map.put("code", 400);
			map.put("msg", "操作失败");
			return jobj.toString();
		}
	}
    
    
    /**
     * 	获取左model和右model共同的关系
     * @param leftModelCode
     * @param rightModelCode
     * @return
     */
    @ResponseBody
	@RequestMapping("/getModelRela")
	public String getModelRela(String leftModelCode, String rightModelCode){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
			
			List<ModelRelationType> modelRelaList = modelRelationTypeService.getEntityRelaByBitemId(leftModelCode, rightModelCode);
			map.put("modelRelaList", modelRelaList);
			map.put("code", 200);
			map.put("msg", "操作成功");
			return jobj.toString();
		} catch (Exception e) {
			logger.error("操作失败", e);
			map.put("code", 400);
			map.put("msg", "操作失败");
			return jobj.toString();
		}
	}
    
    
    /**
     * 获取与左MODEL 存在关系的右MODEL
     * @param leftModelCode
     * @return
     */
    @ResponseBody
   	@RequestMapping("/getExistRelaRightMi")
   	public String getExistRelaRightMi(String leftModelCode){
   		Map<String, Object> map = new HashMap<String, Object>();
   		JSONObject jobj = new JSONObject(map);
   		try {
   			
   			List<ModelItem> existRelaRightMiList = modelRelationTypeService.getExistRelaRightMi(leftModelCode);
   			map.put("existRelaRightMiList", existRelaRightMiList);
   			map.put("code", 200);
   			map.put("msg", "操作成功");
   			return jobj.toString();
   		} catch (Exception e) {
   			logger.error("操作失败", e);
   			map.put("code", 400);
   			map.put("msg", "操作失败");
   			return jobj.toString();
   		}
   	}
	
}
