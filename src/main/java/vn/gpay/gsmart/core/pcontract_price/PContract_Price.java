package vn.gpay.gsmart.core.pcontract_price;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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

import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.sizeset.SizeSet;


@Table(name="pcontract_price")
@Entity
public class PContract_Price implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_price_generator")
	@SequenceGenerator(name="pcontract_price_generator", sequenceName = "pcontract_price_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long pcontractid_link;
	private Long pcontract_poid_link;
	private Long productid_link;
	private Long sizesetid_link;
	private Float price_cmp;
	private Float price_fob;
	private Float sewfobratio;
	private Float price_sewingtarget;
	private Float price_sewingcost;
	private Float totalprice;
	private Float salaryfund;
	private Integer quantity;
	private Float price_vendortarget;
	private Boolean is_fix;
	private Date date_importdata;
	
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="sizesetid_link",insertable=false,updatable =false)
    private SizeSet sizeset;
	@Transient
	public String getSizesetname() {
		if(sizeset != null) {
			return sizeset.getName();
		}
		return "";
	}
	@Transient
	public Integer getSortvalue() {
		if(sizeset != null) {
			return sizeset.getSortvalue();
		}
		return 0;
	}
	
	public List<String> getlist_sizename(){
		if(sizeset != null) {
			return sizeset.getlist_sizename();
		}
		return new ArrayList<String>();
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productid_link",insertable=false,updatable =false)
    private Product product;
	@Transient
	public Integer getProducttypeid_link() {
		if(product != null) {
			return product.getProducttypeid_link();
		}
		return null;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	@JoinColumn( name="pcontractpriceid_link", referencedColumnName="id")
	private List<PContract_Price_D>  pcontract_price_d  = new ArrayList<>();
	
	
	public List<PContract_Price_D> getPcontract_price_d() {
		return pcontract_price_d;
	}
	public void setPcontract_price_d(List<PContract_Price_D> pcontract_price_d) {
		this.pcontract_price_d = pcontract_price_d;
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
	public Long getPcontractid_link() {
		return pcontractid_link;
	}
	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}
	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}
	public Long getProductid_link() {
		return productid_link;
	}
	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}
	public Long getSizesetid_link() {
		return sizesetid_link;
	}
	public void setSizesetid_link(Long sizesetid_link) {
		this.sizesetid_link = sizesetid_link;
	}
	public Float getPrice_cmp() {
		return price_cmp;
	}
	public void setPrice_cmp(Float price_cmp) {
		this.price_cmp = price_cmp;
	}
	public Float getPrice_fob() {
		return price_fob;
	}
	public void setPrice_fob(Float price_fob) {
		this.price_fob = price_fob;
	}
	public Float getPrice_sewingtarget() {
		return price_sewingtarget;
	}
	public void setPrice_sewingtarget(Float price_sewingtarget) {
		this.price_sewingtarget = price_sewingtarget;
	}
	public Float getPrice_sewingcost() {
		return price_sewingcost;
	}
	public void setPrice_sewingcost(Float price_sewingcost) {
		this.price_sewingcost = price_sewingcost;
	}
	public Float getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(Float totalprice) {
		this.totalprice = totalprice;
	}
	public Float getSalaryfund() {
		return salaryfund;
	}
	public void setSalaryfund(Float salaryfund) {
		this.salaryfund = salaryfund;
	}
	public Float getSewfobratio() {
		return sewfobratio;
	}
	public void setSewfobratio(Float sewfobratio) {
		this.sewfobratio = sewfobratio;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Float getPrice_vendortarget() {
		return price_vendortarget;
	}
	public void setPrice_vendortarget(Float price_vendortarget) {
		this.price_vendortarget = price_vendortarget;
	}
	public Boolean getIs_fix() {
		return is_fix;
	}
	public void setIs_fix(Boolean is_fix) {
		this.is_fix = is_fix;
	}
	public Date getDate_importdata() {
		return date_importdata;
	}
	public void setDate_importdata(Date date_importdata) {
		this.date_importdata = date_importdata;
	}

}
