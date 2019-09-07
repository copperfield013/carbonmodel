package cho.carbon.imodel.model.modelitem.strategy;

import cho.carbon.meta.enun.ModelItemType;

/**
 * MiStrategy 策略工程
 * @author so-well
 *
 */
public class MiStrategyFactory {
	
	
	/**
	 * 
	 * @param modelItemType  本身数据类型
	 * 
	 * @return
	 */
	public static MiStrategy getMiStrategy(ModelItemType modelItemType, ModelItemType belongModelType) {
		// 这里需要根据， belongModelType 和modelItemType 选择具体的策略
		
		switch (modelItemType) {
		case MODEL:
		case SQL_MODEL:
			return new ModelItemMiStrategy();
		case STAT_MODEL:
			return new StatModelItemMiStrategy();
		case VALUE_ITEM:
		case CASCADE_REFERENCE_ITEM:
		case CALCULATED_ITEM:
			return new ValueItemMiStrategy();
		case FACT_ITEM:
			return new FactMiStrategy();
		case DIMENSION_ITEM:	
			return new DimensionMiStrategy();
		case DIMENSION_ENUM_ITEM:
			return new DimenEnumItemMiStrategy();
		case ENUM_ITEM:
		case PREENUM_STRING_ITEM:
			return new EnumItemMiStrategy();
		case CASCADE_ENUM_ITEM:
			return new CascadeEnumItemMiStrategy();
		case MULTI_ENUM_ITEM:
			return new MultiEnumItemMiStrategy();
		case FILE_ITEM:
			return new FileItemMiStrategy();
		case REFERENCE_ITEM:
			return new ReferenceItemMiStrategy();
		case MULTI_LINE_GROUP:
		case GIANT_LINE_GROUP:
			return new MultiLineGroupMiStrategy();
		case TWO_LEVEL_ITEM:
			return new TwoLevelAttrMiStrategy();
		case FACT_GROUP:
			return new FactGroupMiStrategy();
			
		default:
			break;
		}
		
		return null;
	}
}
