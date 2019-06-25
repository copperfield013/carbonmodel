package cho.carbon.imodel.model.modelitem.strategy;

import cho.carbon.meta.enun.ModelItemType;

/**
 * MiStrategy 策略工程
 * @author so-well
 *
 */
public class MiStrategyFactory {
	
	
	public static MiStrategy getMiStrategy(ModelItemType modelItemType) {
		
		switch (modelItemType) {
		case MODEL:
			return new ModelItemMiStrategy();
		case STAT_MODEL:
			return new StatModelItemMiStrategy();
		case VALUE_ITEM:
		case CASCADE_REFERENCE_ITEM:
			return new ValueItemMiStrategy();
		case FACT_ITEM:
		case DIMENSION_ITEM:	
			return new FactDimeMiStrategy();
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
			
		default:
			break;
		}
		
		return null;
	}
}
