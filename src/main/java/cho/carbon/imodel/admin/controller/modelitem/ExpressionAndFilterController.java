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
import com.alibaba.fastjson.serializer.SerializerFeature;

import cho.carbon.imodel.admin.controller.AdminConstants;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.Constants;
import cho.carbon.imodel.model.modelitem.pojo.MiCalExpress;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterCriterion;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterGroup;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterRgroup;
import cho.carbon.imodel.model.modelitem.pojo.MiModelStat;
import cho.carbon.imodel.model.modelitem.pojo.MiReference;
import cho.carbon.imodel.model.modelitem.pojo.MiStatDimension;
import cho.carbon.imodel.model.modelitem.pojo.MiStatFact;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.pojo.ModelRelationType;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.service.ModelRelationTypeService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;
import cho.carbon.meta.enun.ModelItemType;
import cho.carbon.meta.enun.RelationType;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.NoticeType;
import cn.sowell.copframe.dto.page.PageInfo;
import springfox.documentation.schema.configuration.ModelsConfiguration;

/**
 * 	表达式和过滤条件
 * @author so-well
 *
 */
@Controller
@RequestMapping(AdminConstants.URI_BASE + "/expressionAndFilter")
public class ExpressionAndFilterController {
	
	@Resource
	ModelItemService miService;
	
	@Resource
	CommService commService;
	
	Logger logger = Logger.getLogger(ExpressionAndFilterController.class);
	@org.springframework.web.bind.annotation.InitBinder
	public void InitBinder(ServletRequestDataBinder binder) {
		System.out.println("执行了InitBinder方法");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}
	
