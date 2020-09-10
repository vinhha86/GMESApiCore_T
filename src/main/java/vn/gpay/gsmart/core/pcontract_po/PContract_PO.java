package vn.gpay.gsmart.core.pcontract_po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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

import vn.gpay.gsmart.core.currency.Currency;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price;
import vn.gpay.gsmart.core.porder_req.POrder_Req;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.security.GpayUser;

@Table(name="pcontract_po")
@Entity
public class PContract_PO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_po_generator")
	@SequenceGenerator(name="pcontract_po_generator", sequenceName = "pcontract_po_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long pcontractid_link;
	private String code;
	private String po_buyer;
	private String po_vendor;
	private Long productid_link;
	private Integer po_quantity;
	private Long unitid_link;
	private Date shipdate;
	private Date matdate;
	private Float actual_quantity;
	private Date actual_shipdate;
	private Float price_cmp;
	private Float price_fob;
	private Float price_sweingtarget;
	private Float price_sweingfact;
	private Float price_add;
	private Float price_commission;
	private Float salaryfund;
	private Long currencyid_link;
	private Float exchangerate;
	private Date productiondate;
	private String packingnotice;
	private Long qcorgid_link;
	private String qcorgname;
	private Integer etm_from;
	private Integer etm_to;
	private Integer etm_avr;
	private Long usercreatedid_link;
	private Date datecreated;
	private Integer status;
	private Integer productiondays;
	private Long orgmerchandiseid_link;
	private Long merchandiserid_link;
	private Long parentpoid_link;
	private Boolean is_tbd;
	private Float sewtarget_percent;
	private Long portfromid_link;
	private Long porttoid_link;
	private Boolean isauto_calculate;
	
	
	
	public Integer getProductiondays() {
		return productiondays;
	}

	public void setProductiondays(Integer productiondays) {
		this.productiondays = productiondays;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="pcontract_poid_link",insertable=false,updatable =false)
    private List<POrder_Req> porder_req = new ArrayList<POrder_Req>();
    	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="usercreatedid_link",insertable=false,updatable =false)
    private GpayUser usercreated;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productid_link",insertable=false,updatable =false)
    private Product product;
    
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="pcontract_poid_link",insertable=false,updatable =false)
    private List<PContract_Price> pcontract_price = new ArrayList<PContract_Price>();
    
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="parentpoid_link",insertable=false,updatable =false)
    private List<PContract_PO> sub_po = new ArrayList<PContract_PO>();
   
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="merchandiserid_link",insertable=false,updatable =false)
    private GpayUser merchandiser;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="currencyid_link",insertable=false,updatable =false)
    private Currency currency;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgmerchandiseid_link",insertable=false,updatable =false)
    private Org org_factory;
	
	@Transient
	public String getFactory_name() {
		if(org_factory!=null)
			return org_factory.getName();
		return "";
	}
	@Transient
	public String getMerchandiser_name() {
		if(merchandiser!=null) {
			return merchandiser.getFullname();
		}
		return "";
	}
	
	@Transient
	public String getUsercreatedName() {
		if(usercreated != null) {
			return usercreated.getFullName();
		}
		return "";
	}
	
	@Transient
	public String getCurrencyCode() {
		if(currency!=null)
			currency.getCode();
		return "$";
	}
	
	@Transient
    public String getFactories() {
    	String name = "";
    	for(POrder_Req req : porder_req) {
    		if(name.contains(req.getGranttoorgname())) continue;
    		if(name == "")
    			name += req.getGranttoorgcode();
    		else
    			name += ", " + req.getGranttoorgcode();
    	}
    	return name;
    }
    
	public List<PContract_Price> getPcontract_price() {
		return pcontract_price;
	}

	public void setPcontract_price(List<PContract_Price> pcontract_price) {
		this.pcontract_price = pcontract_price;
	}
	
	public List<PContract_PO> getSub_po() {
		return sub_po;
	}

	public void setSub_po(List<PContract_PO> sub_po) {
		this.sub_po = sub_po;
	}
	
	@Transient
    public int getAmount_org() {
    	int sum_product = 0;
    	if(parentpoid_link == null) {
    		if(product.getProducttypeid_link() == 5) {
    			HashMap<Long, Integer> lst_org = new HashMap<>();
    			
    			for(POrder_Req req : porder_req) {
	    			Integer amountInset = req.getAmount_inset();
	    			if(amountInset == null) amountInset = 1;
	    			
    	    		if(!lst_org.containsKey(req.getGranttoorgid_link())){
//    	    			lst_org.put(req.getGranttoorgid_link(), (req.getTotalorder() / req.getAmount_inset()));
    	    			lst_org.put(req.getGranttoorgid_link(), (req.getTotalorder() / amountInset));
    	    		}
    	    		
    	    		int amount_org = lst_org.get(req.getGranttoorgid_link());
//    	    		amount_org = (req.getTotalorder() / req.getAmount_inset()) < amount_org ? (req.getTotalorder()/req.getAmount_inset()) : amount_org;
    	    		amount_org = (req.getTotalorder() / amountInset) < amount_org ? (req.getTotalorder()/amountInset) : amount_org;
    	    		lst_org.replace(req.getGranttoorgid_link(), amount_org);
    	    	}
    			Set<Long> keySet = lst_org.keySet();
    			for (Long key : keySet) {
    				sum_product  = lst_org.get(key);
    	        }
    	    	
    		}
    		else {
    			for(POrder_Req req : porder_req) {
    				sum_product += req.getTotalorder();
    			}
    		}
    	}
		
    	
    	return sum_product;
    }
	
	@Transient
	public String getProductbuyercode() {
		if(product != null) {
			return product.getBuyercode();
		}
		return "";
	}
	
	@Transient
	public String getProductvendorcode() {
		if(product != null) {
			return product.getVendorcode();
		}
		return "";
	}
	
	@Transient
	public String getProduct_code() {
		if(product != null) {
			return product.getBuyercode();
		}
		return "";
	}
	
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

	public Long getPcontractid_link() {
		return pcontractid_link;
	}

	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPo_buyer() {
		return po_buyer;
	}

	public void setPo_buyer(String po_buyer) {
		this.po_buyer = po_buyer;
	}

	public String getPo_vendor() {
		return po_vendor;
	}

	public void setPo_vendor(String po_vendor) {
		this.po_vendor = po_vendor;
	}

	public Long getProductid_link() {
		return productid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}

	public Integer getPo_quantity() {
		return po_quantity;
	}

	public void setPo_quantity(Integer po_quantity) {
		this.po_quantity = po_quantity;
	}

	public Long getUnitid_link() {
		return unitid_link;
	}

	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}

	public Date getShipdate() {
		return shipdate;
	}

	public void setShipdate(Date shipdate) {
		this.shipdate = shipdate;
	}

	public Date getMatdate() {
		return matdate;
	}

	public void setMatdate(Date matdate) {
		this.matdate = matdate;
	}

	public Float getActual_quantity() {
		return actual_quantity;
	}

	public void setActual_quantity(Float actual_quantity) {
		this.actual_quantity = actual_quantity;
	}

	public Date getActual_shipdate() {
		return actual_shipdate;
	}

	public void setActual_shipdate(Date actual_shipdate) {
		this.actual_shipdate = actual_shipdate;
	}

	public Float getPrice_cmp() {
		return price_cmp;
	}

	public void setPrice_cmp(Float price_cmp) {
		this.price_cmp = price_cmp;
	}

	public Float getPrice_fob() {
		return price_fob;
	}

	public void setPrice_fob(Float price_fob) {
		this.price_fob = price_fob;
	}

	public Float getPrice_sweingtarget() {
		return price_sweingtarget;
	}

	public void setPrice_sweingtarget(Float price_sweingtarget) {
		this.price_sweingtarget = price_sweingtarget;
	}

	public Float getPrice_sweingfact() {
		return price_sweingfact;
	}

	public void setPrice_sweingfact(Float price_sweingfact) {
		this.price_sweingfact = price_sweingfact;
	}

	public Float getPrice_add() {
		return price_add;
	}

	public void setPrice_add(Float price_add) {
		this.price_add = price_add;
	}

	public Float getPrice_commission() {
		return price_commission;
	}

	public void setPrice_commission(Float price_commission) {
		this.price_commission = price_commission;
	}

	public Float getSalaryfund() {
		return salaryfund;
	}

	public void setSalaryfund(Float salaryfund) {
		this.salaryfund = salaryfund;
	}

	public Long getCurrencyid_link() {
		return currencyid_link;
	}

	public void setCurrencyid_link(Long currencyid_link) {
		this.currencyid_link = currencyid_link;
	}

	public Float getExchangerate() {
		return exchangerate;
	}

	public void setExchangerate(Float exchangerate) {
		this.exchangerate = exchangerate;
	}

	public Date getProductiondate() {
		return productiondate;
	}

	public void setProductiondate(Date productiondate) {
		this.productiondate = productiondate;
	}

	public String getPackingnotice() {
		return packingnotice;
	}

	public void setPackingnotice(String packingnotice) {
		this.packingnotice = packingnotice;
	}

	public Long getQcorgid_link() {
		return qcorgid_link;
	}

	public void setQcorgid_link(Long qcorgid_link) {
		this.qcorgid_link = qcorgid_link;
	}

	public Integer getEtm_from() {
		return etm_from;
	}

	public void setEtm_from(Integer etm_from) {
		this.etm_from = etm_from;
	}

	public Integer getEtm_to() {
		return etm_to;
	}

	public void setEtm_to(Integer etm_to) {
		this.etm_to = etm_to;
	}

	public Integer getEtm_avr() {
		return etm_avr;
	}

	public void setEtm_avr(Integer etm_avr) {
		this.etm_avr = etm_avr;
	}

	public Long getUsercreatedid_link() {
		return usercreatedid_link;
	}

	public void setUsercreatedid_link(Long usercreatedid_link) {
		this.usercreatedid_link = usercreatedid_link;
	}

	public Date getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getQcorgname() {
		return qcorgname;
	}

	public void setQcorgname(String qcorgname) {
		this.qcorgname = qcorgname;
	}

	public Long getOrgmerchandiseid_link() {
		return orgmerchandiseid_link;
	}

	public Long getMerchandiserid_link() {
		return merchandiserid_link;
	}

	public void setOrgmerchandiseid_link(Long orgmerchandiseid_link) {
		this.orgmerchandiseid_link = orgmerchandiseid_link;
	}

	public void setMerchandiserid_link(Long merchandiserid_link) {
		this.merchandiserid_link = merchandiserid_link;
	}

	public Long getParentpoid_link() {
		return parentpoid_link;
	}

	public void setParentpoid_link(Long parentpoid_link) {
		this.parentpoid_link = parentpoid_link;
	}

	public Boolean getIs_tbd() {
		return is_tbd;
	}

	public void setIs_tbd(Boolean is_tbd) {
		this.is_tbd = is_tbd;
	}

	public Float getSewtarget_percent() {
		return sewtarget_percent;
	}

	public void setSewtarget_percent(Float sewtarget_percent) {
		this.sewtarget_percent = sewtarget_percent;
	}

	public Long getPortfromid_link() {
		return portfromid_link;
	}

	public void setPortfromid_link(Long portfromid_link) {
		this.portfromid_link = portfromid_link;
	}

	public Long getPorttoid_link() {
		return porttoid_link;
	}

	public void setPorttoid_link(Long porttoid_link) {
		this.porttoid_link = porttoid_link;
	}

	public Boolean getIsauto_calculate() {
		return isauto_calculate;
	}

	public void setIsauto_calculate(Boolean isauto_calculate) {
		this.isauto_calculate = isauto_calculate;
	}
	
}
