package vn.gpay.gsmart.core.handover;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="handover")
@Entity
public class Handover implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "handover_generator")
	@SequenceGenerator(name="handover_generator", sequenceName = "handover_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgrootid_link")
	private Long orgrootid_link;
	
	@Column(name ="handovertypeid_link")
	private Long handovertypeid_link;
	
	@Column(name ="handover_code", length =50)
	private String handover_code;
	
	@Column(name ="handover_date")
	private Date handover_date;
	
	@Column(name ="handoverintypeid_link")
	private Long handoverintypeid_link;
	
	@Column(name ="porderid_link")
	private Long porderid_link;
	
	@Column(name ="pordergrantid_link")
	private Long pordergrantid_link;
	
	@Column(name ="handover_outid_link")
	private Long handover_outid_link;
	
	@Column(name ="orgid_from_link")
	private Long orgid_from_link;
	
	@Column(name ="orgid_to_link")
	private Long orgid_to_link;
	
	@Column(name ="handover_userid_link")
	private Long handover_userid_link;
	
	@Column(name ="receiver_userid_link")
	private Long receiver_userid_link;
	
	@Column(name ="approver_userid_link")
	private Long approver_userid_link;
	
	@Column(name ="totalpackage")
	private Integer totalpackage;
	
	@Column(name ="reason", length =200)
	private String reason;
	
	@Column(name ="extrainfo", length =200)
	private String extrainfo;
	
	@Column(name ="status")
	private Integer status;
	
	@Column(name ="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name ="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public Long getHandovertypeid_link() {
		return handovertypeid_link;
	}

	public void setHandovertypeid_link(Long handovertypeid_link) {
		this.handovertypeid_link = handovertypeid_link;
	}

	public String getHandover_code() {
		return handover_code;
	}

	public void setHandover_code(String handover_code) {
		this.handover_code = handover_code;
	}

	public Date getHandover_date() {
		return handover_date;
	}

	public void setHandover_date(Date handover_date) {
		this.handover_date = handover_date;
	}

	public Long getHandoverintypeid_link() {
		return handoverintypeid_link;
	}

	public void setHandoverintypeid_link(Long handoverintypeid_link) {
		this.handoverintypeid_link = handoverintypeid_link;
	}

	public Long getPorderid_link() {
		return porderid_link;
	}

	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}

	public Long getPordergrantid_link() {
		return pordergrantid_link;
	}

	public void setPordergrantid_link(Long pordergrantid_link) {
		this.pordergrantid_link = pordergrantid_link;
	}

	public Long getHandover_outid_link() {
		return handover_outid_link;
	}

	public void setHandover_outid_link(Long handover_outid_link) {
		this.handover_outid_link = handover_outid_link;
	}

	public Long getOrgid_from_link() {
		return orgid_from_link;
	}

	public void setOrgid_from_link(Long orgid_from_link) {
		this.orgid_from_link = orgid_from_link;
	}

	public Long getOrgid_to_link() {
		return orgid_to_link;
	}

	public void setOrgid_to_link(Long orgid_to_link) {
		this.orgid_to_link = orgid_to_link;
	}

	public Long getHandover_userid_link() {
		return handover_userid_link;
	}

	public void setHandover_userid_link(Long handover_userid_link) {
		this.handover_userid_link = handover_userid_link;
	}

	public Long getReceiver_userid_link() {
		return receiver_userid_link;
	}

	public void setReceiver_userid_link(Long receiver_userid_link) {
		this.receiver_userid_link = receiver_userid_link;
	}

	public Long getApprover_userid_link() {
		return approver_userid_link;
	}

	public void setApprover_userid_link(Long approver_userid_link) {
		this.approver_userid_link = approver_userid_link;
	}

	public Integer getTotalpackage() {
		return totalpackage;
	}

	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getExtrainfo() {
		return extrainfo;
	}

	public void setExtrainfo(String extrainfo) {
		this.extrainfo = extrainfo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getUsercreateid_link() {
		return usercreateid_link;
	}

	public void setUsercreateid_link(Long usercreateid_link) {
		this.usercreateid_link = usercreateid_link;
	}

	public Date getTimecreate() {
		return timecreate;
	}

	public void setTimecreate(Date timecreate) {
		this.timecreate = timecreate;
	}

	public Long getLastuserupdateid_link() {
		return lastuserupdateid_link;
	}

	public void setLastuserupdateid_link(Long lastuserupdateid_link) {
		this.lastuserupdateid_link = lastuserupdateid_link;
	}

	public Date getLasttimeupdate() {
		return lasttimeupdate;
	}

	public void setLasttimeupdate(Date lasttimeupdate) {
		this.lasttimeupdate = lasttimeupdate;
	}
	
	
}
