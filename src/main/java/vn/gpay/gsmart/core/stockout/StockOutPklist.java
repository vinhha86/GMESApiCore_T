package vn.gpay.gsmart.core.stockout;

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

@Table(name="stockout_pklist")
@Entity
public class StockOutPklist implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockout_pklist_generator")
	@SequenceGenerator(name="stockout_pklist_generator", sequenceName = "stock_out_pklist_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="stockoutid_link")
    private Long stockoutid_link;
	
	@Column(name ="stockoutdid_link")
    private Long stockoutdid_link;
	
	@Column(name ="skuid_link")
    private Long skuid_link;
	
	@Column(name ="lotnumber",length=100)
    private String lotnumber ;
	
	@Column(name ="packageid")
    private Integer packageid;
	
	@Column(name ="ydsorigin")
    private Float ydsorigin;
	@Transient
    private Float ydsoriginold;		

	@Column(name ="ydscheck")
    private Float ydscheck;
	@Transient
    private Float ydscheckold;		
	
	@Column(name ="ydsprocessed")
    private Float ydsprocessed;	
	@Transient
    private Float ydsprocessedold;			
	
	@Column(name ="widthorigin")
    private Float widthorigin;
	@Transient
    private Float widthoriginold;	

	@Column(name ="widthcheck")
    private Float widthcheck;
	@Transient
    private Float widthcheckold;	
	
	@Column(name ="widthprocessed")
    private Float widthprocessed;
	@Transient
    private Float widthprocessedold;		
	
	@Column(name ="totalerror")
    private Float totalerror;
	@Transient
    private Float totalerrorold;	
	
	@Column(name ="netweight")
    private Float netweight;
	
	@Column(name ="grossweight")
    private Float grossweight;
	
	@Column(name ="epc",length =50)
    private String epc;
	
	@Column(name ="rssi")
	private Integer rssi;		
	
	@Column(name ="status")
	private Integer status;				
	
	@Column(name ="extrainfo", length=1000)
    private String extrainfo;	
	
	@Column(name ="encryptdatetime")
    private Date encryptdatetime;
	
	@Column(name ="usercheckid_link")
    private Long usercheckid_link;	
	
	@Column(name ="timecheck")
    private Date timecheck;
	
	@Column(name ="userprocessedkid_link")
    private Long userprocessedkid_link;	
	
	@Column(name ="timeprocessed")
    private Date timeprocessed;	
	
	@Column(name="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;
	
	//mo rong
	public String getSkucode() {
		if(sku!=null) {
			return sku.getCode();
		}
		return "";
		
	}
	public String getSkuname() {
		if(sku!=null) {
			return sku.getName();
		}
		return "";
	}
	
	public String getHscode() {
		if(sku!=null) {
			return sku.getHscode();
		}
		return "";
		
	}
	
	public String getHsname() {
		if(sku!=null) {
			return sku.getHsname();
		}
		return "";
	}
	
	public String getProducttype_name() {
		if(sku!=null) {
			return sku.getProducttype_name();
		}
		return "";
	}
	
	public Integer getProducttypeid_link() {
		if(sku!=null) {
			return sku.getProducttypeid_link();
		}
		return null;
	}
	
	public String getMauSanPham() {
		if(sku!=null) {
			return sku.getMauSanPham();
		}
		return "";
	}
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",updatable =false,insertable =false)
    private SKU sku;

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
	public Long getStockoutid_link() {
		return stockoutid_link;
	}
	public void setStockoutid_link(Long stockoutid_link) {
		this.stockoutid_link = stockoutid_link;
	}
	public Long getStockoutdid_link() {
		return stockoutdid_link;
	}
	public void setStockoutdid_link(Long stockoutdid_link) {
		this.stockoutdid_link = stockoutdid_link;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}

	public String getLotnumber() {
		return lotnumber;
	}
	public void setLotnumber(String lotnumber) {
		this.lotnumber = lotnumber;
	}
	public Integer getPackageid() {
		return packageid;
	}
	public void setPackageid(Integer packageid) {
		this.packageid = packageid;
	}
	public Float getYdsorigin() {
		return ydsorigin;
	}
	public void setYdsorigin(Float ydsorigin) {
		this.ydsorigin = ydsorigin;
	}
	public Float getYdsoriginold() {
		return ydsoriginold;
	}
	public void setYdsoriginold(Float ydsoriginold) {
		this.ydsoriginold = ydsoriginold;
	}
	public Float getYdscheck() {
		return ydscheck;
	}
	public void setYdscheck(Float ydscheck) {
		this.ydscheck = ydscheck;
	}
	public Float getYdscheckold() {
		return ydscheckold;
	}
	public void setYdscheckold(Float ydscheckold) {
		this.ydscheckold = ydscheckold;
	}
	public Float getYdsprocessedold() {
		return ydsprocessedold;
	}
	public void setYdsprocessedold(Float ydsprocessedold) {
		this.ydsprocessedold = ydsprocessedold;
	}
	public Float getWidthoriginold() {
		return widthoriginold;
	}
	public void setWidthoriginold(Float widthoriginold) {
		this.widthoriginold = widthoriginold;
	}
	public Float getWidthcheckold() {
		return widthcheckold;
	}
	public void setWidthcheckold(Float widthcheckold) {
		this.widthcheckold = widthcheckold;
	}
	public Float getWidthprocessedold() {
		return widthprocessedold;
	}
	public void setWidthprocessedold(Float widthprocessedold) {
		this.widthprocessedold = widthprocessedold;
	}
	public Float getTotalerrorold() {
		return totalerrorold;
	}
	public void setTotalerrorold(Float totalerrorold) {
		this.totalerrorold = totalerrorold;
	}
	public Float getNetweight() {
		return netweight;
	}
	public void setNetweight(Float netweight) {
		this.netweight = netweight;
	}
	public Float getGrossweight() {
		return grossweight;
	}
	public void setGrossweight(Float grossweight) {
		this.grossweight = grossweight;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public Integer getRssi() {
		return rssi;
	}
	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}
	public Date getEncryptdatetime() {
		return encryptdatetime;
	}
	public void setEncryptdatetime(Date encryptdatetime) {
		this.encryptdatetime = encryptdatetime;
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

	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Float getYdsprocessed() {
		return ydsprocessed;
	}
	public void setYdsprocessed(Float ydsprocessed) {
		this.ydsprocessed = ydsprocessed;
	}
	public Float getWidthorigin() {
		return widthorigin;
	}
	public void setWidthorigin(Float widthorigin) {
		this.widthorigin = widthorigin;
	}
	public Float getWidthcheck() {
		return widthcheck;
	}
	public void setWidthcheck(Float widthcheck) {
		this.widthcheck = widthcheck;
	}
	public Float getWidthprocessed() {
		return widthprocessed;
	}
	public void setWidthprocessed(Float widthprocessed) {
		this.widthprocessed = widthprocessed;
	}
	public Float getTotalerror() {
		return totalerror;
	}
	public void setTotalerror(Float totalerror) {
		this.totalerror = totalerror;
	}
	public String getExtrainfo() {
		return extrainfo;
	}
	public void setExtrainfo(String extrainfo) {
		this.extrainfo = extrainfo;
	}
	public Long getUsercheckid_link() {
		return usercheckid_link;
	}
	public void setUsercheckid_link(Long usercheckid_link) {
		this.usercheckid_link = usercheckid_link;
	}
	public Date getTimecheck() {
		return timecheck;
	}
	public void setTimecheck(Date timecheck) {
		this.timecheck = timecheck;
	}
	public Long getUserprocessedkid_link() {
		return userprocessedkid_link;
	}
	public void setUserprocessedkid_link(Long userprocessedkid_link) {
		this.userprocessedkid_link = userprocessedkid_link;
	}
	public Date getTimeprocessed() {
		return timeprocessed;
	}
	public void setTimeprocessed(Date timeprocessed) {
		this.timeprocessed = timeprocessed;
	}
	
}
