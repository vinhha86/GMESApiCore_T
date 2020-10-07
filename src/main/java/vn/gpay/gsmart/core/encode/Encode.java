package vn.gpay.gsmart.core.encode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import vn.gpay.gsmart.core.encodeepc.Encode_EPC;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.security.GpayUser;

@Table(name="encode")
@Entity
public class Encode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "encode_generator")
	@SequenceGenerator(name="encode_generator", sequenceName = "encode_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long porderid_link;
	private Long deviceid_link;
	private Date encode_date;
	private Long skuid_link;
	private String skucode;
	private Integer encode_amount;
	private Long usercreateid_link;
	private Date timecreate;
	private Integer status;
	private String session_code;
	private Long orgencodeid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="encodeid_link",insertable=false,updatable =false)
    private List<Encode_EPC> listepc = new ArrayList<Encode_EPC>();
    
    @NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="porderid_link",insertable=false,updatable =false)
    private POrder porder ;
    
    public List<Encode_EPC> getListepc() {
		return listepc;
	}
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="usercreateid_link",insertable=false,updatable =false)
    private GpayUser user ;
    
    @NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="deviceid_link",insertable=false,updatable =false)
    private Devices device ;
    
    @Transient
    public String getStatus_name() {
    	if(status == 0) return "Đang mã hóa";
		else if (status == 1) return "Đã mã hóa";
		else return "";
    }
    
    @Transient
    public String getDevice_name() {
    	if(device != null)
    		return device.getName();
    	return "";
    }
    
    @Transient
    public String getUsercreate_name() {
    	if(user!=null)
    		return user.getFullname();
    	return "";
    }
    
    @Transient
    public String getPorder_code() {
    	if(porder!=null)
    		return porder.getOrdercode();
    	return "";
    }
    
    @Transient
    public int getAmount_encoded() {
    	return listepc.size();
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
	public Long getPorderid_link() {
		return porderid_link;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public Long getDeviceid_link() {
		return deviceid_link;
	}
	public void setDeviceid_link(Long deviceid_link) {
		this.deviceid_link = deviceid_link;
	}
	public Date getEncode_date() {
		return encode_date;
	}
	public void setEncode_date(Date encode_date) {
		this.encode_date = encode_date;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public String getSkucode() {
		return skucode;
	}
	public void setSkucode(String skucode) {
		this.skucode = skucode;
	}
	public Integer getEncode_amount() {
		return encode_amount;
	}
	public void setEncode_amount(Integer encode_amount) {
		this.encode_amount = encode_amount;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getSession_code() {
		return session_code;
	}
	public void setSession_code(String session_code) {
		this.session_code = session_code;
	}
	public Long getOrgencodeid_link() {
		return orgencodeid_link;
	}
	public void setOrgencodeid_link(Long orgencodeid_link) {
		this.orgencodeid_link = orgencodeid_link;
	}
	
	
}
