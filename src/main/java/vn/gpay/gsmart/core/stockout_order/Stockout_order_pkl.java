package vn.gpay.gsmart.core.stockout_order;

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

@Table(name="stockout_order_pklist")
@Entity
public class Stockout_order_pkl implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockout_order_pklist_generator")
	@SequenceGenerator(name="stockout_order_pklist_generator", sequenceName = "stockout_order_pklist_id_seq", allocationSize=1)
	private Long id;
	private Long orgid_link;
	private Long stockoutorderid_link;
	private Long stockoutorderdid_link;
	private Long skuid_link;
	private Long colorid_link;
	private String lotnumber;
	private Integer packageid;
	private Float ydsorigin;
	private Float ydscheck;
	private Float width;
	private Float netweight;
	private Float grossweight;
	private String epc;
	private Date encryptdatetime;
	private Long usercreateid_link;
	private Date timecreate;
	private Long lastuserupdateid_link;
	private Date lasttimeupdate;
	private String spaceepc_link;
	private Float met;
	private Integer status;
	
	public Float getMet() {
		return met;
	}

	public void setMet(Float met) {
		this.met = met;
	}
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="stockoutorderdid_link",insertable=false,updatable =false)
    private Stockout_order_d detail;
	
	@Transient
	public String getmaterial_product_code() {
		return detail.getMaterialCode();
	}
	
	@Transient
	public String getColor_name() {
		return detail.getTenMauNPL();
	}
	
	@Transient
	public Long getIdx() {
		return id;
	}
	
	@Transient
	public String getunit_name() {
		return detail.getUnitName();
	}
	
	public Long getId() {
		return id;
	}
	public Long getOrgid_link() {
		return orgid_link;
	}
	public Long getStockoutorderid_link() {
		return stockoutorderid_link;
	}
	public Long getStockoutorderdid_link() {
		return stockoutorderdid_link;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public Long getColorid_link() {
		return colorid_link;
	}
	public String getLotnumber() {
		return lotnumber;
	}
	public Integer getPackageid() {
		return packageid;
	}
	public Float getYdsorigin() {
		return ydsorigin;
	}
	public Float getYdscheck() {
		return ydscheck;
	}
	public Float getWidth() {
		return width;
	}
	public Float getNetweight() {
		return netweight;
	}
	public Float getGrossweight() {
		return grossweight;
	}
	public String getEpc() {
		return epc;
	}
	public Date getEncryptdatetime() {
		return encryptdatetime;
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
	public String getSpaceepc_link() {
		return spaceepc_link;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}
	public void setStockoutorderid_link(Long stockoutorderid_link) {
		this.stockoutorderid_link = stockoutorderid_link;
	}
	public void setStockoutorderdid_link(Long stockoutorderdid_link) {
		this.stockoutorderdid_link = stockoutorderdid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public void setColorid_link(Long colorid_link) {
		this.colorid_link = colorid_link;
	}
	public void setLotnumber(String lotnumber) {
		this.lotnumber = lotnumber;
	}
	public void setPackageid(Integer packageid) {
		this.packageid = packageid;
	}
	public void setYdsorigin(Float ydsorigin) {
		this.ydsorigin = ydsorigin;
	}
	public void setYdscheck(Float ydscheck) {
		this.ydscheck = ydscheck;
	}
	public void setWidth(Float width) {
		this.width = width;
	}
	public void setNetweight(Float netweight) {
		this.netweight = netweight;
	}
	public void setGrossweight(Float grossweight) {
		this.grossweight = grossweight;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public void setEncryptdatetime(Date encryptdatetime) {
		this.encryptdatetime = encryptdatetime;
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
	public void setSpaceepc_link(String spaceepc_link) {
		this.spaceepc_link = spaceepc_link;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
