package vn.gpay.gsmart.core.invoice;

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

import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.sku.SKU;

@Table(name="invoice_d")
@Entity
public class InvoiceD implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_d_generator")
	@SequenceGenerator(name="invoice_d_generator", sequenceName = "invoice_d_id_seq")
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="invoiceid_link")
    private Long invoiceid_link;
	
	@Column(name ="skuid_link")
    private Long skuid_link;
	
	@Column(name ="colorid_link")
    private Long colorid_link;
	
	@Column(name ="unitid_link")
    private Long unitid_link;
	
	@Column(name ="totalpackage")
    private Integer totalpackage;
	
	@Column(name ="netweight")
    private Float netweight;
	
	@Column(name ="grossweight")
    private Float grossweight;
	
	@Column(name ="foc")
    private Float foc;
	
	@Column(name ="yds")
    private Float yds;
	
	@Column(name ="unitprice")
    private Float unitprice;
	
	@Column(name ="totalamount")
    private Float totalamount;
		
	@Column(name="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;
	
	private Float m3;
	private Long sizeid_link;
	
//	@NotFound(action = NotFoundAction.IGNORE)
//	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
//	@JoinColumn( name="invoicedid_link", referencedColumnName="id")
//	private List<PackingList>  packinglist  = new ArrayList<>();
	
	//mo rong
	public String getSkucode() {
		if(sku!=null) {
			return sku.getCode();
		}
		return "";
		
	}
	@Transient
	public String getColor_name() {
		if(sku!=null) {
			return sku.getColor_name();
		}
		return "";
	}
	
	@Transient
	public String getSize_name() {
		if(sku!=null) {
			return sku.getSize_name();
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
    @JoinColumn(name="unitid_link",updatable =false,insertable =false)
    private Unit unit;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Long getColorid_link() {
		return colorid_link;
	}
	public void setColorid_link(Long colorid_link) {
		this.colorid_link = colorid_link;
	}
	public Long getUnitid_link() {
		return unitid_link;
	}
	public void setUnitid_link(Long unitid_link) {
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
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public Long getSizeid_link() {
		return sizeid_link;
	}
	public void setSizeid_link(Long sizeid_link) {
		this.sizeid_link = sizeid_link;
	}
	public Float getM3() {
		return m3;
	}
	public void setM3(Float m3) {
		this.m3 = m3;
	}
//	public List<PackingList> getPackinglist() {
//		return packinglist;
//	}
//	public void setPackinglist(List<PackingList> packinglist) {
//		this.packinglist = packinglist;
//	}
	
}
