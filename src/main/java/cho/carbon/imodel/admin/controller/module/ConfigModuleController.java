package cho.carbon.imodel.admin.controller.module;


import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cho.carbon.imodel.admin.controller.AdminConstants;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.struct.pojo.StrucBase;
import cho.carbon.imodel.model.struct.service.StrucBaseService;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.entityResolver.config.ModuleConfigureMediator;
import cn.sowell.datacenter.entityResolver.config.abst.Module;
import cn.sowell.datacenter.entityResolver.config.param.CreateModuleParam;
import cn.sowell.datacenter.entityResolver.config.param.QueryModuleCriteria;

@Controller
@RequestMapping(AdminConstants.URI_MODULE + "/configModule")
public class ConfigModuleController {
    
	@Resource
	ModuleConfigureMediator dBModuleConfigMediator;
	
	@Resource
	StrucBaseService strucBaseService;
	
	@Resource
	CommService commService;
	
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
	
	
	  @RequestMapping(value = "/add", method = RequestMethod.POST) 
	  public  ModelAndView add(){ // 获取所有的配置文件 
		   List<StrucBase> strucList = strucBaseService.getAllStruc(null);
		   List<StrucBase> strucChildList = strucBaseService.getGroup1DChild(strucList.get(0).getId());
		  ModelAndView mv = new ModelAndView(); 
		  mv.addObject("strucList", strucList);
		  mv.addObject("strucChildList", strucChildList);
		  mv.setViewName(AdminConstants.JSP_MODULE + "/configModule/add.jsp"); 
		  return  mv;
	  }
	 
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
	
	
	  @ResponseBody
	  @RequestMapping(value = "/edit", method = RequestMethod.POST) 
	  public ModelAndView edit(String name){ 
		  Module module = dBModuleConfigMediator.getModule(name);
		  
		  StrucBase strucBase = commService.get(StrucBase.class, module.getMappingId().intValue());
		  List<StrucBase> strucChildList = strucBaseService.getGroup1DChild(strucBase.getId());
		  ModelAndView mv =	  new ModelAndView();
		  mv.addObject("module", module); 
		  mv.addObject("strucChildList", strucChildList); 
		  mv.addObject("strucBase", strucBase);
		  mv.setViewName(AdminConstants.JSP_MODULE + "/configModule/edit.jsp"); return
		  mv;
	  }
	 
	
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
	
