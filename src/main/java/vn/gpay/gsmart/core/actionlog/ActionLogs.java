package vn.gpay.gsmart.core.actionlog;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
import javax.persistence.Table;
//import javax.persistence.Transient;

@Table(name="actionlogs")
@Entity
public class ActionLogs implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Column(name ="orgid_link")
    private Long orgid_link ;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link ;
	
	@Column(name ="userid_link")
    private Long userid_link ;
	
	@Column(name ="porderid_link")
    private Long porderid_link ;
	
	@Column(name ="ordercode", length =10)
    private String ordercode ;
	
	@Column(name ="action_time")
    private Date action_time ;
	
	@Column(name ="action_ip", length =50)
    private String action_ip ;
	
	@Column(name ="action_task", length =50)
    private String action_task ;
	
	@Column(name ="action_content", length =2000)
    private String action_content ;
	
	@Column(name ="response_time")
    private Date response_time ;

	@Column(name ="response_status")
    private Integer response_status ;
	
	@Column(name ="response_msg", length =100)
    private String response_msg ;	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgid_link() {
		return orgid_link;
	}

	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public Long getUserid_link() {
		return userid_link;
	}

	public void setUserid_link(Long userid_link) {
		this.userid_link = userid_link;
	}

	public Date getAction_time() {
		return action_time;
	}

	public void setAction_time(Date action_time) {
		this.action_time = action_time;
	}

	public String getAction_ip() {
		return action_ip;
	}

	public void setAction_ip(String action_ip) {
		this.action_ip = action_ip;
	}

	public String getAction_task() {
		return action_task;
	}

	public void setAction_task(String action_task) {
		this.action_task = action_task;
	}

	public String getAction_content() {
		return action_content;
	}

	public void setAction_content(String action_content) {
		this.action_content = action_content;
	}

	public Date getResponse_time() {
		return response_time;
	}

	public void setResponse_time(Date response_time) {
		this.response_time = response_time;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getResponse_status() {
		return response_status;
	}

	public void setResponse_status(Integer response_status) {
		this.response_status = response_status;
	}

	public String getResponse_msg() {
		return response_msg;
	}

	public void setResponse_msg(String response_msg) {
		this.response_msg = response_msg;
	}

	public String getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public Long getPorderid_link() {
		return porderid_link;
	}

	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	
	
}
