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

import cho.carbon.imodel.admin.controller.AdminConstants;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.Constants;
import cho.carbon.imodel.model.modelitem.pojo.MiCalExpress;
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
     *	 跳转到过滤条件页面
     * @param pmiCode
     * @return
     */
    @RequestMapping(value = "/skipFilter")
	public ModelAndView skipFilter(String miCode) {
		try {
			
			ModelAndView modelAndView = new ModelAndView();
			
			modelAndView.setViewName(AdminConstants.JSP_BASE + "/expresAndFilter/expression.jsp");
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
