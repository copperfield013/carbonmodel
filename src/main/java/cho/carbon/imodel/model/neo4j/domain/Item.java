package cho.carbon.imodel.model.neo4j.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIgnore;

@NodeEntity
public class Item {

    @Id @GeneratedValue
    private Long id;
    String code;
    String name;
    String type;

    /**
	 * 是否记录历史
	 * 0.否
	 * 1.是 
	 */
	 String needHistory;
	
	/**
	 * 是否需要缓存
	 * 0.否
	 * 1.是
	 */
	 String needCache;
	
	/**
	 * 是否记录运行日志
	 * 0.否
	 * 1.是
	 */
	 String needRunningLogger;
    
    @Relationship
    public Set<ItemRelation> relas;
   
   
   public void addItemRelation(ItemRelation re) {
       if (relas == null) {
       	relas = new HashSet<ItemRelation>();
       }
       relas.add(re);
   }
    
	public Item() {}

	public Item(String code, String name, String type, String needHistory, String needCache, String needRunningLogger) {
		super();
		this.code = code;
		this.name = name;
		this.type = type;
		this.needHistory = "1".equals(needHistory)?"是":"0".equals(needHistory)?"否":needHistory;
		this.needCache ="1".equals(needCache)?"是":"0".equals(needCache)?"否":needCache;
		this.needRunningLogger = "1".equals(needRunningLogger)?"是":"0".equals(needRunningLogger)?"否":needRunningLogger;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNeedHistory() {
		return needHistory;
	}

	public void setNeedHistory(String needHistory) {
		this.needHistory = needHistory;
	}

	public String getNeedCache() {
		return needCache;
	}

	public void setNeedCache(String needCache) {
		this.needCache = needCache;
	}

	public String getNeedRunningLogger() {
		return needRunningLogger;
	}

	public void setNeedRunningLogger(String needRunningLogger) {
		this.needRunningLogger = needRunningLogger;
	}
	
}