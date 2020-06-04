package vn.gpay.gsmart.core.pcontract;

import java.io.Serializable;
import java.util.Date;

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

import vn.gpay.gsmart.core.branch.Branch;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.season.Season;
import vn.gpay.gsmart.core.security.GpayUser;

@Table(name="pcontract")
@Entity
public class PContract implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_generator")
	@SequenceGenerator(name="pcontract_generator", sequenceName = "pcontract_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long orgcustomerid_link;
	private Long orgvenderid_link;
	private Long orgbuyerid_link;
	private String cust_contractcode;
	private String contractcode;
	private Date contractdate;
	private Date confirmdate;
	private Long seasonid_link;
	private Long branchid_link;
	private String description;
	private Long usercreatedid_link;
	private Date datecreated;
	private Integer status;
	private Float complete_rate;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="usercreatedid_link",insertable=false,updatable =false)
    private GpayUser usercreated;
	
	@Transient
	public String getUsercreatedName() {
		if(usercreated != null) {
			return usercreated.getFullName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgcustomerid_link",insertable=false,updatable =false)
    private Org customer;
	
	@Transient
	public String getOrgcustomerName() {
		if(customer != null) {
			return customer.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="branchid_link",insertable=false,updatable =false)
    private Branch branch;
	
	@Transient
	public String getBranchName() {
		if(branch != null) {
			return branch.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="seasonid_link",insertable=false,updatable =false)
    private Season season;
	
	@Transient
	public String getSeasonName() {
		if(season != null) {
			return season.getName();
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

	public Long getOrgcustomerid_link() {
		return orgcustomerid_link;
	}

	public void setOrgcustomerid_link(Long orgcustomerid_link) {
		this.orgcustomerid_link = orgcustomerid_link;
	}

	public Long getOrgvenderid_link() {
		return orgvenderid_link;
	}

	public void setOrgvenderid_link(Long orgvenderid_link) {
		this.orgvenderid_link = orgvenderid_link;
	}

	public Long getOrgbuyerid_link() {
		return orgbuyerid_link;
	}

	public void setOrgbuyerid_link(Long orgbuyerid_link) {
		this.orgbuyerid_link = orgbuyerid_link;
	}

	public String getCust_contractcode() {
		return cust_contractcode;
	}

	public void setCust_contractcode(String cust_contractcode) {
		this.cust_contractcode = cust_contractcode;
	}

	public String getContractcode() {
		return contractcode;
	}

	public void setContractcode(String contractcode) {
		this.contractcode = contractcode;
	}

	public Date getContractdate() {
		return contractdate;
	}

	public void setContractdate(Date contractdate) {
		this.contractdate = contractdate;
	}



	public Date getConfirmdate() {
		return confirmdate;
	}

	public void setConfirmdate(Date confirmdate) {
		this.confirmdate = confirmdate;
	}

	public Long getSeasonid_link() {
		return seasonid_link;
	}

	public void setSeasonid_link(Long seasonid_link) {
		this.seasonid_link = seasonid_link;
	}

	public Long getBranchid_link() {
		return branchid_link;
	}

	public void setBranchid_link(Long branchid_link) {
		this.branchid_link = branchid_link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public GpayUser getUsercreated() {
		return usercreated;
	}

	public void setUsercreated(GpayUser usercreated) {
		this.usercreated = usercreated;
	}

	public Org getCustomer() {
		return customer;
	}

	public void setCustomer(Org customer) {
		this.customer = customer;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}

	public Float getComplete_rate() {
		return complete_rate;
	}

	public void setComplete_rate(Float complete_rate) {
		this.complete_rate = complete_rate;
	}
}
