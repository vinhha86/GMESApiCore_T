package vn.gpay.gsmart.core.product_balance_process;

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

import vn.gpay.gsmart.core.product_balance.ProductBalance;
import vn.gpay.gsmart.core.product_sewingcost.ProductSewingCost;

@Table(name="product_balance_process")
@Entity
public class ProductBalanceProcess implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_balance_process_generator")
	@SequenceGenerator(name="product_balance_process_generator", sequenceName = "product_balance_process_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long productbalanceid_link;
	private Long productsewingcostid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productbalanceid_link",insertable=false,updatable =false)
	private ProductBalance productBalance;
	
	@Transient
	public String getBalance_name() {
		if(productBalance!=null)
			return productBalance.getBalance_name();
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productsewingcostid_link",insertable=false,updatable =false)
	private ProductSewingCost productSewingCost;
	
	@Transient
	public String getWorkingprocess_name() {
		if(productSewingCost!=null)
			return productSewingCost.getWorkingprocess_name();
		return "";
	}
	
	@Transient
	public Integer getTimespent_standard() {
		Integer time = 0;
		if(productSewingCost!=null) {
			if(productSewingCost.getTimespent_standard() != null)
				time = productSewingCost.getTimespent_standard();
		}
		return time;
	}
	
	@Transient
	public String getDevice_name() {
		if(productSewingCost!=null) {
			if(productSewingCost.getDevice_name() != null)
				return productSewingCost.getDevice_name();
		}
		return "";
	}
	
	@Transient
	public String getLaborlevel_name() {
		if(productSewingCost!=null) {
			if(productSewingCost.getLaborlevel_name() != null)
				return productSewingCost.getLaborlevel_name();
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

	public Long getProductbalanceid_link() {
		return productbalanceid_link;
	}

	public void setProductbalanceid_link(Long productbalanceid_link) {
		this.productbalanceid_link = productbalanceid_link;
	}

	public Long getProductsewingcostid_link() {
		return productsewingcostid_link;
	}

	public void setProductsewingcostid_link(Long productsewingcostid_link) {
		this.productsewingcostid_link = productsewingcostid_link;
	}
	
	
}
