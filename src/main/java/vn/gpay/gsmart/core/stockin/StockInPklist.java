package vn.gpay.gsmart.core.stockin;

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

import vn.gpay.gsmart.core.category.Color;
import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.sku.SKU;

@Table(name="stockin_pklist")
@Entity
public class StockInPklist implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockout_pklist_generator")
	@SequenceGenerator(name="stockout_pklist_generator", sequenceName = "stock_out_pklist_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgid_link")
    private Long orgid_link;
	
	@Column(name ="stockinid_link")
    private Long stockinid_link;
	
	@Column(name ="stockindid_link")
    private Long stockindid_link;
	
	@Column(name ="skuid_link")
    private Long skuid_link;

	@Column(name ="colorid_link")
    private Integer colorid_link;
	
	@Column(name ="unitid_link")
    private Integer unitid_link;
	
	@Column(name ="lotnumber",length=100)
    private String lotnumber ;
	
	@Column(name ="packageid")
    private Integer packageid;
	
	@Column(name="ydsorigin")
	private Float ydsorigin;
	
	@Column(name="ydscheck")
	private Float ydscheck;
	
	@Column(name ="width")
    private Float width;
	
	@Column(name ="netweight")
    private Float netweight;
	
	@Column(name ="grossweight")
    private Float grossweight;
	
	@Column(name ="epc",length =50)
    private String epc;
	
	@Column(name ="rssi")
	private Integer rssi;	
	
	@Column(name ="skutypeid_link")
	private Integer skutypeid_link;	
	
	@Column(name ="encryptdatetime")
    private Date encryptdatetime;
	
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
	
	
	public String getColorcode() {
		if(color!=null) {
			return color.getCode();
		}
		return "";
		
	}
	public String getColorname() {
		if(color!=null) {
			return color.getName();
		}
		return "";
		
	}
	public String getColorRGB() {
		if(color!=null) {
			return color.getRgbvalue();
		}
		return "";
		
	}
	
	@Transient
	public String getUnitname() {
		if(unit!=null) {
			return unit.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",updatable =false,insertable =false)
    private SKU sku;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="colorid_link",updatable =false,insertable =false)
    private Color color;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
    private Unit unit;

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
	public Long getStockinid_link() {
		return stockinid_link;
	}
	public void setStockinid_link(Long stockinid_link) {
		this.stockinid_link = stockinid_link;
	}
	public Long getStockindid_link() {
		return stockindid_link;
	}
	public void setStockindid_link(Long stockindid_link) {
		this.stockindid_link = stockindid_link;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public Integer getColorid_link() {
		return colorid_link;
	}
	public void setColorid_link(Integer colorid_link) {
		this.colorid_link = colorid_link;
	}
	public Integer getUnitid_link() {
		return unitid_link;
	}
	public void setUnitid_link(Integer unitid_link) {
		this.unitid_link = unitid_link;
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
	public Float getYdscheck() {
		return ydscheck;
	}
	public void setYdscheck(Float ydscheck) {
		this.ydscheck = ydscheck;
	}
	public Float getWidth() {
		return width;
	}
	public void setWidth(Float width) {
		this.width = width;
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
	public Integer getSkutypeid_link() {
		return skutypeid_link;
	}
	public void setSkutypeid_link(Integer skutypeid_link) {
		this.skutypeid_link = skutypeid_link;
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
	public SKU getSku() {
		return sku;
	}
	public void setSku(SKU sku) {
		this.sku = sku;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	

	
}
