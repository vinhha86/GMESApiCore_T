package vn.gpay.gsmart.core.pcontract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import vn.gpay.gsmart.core.branch.Branch;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontractproduct.PContractProduct;
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
	private Long orgpayerid_link;
	private Long orgvendorid_link;
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
	private Integer lengthunitid_link;
	private Integer weightunitid_link;
	private Integer contracttypeid_link;
	private Long orgmerchandiseid_link;
	private Long merchandiserid_link;
	private Long orgshowid_link;
	private Long marketypeid_link;
	
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
    @JoinColumn(name="branchid_link",insertable=false,updatable =false)
    private Branch branch;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgshowid_link",insertable=false,updatable =false)
    private Org orgShow;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgbuyerid_link",insertable=false,updatable =false)
    private Org buyer;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgvendorid_link",insertable=false,updatable =false)
    private Org vendor;
	
	@Transient
	public String getBuyercode() {
		if(buyer!=null)
			return buyer.getCode();
		return "";
	}
	@Transient
	public String getBuyername() {
		if(buyer!=null)
			return buyer.getName();
		return "";
	}
	
	@Transient
	public String getVendorname() {
		if(vendor!=null)
			return vendor.getName();
		return "";
	}
	
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
	public String getcls() {
		if(orgShow!=null )
			return orgShow.getCls();
		else
			return "";
	}
	
	@Transient
	public int getPayer() {
		if(orgpayerid_link == orgvendorid_link )
			return 1;
		else
			return 2;
	}
	
	@Transient
	public int getOrgshow() {
		if(orgpayerid_link == orgvendorid_link )
			return 1;
		else
			return 2;
	}
	
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

	public Long getOrgpayerid_link() {
		return orgpayerid_link;
	}

	public void setOrgpayerid_link(Long orgpayerid_link) {
		this.orgpayerid_link = orgpayerid_link;
	}

	public Long getOrgvendorid_link() {
		return orgvendorid_link;
	}

	public void setOrgvendorid_link(Long orgvendorid_link) {
		this.orgvendorid_link = orgvendorid_link;
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

	public void setUsercreated(GpayUser usercreated) {
		this.usercreated = usercreated;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
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

	public Integer getLengthunitid_link() {
		return lengthunitid_link;
	}

	public void setLengthunitid_link(Integer lengthunitid_link) {
		this.lengthunitid_link = lengthunitid_link;
	}

	public Integer getWeightunitid_link() {
		return weightunitid_link;
	}

	public void setWeightunitid_link(Integer weightunitid_link) {
		this.weightunitid_link = weightunitid_link;
	}

	public Integer getContracttypeid_link() {
		return contracttypeid_link;
	}

	public void setContracttypeid_link(Integer contracttypeid_link) {
		this.contracttypeid_link = contracttypeid_link;
	}

	public Long getOrgmerchandiseid_link() {
		return orgmerchandiseid_link;
	}

	public void setOrgmerchandiseid_link(Long orgmerchandiseid_link) {
		this.orgmerchandiseid_link = orgmerchandiseid_link;
	}

	public Long getMerchandiserid_link() {
		return merchandiserid_link;
	}

	public void setMerchandiserid_link(Long merchandiserid_link) {
		this.merchandiserid_link = merchandiserid_link;
	}

	public Long getOrgshowid_link() {
		return orgshowid_link;
	}

	public void setOrgshowid_link(Long orgshowid_link) {
		this.orgshowid_link = orgshowid_link;
	}

	public Long getMarketypeid_link() {
		return marketypeid_link;
	}

	public void setMarketypeid_link(Long marketypeid_link) {
		this.marketypeid_link = marketypeid_link;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="pcontractid_link",insertable=false,updatable =false)
    private List<PContract_PO> pos = new ArrayList<PContract_PO>();
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="pcontractid_link",insertable=false,updatable =false)
    private List<PContractProduct> products = new ArrayList<PContractProduct>();
	
	@Transient
	public String getProductlist() {
		String a = "";
		if(products!=null ){
			for(PContractProduct product:products){
				a += product.getProductBuyerCode() + "; ";
			}
		}
		return a;
	}
	@Transient
	public String getProductVendorCodelist() {
		String a = "";
		if(products!=null ){
			for(PContractProduct product:products){
				a += product.getProductVendorCode() + "; ";
			}
		}
		return a;
	}
	@Transient
	public String getPolist() {
		String a = "";
		if(pos!=null ){
			for(PContract_PO po:pos){
				a += po.getPo_buyer() + "; ";
			}
		}
		return a;
	}
}
