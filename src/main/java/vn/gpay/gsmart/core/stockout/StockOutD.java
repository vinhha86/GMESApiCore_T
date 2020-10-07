package vn.gpay.gsmart.core.stockout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

import vn.gpay.gsmart.core.category.Color;
import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.sku.SKU;

@Table(name="stockout_d")
@Entity
public class StockOutD implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockind_generator")
	@SequenceGenerator(name="stockind_generator", sequenceName = "stock_in_d_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="stockoutid_link")
    private Long stockoutid_link;
	
	@Column(name ="pordercode", length=10)
    private String pordercode;
	
	@Column(name ="stockoutdate")
    private Date stockoutdate;	
	
	@Column(name ="mainskucode", length=50)
    private String mainskucode;
	
	@Column(name ="skuid_link")
    private Long skuid_link;
	
	@Column(name ="colorid_link")
    private Long colorid_link;
	
	@Column(name ="unitid_link")
    private Integer unitid_link;
	
	@Column(name ="totalorder_design")
    private Float totalorder_design;	
	
	@Column(name ="totalorder_tech")
    private Float totalorder_tech;
	
	@Column(name ="widthorder")
    private Float widthorder;
	
	@Column(name ="totalpackage")
    private Integer totalpackage;
	
	@Column(name ="listpackage", length=1000)
    private String listpackage;	
	
	@Column(name ="totalyds")
    private Float totalyds;
	
	@Column(name ="totalpackagecheck")
    private Integer totalpackagecheck;
	
	@Column(name ="totalydscheck")
    private Float totalydscheck;
	
	@Column(name ="totalpackageprocessed")
    private Integer totalpackageprocessed;
	
	@Column(name ="totalydsprocessed")
    private Float totalydsprocessed;
	
	@Column(name ="totalpackagestockout")
    private Integer totalpackagestockout;
	
	@Column(name ="totalydsstockout")
    private Float totalydsstockout;		
	
	@Column(name ="totalerror")
    private Float totalerror;		
	
	@Column(name ="unitprice")
    private Float unitprice;
	
	@Column(name = "p_skuid_link")
    private Long p_skuid_link;
	
	@Column(name ="extrainfo", length=1000)
    private String extrainfo;	
	
	@Column(name ="status")
    private Integer status;	
	
	@Column(name="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;
	
	private Long sizeid_link;
	
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	@JoinColumn( name="stockoutdid_link", referencedColumnName="id")
	private List<StockOutPklist>  stockoutpklist  = new ArrayList<>();
	
	@Transient
	public String getProduct_code() {
		if(sku!=null)
			return sku.getProduct_code();
		return "";
	}
	
	@Transient
	public String getColor_name() {
		if(sku!=null)
			return sku.getColor_name();
		return "";
	}
	
	@Transient
	public String getSize_name() {
		if(sku!=null)
			return sku.getSize_name();
		return "";
	}
	
	@Transient
	public String getSkucode() {
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
	@Transient
	public String getHscode() {
		if(sku!=null) {
			return sku.getHscode();
		}
		return "";
	}
	@Transient
	public String getHsname() {
		if(sku!=null) {
			return sku.getHsname();
		}
		return "";
	}
	
	@Transient
	public String getUnit_name() {
		if(sku!=null) {
			return sku.getUnit_name();
		}
		return "";
	}
	@Transient
	public String getColorcode() {
		if(color!=null) {
			return color.getCode();
		}
		return "";
	}
	@Transient
	public String getColorname() {
		if(color!=null) {
			return color.getName();
		}
		return "";
	}
	@Transient
	public String getColorRGB() {
		if(color!=null) {
			return color.getRgbvalue();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="colorid_link",insertable=false,updatable =false)
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
	public String getPordercode() {
		return pordercode;
	}
	public void setPordercode(String pordercode) {
		this.pordercode = pordercode;
	}
	public Date getStockoutdate() {
		return stockoutdate;
	}
	public void setStockoutdate(Date stockoutdate) {
		this.stockoutdate = stockoutdate;
	}
	public String getMainskucode() {
		return mainskucode;
	}
	public void setMainskucode(String mainskucode) {
		this.mainskucode = mainskucode;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public Long getColorid_link() {
		return colorid_link;
	}
	public void setColorid_link(Long colorid_link) {
		this.colorid_link = colorid_link;
	}

	public Integer getUnitid_link() {
		return unitid_link;
	}
	public void setUnitid_link(Integer unitid_link) {
		this.unitid_link = unitid_link;
	}
	public Float getTotalorder_design() {
		return totalorder_design;
	}
	public void setTotalorder_design(Float totalorder_design) {
		this.totalorder_design = totalorder_design;
	}
	public Float getTotalorder_tech() {
		return totalorder_tech;
	}
	public void setTotalorder_tech(Float totalorder_tech) {
		this.totalorder_tech = totalorder_tech;
	}
	public Float getWidthorder() {
		return widthorder;
	}
	public void setWidthorder(Float widthorder) {
		this.widthorder = widthorder;
	}
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	public String getListpackage() {
		return listpackage;
	}
	public void setListpackage(String listpackage) {
		this.listpackage = listpackage;
	}
	public Float getTotalyds() {
		return totalyds;
	}
	public void setTotalyds(Float totalyds) {
		this.totalyds = totalyds;
	}
	public Integer getTotalpackagecheck() {
		return totalpackagecheck;
	}
	public void setTotalpackagecheck(Integer totalpackagecheck) {
		this.totalpackagecheck = totalpackagecheck;
	}
	public Float getTotalydscheck() {
		return totalydscheck;
	}
	public void setTotalydscheck(Float totalydscheck) {
		this.totalydscheck = totalydscheck;
	}
	public Integer getTotalpackageprocessed() {
		return totalpackageprocessed;
	}
	public void setTotalpackageprocessed(Integer totalpackageprocessed) {
		this.totalpackageprocessed = totalpackageprocessed;
	}
	public Float getTotalydsprocessed() {
		return totalydsprocessed;
	}
	public void setTotalydsprocessed(Float totalydsprocessed) {
		this.totalydsprocessed = totalydsprocessed;
	}
	public Integer getTotalpackagestockout() {
		return totalpackagestockout;
	}
	public void setTotalpackagestockout(Integer totalpackagestockout) {
		this.totalpackagestockout = totalpackagestockout;
	}
	public Float getTotalydsstockout() {
		return totalydsstockout;
	}
	public void setTotalydsstockout(Float totalydsstockout) {
		this.totalydsstockout = totalydsstockout;
	}
	public Float getTotalerror() {
		return totalerror;
	}
	public void setTotalerror(Float totalerror) {
		this.totalerror = totalerror;
	}
	public Float getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}
	public Long getP_skuid_link() {
		return p_skuid_link;
	}
	public void setP_skuid_link(Long p_skuid_link) {
		this.p_skuid_link = p_skuid_link;
	}
	public String getExtrainfo() {
		return extrainfo;
	}
	public void setExtrainfo(String extrainfo) {
		this.extrainfo = extrainfo;
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
	public List<StockOutPklist> getStockoutpklist() {
		return stockoutpklist;
	}
	public void setStockoutpklist(List<StockOutPklist> stockoutpklist) {
		this.stockoutpklist = stockoutpklist;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getSizeid_link() {
		return sizeid_link;
	}
	public void setSizeid_link(Long sizeid_link) {
		this.sizeid_link = sizeid_link;
	}
}
