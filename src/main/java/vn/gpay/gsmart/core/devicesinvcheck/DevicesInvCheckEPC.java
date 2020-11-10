package vn.gpay.gsmart.core.devicesinvcheck;

import java.io.Serializable;

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

import vn.gpay.gsmart.core.devices.Devices;

@Table(name="devices_invcheck_epc")
@Entity
public class DevicesInvCheckEPC implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "devices_invcheck_epc_generator")
	@SequenceGenerator(name="devices_invcheck_epc_generator", sequenceName = "devices_invcheck_epc_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="devices_invcheckid_link")
    private Long devices_invcheckid_link;
	
	@Column(name ="deviceid_link")
    private Long deviceid_link;
	
	@Column(name ="checkstatus")
    private Integer checkstatus;
	
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

	public Long getDevices_invcheckid_link() {
		return devices_invcheckid_link;
	}

	public void setDevices_invcheckid_link(Long devices_invcheckid_link) {
		this.devices_invcheckid_link = devices_invcheckid_link;
	}

	public Long getDeviceid_link() {
		return deviceid_link;
	}

	public void setDeviceid_link(Long deviceid_link) {
		this.deviceid_link = deviceid_link;
	}

	public Integer getCheckstatus() {
		return checkstatus;
	}

	public void setCheckstatus(Integer checkstatus) {
		this.checkstatus = checkstatus;
	}
	
	
}
