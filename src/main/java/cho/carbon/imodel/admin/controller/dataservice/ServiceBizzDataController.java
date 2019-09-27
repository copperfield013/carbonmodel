package cho.carbon.imodel.admin.controller.dataservice;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cho.carbon.hc.copframe.dto.ajax.AjaxPageResponse;
import cho.carbon.hc.copframe.dto.ajax.NoticeType;
import cho.carbon.hc.copframe.dto.page.PageInfo;
import cho.carbon.imodel.admin.controller.AdminConstants;
import cho.carbon.imodel.model.dataservice.criteria.ServiceBizzDataCriteria;
import cho.carbon.imodel.model.dataservice.pojo.ServiceBizzData;
import cho.carbon.imodel.model.dataservice.service.ServiceBizzDataService;
import cho.carbon.imodel.utils.WebServiceUtil;

@Controller
@RequestMapping(AdminConstants.URI_DATASERVICE + "/serviceBizzData")
public class ServiceBizzDataController {
    
	@Resource
	ServiceBizzDataService sBizzDataService;
	
    @RequestMapping(value = "/list",
        method = RequestMethod.POST)
	public ModelAndView list(ServiceBizzDataCriteria criteria, PageInfo pageInfo){
		 List<ServiceBizzData> list = sBizzDataService.queryList(criteria, pageInfo);
		ModelAndView mv = new ModelAndView();
		mv.addObject("list", list);
		mv.addObject("criteria", criteria);
		mv.addObject("pageInfo", pageInfo);
		mv.setViewName(AdminConstants.JSP_DATASERVICE + "/serviceBizzData/list.jsp");
		return mv;
	}
	
    @RequestMapping(value = "/add")
	public ModelAndView add(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName(AdminConstants.JSP_DATASERVICE + "/serviceBizzData/add.jsp");
		return mv;
	}
	 
	@ResponseBody
    @RequestMapping(value = "/do_add")
	public AjaxPageResponse doAdd(ServiceBizzData sd){
		try {
			sBizzDataService.insert(sd);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("添加成功", "serviceBizzData_list");
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxPageResponse.FAILD("添加失败");
		}
	}
	
	@ResponseBody
    @RequestMapping(value = "/do_delete",
        method = RequestMethod.POST)
	public AjaxPageResponse doDelte(Integer id){
		try {
			sBizzDataService.delete(id);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
	//禁用启用
	@ResponseBody
    @RequestMapping(value = "/testService",
        method = RequestMethod.POST)
	public AjaxPageResponse testService(Integer id){
		try {
			ServiceBizzData serviceBizzData = sBizzDataService.getOne(id);
			//检测ip是否通过
			String detectionIp = detectionIp(serviceBizzData);
			
			if ("200".equals(detectionIp)) {
				serviceBizzData.setState("1");
				sBizzDataService.update(serviceBizzData);
				return AjaxPageResponse.REFRESH_LOCAL("测试通过");
			} else {
				serviceBizzData.setState("2");
				sBizzDataService.update(serviceBizzData);
				return AjaxPageResponse.REFRESH_LOCAL_BY_TYPE("测试不通过", NoticeType.INFO);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxPageResponse.FAILD("操作失败");
		}
	}

	/**
	 * 组装uri
	 * @param serviceBizzData
	 * @return
	 */
	protected String buildWSURI(ServiceBizzData serviceBizzData) {
		return "http://"+serviceBizzData.getIp()+":"+serviceBizzData.getPort()+"/"+serviceBizzData.getName()+"/services/";
	}
	
	/**
	 * 组装url
	 * @param serviceBizzData
	 * @param interMethod
	 * @return
	 */
	protected String buildWSURL(ServiceBizzData serviceBizzData, String interMethod) {
		String buildWSURI = buildWSURI(serviceBizzData);
		return buildWSURI + interMethod + "?wsdl";
	}
	
	/**
	 * 执行调用
	 * @param serviceBizzData
	 * @return
	 */
	private String executeWSURL(ServiceBizzData serviceBizzData) {
		String url = buildWSURL(serviceBizzData, "modelReLoadService");
		String wsdlResult = WebServiceUtil.getWsdlResult(url, "reload", null);
		
		String urlTwo = buildWSURL(serviceBizzData, "configReloadService");
		String dataResult = WebServiceUtil.getWsdlResult(urlTwo, "syncCache", null);

		if ("200".equals(wsdlResult) && "200".equals(dataResult)) {
			return "200";
		} else {
			return "400";
		}
	}
	
	/**
	 * 检测ip是否通过
	 */
	private String detectionIp(ServiceBizzData serviceBizzData){
		String url = buildWSURL(serviceBizzData, "modelReLoadService");
		String wsdlResult = WebServiceUtil.getWsdlResult(url);
		
		String urlTwo = buildWSURL(serviceBizzData, "configReloadService");
		String dataResult = WebServiceUtil.getWsdlResult(urlTwo);
		 
		if ("200".equals(wsdlResult) && "200".equals(dataResult)) {
			return "200";
		} else {
			return "400";
		}
	}
	
	@ResponseBody
    @RequestMapping(value = "/refreshERXmlDom",
        method = RequestMethod.POST)
	public AjaxPageResponse refreshERXmlDom(Integer id){
		try {
			ServiceBizzData serviceBizzData = sBizzDataService.getOne(id);
			
			/**
			 * 调用接口
			 */
			String executeWSURL = executeWSURL(serviceBizzData);
			
			if ("200".equals(executeWSURL)) {
				return AjaxPageResponse.REFRESH_LOCAL("刷新成功");
			} else {
				return AjaxPageResponse.FAILD("刷新失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxPageResponse.FAILD("刷新失败");
		}
	}
	
    @RequestMapping(value = "/edit")
	public ModelAndView edit(Integer id){
		ServiceBizzData serviceBizzData = sBizzDataService.getOne(id);
		ModelAndView mv = new ModelAndView();
		mv.addObject("serviceBizzData", serviceBizzData);
		mv.setViewName(AdminConstants.JSP_DATASERVICE + "/serviceBizzData/edit.jsp");
		return mv;
	}
	
	@ResponseBody
    @RequestMapping(value = "/do_edit",
        method = RequestMethod.POST)
	public AjaxPageResponse do_edit(ServiceBizzData sd){
		try {
			sBizzDataService.update(sd);
		return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("编辑成功", "serviceBizzData_list");
	} catch (Exception e) {
		e.printStackTrace();
		return AjaxPageResponse.FAILD("编辑失败");
	}
		
	}
	
}
	
