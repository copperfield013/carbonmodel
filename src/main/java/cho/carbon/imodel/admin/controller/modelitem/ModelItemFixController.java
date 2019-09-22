package cho.carbon.imodel.admin.controller.modelitem;


import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cho.carbon.imodel.admin.controller.AdminConstants;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.ModelItemFix;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;

@Controller
@RequestMapping(AdminConstants.URI_BASE + "/modelItemFix")
public class ModelItemFixController {
	
	@Resource
	CommService commService;
	
    @RequestMapping(value = "/list",
        method = RequestMethod.POST)
	public ModelAndView list(){
    	List modelItemFixList = (List) commService.excuteBySqlSelect("SELECT * FROM t_cc_model_item_fix");
    	
		ModelAndView mv = new ModelAndView();
		mv.addObject("modelItemFixList", modelItemFixList);
		mv.setViewName(AdminConstants.JSP_BASE + "/modelitem/miFix/list.jsp");
		return mv;
	}
	
	
	  @RequestMapping(value = "/add", method = RequestMethod.POST) 
	  public  ModelAndView add(){ 
		  ModelAndView mv = new ModelAndView();
		  mv.setViewName(AdminConstants.JSP_BASE + "/modelitem/miFix/add.jsp"); 
		  return  mv;
	  }
	 
	@ResponseBody
    @RequestMapping(value = "/do_add")
	public AjaxPageResponse doAdd(ModelItemFix modelItemFix){
		try {
			String prefix = modelItemFix.getPrefix();
			String str = "^[a-zA-Z]+$";
			boolean matches = Pattern.matches(str, prefix);
			if (!matches || prefix.length()>6) {
				return AjaxPageResponse.FAILD("前缀只能是英文字母, 并且字母数量小于等于6");
			}
			
			modelItemFix.setPrefix(prefix.toUpperCase());
			commService.insert(modelItemFix);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("添加成功", "modelItemFix_list");
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxPageResponse.FAILD("添加失败");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/do_delete", method = RequestMethod.POST)
	public AjaxPageResponse doDelte(Integer id){
		try {
			ModelItemFix modelItemFix = new ModelItemFix();
			modelItemFix.setId(id);
			commService.delete(modelItemFix);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxPageResponse.FAILD("删除失败");
		}
	}

	@RequestMapping(value="/edit") 
	  public ModelAndView edit(Integer id){ 
		 ModelItemFix modelItemFix = commService.get(ModelItemFix.class, id);
		  ModelAndView mv =	  new ModelAndView();
		  mv.addObject("modelItemFix", modelItemFix); 
		  mv.setViewName(AdminConstants.JSP_BASE + "/modelitem/miFix/edit.jsp"); 
		  return mv;
	  }
	 
	
	@ResponseBody
    @RequestMapping(value = "/do_edit",method = RequestMethod.POST)
	public AjaxPageResponse do_edit(ModelItemFix modelItemFix){
		try {
			
			String prefix = modelItemFix.getPrefix();
			String str = "^[a-zA-Z]+$";
			boolean matches = Pattern.matches(str, prefix);
			if (!matches || prefix.length()>6) {
				return AjaxPageResponse.FAILD("前缀只能是英文字母, 并且字母数量小于等于6");
			}
			
			modelItemFix.setPrefix(prefix.toUpperCase());
			commService.update(modelItemFix);
		return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("编辑成功", "modelItemFix_list");
	} catch (Exception e) {
		return AjaxPageResponse.FAILD("编辑失败");
	}
		
	}
}
	
