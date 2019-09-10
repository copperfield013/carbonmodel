package cho.carbon.imodel.model.modelitem.vo;

import cho.carbon.imodel.model.modelitem.pojo.MiCalculated;
import cho.carbon.imodel.model.modelitem.pojo.MiEnum;
import cho.carbon.imodel.model.modelitem.pojo.MiModel;
import cho.carbon.imodel.model.modelitem.pojo.MiModelStat;
import cho.carbon.imodel.model.modelitem.pojo.MiReference;
import cho.carbon.imodel.model.modelitem.pojo.MiStatFact;
import cho.carbon.imodel.model.modelitem.pojo.MiTowlevel;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;

/**
 * modelItem 集合
 * @author so-well
 *
 */
public class ModelItemContainer {
	
	private ModelItem modelItem;
	private MiValue miValue;
	private MiEnum miEnum;
	private MiReference miReference;
	private MiTowlevel miTowlevel;
	private MiModel miModel;
	private MiStatFact miStatFact;
	private MiCalculated miCalculated;
	
	//统计实体
	private MiModelStat miModelStat;

	public ModelItem getModelItem() {
		return modelItem;
	}

	public void setModelItem(ModelItem modelItem) {
		this.modelItem = modelItem;
	}

	public MiValue getMiValue() {
		return miValue;
	}

	public void setMiValue(MiValue miValue) {
		this.miValue = miValue;
	}

	public MiEnum getMiEnum() {
		return miEnum;
	}

	public void setMiEnum(MiEnum miEnum) {
		this.miEnum = miEnum;
	}

	public MiReference getMiReference() {
		return miReference;
	}

	public void setMiReference(MiReference miReference) {
		this.miReference = miReference;
	}

	public MiTowlevel getMiTowlevel() {
		return miTowlevel;
	}

	public void setMiTowlevel(MiTowlevel miTowlevel) {
		this.miTowlevel = miTowlevel;
	}

	public MiModelStat getMiModelStat() {
		return miModelStat;
	}

	public void setMiModelStat(MiModelStat miModelStat) {
		this.miModelStat = miModelStat;
	}

	public MiModel getMiModel() {
		return miModel;
	}

	public void setMiModel(MiModel miModel) {
		this.miModel = miModel;
	}

	public MiStatFact getMiStatFact() {
		return miStatFact;
	}

	public void setMiStatFact(MiStatFact miStatFact) {
		this.miStatFact = miStatFact;
	}

	public MiCalculated getMiCalculated() {
		return miCalculated;
	}

	public void setMiCalculated(MiCalculated miCalculated) {
		this.miCalculated = miCalculated;
	}
	
}
