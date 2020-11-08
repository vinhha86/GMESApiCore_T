package vn.gpay.gsmart.core.devicein_d;

import java.io.Serializable;

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

import vn.gpay.gsmart.core.devices.Devices;

@Table(name="devicein_d")
@Entity
public class DeviceIn_d implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "devicein_d_generator")
	@SequenceGenerator(name="devicein_d_generator", sequenceName = "devicein_d_id_seq", allocationSize=1)
	protected Long id;

    private Long orgrootid_link;
    private Long deviceinid_link;
    private Long deviceid_link;
    @Transient
    private String epc;
    @Transient
    private String code;
    @Transient
    private String devicegroupname;
    @Transient
    private Long devicegroupid_link;
    
    @NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="deviceid_link",insertable=false,updatable =false)
    private Devices device;
	
    @Transient
	public String getDeviceEPCCode() {
		if(device!=null) {
			return device.getEpc();
		}
		return "";
	}
    
	@Transient
	public String getDeviceName() {
		if(device!=null) {
			return device.getName();
		}
		return "";
	}
	
	@Transient
	public String getDeviceCode() {
		if(device!=null) {
			return device.getCode();
		}
		return "";
	}
	
	@Transient
	public String getDeviceGroupName() {
		if(device!=null) {
			return device.getDeviceGroupName();
		}
		return "";
	}
	
	@Transient
	public String getDeviceFixassetno() {
		if(device!=null) {
			return device.getFixassetno();
		}
		return "";
	}
	
	@Transient
	public String getDeviceSerialNo() {
		if(device!=null) {
			return device.getSerialno();
		}
		return "";
	}
	
	@Transient
	public String getDeviceType() {
		if(device!=null) {
			return device.getDeviceType();
		}
		return "";
	}
	
	@Transient
	public String getDeviceModel() {
		if(device!=null) {
			return device.getDeviceModel();
		}
		return "";
	}
    
	public Long getDevicegroupid_link() {
		return devicegroupid_link;
	}
	public void setDevicegroupid_link(Long devicegroupid_link) {
		this.devicegroupid_link = devicegroupid_link;
	}
	public String getDevicegroupname() {
		return devicegroupname;
	}
	public void setDevicegroupname(String devicegroupname) {
		this.devicegroupname = devicegroupname;
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

	public Long getDeviceinid_link() {
		return deviceinid_link;
	}
	public void setDeviceinid_link(Long deviceinid_link) {
		this.deviceinid_link = deviceinid_link;
	}
	public Long getDeviceid_link() {
		return deviceid_link;
	}
	public void setDeviceid_link(Long deviceid_link) {
		this.deviceid_link = deviceid_link;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
