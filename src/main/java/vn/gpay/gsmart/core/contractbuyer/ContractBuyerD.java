package vn.gpay.gsmart.core.contractbuyer;

import java.io.Serializable;

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

import vn.gpay.gsmart.core.org.Org;

@Table(name="contract_buyer_d")
@Entity
public class ContractBuyerD implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_buyer_d_generator")
	@SequenceGenerator(name="contract_buyer_d_generator", sequenceName = "contract_buyer_d_id_seq", allocationSize=1)
	private Long id;
	private Long contractbuyerid_link;
	private Long buyerid_link;
	
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
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getContractbuyerid_link() {
		return contractbuyerid_link;
	}
	public void setContractbuyerid_link(Long contractbuyerid_link) {
		this.contractbuyerid_link = contractbuyerid_link;
	}
	public Long getBuyerid_link() {
		return buyerid_link;
	}
	public void setBuyerid_link(Long buyerid_link) {
		this.buyerid_link = buyerid_link;
	}
	
}
