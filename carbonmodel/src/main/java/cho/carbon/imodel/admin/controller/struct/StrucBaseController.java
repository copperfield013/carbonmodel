package cho.carbon.imodel.admin.controller.struct;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cho.carbon.imodel.admin.controller.AdminConstants;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.vo.ViewLabel;
import cho.carbon.imodel.model.struct.pojo.StrucBase;
import cho.carbon.imodel.model.struct.pojo.StrucMiCode;
import cho.carbon.imodel.model.struct.service.StrucBaseService;
import cho.carbon.imodel.model.struct.vo.StrucBaseContainer;
import cho.carbon.meta.enun.AttributeValueType;
import cho.carbon.meta.enun.ItemValueType;
import cho.carbon.meta.enun.StrucElementType;
import cho.carbon.meta.mapping.ValueTypeMapping;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.page.PageInfo;

@Controller
@RequestMapping(AdminConstants.URI_BASE + "/structBase")
public class StrucBaseController {
	
	@Resource
	CommService commService;
	
	@Resource
	StrucBaseService strucBaseService;
	
	Logger logger = Logger.getLogger(StrucBaseController.class);
	@org.springframework.web.bind.annotation.InitBinder
	public void InitBinder(ServletRequestDataBinder binder) {
		System.out.println("执行了InitBinder方法");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}
	
