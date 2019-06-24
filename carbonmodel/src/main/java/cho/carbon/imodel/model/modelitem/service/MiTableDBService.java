package cho.carbon.imodel.model.modelitem.service;

import java.util.List;

import com.abc.model.enun.ModelItemType;

import cho.carbon.imodel.model.cascadedict.pojo.CascadedictBasicItem;
import cho.carbon.imodel.model.modelitem.pojo.MiCascade;
import cho.carbon.imodel.model.modelitem.pojo.MiTwolevelMapping;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;
import cho.carbon.imodel.model.modelitem.vo.ViewLabel;
import cn.sowell.copframe.dto.page.PageInfo;

public interface MiTableDBService {

	
	public void createDBTable();
}
