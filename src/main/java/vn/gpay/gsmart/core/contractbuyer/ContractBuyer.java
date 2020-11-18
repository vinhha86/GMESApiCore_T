package vn.gpay.gsmart.core.contractbuyer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import vn.gpay.gsmart.core.org.Org;

@Table(name="contract_buyer")
@Entity
public class ContractBuyer implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_buyer_generator")
	@SequenceGenerator(name="contract_buyer_generator", sequenceName = "contract_buyer_id_seq", allocationSize=1)
	private Long id;
	private String contract_code;
	private Integer contract_year;
	private Date contract_date;
	private Long buyerid_link;
	private Long vendorid_link;
	private Date contract_date_finish;
	private String comment;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="contractbuyerid_link",insertable=false,updatable =false)
	private List<ContractBuyerD> contractBuyerDs = new ArrayList<ContractBuyerD>();
	
	@Transient
	public String getBuyerCodes() {
		String result = "";
		for(ContractBuyerD cbd : contractBuyerDs) {
			if(result.equals("")) {
				result+=cbd.getBuyerCode();
			}else {
				result+="; "+cbd.getBuyerCode();
			}
		}
		return result;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="buyerid_link",insertable=false,updatable =false)
	private Org buyer;
	
	@Transient
	public String getBuyerName() {
		if(buyer != null) {
			return buyer.getName();
		}
		return "";
	}
	@Transient
	public String getBuyerCode() {
		if(buyer != null) {
			return buyer.getCode();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="vendorid_link",insertable=false,updatable =false)
	private Org vendor;
	
	@Transient
	public String getVendorName() {
		if(vendor != null) {
			return vendor.getName();
		}
		return "";
	}
	@Transient
	public String getVendorCode() {
		if(vendor != null) {
			return vendor.getCode();
		}
		return "";
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContract_code() {
		return contract_code;
	}
	public void setContract_code(String contract_code) {
		this.contract_code = contract_code;
	}
	public Integer getContract_year() {
		return contract_year;
	}
	public void setContract_year(Integer contract_year) {
		this.contract_year = contract_year;
	}
	public Date getContract_date() {
		return contract_date;
	}
	public void setContract_date(Date contract_date) {
		this.contract_date = contract_date;
	}
	public Long getBuyerid_link() {
		return buyerid_link;
	}
	public void setBuyerid_link(Long buyerid_link) {
		this.buyerid_link = buyerid_link;
	}
	public Long getVendorid_link() {
		return vendorid_link;
	}
	public void setVendorid_link(Long vendorid_link) {
		this.vendorid_link = vendorid_link;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getContract_date_finish() {
		return contract_date_finish;
	}
	public void setContract_date_finish(Date contract_date_finish) {
		this.contract_date_finish = contract_date_finish;
	}
	public List<ContractBuyerD> getContractBuyerDs() {
		Collections.sort(contractBuyerDs, new Comparator<ContractBuyerD>() {
			  public int compare(ContractBuyerD o1, ContractBuyerD o2) {
			      return o1.getBuyerCode().compareTo(o2.getBuyerCode());
			  }
			});
		return contractBuyerDs;
	}
	public void setContractBuyerDs(List<ContractBuyerD> contractBuyerDs) {
		this.contractBuyerDs = contractBuyerDs;
	}
	
	
}
