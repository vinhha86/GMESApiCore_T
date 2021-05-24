package vn.gpay.gsmart.core.pcontractproduct;

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
import vn.gpay.gsmart.core.product.Product;

@Table(name="pcontract_products")
@Entity
public class PContractProduct implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_products_generator")
	@SequenceGenerator(name="pcontract_products_generator", sequenceName = "pcontract_products_id_seq", allocationSize=1)
	private Long id;
	private Long pcontractid_link;
	private Long productid_link;
	private Integer pquantity;
	private Long orgrootid_link;
	private Date production_date;
	private Date delivery_date;
	private Float unitprice;
	private Integer emt;
	private Boolean isbomdone;
	private Boolean isbom2done;
	private Boolean is_breakdown_done;
	
		
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productid_link",insertable=false,updatable =false)
    private Product product;
	
	@Transient
	public String getProductinfo() {
		if(product!=null)
			return product.getDescription();
		return "";
	}
	@Transient
	public String getProductBuyerCode() {
		if(product!=null)
			return product.getBuyercode();
		return "";
	}
	
	@Transient
	public String getProductVendorCode() {
		if(product!=null)
			return product.getVendorcode();
		return "";
	}
	
	@Transient
	public int getProducttypeid_link() {
		if(product!=null) {
			return product.getProducttypeid_link();
		}
		return 0;
	}
	@Transient
	public String getProductName() {
		if(product!=null) {
			return product.getBuyername();
		}
		return "";
	}
	
	@Transient
	public String getProductCode() {
		if(product!=null) {
			return product.getBuyercode();
		}
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

	public Long getPcontractid_link() {
		return pcontractid_link;
	}

	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}

	public Long getProductid_link() {
		return productid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}

	public Integer getPquantity() {
		return pquantity;
	}

	public void setPquantity(Integer pquantity) {
		this.pquantity = pquantity;
	}

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Date getProduction_date() {
		return production_date;
	}

	public Date getDelivery_date() {
		return delivery_date;
	}

	public Float getUnitprice() {
		return unitprice;
	}

	public void setProduction_date(Date production_date) {
		this.production_date = production_date;
	}

	public void setDelivery_date(Date delivery_date) {
		this.delivery_date = delivery_date;
	}

	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}

	public Integer getEmt() {
		return emt;
	}

	public void setEmt(Integer emt) {
		this.emt = emt;
	}

	public Boolean getIsbomdone() {
		return isbomdone;
	}

	public Boolean getIsbom2done() {
		return isbom2done;
	}

	public Boolean getIs_breakdown_done() {
		return is_breakdown_done;
	}

	public void setIsbomdone(Boolean isbomdone) {
		this.isbomdone = isbomdone;
	}

	public void setIsbom2done(Boolean isbom2done) {
		this.isbom2done = isbom2done;
	}

	public void setIs_breakdown_done(Boolean is_breakdown_done) {
		this.is_breakdown_done = is_breakdown_done;
	}
	
	
}