	/**
     *	 跳转到表达式页面
     * @param pmiCode
     * @return
     */
    @RequestMapping(value = "/skipExpression")
	public ModelAndView skipExpression(String miCode) {
		try {
			
			ModelItem modelItem = commService.get(ModelItem.class, miCode);
			ModelItemType itemType = ModelItemType.getItemType(modelItem.getType());
		
			String codeTxt = "";
			switch (itemType) {
			case DIMENSION_ITEM:
				//查询维度对应的表达式
				MiStatDimension miStatDimension = commService.get(MiStatDimension.class, miCode);
				if (miStatDimension != null) {
					MiCalExpress miCalExpress = commService.get(MiCalExpress.class, miStatDimension.getExpressId());
					codeTxt = miCalExpress == null ?"":miCalExpress.getCodeTxt();
				}
				break;
			case FACT_ITEM:
				 MiStatFact miStatFact = commService.get(MiStatFact.class, miCode);
				 if (miStatFact != null) {
						MiCalExpress miCalExpress = commService.get(MiCalExpress.class, miStatFact.getExpressId());
						codeTxt = miCalExpress == null ?"":miCalExpress.getCodeTxt();
					}
				break;
			}
			
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("miCode", miCode);
			modelAndView.addObject("codeTxt", codeTxt);
			
			modelAndView.setViewName(AdminConstants.JSP_BASE + "/expresAndFilter/expression.jsp");
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
   /**
    * 	保存表达式
    * @param codeTxt   表达式
    * @param modelItemCode  code 
    * @return
    */
    @ResponseBody
	@RequestMapping(value = "/saveExpress")
	public AjaxPageResponse saveExpress(String codeTxt, String modelItemCode) {
		try {
			//先获取数据吧
			miService.saveExpress(codeTxt, modelItemCode);
			
			return AjaxPageResponse.REFRESH_LOCAL("操作成功");
		} catch (Exception e) {
			logger.error("操作失败", e);
			return AjaxPageResponse.FAILD("操作失败");
		}
	}
    
   /**
    * 跳转到过滤条件页面
    * @param miCode  
    * @param type 0.统计实体   1.事实属性 2. 计算属性 3. 配置文件
    * @return
    */
    @RequestMapping(value = "/skipFilter")
	public ModelAndView skipFilter(String miCode, Integer type) {
		try {
			ModelAndView modelAndView = new ModelAndView();
			//过滤条件id
			Integer filterId = null;
			String belongModel = null;
			
			if (type == 1) {
				// 获取事实属性
				MiStatFact miStatFact = commService.get(MiStatFact.class, miCode);
				filterId = miStatFact.getFilterId();
				
				//获取事实属性， 对应的实体
				ModelItem modelItem = commService.get(ModelItem.class, miCode);
				MiModelStat miModelStat = commService.get(MiModelStat.class, modelItem.getBelongModel());
				
				belongModel = miModelStat.getSourceCode();
			} else if(type == 0) {
				//统计实体过滤条件
				MiModelStat miModelStat = commService.get(MiModelStat.class, miCode);
				filterId = miModelStat.getFilterId();
				belongModel = miModelStat.getSourceCode();
			}
			
			modelAndView.addObject("miCode", miCode);
			modelAndView.addObject("belongModel", belongModel);
			modelAndView.addObject("filterId", filterId);
			modelAndView.addObject("type", type);
			modelAndView.setViewName(AdminConstants.JSP_BASE + "/expresAndFilter/filters/filterTree.jsp");
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    /**
     * 保存过滤条件
     * @param miFilterGroup
     * @return
     */
    @ResponseBody
   	@RequestMapping("/saveFilter")
   	public String saveFilter(String miCode, Integer type, Integer filterId){
   		Map<String, Object> map = new HashMap<String, Object>();
   		JSONObject jobj = new JSONObject(map);
   		try {
   			miService.saveFilter(miCode, type, filterId);
   			
   			
   			
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
     * 	保存过滤条件普通组
     * @param 
     * @return
     */
    @ResponseBody
	@RequestMapping("/createFilterCommGroup")
	public String createFilterCommGroup(MiFilterGroup miFilterGroup){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
			
			miService.saveCommFilterGroup(miFilterGroup);
			
			map.put("miFilterGroup", miFilterGroup);
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
   	@RequestMapping("/createFilterCriterion")
   	public String createFilterCriterion(MiFilterCriterion miFilterCriterion){
   		Map<String, Object> map = new HashMap<String, Object>();
   		JSONObject jobj = new JSONObject(map);
   		try {
   			
   			miService.saveMiFilterCriterion(miFilterCriterion);
   			
   			map.put("miFilterCriterion", miFilterCriterion);
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
	@RequestMapping("/getMiFilterGroup")
	public String getMiFilterGroup(Integer filterId){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
			MiFilterGroup miFilterGroup = commService.get(MiFilterGroup.class, filterId);
			map.put("miFilterGroup", miFilterGroup);
			map.put("code", 200);
			map.put("msg", "成功！");
			return jobj.toJSONString();
		} catch (Exception e) {
			logger.error("添加失败", e);
			e.printStackTrace();
			map.put("code", 400);
			map.put("msg", "操作失败！");
			return jobj.toString();
		}
	}
    
    @ResponseBody
	@RequestMapping("/getMiFilterRgroup")
	public String getMiFilterRgroup(String filterId){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
			MiFilterRgroup miFilterRgroup = commService.get(MiFilterRgroup.class, filterId);
			map.put("miFilterRgroup", miFilterRgroup);
			map.put("code", 200);
			map.put("msg", "成功！");
			return jobj.toJSONString();
		} catch (Exception e) {
			logger.error("添加失败", e);
			e.printStackTrace();
			map.put("code", 400);
			map.put("msg", "操作失败！");
			return jobj.toString();
		}
	}
    
    
    /**
     * 	获取分组的孩子， 表达式
     * @param id
     * @return
     */
    @ResponseBody
	@RequestMapping("/getMiFiltergroupChild")
	public String getMiFiltergroupChild(Integer groupId){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
		
			List<MiFilterCriterion> miFilterCriterionList = miService.getMiFiltergroupChild(groupId);
			
			map.put("miFilterCriterionList", miFilterCriterionList);
			map.put("code", 200);
			map.put("msg", "成功！");
			return jobj.toJSONString();
		} catch (Exception e) {
			logger.error("添加失败", e);
			e.printStackTrace();
			map.put("code", 400);
			map.put("msg", "操作失败！");
			return jobj.toString();
		}
	}
    
    
    /**
     * 删除普通组
     * @param id
     * @return
     */
    @ResponseBody
	@RequestMapping("/delMiFiltergroup")
	public String delMiFiltergroup(Integer groupId){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
		
			List<MiFilterCriterion> miFilterCriterionList = miService.getMiFiltergroupChild(groupId);
			
			if (!miFilterCriterionList.isEmpty()) {
				map.put("code", 400);
				map.put("msg", "请先删除孩子！");
				return jobj.toJSONString();
			}
			
			MiFilterGroup group = new MiFilterGroup();
			group.setId(groupId);
			commService.delete(group);
			
			map.put("code", 200);
			map.put("msg", "成功！");
			return jobj.toJSONString();
		} catch (Exception e) {
			logger.error("添加失败", e);
			e.printStackTrace();
			map.put("code", 400);
			map.put("msg", "操作失败！");
			return jobj.toString();
		}
	}
    
    
    /**
     * 删除表达式
     * @param id
     * @return
     */
    @ResponseBody
	@RequestMapping("/delMiFilterCriterion")
	public String delMiFilterCriterion(Integer id){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jobj = new JSONObject(map);
		try {
		
			MiFilterCriterion criterion = new MiFilterCriterion();
			criterion.setId(id);
			commService.delete(criterion);
			
			map.put("code", 200);
			map.put("msg", "成功！");
			return jobj.toJSONString();
		} catch (Exception e) {
			logger.error("添加失败", e);
			e.printStackTrace();
			map.put("code", 400);
			map.put("msg", "操作失败！");
			return jobj.toString();
		}
	}
    
	
}
