package cho.carbon.imodel.model.modelitem.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_cc_mi_model")
public class MiModel {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "code")
	private String code;

	/**
	 * 是否记录历史
	 * 0.否
	 * 1.是 
	 */
	@Column(name = "need_history")
	private Integer needHistory;
	
	/**
	 * 是否需要缓存
	 * 0.否
	 * 1.是
	 */
	@Column(name = "need_cache")
	private Integer needCache;
	
	/**
	 * 是否记录运行日志
	 * 0.否
	 * 1.是
	 */
	@Column(name="need_running_logger")
	private Integer needRunningLogger;
	
	public MiModel() {}
	
	public MiModel(String code, Integer needHistory, Integer needCache, Integer needRunningLogger) {
		super();
		this.code = code;
		this.needHistory = needHistory;
		this.needCache = needCache;
		this.needRunningLogger = needRunningLogger;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getNeedHistory() {
		return needHistory;
	}

	public void setNeedHistory(Integer needHistory) {
		this.needHistory = needHistory;
	}

	public Integer getNeedCache() {
		return needCache;
	}

	public void setNeedCache(Integer needCache) {
		this.needCache = needCache;
	}

	public Integer getNeedRunningLogger() {
		return needRunningLogger;
	}

	public void setNeedRunningLogger(Integer needRunningLogger) {
		this.needRunningLogger = needRunningLogger;
	}
	
}
