package cho.carbon.imodel.admin.controller.module;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import cho.carbon.imodel.admin.controller.AdminConstants;
import cho.carbon.imodel.model.struct.service.StrucBaseService;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.entityResolver.config.ModuleConfigureMediator;
import cn.sowell.datacenter.entityResolver.config.abst.Module;
import cn.sowell.datacenter.entityResolver.config.param.CreateModuleParam;
import cn.sowell.datacenter.entityResolver.config.param.QueryModuleCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping(AdminConstants.URI_MODULE + "/configModule")
public class ConfigModuleController {
    
	@Resource
	ModuleConfigureMediator dBModuleConfigMediator;
	
	@Resource
	StrucBaseService strucBaseService;
	
	@Resource
	SessionFactory sFactory;
	
    @RequestMapping(value = "/list",
        method = RequestMethod.POST)
	public ModelAndView list(QueryModuleCriteria criteria, PageInfo pageInfo){
		List<Module> list = dBModuleConfigMediator.queryModules(criteria);
		ModelAndView mv = new ModelAndView();
		mv.addObject("list", list);
		mv.addObject("criteria", criteria);
		mv.addObject("pageInfo", pageInfo);
		mv.setViewName(AdminConstants.JSP_MODULE + "/configModule/list.jsp");
		return mv;
	}
	
	/*
	 * @RequestMapping(value = "/add", method = RequestMethod.POST) public
	 * ModelAndView add(){ // 获取所有的配置文件 List<BasicItemNode> abcList =
	 * strucBaseService.getAllAbc(); List<BasicItemNode> childNode =
	 * strucBaseService.getAttribute(String.valueOf(abcList.get(0).getId()));
	 * ModelAndView mv = new ModelAndView(); mv.addObject("abcList", abcList);
	 * mv.addObject("childNode", childNode);
	 * mv.setViewName(AdminConstants.JSP_MODULE + "/configModule/add.jsp"); return
	 * mv; }
	 */
	
	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(value = "/childNodeList", method = RequestMethod.POST) public
	 * ResponseEntity<BasicItemNodes> childNodeList(String parentId){ try {
	 * List<BasicItemNode> childNodeList = strucBaseService.getAttribute(parentId);
	 * Map<String, Object> map = new HashMap<String, Object>(); BasicItemNodes btn =
	 * new BasicItemNodes(); btn.childNode(childNodeList); return new
	 * ResponseEntity<BasicItemNodes>(btn, HttpStatus.OK); } catch (Exception e) {
	 * return new ResponseEntity<BasicItemNodes>(HttpStatus.INTERNAL_SERVER_ERROR);
	 * } }
	 */
	
	@ResponseBody
    @RequestMapping(value = "/do_add")
	public AjaxPageResponse doAdd(String moduleName, String moduleTitle, Long mappingId, String codeName, String titleName){
		try {
				
			if (codeName == null) {
				codeName="";
			}
			
			if (titleName == null) {
				titleName="";
			}
			
			CreateModuleParam param = new CreateModuleParam(moduleTitle, mappingId);
			param.setModuleName(moduleName);
			param.setCodeName(codeName);
			param.setTitleName(titleName);
			dBModuleConfigMediator.createModule(param);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("添加成功", "configModule_list");
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxPageResponse.FAILD("添加失败");
		}
	}
	
	@ResponseBody
    @RequestMapping(value = "/do_delete",
        method = RequestMethod.POST)
	public AjaxPageResponse doDelte(String name){
		try {
			dBModuleConfigMediator.removeModule(name);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
	//禁用启用
	@ResponseBody
    @RequestMapping(value = "/disabled",
        method = RequestMethod.POST)
	public AjaxPageResponse disabled(String name, String endisabled){
		try {
			if ("true".equals(endisabled)) {//disabled 为true， 说明为启用状态
				dBModuleConfigMediator.disableModule(name);
			} else {
				dBModuleConfigMediator.enableModule(name);
			}
			return AjaxPageResponse.REFRESH_LOCAL("操作成功");
		} catch (Exception e) {
			return AjaxPageResponse.FAILD("操作失败");
		}
	}
	
	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(value = "/edit", method = RequestMethod.POST) public
	 * ModelAndView edit(String name){ Module module =
	 * dBModuleConfigMediator.getModule(name);
	 * 
	 * BasicItemNode abc = strucBaseService.getAbc(module.getMappingId());
	 * List<BasicItemNode> childNode =
	 * strucBaseService.getAttribute(String.valueOf(abc.getId())); ModelAndView mv =
	 * new ModelAndView(); mv.addObject("module", module); mv.addObject("childNode",
	 * childNode); mv.addObject("abc", abc);
	 * mv.setViewName(AdminConstants.JSP_MODULE + "/configModule/edit.jsp"); return
	 * mv; }
	 */
	
	@ResponseBody
    @RequestMapping(value = "/do_edit",
        method = RequestMethod.POST)
	public AjaxPageResponse do_edit(String moduleName, String moduleTitle, String mappingName, String codeName, String titleName){
		try {
			//dBModuleConfigMediator.updateModulePropertyName(moduleName, codeName, titleName);
			dBModuleConfigMediator.updateModule(moduleName, moduleTitle, codeName, titleName);
		return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("编辑成功", "configModule_list");
	} catch (Exception e) {
		return AjaxPageResponse.FAILD("编辑失败");
	}
		
	}
	
}
	
