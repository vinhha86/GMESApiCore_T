package vn.gpay.gsmart.core.devices;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Table(name="devices")
@Entity
public class Devices implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "devices_generator")
	@SequenceGenerator(name="devices_generator", sequenceName = "devices_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgid_link")
    private Long orgid_link;
	
	@Column(name ="org_governid_link")
    private Long org_governid_link;
	
	@Column(name ="code",length=50)
    private String code;
	
	@Column(name ="name",length=100)
    private String name;
	
	@Column(name ="type")
    private Integer type;
	
	@Column(name ="extrainfo",length=200)
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
	
	@Column(name ="group_id")
    private Integer group_id;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="group_id",insertable=false,updatable =false)
    private DeviceGroup devicegroup;
	
	@Transient
	public String getDeviceGroupName() {
		if(devicegroup!=null) {
			return devicegroup.getName();
		}
		return "";
	}

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

	public Long getOrg_governid_link() {
		return org_governid_link;
	}

	public void setOrg_governid_link(Long org_governid_link) {
		this.org_governid_link = org_governid_link;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	public DeviceGroup getDevicegroup() {
		return devicegroup;
	}

	public void setDevicegroup(DeviceGroup devicegroup) {
		this.devicegroup = devicegroup;
	}
	
	/*
	@OneToMany 
	@JoinColumn(name = "parent_id") 
	private List<Devices> children = new LinkedList<Devices>();
	
	public String getText() {
		return this.name;
	}
	public boolean getLeaf() {
		if(children.size()>0) {
			return false;
		}
		return true;
	}
	*/
}
