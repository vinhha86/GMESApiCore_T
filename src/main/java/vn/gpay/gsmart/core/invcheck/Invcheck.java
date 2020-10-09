package vn.gpay.gsmart.core.invcheck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

@Table(name="invcheck")
@Entity
public class Invcheck implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invcheck_generator")
	@SequenceGenerator(name="invcheck_generator", sequenceName = "invcheck_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="invcheckcode",length=50)
    private String invcheckcode;
	
	@Column(name ="invcheckdatetime")
    private Date invcheckdatetime;
	
	@Column(name ="orgcheckid_link")
    private Long orgcheckid_link;
	
	@Column(name="p_skuid_link")
	private Long p_skuid_link;
	
	@Column(name="bossid_link")
	private Long bossid_link;
	
	@Column(name="extrainfo")
	private String extrainfo;
	
	@Column(name ="status")
    private Integer status;
	
	@Column(name="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
	@JoinColumn( name="invcheckid_link", referencedColumnName="id")
	private List<InvcheckSku>  skus  = new ArrayList<>();

	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
	@JoinColumn( name="invcheckid_link", referencedColumnName="id")
	private List<InvcheckEpc>  epcs  = new ArrayList<>();
	
	
	public String getUser_name() {
		return "admin";
	}
	
	@Transient
	public String getOrgcheck_name() {
		if(org!=null) {
			return org.getName();
		}
		return "";
	}
	@Transient
	public String getOrgcheck_code() {
		if(org!=null) {
			return org.getCode();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgcheckid_link",insertable=false,updatable =false)
    private Org org ;


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

	public List<InvcheckSku> getSkus() {
		return skus;
	}

	public void setSkus(List<InvcheckSku> skus) {
		this.skus = skus;
	}

	public List<InvcheckEpc> getEpcs() {
		return epcs;
	}

	public void setEpcs(List<InvcheckEpc> epcs) {
		this.epcs = epcs;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}
	
	
}
