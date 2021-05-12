package vn.gpay.gsmart.core.warehouse;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.IdClass;
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

@Table(name="Warehouse")
@Entity
//@IdClass(WarehouseId.class)
public class Warehouse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_generator")
	@SequenceGenerator(name="warehouse_generator", sequenceName = "warehouse_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="epc",length=50)
    private String epc;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="encryptdatetime")
    private Date encryptdatetime;
	
	@Column(name ="stockinid_link")
    private Long stockinid_link;	
	
	@Column(name ="stockindid_link")
    private Long stockindid_link;
	
	@Column(name ="skucode")
	private String skucode;
	
	@Column(name ="skuid_link")
    private Long skuid_link ;
	
	@Column(name ="skutypeid_link")
    private Integer skutypeid_link ;	
	
	@Column(name ="unitid_link")
    private Integer unitid_link;	
	
	@Column(name ="stockid_link")
    private Long stockid_link ;
	
	@Column(name ="p_skuid_link")
    private Long p_skuid_link;
	
	@Column(name ="colorid_link")
    private Long colorid_link;
	
	@Column(name = "lotnumber",length=50)
    private String lotnumber;
	
	@Column(name ="packageid")
    private Integer packageid;
	
	@Column(name ="yds")
    private Float yds;	

	@Column(name ="width")
    private Float width;	
	
	@Column(name ="netweight")
    private Float netweight;
	
	@Column(name ="grossweight")
    private Float grossweight;
	
	@Column(name ="unitprice")
    private Float unitprice;
	
	@Column(name = "spaceepc_link",length=50)
    private String spaceepc_link;	
	
	@Column(name ="spaceadddatetime")
    private Date spaceadddatetime;	
	
	@Column(name="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;
	
	private long sizeid_link;
	private Float met;
	private Integer status;
	
	

	public String getSkucode() {
		return skucode;
	}

	public void setSkucode(String skucode) {
		this.skucode = skucode;
	}

	//	//mo rong
//	public String getSkucode() {
//		if(sku!=null) {
//			return sku.getCode();
//		}
//		return "";
//		
//	}
	public String getPSkuname() {
		if(psku!=null) {
			return psku.getName();
		}
		return "";
	}
	
	public String getPSkucode() {
		if(psku!=null) {
			return psku.getCode();
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
    @JoinColumn(name="p_skuid_link ",updatable =false,insertable =false)
    private SKU psku;
	
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

	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}

	public Date getEncryptdatetime() {
		return encryptdatetime;
	}
	public void setEncryptdatetime(Date encryptdatetime) {
		this.encryptdatetime = encryptdatetime;
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
	public Integer getUnitid_link() {
		return unitid_link;
	}
	public void setUnitid_link(Integer unitid_link) {
		this.unitid_link = unitid_link;
	}
	public Long getStockid_link() {
		return stockid_link;
	}
	public void setStockid_link(Long stockid_link) {
		this.stockid_link = stockid_link;
	}
	public Long getP_skuid_link() {
		return p_skuid_link;
	}
	public void setP_skuid_link(Long p_skuid_link) {
		this.p_skuid_link = p_skuid_link;
	}
	public Long getColorid_link() {
		return colorid_link;
	}
	public void setColorid_link(Long colorid_link) {
		this.colorid_link = colorid_link;
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
	public Float getYds() {
		return yds;
	}
	public void setYds(Float yds) {
		this.yds = yds;
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
	public Float getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}
	public String getSpaceepc_link() {
		return spaceepc_link;
	}
	public void setSpaceepc_link(String spaceepc_link) {
		this.spaceepc_link = spaceepc_link;
	}
	public Date getSpaceadddatetime() {
		return spaceadddatetime;
	}
	public void setSpaceadddatetime(Date spaceadddatetime) {
		this.spaceadddatetime = spaceadddatetime;
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
	public SKU getPsku() {
		return psku;
	}
	public void setPsku(SKU psku) {
		this.psku = psku;
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

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public Integer getSkutypeid_link() {
		return skutypeid_link;
	}

	public void setSkutypeid_link(Integer skutypeid_link) {
		this.skutypeid_link = skutypeid_link;
	}

	public long getSizeid_link() {
		return sizeid_link;
	}

	public void setSizeid_link(long sizeid_link) {
		this.sizeid_link = sizeid_link;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getMet() {
		return met;
	}

	public void setMet(Float met) {
		this.met = met;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
