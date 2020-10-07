package vn.gpay.gsmart.core.encodeepc;

import java.io.Serializable;
import java.util.Date;

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

@Table(name="encode_epc")
@Entity
public class Encode_EPC implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Long getId() {
		return id;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public Long getEncodeid_link() {
		return encodeid_link;
	}
	public String getEpc() {
		return epc;
	}
	public String getOldepc() {
		return oldepc;
	}
	public String getTid() {
		return tid;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public Long getDeviceid_link() {
		return deviceid_link;
	}
	public Integer getStatus() {
		return status;
	}
	public Long getUsercreateid_link() {
		return usercreateid_link;
	}
	public Date getTimecreate() {
		return timecreate;
	}
	public Long getLastuserupdateid_link() {
		return lastuserupdateid_link;
	}
	public Date getLasttimeupdate() {
		return lasttimeupdate;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public void setEncodeid_link(Long encodeid_link) {
		this.encodeid_link = encodeid_link;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public void setOldepc(String oldepc) {
		this.oldepc = oldepc;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public void setDeviceid_link(Long deviceid_link) {
		this.deviceid_link = deviceid_link;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public void setUsercreateid_link(Long usercreateid_link) {
		this.usercreateid_link = usercreateid_link;
	}
	public void setTimecreate(Date timecreate) {
		this.timecreate = timecreate;
	}
	public void setLastuserupdateid_link(Long lastuserupdateid_link) {
		this.lastuserupdateid_link = lastuserupdateid_link;
	}
	public void setLasttimeupdate(Date lasttimeupdate) {
		this.lasttimeupdate = lasttimeupdate;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "encode_epc_generator")
	@SequenceGenerator(name="encode_epc_generator", sequenceName = "encode_epc_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long encodeid_link;
	private String epc;
	private String oldepc;
	private String tid;
	private Long skuid_link;
	private Long deviceid_link;
	private Integer status;
	private Long usercreateid_link;
	private Date timecreate;
	private Long lastuserupdateid_link;
	private Date lasttimeupdate;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private SKU sku ;
	
	@Transient
	public String getSkucode() {
		if(sku!=null)
			return sku.getCode();
		return "";
	}
}
