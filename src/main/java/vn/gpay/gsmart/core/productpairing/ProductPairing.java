package vn.gpay.gsmart.core.productpairing;

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

import vn.gpay.gsmart.core.product.Product;

@Table(name="product_pairing")
@Entity
public class ProductPairing implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_pairing_generator")
	@SequenceGenerator(name="product_pairing_generator", sequenceName = "product_pairing_id_seq", allocationSize=1)
	private Long id;
	private Long productid_link;
	private Long productpairid_link;
	private Long orgrootid_link;
	private Integer amount;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productid_link",insertable=false,updatable =false)
    private Product product;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productpairid_link",insertable=false,updatable =false)
    private Product parent;
	
	@Transient
	public String getSetCode() {
		if(parent!=null)
			return parent.getBuyercode();
		return "";
	}
	
	@Transient
	public  String getProductName() {
		if(product != null)
			return product.getName();
		return "";
	}
	
	@Transient
	public  String getProductinfo() {
		if(product != null)
			return product.getDescription();
		return "";
	}
	
	@Transient
	public  String getProductCode() {
		if(product != null)
			return product.getCode();
		return "";
	}
	
	@Transient
	public  String getProductBuyerCode() {
		if(product != null)
			return product.getBuyercode();
		return "";
	}
	
	@Transient
	public String getImgurl1() {
		if(product!=null) {
			return product.getImgurl1();
		}
		return "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductid_link() {
		return productid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}

	public Long getProductpairid_link() {
		return productpairid_link;
	}

	public void setProductpairid_link(Long productpairid_link) {
		this.productpairid_link = productpairid_link;
	}

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
