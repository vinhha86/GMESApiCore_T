package vn.gpay.gsmart.core.porder_req;

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
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.sizeset.SizeSet;
import vn.gpay.gsmart.core.utils.POrderStatus;

@Table(name="porder_req")
@Entity
public class POrder_Req implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porder_req_generator")
	@SequenceGenerator(name="porder_req_generator", sequenceName = "porder_req_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long granttoorgid_link;
	private Long pcontractid_link;
	private Long pcontract_poid_link;
	private Long productid_link;
	private Long sizesetid_link;
	private String ordercode;
	private Date orderdate;
	private Integer totalorder;
	private Long usercreatedid_link;
	private Date timecreated;
	private Integer status;
	private Boolean is_calculate;
	private Integer amount_inset;
	private Date plandate_required;
	

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true)
	@JoinColumn( name="porderreqid_link", referencedColumnName="id")
	private List<POrder>  porderlist  = new ArrayList<POrder>();	
	
	
	public List<POrder> getPorderlist() {
		return porderlist;
	}
	public List<POrder> getPorderlist_running() {
		List<POrder> porder_running = new ArrayList<POrder>();
		for (POrder thePOrder: porderlist){
			if (thePOrder.getStatus() >= POrderStatus.PORDER_STATUS_RUNNING)
				porder_running.add(thePOrder);
		}
		return porder_running;
	}
	public void setPorderlist(List<POrder> porderlist) {
		this.porderlist = porderlist;
	}

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
	
	@Transient
	public Integer get_ProductivityPO() {
		if(pcontract_po!=null)
		{
			for(PContract_PO_Productivity po_productivity: pcontract_po.getPcontract_po_productivity()) {
				if(po_productivity.getProductid_link() != null) {
					if(po_productivity.getProductid_link().equals(productid_link) || po_productivity.getProductid_link() == productid_link) {
						return po_productivity.getPlan_productivity();
					}
				}
			}
		}
		return 0;
	}
	@Transient
	public String getGranttoorgname() {
		if(org != null) {
			return org.getName();
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
	
	@Transient
	public String getProductinfo() {
		if(product != null) {
			 DecimalFormat myFormatter = new DecimalFormat("#,###");
			 Integer amountInset = amount_inset;
			 Integer poQuantity = pcontract_po.getPo_quantity();
			 if(amountInset == null) amountInset = 1;
			 if(poQuantity == null) poQuantity = 0;
			 
//			 String s_quantity =  myFormatter.format(pcontract_po.getPo_quantity()*amount_inset);
			 String s_quantity =  myFormatter.format(poQuantity*amountInset);
			return product.getBuyercode() + " ("+s_quantity+")";
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
			return pcontract.getcls();
		return "";
	}
	
	@Transient
	public String getMahang() {
		String name = "";
		int total = totalorder == null ? 0 : totalorder;
		float totalPO = pcontract_po.getPo_quantity() == null ? 0 : pcontract_po.getPo_quantity();
		
		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		decimalFormat.setGroupingSize(3);
		
		if(product != null && pcontract_po!=null) {
			String ST = product.getBuyercode() == null ? "" : product.getBuyercode();
			String PO = pcontract_po.getPo_buyer() == null ? "" : pcontract_po.getPo_vendor();
			name += "#"+ST+"-PO: "+PO+"-"+decimalFormat.format(total)+"/"+decimalFormat.format(totalPO);
		}
		
		return name;
	}
	
	@Transient
	public String getContractcode() {
		if(pcontract != null) {
			return pcontract.getContractcode();
		}
		return "";
	}
	
	@Transient
	public String getPo_buyer() {
		if(pcontract_po != null) {
			return pcontract_po.getPo_buyer();
		}
		return "";
	}
	
	@Transient
	public String getPo_vendor() {
		if(pcontract_po != null) {
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
	public Long getProductid_link() {
		return productid_link;
	}
	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public PContract getPcontract() {
		return pcontract;
	}

	public PContract_PO getPcontract_po() {
		return pcontract_po;
	}

	public Product getProduct() {
		return product;
	}

	public Boolean getIs_calculate() {
		return is_calculate;
	}

	public void setIs_calculate(Boolean is_calculate) {
		this.is_calculate = is_calculate;
	}

	public Integer getAmount_inset() {
		return amount_inset;
	}

	public void setAmount_inset(Integer amount_inset) {
		this.amount_inset = amount_inset;
	}

	public Date getPlandate_required() {
		return plandate_required;
	}

	public void setPlandate_required(Date plandate_required) {
		this.plandate_required = plandate_required;
	}
	
}
