package vn.gpay.gsmart.core.deviceout_d;

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

@Table(name="deviceout_d")
@Entity
public class DeviceOut_d implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deviceout_d_generator")
	@SequenceGenerator(name="deviceout_d_generator", sequenceName = "deviceout_d_id_seq", allocationSize=1)
	protected Long id;

    private Long orgrootid_link;
    private Long deviceoutid_link;
    private Long deviceid_link;
    
    @Transient
    private String epc;
    @Transient
    private String code;
    @Transient
    private String devicegroupname;
    @Transient
    private Integer devicegroupid_link;
    
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


	public Long getDeviceoutid_link() {
		return deviceoutid_link;
	}
	public void setDeviceoutid_link(Long deviceoutid_link) {
		this.deviceoutid_link = deviceoutid_link;
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

	public Integer getDevicegroupid_link() {
		return devicegroupid_link;
	}

	public void setDevicegroupid_link(Integer devicegroupid_link) {
		this.devicegroupid_link = devicegroupid_link;
	}

	
}