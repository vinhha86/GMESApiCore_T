package vn.gpay.gsmart.core.invoice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.packinglist.PackingList;
import vn.gpay.gsmart.core.sku.SKU;

@Table(name = "invoice_d")
@Entity
public class InvoiceD implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_d_generator")
	@SequenceGenerator(name = "invoice_d_generator", sequenceName = "invoice_d_id_seq", allocationSize = 1)
	protected Long id;
	
	@Column(name = "orgrootid_link")
	private Long orgrootid_link;

	@Column(name = "orgid_link")
	private Long orgid_link;

	@Column(name = "invoiceid_link")
	private Long invoiceid_link;

	@Column(name = "skuid_link")
	private Long skuid_link;

//	@Column(name ="colorid_link")
//    private Integer colorid_link;

	@Column(name = "unitid_link")
	private Integer unitid_link;

	@Column(name = "totalpackage")
	private Integer totalpackage;

	@Column(name = "netweight")
	private Float netweight;

	@Column(name = "grossweight")
	private Float grossweight;

	@Column(name = "foc")
	private Float foc;
	
	@Column(name = "m3")
	private Float m3;

	@Column(name = "yds")
	private Float yds;

	@Column(name = "unitprice")
	private Float unitprice;

	@Column(name = "totalamount")
	private Float totalamount;

	@Column(name = "p_skuid_link")
	private Long p_skuid_link;

	@Column(name = "usercreateid_link")
	private Long usercreateid_link;

	@Column(name = "timecreate")
	private Date timecreate;

	@Column(name = "lastuserupdateid_link")
	private Long lastuserupdateid_link;

	@Column(name = "lasttimeupdate")
	private Date lasttimeupdate;
	
//	private Float m3;
	
	@NotFound(action = NotFoundAction.IGNORE)
//	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@OneToMany
//	@JoinColumn(name = "invoicedid_link", referencedColumnName = "id")
	@JoinColumn(name="invoicedid_link",insertable=false,updatable =false)
	private List<PackingList> packinglist = new ArrayList<>();

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	// mo rong
	public String getSkucode() {
		if (sku != null) {
			return sku.getCode();
		}
		return "";

	}

	public String getSkuname() {
		if (sku != null) {
			return sku.getName();
		}
		return "";
	}

	@Transient
	public String getColor_name() {
		if (sku != null) {
			return sku.getColor_name();
		}
		return "";
	}

	@Transient
	public String getSize_name() {
		if (sku != null) {
			return sku.getSize_name();
		}
		return "";
	}

	public String getHscode() {
		if (sku != null) {
			return sku.getHscode();
		}
		return "";

	}

	public String getHsname() {
		if (sku != null) {
			return sku.getHsname();
		}
		return "";
	}

	public String getUnitname() {
		if (unit != null) {
			return unit.getName();
		}
		return "";
	}

//	public String getColorcode() {
//		if(color!=null) {
//			return color.getCode();
//		}
//		return "";
//		
//	}
//	public String getColorname() {
//		if(color!=null) {
//			return color.getName();
//		}
//		return "";
//		
//	}
//	public String getColorRGB() {
//		if(color!=null) {
//			return color.getRgbvalue();
//		}
//		return "";
//		
//	}

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "skuid_link", updatable = false, insertable = false)
	private SKU sku;

//	@NotFound(action = NotFoundAction.IGNORE)
//	@ManyToOne
//    @JoinColumn(name="colorid_link",updatable =false,insertable =false)
//    private Color color;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "unitid_link", updatable = false, insertable = false)
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

	public Long getInvoiceid_link() {
		return invoiceid_link;
	}

	public void setInvoiceid_link(Long invoiceid_link) {
		this.invoiceid_link = invoiceid_link;
	}

	public Long getSkuid_link() {
		return skuid_link;
	}

	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}

//	public Integer getColorid_link() {
//		return colorid_link;
//	}
//	public void setColorid_link(Integer colorid_link) {
//		this.colorid_link = colorid_link;
//	}
	public Integer getUnitid_link() {
		return unitid_link;
	}

	public void setUnitid_link(Integer unitid_link) {
		this.unitid_link = unitid_link;
	}

	public Integer getTotalpackage() {
		return totalpackage;
	}

	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
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

	public Float getFoc() {
		return foc;
	}

	public void setFoc(Float foc) {
		this.foc = foc;
	}

	public Float getYds() {
		return yds;
	}

	public void setYds(Float yds) {
		this.yds = yds;
	}

	public Float getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}

	public Float getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(Float totalamount) {
		this.totalamount = totalamount;
	}

	public Long getP_skuid_link() {
		return p_skuid_link;
	}

	public void setP_skuid_link(Long p_skuid_link) {
		this.p_skuid_link = p_skuid_link;
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

	public List<PackingList> getPackinglist() {
		return packinglist;
	}

	public void setPackinglist(List<PackingList> packinglist) {
		this.packinglist = packinglist;
	}

	public SKU getSku() {
		return sku;
	}

	public void setSku(SKU sku) {
		this.sku = sku;
	}

//	public Color getColor() {
//		return color;
//	}
//	public void setColor(Color color) {
//		this.color = color;
//	}
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	public Float getM3() {
		return m3;
	}
	public void setM3(Float m3) {
		this.m3 = m3;
	}
	
	
}
