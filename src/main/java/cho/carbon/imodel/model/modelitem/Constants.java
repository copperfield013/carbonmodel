package cho.carbon.imodel.model.modelitem;

import java.util.HashMap;
import java.util.Map;

import cho.carbon.meta.enun.ModelItemType;

public interface Constants {
	
	public static final Integer USING_STATE_NORMAL = 0;//新增正常
	public static final Integer USING_STATE_PASTDUE = 2;//弃用
	public static final Integer USING_STATE_ERROR = -1;//有错误
	public static final Integer USING_STATE_USING = 1; //正在使用
	
	/**
	 * 元数据管理--数据类型
	 */
	/*
	 * Map<String, Object> DATA_TYPE_MAP = new HashMap<String, Object>(){ {
	 * put(String.valueOf(ValueType.STRING.getIndex()),
	 * ValueType.STRING.getCName()); put(String.valueOf(ValueType.INT.getIndex()),
	 * ValueType.INT.getCName()); put(String.valueOf(ValueType.NUMBER.getIndex()),
	 * ValueType.NUMBER.getCName()); put(String.valueOf(ValueType.DATE.getIndex()),
	 * ValueType.DATE.getCName());
	 * put(String.valueOf(ValueType.DATETIME.getIndex()),
	 * ValueType.DATETIME.getCName());
	 * put(String.valueOf(ValueType.RECORD.getIndex()),
	 * ValueType.RECORD.getCName());
	 * put(String.valueOf(ValueType.REPEAT.getIndex()),
	 * ValueType.REPEAT.getCName()); put(String.valueOf(ValueType.GROUP.getIndex()),
	 * ValueType.GROUP.getCName());
	 * put(String.valueOf(ValueType.ENUMTYPE.getIndex()),
	 * ValueType.ENUMTYPE.getCName());
	 * put(String.valueOf(ValueType.BYTES.getIndex()), ValueType.BYTES.getCName());
	 * put(String.valueOf(ValueType.PASSWORD.getIndex()),
	 * ValueType.PASSWORD.getCName());
	 * put(String.valueOf(ValueType.REFERENCE.getIndex()),
	 * ValueType.REFERENCE.getCName());
	 * put(String.valueOf(ValueType.CASCADETYPE.getIndex()),
	 * ValueType.CASCADETYPE.getCName());
	 * put(String.valueOf(ValueType.CASCADEREFTYPE.getIndex()),
	 * ValueType.CASCADEREFTYPE.getCName());
	 * put(String.valueOf(ValueType.STRING_PREENUM.getIndex()),
	 * ValueType.STRING_PREENUM.getCName());
	 * put(String.valueOf(ValueType.ENUMTYPE_MULTI.getIndex()),
	 * ValueType.ENUMTYPE_MULTI.getCName());
	 * 
	 * } };
	 */
	
	Map<String, Integer> USING_STATE_MAP = new HashMap<String, Integer>(){
		{
			put("normal", Constants.USING_STATE_NORMAL);//新增
			put("pastDue", Constants.USING_STATE_PASTDUE);//已过期
			put("error", Constants.USING_STATE_ERROR);//-1 有错误（执行 更新实体存储时报错）
			put("using", Constants.USING_STATE_USING);//1 在用 （成功执行 更新实体存储后）
		}
	};
	
	/**
	 * 
	 * @param itemType
	 * @return 实体返回true
	 */
	public static boolean isModel(ModelItemType itemType) {
		
		boolean bool = false;
		
		switch (itemType) {
			case MODEL:
			case STAT_MODEL:
			case SQL_MODEL:
				bool = true;
			break;
		}
		
		return bool;
	}
}
