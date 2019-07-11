package cho.carbon.imodel.model.modelitem.vo;

import cho.carbon.imodel.model.modelitem.pojo.MiFilterGroup;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterRgroup;

/**
 * 过滤条件合
 * @author so-well
 *
 */
public class MiFilterContainer {
	
	private MiFilterGroup miFilterGroup;

	private MiFilterRgroup miFilterRgroup;

	public MiFilterGroup getMiFilterGroup() {
		return miFilterGroup;
	}

	public void setMiFilterGroup(MiFilterGroup miFilterGroup) {
		this.miFilterGroup = miFilterGroup;
	}

	public MiFilterRgroup getMiFilterRgroup() {
		return miFilterRgroup;
	}

	public void setMiFilterRgroup(MiFilterRgroup miFilterRgroup) {
		this.miFilterRgroup = miFilterRgroup;
	}
	
}
