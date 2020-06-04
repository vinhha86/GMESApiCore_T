package vn.gpay.gsmart.core.salebill;

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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.customer.Customer;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.security.GpayUser;

@Table(name="salebill")
@Entity
public class SaleBill implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salebill_generator")
	@SequenceGenerator(name="salebill_generator", sequenceName = "salebill_id_seq", allocationSize=1)
	protected Long id;

	@Column(name = "orgid_link")
	private Long orgid_link;
	
	@Column(name = "orgbillid_link")
	private Long orgbillid_link;
	
	@Column(name = "billcode")
	private String billcode;
	
	@Column(name = "billdate")
	private Date billdate;
	
	@Column(name = "customerid_link")
	private Integer customerid_link;
	
	@Column(name = "total")
	private Float total;
	
	@Column(name = "discount")
	private Float discount;
	
	@Column(name = "totalamount")
	private Float totalamount;
	
	@Column(name = "vatpercent")
	private Float vatpercent;
	
	@Column(name = "totalvat")
	private Float totalvat;
	
	@Column(name = "totalsum")
	private Float totalsum;
	
	@Column(name = "totalpaid")
	private Float totalpaid;
	
	@Column(name = "totalunpaid")
	private Float totalunpaid;
	
	@Column(name = "extrainfo")
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
	@JoinColumn( name="billid_link", referencedColumnName="id")
	private List<SaleBillSku>  skus  = new ArrayList<>();


	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	@JoinColumn( name="billid_link", referencedColumnName="id")
	private List<SaleBillEpc>  epcs  = new ArrayList<>();
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="usercreateid_link",insertable=false,updatable =false)
    private GpayUser appUser;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="customerid_link",insertable=false,updatable =false)
    private Customer customer;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgbillid_link",insertable=false,updatable =false)
    private Org orgbill;
	
	
	public String getCustomercode() {
		if(customer!=null) {
			return customer.getCustomercode();
		}else {
			return "";
		}
	}
	public String getCustomername() {
		if(customer!=null) {
			return customer.getCustomername();
		}else {
			return "";
		}
	}
	
	public Date getCustomerbirday() {
		if(customer!=null) {
			return customer.getCustomerbirday();
		}else {
			return null;
		}
	}
	public String getCustomermobile() {
		if(customer!=null) {
			return customer.getCustomermobile();
		}else {
			return "";
		}
	}
	public String getSalesname() {
		if(appUser!=null) {
			return appUser.getFullName();
		}else {
			return "";
		}
	}
	
	public String getOrgname() {
		if(orgbill!=null) {
			return orgbill.getName();
		}else {
			return "";
		}
	}
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
	public Long getOrgbillid_link() {
		return orgbillid_link;
	}
	public void setOrgbillid_link(Long orgbillid_link) {
		this.orgbillid_link = orgbillid_link;
	}
	public String getBillcode() {
		return billcode;
	}
	public void setBillcode(String billcode) {
		this.billcode = billcode;
	}
	public Date getBilldate() {
		return billdate;
	}
	public void setBilldate(Date billdate) {
		this.billdate = billdate;
	}
	public Integer getCustomerid_link() {
		return customerid_link;
	}
	public void setCustomerid_link(Integer customerid_link) {
		this.customerid_link = customerid_link;
	}
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}
	public Float getDiscount() {
		return discount;
	}
	public void setDiscount(Float discount) {
		this.discount = discount;
	}
	public Float getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(Float totalamount) {
		this.totalamount = totalamount;
	}
	public Float getVatpercent() {
		return vatpercent;
	}
	public void setVatpercent(Float vatpercent) {
		this.vatpercent = vatpercent;
	}
	public Float getTotalvat() {
		return totalvat;
	}
	public void setTotalvat(Float totalvat) {
		this.totalvat = totalvat;
	}
	public Float getTotalsum() {
		return totalsum;
	}
	public void setTotalsum(Float totalsum) {
		this.totalsum = totalsum;
	}
	public Float getTotalpaid() {
		return totalpaid;
	}
	public void setTotalpaid(Float totalpaid) {
		this.totalpaid = totalpaid;
	}
	public Float getTotalunpaid() {
		return totalunpaid;
	}
	public void setTotalunpaid(Float totalunpaid) {
		this.totalunpaid = totalunpaid;
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
	public List<SaleBillSku> getSkus() {
		return skus;
	}
	public void setSkus(List<SaleBillSku> skus) {
		this.skus = skus;
	}
	public List<SaleBillEpc> getEpcs() {
		return epcs;
	}
	public void setEpcs(List<SaleBillEpc> epcs) {
		this.epcs = epcs;
	}
	public GpayUser getAppUser() {
		return appUser;
	}
	public void setAppUser(GpayUser appUser) {
		this.appUser = appUser;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Org getOrgbill() {
		return orgbill;
	}
	public void setOrgbill(Org orgbill) {
		this.orgbill = orgbill;
	}
}
