package vn.gpay.gsmart.core.devicesinvcheck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.security.GpayUser;

@Table(name="devices_invcheck")
@Entity
public class DevicesInvCheck implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "devices_invcheck_generator")
	@SequenceGenerator(name="devices_invcheck_generator", sequenceName = "devices_invcheck_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="invcheckcode")
    private String invcheckcode;
	
	@Column(name ="invcheckdatetime")
    private Date invcheckdatetime;
	
	@Column(name ="orgcheckid_link")
    private Long orgcheckid_link;
	
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
	
	@Column(name ="p_skuid_link")
    private Long p_skuid_link;
	
	@Column(name ="bossid_link")
    private Long bossid_link;
	
	@Column(name ="extrainfo")
    private String extrainfo;
	
	//Danh sach thiet bi dc check trong phien kiem ke
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	@JoinColumn( name="devices_invcheckid_link", referencedColumnName="id")
	private List<DevicesInvCheckEPC>  epcs  = new ArrayList<>();	
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgcheckid_link",insertable=false,updatable =false)
	private Org orgCheck;
	
	@Transient
	public String getOrgCheckName() {
		if(orgCheck!=null) {
			return orgCheck.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="usercreateid_link",insertable=false,updatable =false)
	private GpayUser user;
	
	@Transient
	public String getUserName() {
		if(user!=null) {
			return user.getFullName();
		}
		return "";
	}

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

	public String getInvcheckcode() {
		return invcheckcode;
	}

	public void setInvcheckcode(String invcheckcode) {
		this.invcheckcode = invcheckcode;
	}

	public Date getInvcheckdatetime() {
		return invcheckdatetime;
	}

	public void setInvcheckdatetime(Date invcheckdatetime) {
		this.invcheckdatetime = invcheckdatetime;
	}

	public Long getOrgcheckid_link() {
		return orgcheckid_link;
	}

	public void setOrgcheckid_link(Long orgcheckid_link) {
		this.orgcheckid_link = orgcheckid_link;
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

	public Long getP_skuid_link() {
		return p_skuid_link;
	}

	public void setP_skuid_link(Long p_skuid_link) {
		this.p_skuid_link = p_skuid_link;
	}

	public Long getBossid_link() {
		return bossid_link;
	}

	public void setBossid_link(Long bossid_link) {
		this.bossid_link = bossid_link;
	}

	public String getExtrainfo() {
		return extrainfo;
	}

	public void setExtrainfo(String extrainfo) {
		this.extrainfo = extrainfo;
	}

	public List<DevicesInvCheckEPC> getEpcs() {
		return epcs;
	}

	public void setEpcs(List<DevicesInvCheckEPC> epcs) {
		this.epcs = epcs;
	}

}