	/**
	 * jsp 页面 分页 显示实体列表
	 * 
	 * @param pageInfo
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(StrucBase strucBase, PageInfo pageInfo) {
		try {
			List<StrucBase> strucBaseList =  strucBaseService.queryList(strucBase, pageInfo);
			 
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("strucBaseList", strucBaseList);
			modelAndView.addObject("strucBase", strucBase);
			modelAndView.addObject("pageInfo", pageInfo);
			modelAndView.setViewName(AdminConstants.JSP_BASE + "/struct/list.jsp");
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	  /**
     * 	根据父sbPid, 获取下一阶梯的孩子
     * @param sbPid
     * @return
     */
    @ResponseBody
	@RequestMapping("/getStructStairChild")
	public String getStructStairChild(Integer sbPid){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
			List<StrucBase> strucBaseList = strucBaseService.getStructStairChild(sbPid);
	    	map.put("strucBaseList", strucBaseList);
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
   	@RequestMapping("/delStruct")
   	public String delStruct(Integer sbId){
   		Map<String, Object> map = new HashMap<String, Object>();
   		JSONObject jobj = new JSONObject(map);
   		try {
   			
   			List<StrucBase> structStairChild = strucBaseService.getStructStairChild(sbId);
   			
   			if (!structStairChild.isEmpty()) {
   				map.put("code", 400);
   	   			map.put("msg", "请先删除孩子！");
   	   			return jobj.toString();
   			}
   			
   			strucBaseService.deleteStruct(sbId);
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
	 * 	跳转到结构体添加页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/addStruct")
	public String add(Model model) {
		return AdminConstants.JSP_BASE + "/struct/addStruct.jsp";
	}
	
	/**
	 * 	跳转到结构体修改页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/editStruct")
	public String add(Model model, Integer sbId) {
		
		StrucBase strucBase = commService.get(StrucBase.class, sbId);
		StrucMiCode strucMiCode = commService.get(StrucMiCode.class, sbId);
		
		ModelItem modelItem = commService.get(ModelItem.class, strucMiCode.getItemCode());
		
		
		model.addAttribute("strucBase", strucBase);
		model.addAttribute("modelItem", modelItem);
		return AdminConstants.JSP_BASE + "/struct/editStruct.jsp";
	}
	
	/**
	 *	 本条件只创建结构体， type=1
	 * @param strucBaseContainer
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createStrucBase")
	public AjaxPageResponse createModelItem(StrucBaseContainer strucBaseContainer) {
		try {
			strucBaseContainer.getStrucBase().setType(StrucElementType.STRUC.getCode());
			strucBaseService.saveOrUpdate(strucBaseContainer);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("添加成功", "strucBase_list");
		} catch (Exception e) {
			logger.error("添加失败", e);
			return AjaxPageResponse.FAILD("添加失败");
		}
	}
	
	@ResponseBody
	@RequestMapping("/createStrucBaseAttrField")
	public String createModelItemAttr(StrucBaseContainer strucBaseContainer){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
			StrucBaseContainer strucBaseC = strucBaseService.saveOrUpdate(strucBaseContainer);
			map.put("strucBase", strucBaseC.getStrucBase());
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
	 * 跳转到配置子结构页面
	 * @param itemCode
	 * @return
	 */
	  @RequestMapping(value = "/structTree")
		public ModelAndView structTree(Integer sbId) {
			ModelAndView mv = new ModelAndView();
			StrucBase strucBase = commService.get(StrucBase.class, sbId);
			StrucMiCode strucMiCode = commService.get(StrucMiCode.class, sbId);
			ModelItem modelItem = commService.get(ModelItem.class, strucMiCode.getItemCode());
			
			mv.addObject("strucBase", strucBase);
			mv.addObject("strucMiCode", strucMiCode);
			mv.addObject("modelItem", modelItem);
			mv.setViewName(AdminConstants.JSP_BASE + "/struct/structTree.jsp");
			return mv;
		}
	
	
	 /**
	  * 
	  * @param sbId   strucBase 的id
	  * @param structType StrucElementType中的类型
	  * @param sbPid   strucBase父id
	  * @param model
	  * @return
	  */
	    @RequestMapping("/addAttrField")
		public String addAttrField(Integer sbId, Integer structType,String sbPid,  Model model) {
	    	// 根据 type 类型， 获取本类型到底需要哪些属性以及默认属性
	    	
	    	model.addAttribute("sbId", sbId);
	    	model.addAttribute("structType", structType);
	    	model.addAttribute("sbPid", sbPid);
			return AdminConstants.JSP_BASE + "/struct/addAttrField.jsp";
		}
	    
	    /**
	     * structType 根据属性获取必填的属性
	     * @param sbId
	     * @param structType
	     * @param sbPid
	     * @return
	     */
	    @ResponseBody
		@RequestMapping("/getDefaultAttrByMType")
		public String getDefaultAttrByMType(Integer sbId, Integer structType,Integer sbPid){
			Map<String, Object> map = new HashMap<String, Object>();
			JSONObject jobj = new JSONObject(map);
			try {
				
				List<ViewLabel> viewLabelList = strucBaseService.getDefaultAttrByMType(sbId, StrucElementType.getType(structType), sbPid);
				
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
	    
	    @ResponseBody
		@RequestMapping("/getValueType")
		public String getValueType(String miCode){
			Map<String, Object> map = new HashMap<String, Object>();
			JSONObject jobj = new JSONObject(map);
			try {
				
				MiValue miValue = commService.get(MiValue.class, miCode);
				ItemValueType itemType = ItemValueType.STRING;
				if (miValue != null) {
					itemType = ItemValueType.getValueType(Integer.parseInt(miValue.getDataType()));
				}
					
				Map<String, Object> valueTypeMap = new HashMap<String, Object>();
				Collection<AttributeValueType> valueTypeCo = ValueTypeMapping.getCanTransType(itemType);
			
			  for (AttributeValueType attrValueType : valueTypeCo) {
				  valueTypeMap.put(attrValueType.getIndex() + "", attrValueType.getCName()); 
			  }
				
				map.put("valueTypeMap", valueTypeMap);
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
	     * 	根据 sbId 获取strucMiCode
	     * @param sbId
	     * @return
	     */
	    @ResponseBody
		@RequestMapping("/getStrucMiCode")
		public String getStrucMiCode(Integer sbId){
			Map<String, Object> map = new HashMap<String, Object>();
			JSONObject jobj = new JSONObject(map);
			try {
				StrucMiCode strucMiCode = commService.get(StrucMiCode.class, sbId);
				
				map.put("strucMiCode", strucMiCode);
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
	     * 快速生成结构体
	     * @return
	     */
	    @ResponseBody
		@RequestMapping(value = "/quickCreateStrucBase")
		public AjaxPageResponse quickCreateStrucBase(String belongModel) {
			try {
				
				strucBaseService.createStrucBaseQuick(belongModel);
				
				return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("操作成功", "modelitem_list");
			} catch (Exception e) {
				logger.error("操作失败", e);
				return AjaxPageResponse.FAILD("操作失败");
			}
		}
	    
}
