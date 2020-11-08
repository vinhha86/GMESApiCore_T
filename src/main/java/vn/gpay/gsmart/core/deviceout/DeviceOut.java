package vn.gpay.gsmart.core.deviceout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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

import vn.gpay.gsmart.core.deviceout_d.DeviceOut_d;
import vn.gpay.gsmart.core.deviceout_type.DeviceOutType;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.security.GpayUser;

@Table(name="deviceout")
@Entity
public class DeviceOut implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deviceout_generator")
	@SequenceGenerator(name="deviceout_generator", sequenceName = "deviceout_id_seq", allocationSize=1)
	protected Long id;

    private Long orgrootid_link;
    private String deviceout_code;
    private Date deviceout_date;
    private Integer deviceouttypeid_link;
	private String invoice_code;
	private Date invoice_date;
	private Long orgid_from_link;
	private Long orgid_to_link;
	private String shipperson;
	private Integer totalpackage;
	private Long usercreateid_link;
	private Date timecreate;
	private Long userupdateid_link;
	private Date timeupdate;
	private Integer status;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="deviceouttypeid_link",insertable=false,updatable =false)
	private DeviceOutType type;
	
	@Transient
	public String getDeviceOutTypeName() {
		if(type!=null) {
			return type.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgid_from_link",insertable=false,updatable =false)
    private Org orgFrom ;
	
	@Transient
	public String getOrgFromName() {
		if(orgFrom!=null) {
			return orgFrom.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgid_to_link",insertable=false,updatable =false)
    private Org orgTo ;
	
	@Transient
	public String getOrgToName() {
		if(orgTo!=null) {
			return orgTo.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn( name="usercreateid_link", referencedColumnName="id",insertable=false,updatable =false)
	private GpayUser user;
	
	@Transient
	public String getUsercreate_name() {
		if(user != null)
			return user.getFullname();
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	@JoinColumn( name="deviceoutid_link", referencedColumnName="id")
	private List<DeviceOut_d>  deviceout_d  = new ArrayList<DeviceOut_d>();
	
	
	public List<DeviceOut_d> getDeviceout_d() {
		return deviceout_d;
	}
	public void setDeviceout_d(List<DeviceOut_d> deviceout_d) {
		this.deviceout_d = deviceout_d;
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
	public String getDeviceout_code() {
		return deviceout_code;
	}
	public void setDeviceout_code(String deviceout_code) {
		this.deviceout_code = deviceout_code;
	}
	public Date getDeviceout_date() {
		return deviceout_date;
	}
	public void setDeviceout_date(Date deviceout_date) {
		this.deviceout_date = deviceout_date;
	}
	public Integer getDeviceouttypeid_link() {
		return deviceouttypeid_link;
	}
	public void setDeviceouttypeid_link(Integer deviceouttypeid_link) {
		this.deviceouttypeid_link = deviceouttypeid_link;
	}
	public String getInvoice_code() {
		return invoice_code;
	}
	public void setInvoice_code(String invoice_code) {
		this.invoice_code = invoice_code;
	}
	public Date getInvoice_date() {
		return invoice_date;
	}
	public void setInvoice_date(Date invoice_date) {
		this.invoice_date = invoice_date;
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
	public String getShipperson() {
		return shipperson;
	}
	public void setShipperson(String shipperson) {
		this.shipperson = shipperson;
	}
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
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
	public Long getUserupdateid_link() {
		return userupdateid_link;
	}
	public void setUserupdateid_link(Long userupdateid_link) {
		this.userupdateid_link = userupdateid_link;
	}
	public Date getTimeupdate() {
		return timeupdate;
	}
	public void setTimeupdate(Date timeupdate) {
		this.timeupdate = timeupdate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	

}
