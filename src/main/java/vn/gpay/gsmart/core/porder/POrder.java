package vn.gpay.gsmart.core.porder;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import vn.gpay.gsmart.core.pcontract.PContract;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontract_po_productivity.PContract_PO_Productivity;
import vn.gpay.gsmart.core.porder_bom_sku.POrderBOMSKU;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_product.POrder_Product;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.porder_status.POrder_Status;
import vn.gpay.gsmart.core.porderprocessing.POrderProcessing;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.sizeset.SizeSet;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;

@Table(name="porders")
@Entity
public class POrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porder_generator")
	@SequenceGenerator(name="porder_generator", sequenceName = "porders_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long granttoorgid_link;
	private String ordercode;
	private Date orderdate;
	private Long pcontractid_link;
	private Long pcontract_poid_link;
	private Long sizesetid_link;
	private Long productid_link;
	private Integer totalorder;
	private Integer totalorder_req;//SL yeu cau tu Porder_req
	private Integer totalcut;
	private Integer totalstocked;
	private Date golivedate;
	private String golivedesc;
	private Integer balance_status;
	private Date balance_date;
	private Float balance_rate;
	private Date productiondate;
	private Integer productionyear;
	private Integer salaryyear;
	private String season;
	private String collection;
	private String comment;
	private Integer status;
	private Long usercreatedid_link;
	private Date timecreated;
	private Integer salarymonth;
	private Integer priority;
	private Date material_date;
	private Date sample_date;
	private Date cut_date;
	private Date packing_date;
	private Date qc_date;
	private Date stockout_date;
	private Long porderreqid_link;
	private Date productiondate_plan;
	private Date productiondate_fact;
	private Date finishdate_plan;
	private Date finishdate_fact;
	private Boolean isbomdone;
	private Boolean issewingcostdone;
	private Integer plan_productivity;
	private Integer plan_duration;
	private Float plan_linerequired;
//	private Long porder_statusid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="granttoorgid_link",insertable=false,updatable =false)
    private Org org;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="sizesetid_link",insertable=false,updatable =false)
    private SizeSet sizeset;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="pcontractid_link",insertable=false,updatable =false)
    private PContract pcontract;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="pcontract_poid_link",insertable=false,updatable =false)
    private PContract_PO pcontract_po;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productid_link",insertable=false,updatable =false)
    private Product product;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="porderid_link",insertable=false,updatable =false)
    private List<POrderProcessing> list_process = new ArrayList<POrderProcessing>();
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="status",insertable=false,updatable =false)
    private POrder_Status porderstatus;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="porderid_link",insertable=false,updatable =false)
    private List<POrderGrant> list_pordergrant = new ArrayList<POrderGrant>();
	
	@Transient
	public String getPortTo() {
		if(pcontract_po!=null)
			return pcontract_po.getPortTo();
		return "";
	}
	@Transient
	public Integer getDuration() {
		if(pcontract_po != null) {
			for(PContract_PO_Productivity po_productivity: pcontract_po.getPcontract_po_productivity()) {
				if(po_productivity.getProductid_link() != null) {
					if(po_productivity.getProductid_link().equals(productid_link) || po_productivity.getProductid_link() == productid_link) {
						int duration = po_productivity.getProductiondays_ns();
						if(duration == -1) 
							duration = pcontract_po.getProductiondays();
						else if (duration == 0 )
							duration = 1;
						return duration ;
					}
				}
			}
		}
		return null;
	}
	@Transient
	public String getShipMode() {
		if(pcontract_po!=null)
			return pcontract_po.getShipMode();
		return "";
	}
	@Transient
	public String getPackingMethod() {
		if(pcontract_po!=null)
			return pcontract_po.getPackingnotice();
		return "";
	}
	@Transient
	public int getProductivity_po() {
		if(pcontract_po!=null)
			return pcontract_po.getProductivity_byproduct(productid_link);
		return 0;
	}
	@Transient
	public Integer getPo_parent_quantity() {
		if(pcontract_po != null) {
			return pcontract_po.getparent_quantity();
		}
		return 0;
	}
	@Transient
	public Date getStartDatePlan() {
		// lấy ngày vào chuyền sớm nhất của lệnh này theo các tổ đã phân(tính từ ngày hiện tại)
		Date date = null;
		Date currentDate = new Date();
		for(POrderGrant pg : list_pordergrant) {
			if(pg.getStart_date_plan() != null) {
				Date startDate = pg.getStart_date_plan();
				currentDate = GPAYDateFormat.atStartOfDay(currentDate);
				startDate = GPAYDateFormat.atStartOfDay(startDate);
				// so sánh với ngày hiện tại
				if(startDate.compareTo(currentDate) >= 0) {
					// so sánh với ngày vào chuyền hiện tại
					if(date == null) {
						date = startDate;
					}else {
						if(date.compareTo(startDate) > 0) {
							date = startDate;
						}
					}
				}
			}
		}
		// nếu không có ngày vào chuyền từ sau ngày hiện tại
		if(date == null) {
			for(POrderGrant pg : list_pordergrant) {
				if(pg.getStart_date_plan() != null) {
					Date startDate = pg.getStart_date_plan();
					startDate = GPAYDateFormat.atStartOfDay(startDate);
					if(date == null) {
						date = startDate;
					}else {
						if(date.compareTo(startDate) > 0) {
							date = startDate;
						}
					}
				}
			}
		}
		return date;
	}
	@Transient
	public Long getPO_Offer() {
		if(pcontract_po!=null)
			return pcontract_po.getParentpoid_link();
		return null;
	}
	
	@Transient
	public String getStatusName() {
		if(porderstatus != null) {
			return porderstatus.getName();
		}
		return "";
	}	
	
	@Transient
	public int getTotal_process() {
		int total = 0;
		for(POrderProcessing process : list_process) {
			total += process.getAmountinput() == null ? 0: process.getAmountinput();
		}
		return total;
	}
	
	@Transient
	public String getStylebuyer() {
		if(product != null) {
			if(product.getBuyercode() != null)
				return product.getBuyercode();
		}
		return "";
	}	
	@Transient
	public String getStylevendor() {
		if(product != null) {
			if(product.getVendorcode() != null)
				return product.getVendorcode();
		}
		return "";
	}	
	
	@Transient
	public String getGranttoorgname() {
		if(org != null) {
			return org.getName();
		}
		return "";
	}
	
	@Transient
	public String getGranttoorgcode() {
		if(org != null) {
			return org.getCode();
		}
		return "";
	}
		
	@Transient
	public String getSizesetname() {
		if(sizeset != null) {
			return sizeset.getName();
		}
		return "";
	}
	
	@Transient 
	public String getCls() {
		if(pcontract!=null)
			if(status != null && status > -1)
				return pcontract.getcls();
			else
				return "test";
		return "test";
	}
	
	@Transient
	public String getMaHang() {
		String name = "";
		int total = totalorder == null ? 0 : totalorder;
		float totalPO = pcontract_po == null ? 0 : pcontract_po.getPo_quantity();
		
		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		decimalFormat.setGroupingSize(3);
		
		if(product != null && pcontract_po!=null) {
			String buyer = product.getBuyercode() == null ? "" : product.getBuyercode();
			String po_buyer = pcontract_po.getPo_buyer();
			String PO = po_buyer == null ? pcontract_po.getPo_vendor() : po_buyer;
			name += buyer+"/"+PO+"/"+decimalFormat.format(total)+"/"+decimalFormat.format(totalPO);
		}
		
		return name;
	}
	
	@Transient
	public String getMerName() {
		if(pcontract_po!=null)
			return pcontract_po.getMerchandiser_name();
		return "";
	}
	
	@Transient
	public String getContractcode() {
		if(pcontract != null) {
			return pcontract.getContractcode();
		}
		return "";
	}
	
	@Transient
	public String getProductcode() {
		if(product != null) {
			return product.getBuyercode();
		}
		return "";
	}
	
	@Transient
	public String getPo_buyer() {
		if(pcontract_po != null) {
			if(pcontract_po.getPo_buyer() != null)
				return pcontract_po.getPo_buyer();
		}
		return "";
	}
	
	@Transient
	public String getPo_vendor() {
		if(pcontract_po != null) {
			if(pcontract_po.getPo_vendor() != null)
				return pcontract_po.getPo_vendor();
		}
		return "";
	}
	@Transient
	public Integer getPo_quantity() {
		if(pcontract_po != null) {
			return pcontract_po.getPo_quantity();
		}
		return null;
	}
	@Transient
	public Date getShipdate() {
		if(pcontract_po != null) {
			return pcontract_po.getShipdate();
		}
		return null;
	}
	@Transient
	public Date getMatdate() {
		if(pcontract_po != null) {
			return pcontract_po.getMatdate();
		}
		return null;
	}
	@Transient
	public Date getPO_Productiondate() {
		if(pcontract_po != null) {
			return pcontract_po.getProductiondate();
		}
		return null;
	}
	@Transient
	public String getQcorgname() {
		if(pcontract_po != null) {
			return pcontract_po.getQcorgname();
		}
		return "";
	}
	@Transient
	public String getPackingnotice() {
		if(pcontract_po != null) {
			return pcontract_po.getPackingnotice();
		}
		return "";
	}
	
	@Transient
	public String getBuyercode() {
		if(product != null) {
			return product.getBuyercode();
		}
		return "";
	}
	
	@Transient
	public String getImageurl() {
		if(product != null) {
			return product.getImgurl1();
		}
		return "";
	}
	
	@Transient
	public String getProductname() {
		if(product != null) {
			return product.getName();
		}
		return "";
	}
	
	@Transient
	public String getBuyername() {
		if(pcontract != null) {
			return pcontract.getBuyername();
		}
		return "";
	}
	
	@Transient
	public String getVendorname() {
		if(pcontract != null) {
			return pcontract.getVendorname();
		}
		return "";
	}
	
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	@JoinColumn( name="porderid_link", referencedColumnName="id")
	private List<POrder_Product>  porder_product  = new ArrayList<>();
	
	public List<POrder_Product> getPorder_product() {
		return porder_product;
	}
	public void setPorder_product(List<POrder_Product> porder_product) {
		this.porder_product = porder_product;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
	@JoinColumn( name="porderid_link", referencedColumnName="id",insertable=false,updatable =false)
	private List<POrder_Product_SKU>  porder_product_sku  = new ArrayList<>();
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	@JoinColumn( name="porderid_link", referencedColumnName="id")
	private List<POrderBOMSKU>  porder_bom_sku  = new ArrayList<>();	
	
	public List<POrder_Product_SKU> getPorder_product_sku() {
		return porder_product_sku;
	}
	public void setPorder_product_sku(List<POrder_Product_SKU> porder_product_sku) {
		this.porder_product_sku = porder_product_sku;
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
	public Long getGranttoorgid_link() {
		return granttoorgid_link;
	}
	public void setGranttoorgid_link(Long granttoorgid_link) {
		this.granttoorgid_link = granttoorgid_link;
	}
	public String getOrdercode() {
		return ordercode;
	}
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	public Date getOrderdate() {
		return orderdate;
	}
	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}
	public Long getPcontractid_link() {
		return pcontractid_link;
	}
	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
	public Integer getTotalorder() {
		return totalorder;
	}
	public void setTotalorder(Integer totalorder) {
		this.totalorder = totalorder;
	}
	public Integer getTotalcut() {
		return totalcut;
	}
	public void setTotalcut(Integer totalcut) {
		this.totalcut = totalcut;
	}
	public Integer getTotalstocked() {
		return totalstocked;
	}
	public void setTotalstocked(Integer totalstocked) {
		this.totalstocked = totalstocked;
	}
	public Date getGolivedate() {
		return golivedate;
	}
	public void setGolivedate(Date golivedate) {
		this.golivedate = golivedate;
	}
	public String getGolivedesc() {
		return golivedesc;
	}
	public void setGolivedesc(String golivedesc) {
		this.golivedesc = golivedesc;
	}
	public Integer getBalance_status() {
		return balance_status;
	}
	public void setBalance_status(Integer balance_status) {
		this.balance_status = balance_status;
	}
	public Date getBalance_date() {
		return balance_date;
	}
	public void setBalance_date(Date balance_date) {
		this.balance_date = balance_date;
	}
	public Float getBalance_rate() {
		return balance_rate;
	}
	public void setBalance_rate(Float balance_rate) {
		this.balance_rate = balance_rate;
	}
	public Date getProductiondate() {
		return productiondate;
	}
	public void setProductiondate(Date productiondate) {
		this.productiondate = productiondate;
	}
	public Integer getProductionyear() {
		return productionyear;
	}
	public void setProductionyear(Integer productionyear) {
		this.productionyear = productionyear;
	}
	public Integer getSalaryyear() {
		return salaryyear;
	}
	public void setSalaryyear(Integer salaryyear) {
		this.salaryyear = salaryyear;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getUsercreatedid_link() {
		return usercreatedid_link;
	}
	public void setUsercreatedid_link(Long usercreatedid_link) {
		this.usercreatedid_link = usercreatedid_link;
	}
	public Date getTimecreated() {
		return timecreated;
	}
	public void setTimecreated(Date timecreated) {
		this.timecreated = timecreated;
	}
	public Integer getSalarymonth() {
		return salarymonth;
	}
	public void setSalarymonth(Integer salarymonth) {
		this.salarymonth = salarymonth;
	}
	public Long getProductid_link() {
		return productid_link;
	}
	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Date getMaterial_date() {
		return material_date;
	}
	public void setMaterial_date(Date material_date) {
		this.material_date = material_date;
	}
	public Date getSample_date() {
		return sample_date;
	}
	public void setSample_date(Date sample_date) {
		this.sample_date = sample_date;
	}
	public Date getCut_date() {
		return cut_date;
	}
	public void setCut_date(Date cut_date) {
		this.cut_date = cut_date;
	}
	public Date getPacking_date() {
		return packing_date;
	}
	public void setPacking_date(Date packing_date) {
		this.packing_date = packing_date;
	}
	public Date getQc_date() {
		return qc_date;
	}
	public void setQc_date(Date qc_date) {
		this.qc_date = qc_date;
	}
	public Date getStockout_date() {
		return stockout_date;
	}
	public void setStockout_date(Date stockout_date) {
		this.stockout_date = stockout_date;
	}
	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}
	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}
	public Long getSizesetid_link() {
		return sizesetid_link;
	}
	public void setSizesetid_link(Long sizesetid_link) {
		this.sizesetid_link = sizesetid_link;
	}

	public Long getPorderreqid_link() {
		return porderreqid_link;
	}

	public Date getProductiondate_plan() {
		return productiondate_plan;
	}

	public Date getProductiondate_fact() {
		return productiondate_fact;
	}

	public Date getFinishdate_plan() {
		return finishdate_plan;
	}

	public Date getFinishdate_fact() {
		return finishdate_fact;
	}

	public void setPorderreqid_link(Long porderreqid_link) {
		this.porderreqid_link = porderreqid_link;
	}

	public void setProductiondate_plan(Date productiondate_plan) {
		this.productiondate_plan = productiondate_plan;
	}

	public void setProductiondate_fact(Date productiondate_fact) {
		this.productiondate_fact = productiondate_fact;
	}

	public void setFinishdate_plan(Date finishdate_plan) {
		this.finishdate_plan = finishdate_plan;
	}

	public void setFinishdate_fact(Date finishdate_fact) {
		this.finishdate_fact = finishdate_fact;
	}

	public List<POrderGrant> getList_pordergrant() {
		return list_pordergrant;
	}

	public void setList_pordergrant(List<POrderGrant> list_pordergrant) {
		this.list_pordergrant = list_pordergrant;
	}

	public Integer getTotalorder_req() {
		return totalorder_req;
	}

	public void setTotalorder_req(Integer totalorder_req) {
		this.totalorder_req = totalorder_req;
	}

	public Boolean getIsbomdone() {
		return isbomdone;
	}

	public void setIsbomdone(Boolean isbomdone) {
		this.isbomdone = isbomdone;
	}

	public Boolean getIssewingcostdone() {
		return issewingcostdone;
	}

	public void setIssewingcostdone(Boolean issewingcostdone) {
		this.issewingcostdone = issewingcostdone;
	}

	public Integer getPlan_productivity() {
		return plan_productivity == null ? 0 : plan_productivity;
	}

	public void setPlan_productivity(Integer plan_productivity) {
		this.plan_productivity = plan_productivity;
	}

	public Float getPlan_linerequired() {
		return plan_linerequired;
	}

	public void setPlan_linerequired(Float plan_linerequired) {
		this.plan_linerequired = plan_linerequired;
	}

	public Integer getPlan_duration() {
		return plan_duration == null ? 1 : plan_duration;
	}

	public void setPlan_duration(Integer plan_duration) {
		this.plan_duration = plan_duration;
	}
	
	
	
//	public Long getPorder_statusid_link() {
//		return porder_statusid_link;
//	}
//	public void setPorder_statusid_link(Long porder_statusid_link) {
//		this.porder_statusid_link = porder_statusid_link;
//	}
//	
}
