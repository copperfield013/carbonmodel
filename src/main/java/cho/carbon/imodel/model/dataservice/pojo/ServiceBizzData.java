package cho.carbon.imodel.model.dataservice.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_sb_service_bizzdata")
public class ServiceBizzData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "c_name")
	private String name;

	@Column(name = "c_ip")
	private String ip;

	@Column(name = "c_port")
	private String port;
	
	@Column(name="c_state")
	private String state;//状态值为1：正常使用， 2： 服务没启动
	
	@Column(name="c_describe")
	private String describe;
	

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIp() {
		return ip;
	}

	public String getPort() {
		return port;
	}

	public String getState() {
		return state;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
}