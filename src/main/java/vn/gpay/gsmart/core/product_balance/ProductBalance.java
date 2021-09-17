package vn.gpay.gsmart.core.product_balance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.product_balance_process.ProductBalanceProcess;

@Table(name="product_balance")
@Entity
public class ProductBalance implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_balance_generator")
	@SequenceGenerator(name="product_balance_generator", sequenceName = "product_balance_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long productid_link;
	private String balance_name;
	private Long prevbalanceid_link;
	private Long parentbalanceid_link;
	private Integer sortvalue;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="productbalanceid_link",insertable=false,updatable =false)
    private List<ProductBalanceProcess> productBalanceProcesses = new ArrayList<ProductBalanceProcess>();
	
	@Transient
	public String getWorkingprocess_name() {
		String name = "";
		for(ProductBalanceProcess productBalanceProcess : productBalanceProcesses) {
			if(productBalanceProcess.getWorkingprocess_name() != null) {
				if(name.equals("")) {
					name += productBalanceProcess.getWorkingprocess_name();
				}else {
					name += "/ " + productBalanceProcess.getWorkingprocess_name();
				}
			}
		}
		return name;
	}
	
	@Transient
	public Integer getTimespent_standard() {
		int time = 0;
		for(ProductBalanceProcess productBalanceProcess : productBalanceProcesses) {
			if(productBalanceProcess.getTimespent_standard() != null) {
				time += productBalanceProcess.getTimespent_standard();
			}
		}
		return time;
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
	
	public Long getProductid_link() {
		return productid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}

	public String getBalance_name() {
		return balance_name;
	}
	public void setBalance_name(String balance_name) {
		this.balance_name = balance_name;
	}
	public Long getPrevbalanceid_link() {
		return prevbalanceid_link;
	}
	public void setPrevbalanceid_link(Long prevbalanceid_link) {
		this.prevbalanceid_link = prevbalanceid_link;
	}
	public Long getParentbalanceid_link() {
		return parentbalanceid_link;
	}
	public void setParentbalanceid_link(Long parentbalanceid_link) {
		this.parentbalanceid_link = parentbalanceid_link;
	}
	public Integer getSortvalue() {
		return sortvalue;
	}
	public void setSortvalue(Integer sortvalue) {
		this.sortvalue = sortvalue;
	}
	public List<ProductBalanceProcess> getProductBalanceProcesses() {
		return productBalanceProcesses;
	}
	public void setProductBalanceProcesses(List<ProductBalanceProcess> productBalanceProcesses) {
		this.productBalanceProcesses = productBalanceProcesses;
	}

}
