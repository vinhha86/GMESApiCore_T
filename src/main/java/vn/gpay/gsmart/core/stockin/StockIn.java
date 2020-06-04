package vn.gpay.gsmart.core.stockin;

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

import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.sku.SKU;

@Table(name="Stockin")
@Entity
public class StockIn implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockin_generator")
	@SequenceGenerator(name="stockin_generator", sequenceName = "stock_in_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name = "orgid_link")
	private Long orgid_link;
	
	@Column(name = "stockincode",length=50)
    private String stockincode;
	
	//public String getStockindate(){return "20 Feb 2019 10:12:22";}
	@Column(name = "stockindate")
	private Date stockindate;
	
	@Column(name = "stockintypeid_link")
    private Integer stockintypeid_link;
	
	@Column(name = "orgid_from_link")
    private Long orgid_from_link;
	
	@Column(name = "orgid_to_link")
    private Long orgid_to_link;
	
	@Column(name = "stockoutid_link")
    private Long stockoutid_link;	
	
	//@Column(name = "invoiceid_link")
   // private Long invoiceid_link;	
	
	@Column(name = "invoicenumber",length=50)
    private String invoicenumber;
	
	@Column(name = "customsnumber",length =50)
    private String customsnumber;
	
	@Column(name = "shipperson",length =100)
    private String shipperson;
	
	@Column(name = "totalpackage")
    private Integer totalpackage; 
	
	@Column(name ="totalm3")
    private Double totalm3;
	
	@Column(name ="totalnetweight")
    private Double totalnetweight;
	
	@Column(name ="totalgrossweight")
    private Double totalgrossweight;	
	
	@Column(name = "p_skuid_link")
    private Long p_skuid_link;
	
	@Column(name ="extrainfo",length=200)
    private String extrainfo;	
	
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
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	//@BatchSize(size=10)
	@JoinColumn( name="stockinid_link", referencedColumnName="id")
	private List<StockInD>  stockind  = new ArrayList<>();
	
	@Transient
	public String getProductcode() {
		if(sku!=null) {
			return sku.getCode();
		}
		return "";
	}
	@Transient
	public String getProductname() {
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
	public String getOrgfrom_name() {
		if(org!=null) {
			return org.getName();
		}
		return "";
	}
	@Transient
	public String getOrgfrom_code() {
		if(org!=null) {
			return org.getCode();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="p_skuid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgid_from_link",insertable=false,updatable =false)
    private Org org ;

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
	public String getStockincode() {
		return stockincode;
	}
	public void setStockincode(String stockincode) {
		this.stockincode = stockincode;
	}
	public Date getStockindate() {
		return stockindate;
	}
	public void setStockindate(Date stockindate) {
		this.stockindate = stockindate;
	}
	public Integer getStockintypeid_link() {
		return stockintypeid_link;
	}
	public void setStockintypeid_link(Integer stockintypeid_link) {
		this.stockintypeid_link = stockintypeid_link;
	}
	public Long getOrgid_from_link() {
		return orgid_from_link;
	}
	public void setOrgid_from_link(Long orgid_from_link) {
		this.orgid_from_link = orgid_from_link;
	}
	public Long getOrgid_to_link() {
		return orgid_to_link;
	}
	public void setOrgid_to_link(Long orgid_to_link) {
		this.orgid_to_link = orgid_to_link;
	}
	public Long getStockoutid_link() {
		return stockoutid_link;
	}
	public void setStockoutid_link(Long stockoutid_link) {
		this.stockoutid_link = stockoutid_link;
	}
	public String getInvoicenumber() {
		return invoicenumber;
	}
	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}
	public String getCustomsnumber() {
		return customsnumber;
	}
	public void setCustomsnumber(String customsnumber) {
		this.customsnumber = customsnumber;
	}
	public String getShipperson() {
		return shipperson;
	}
	public void setShipperson(String shipperson) {
		this.shipperson = shipperson;
	}
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	public Double getTotalm3() {
		return totalm3;
	}
	public void setTotalm3(Double totalm3) {
		this.totalm3 = totalm3;
	}
	public Double getTotalnetweight() {
		return totalnetweight;
	}
	public void setTotalnetweight(Double totalnetweight) {
		this.totalnetweight = totalnetweight;
	}
	public Double getTotalgrossweight() {
		return totalgrossweight;
	}
	public void setTotalgrossweight(Double totalgrossweight) {
		this.totalgrossweight = totalgrossweight;
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
	public List<StockInD> getStockind() {
		return stockind;
	}
	public void setStockind(List<StockInD> stockind) {
		this.stockind = stockind;
	}
	public SKU getSku() {
		return sku;
	}
	public void setSku(SKU sku) {
		this.sku = sku;
	}
	public Org getOrg() {
		return org;
	}
	public void setOrg(Org org) {
		this.org = org;
	}
	

	
}
