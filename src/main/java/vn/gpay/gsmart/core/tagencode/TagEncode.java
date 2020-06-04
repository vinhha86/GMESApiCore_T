package vn.gpay.gsmart.core.tagencode;

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

import vn.gpay.gsmart.core.sku.SKU;

@Table(name="warehouse_encode")
@Entity
public class TagEncode implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_encode_generator")
	@SequenceGenerator(name="warehouse_encode_generator", sequenceName = "warehouse_encode_id_seq", allocationSize=1)
	protected Long id;

	@Column(name = "orgid_link")
	private Long orgid_link;
	
	@Column(name = "orgencodeid_link")
	private Long orgencodeid_link;
	
	@Column(name = "epc")
	private String epc;
	
	@Column(name = "oldepc")
	private String oldepc;
	
	@Column(name = "tid")
	private String tid;
	
	@Column(name = "skuid_link")
	private Long skuid_link;
	
	@Column(name = "deviceid_link")
	private Long deviceid_link;
	
	@Column(name = "status")
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
	@ManyToOne
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@Transient
	public String getSku() {
		if(sku!=null) {
			return sku.getCode();
		}
		return "";
	}
	@Transient
	public String getSkuname() {
		if(sku!=null) {
			return sku.getName();
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
	public Long getOrgencodeid_link() {
		return orgencodeid_link;
	}
	public void setOrgencodeid_link(Long orgencodeid_link) {
		this.orgencodeid_link = orgencodeid_link;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public String getOldepc() {
		return oldepc;
	}
	public void setOldepc(String oldepc) {
		this.oldepc = oldepc;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public Long getDeviceid_link() {
		return deviceid_link;
	}
	public void setDeviceid_link(Long deviceid_link) {
		this.deviceid_link = deviceid_link;
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
	public void setSku(SKU sku) {
		this.sku = sku;
	}
}
