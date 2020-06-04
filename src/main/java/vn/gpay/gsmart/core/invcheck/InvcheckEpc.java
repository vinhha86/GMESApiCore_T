package vn.gpay.gsmart.core.invcheck;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


@Table(name="invcheck_epc")
@Entity
@IdClass(InvcheckEpcID.class)
public class InvcheckEpc implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//@EmbeddedId
   // private InvcheckEpcID invcheckepc_pk;
	@Id
	@Column(name = "orgid_link")
    private Long orgid_link;
	@Id
    @Column(name = "invcheckid_link")
    private Long invcheckid_link;
	@Id
    @Column(name = "epc",length=50)
    private String epc;
	
	@Column(name ="skuid_link")
    private Long skuid_link;
	
	@Column(name = "unitid_link")
    private Long unitid_link;	
	
	@Column(name ="ydsorigin")
    private Float ydsorigin;
	
	@Column(name ="ydscheck")
    private Float ydscheck;
	
	@Column(name ="unitprice")
    private Float unitprice;
	
	@Column(name ="rssi")
    private Integer rssi;
	
	@Column(name ="width")
    private Float width;
	
	@Column(name="packageid")
	private Long packageid;
	
	@Column(name="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;

	public Long getOrgid_link() {
		return orgid_link;
	}

	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}

	public Long getInvcheckid_link() {
		return invcheckid_link;
	}

	public void setInvcheckid_link(Long invcheckid_link) {
		this.invcheckid_link = invcheckid_link;
	}

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}

	public Long getSkuid_link() {
		return skuid_link;
	}

	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}

	public Long getUnitid_link() {
		return unitid_link;
	}

	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}

	public Float getYdsorigin() {
		return ydsorigin;
	}

	public void setYdsorigin(Float ydsorigin) {
		this.ydsorigin = ydsorigin;
	}

	public Float getYdscheck() {
		return ydscheck;
	}

	public void setYdscheck(Float ydscheck) {
		this.ydscheck = ydscheck;
	}

	public Float getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}

	public Integer getRssi() {
		return rssi;
	}

	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}

	public Float getWidth() {
		return width;
	}

	public void setWidth(Float width) {
		this.width = width;
	}

	public Long getPackageid() {
		return packageid;
	}

	public void setPackageid(Long packageid) {
		this.packageid = packageid;
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
	
}
