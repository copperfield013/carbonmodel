package cho.carbon.imodel.model.struct.strategy;

import cho.carbon.meta.enun.StrucElementType;

public class StructStrategyFactory {
	
	public static StructBaseStrategy getStructStrategy(StrucElementType strucElementType) {
		
		switch (strucElementType) {
		case STRUC:
		case GROUP2D:
			return new StructStrategy();
		case FIELD:
			return  new FieldStrategy();
		case ENUMFIELD:
			return new EnumFieldStrategy();
		case RFIELD:
			return new RfieldStrategy();
		case REFFIELD:
			return new RefFieldStrategy();
		case RSTRUC:
			return new RStructStrategy();
		case RREFFIELD:
			return new RrefFieldStrategy();
			
			
		}
		
		return null;
	}
	
}
