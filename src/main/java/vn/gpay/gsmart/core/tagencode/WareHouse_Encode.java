package vn.gpay.gsmart.core.tagencode;

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

import vn.gpay.gsmart.core.devices.Devices;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.security.GpayUser;

@Table(name="warehouse_encode")
@Entity
public class WareHouse_Encode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_encode_generator")
	@SequenceGenerator(name="warehouse_encode_generator", sequenceName = "warehouse_encode_id_seq", allocationSize=1)
	protected Long id;
	private Long orgrootid_link;
	private Long orgencodeid_link;
	public List<WareHouse_Encode_EPC> getWarehouse_encode_epc() {
		return warehouse_encode_epc;
	}
	public void setWarehouse_encode_epc(List<WareHouse_Encode_EPC> warehouse_encode_epc) {
		this.warehouse_encode_epc = warehouse_encode_epc;
	}
	private Long deviceid_link;
	private Long usercreateid_link;
	private Date timecreate;
	private Long userapproveid_link;
	private Date timeapprove;
	private Integer status;
	private Long lastuserupdate;
	private Date lasttimeupdate;
	
	public Long getLastuserupdate() {
		return lastuserupdate;
	}
	public void setLastuserupdate(Long lastuserupdate) {
		this.lastuserupdate = lastuserupdate;
	}
	public Date getLasttimeupdate() {
		return lasttimeupdate;
	}
	public void setLasttimeupdate(Date lasttimeupdate) {
		this.lasttimeupdate = lasttimeupdate;
	}
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL )
	@JoinColumn( name="warehouse_encodeid_link", referencedColumnName="id")
	private List<WareHouse_Encode_D>  warehouse_encode_d  = new ArrayList<WareHouse_Encode_D>();
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL )
	@JoinColumn( name="warehouse_encodeid_link", referencedColumnName="id")
	private List<WareHouse_Encode_EPC>  warehouse_encode_epc  = new ArrayList<WareHouse_Encode_EPC>();
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="usercreateid_link",insertable=false,updatable =false)
    private GpayUser usercreate;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="userapproveid_link",insertable=false,updatable =false)
    private GpayUser userapprove;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgencodeid_link",insertable=false,updatable =false)
    private Org org;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="deviceid_link",insertable=false,updatable =false)
    private Devices device;
	
	@Transient
	public int getTotalencode() {
		return warehouse_encode_epc.size();
	}
	
	@Transient
	public String getUsercreate_name() {
		if(usercreate!=null)
			return usercreate.getFullname();
		return "";
	}
	
	@Transient
	public String getUserapprove_name() {
		if(userapprove!=null)
			return userapprove.getFullname();
		return "";
	}
	
	@Transient
	public String getDevice_name() {
		if(device!=null)
			return device.getName();
		return "";
	}
	
	@Transient
	public String getOrgencode_name() {
		if(org!=null)
			return org.getName();
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
	public Long getOrgencodeid_link() {
		return orgencodeid_link;
	}
	public void setOrgencodeid_link(Long orgencodeid_link) {
		this.orgencodeid_link = orgencodeid_link;
	}
	public Long getDeviceid_link() {
		return deviceid_link;
	}
	public void setDeviceid_link(Long deviceid_link) {
		this.deviceid_link = deviceid_link;
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
	public Long getUserapproveid_link() {
		return userapproveid_link;
	}
	public void setUserapproveid_link(Long userapproveid_link) {
		this.userapproveid_link = userapproveid_link;
	}
	public Date getTimeapprove() {
		return timeapprove;
	}
	public void setTimeapprove(Date timeapprove) {
		this.timeapprove = timeapprove;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public List<WareHouse_Encode_D> getWarehouse_encode_d() {
		return warehouse_encode_d;
	}
	public void setWarehouse_encode_d(List<WareHouse_Encode_D> warehouse_encode_d) {
		this.warehouse_encode_d = warehouse_encode_d;
	}
	
	
}
