package vn.gpay.gsmart.core.pcontractbomcolor;

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

@Table(name="pcontract_bom2_color")
@Entity
public class PContractBom2Color implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_bom_color_generator")
	@SequenceGenerator(name="pcontract_bom_color_generator", sequenceName = "pcontract_bom_color_id_seq", allocationSize=1)
	private Long id;
	
	private Long colorid_link;
	private Long productid_link;
	private Long materialid_link;
	private Float amount;
	private String description;
	private Long createduserid_link;
	private Date createddate;
	private Long orgrootid_link;
	private Long pcontractid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="materialid_link",insertable=false,updatable =false)
    private Product product;
	
	@Transient
	public String getMaterialName() {
		if(product !=null) {
			return product.getName();
		}
		return "";
	}
	
	@Transient
	public String getTenMauNPL() {
		if(product !=null) {
			return product.getTenMauNPL();
		}
		return "";
	}
	
	@Transient
	public String getCoKho() {
		if(product !=null) {
			return product.getCoKho();
		}
		return "";
	}
	
	@Transient
	public int getProduct_type() {
		if(product != null) {
			return (int)product.getProducttypeid_link();
		}
		return 0;
	}
	
	@Transient
	public String getProduct_typeName() {
		if(product != null) {
			return product.getProducttype_name();
		}
		return "";
	}
	
	@Transient
	public String getThanhPhanVai() {
		if(product !=null) {
			return product.getThanhPhanVai();
		}
		return "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getColorid_link() {
		return colorid_link;
	}

	public void setColorid_link(Long colorid_link) {
		this.colorid_link = colorid_link;
	}

	public Long getProductid_link() {
		return productid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}

	public Long getMaterialid_link() {
		return materialid_link;
	}

	public void setMaterialid_link(Long materialid_link) {
		this.materialid_link = materialid_link;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCreateduserid_link() {
		return createduserid_link;
	}

	public void setCreateduserid_link(Long createduserid_link) {
		this.createduserid_link = createduserid_link;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public Long getPcontractid_link() {
		return pcontractid_link;
	}

	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
