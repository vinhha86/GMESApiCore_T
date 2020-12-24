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

import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.fob_price.FOBPrice;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.SKU;

@Table(name="pcontract_price_d")
@Entity
public class PContract_Price_D implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_price_d_generator")
	@SequenceGenerator(name="pcontract_price_d_generator", sequenceName = "pcontract_price_d_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long pcontractid_link;
	private Long pcontract_poid_link;
	private Long productid_link;
	private Float price; //giá chào
	private Float cost; // giá vốn
	private Boolean isfob;
	private Long currencyid_link;
	private Float exchangerate;
	private Long usercreatedid_link;
	private Date datecreated;
	private Integer status;
	private Long fobpriceid_link;
	private Long sizesetid_link;
	private Long pcontractpriceid_link;
	private Float quota;
	private Float unitprice;
	private Long unitid_link;
	private Float lost_ratio; // tỉ lệ hao hụt
	private Long materialid_link;
	private Long providerid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	@JoinColumn( name="pcontractprice_d_id_link", referencedColumnName="id")
	private List<PContract_Price_D_SKU> pcontract_price_d_sku = new ArrayList<PContract_Price_D_SKU>();
	
	public List<PContract_Price_D_SKU> getPcontract_price_d_sku() {
		return pcontract_price_d_sku;
	}

	public void setPcontract_price_d_sku(List<PContract_Price_D_SKU> pcontract_price_d_sku) {
		this.pcontract_price_d_sku = pcontract_price_d_sku;
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="materialid_link",insertable=false,updatable =false)
	private SKU material;
	
	@Transient
	public String getMaterialCode() {
		if(material != null) {
			if(material.getCode() != null) {
				return material.getCode();
			}
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="providerid_link",insertable=false,updatable =false)
	private Org provider;
	
	@Transient
	public String getProviderCode() {
		if(provider != null) {
			if(provider.getCode() != null) {
				return provider.getCode();
			}
		}
		return "";
	}
	
	public Long getFobpriceid_link() {
		return fobpriceid_link;
	}

	public void setFobpriceid_link(Long fobpriceid_link) {
		this.fobpriceid_link = fobpriceid_link;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="pcontractpriceid_link",insertable=false,updatable =false)
    private PContract_Price pcontractPrice;
	
	@Transient
	public String getSizesetname() {
		if(pcontractPrice != null) {
			return pcontractPrice.getSizesetname();
		}
		return "";
	}
	
	@Transient
	public Integer getSizesetSortValue() {
		if(pcontractPrice != null) {
			return pcontractPrice.getSortvalue();
		}
		return 0;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="pcontract_poid_link",insertable=false,updatable =false)
    private PContract_PO pContractPO;
	
	@Transient
	public String getCurrencyName() {
		if(pContractPO != null) {
			if(pContractPO.getCurrencyid_link() == null || pContractPO.getCurrencyid_link().equals(0L)) {
				return "US Dollar";
			}
			return pContractPO.getCurrencyName();
		}
		return "US Dollar";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productid_link",insertable=false,updatable =false)
    private Product product;
	
	@Transient
	public String getProductCode() {
		if(product != null) {
			return product.getCode();
		}
		return "";
	}
	
	@Transient
	public String getProductBuyerCode() {
		if(product != null) {
			return product.getBuyercode();
		}
		return "";
	}
	
	@Transient
	public Integer getProductType() {
		if(product != null) {
			return product.getProducttypeid_link();
		}
		return 0;
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="usercreatedid_link",insertable=false,updatable =false)
    private GpayUser usercreated;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="fobpriceid_link",insertable=false,updatable =false)
    private FOBPrice fobprice;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
    private Unit unit;
	
	@Transient
	public String getFobprice_name() {
		if(fobprice != null) {
			return fobprice.getName();
		}
		return "";
	}
	
	@Transient
	public String getUsercreatedName() {
		if(usercreated != null) {
			return usercreated.getFullName();
		}
		return "";
	}
	
	@Transient
	public String getUnitcode() {
		if(unit != null) {
			return unit.getCode();
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


	public Long getCurrencyid_link() {
		return currencyid_link;
	}

	public void setCurrencyid_link(Long currencyid_link) {
		this.currencyid_link = currencyid_link;
	}

	public Float getExchangerate() {
		return exchangerate;
	}

	public void setExchangerate(Float exchangerate) {
		this.exchangerate = exchangerate;
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

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getCost() {
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

	public Boolean getIsfob() {
		return isfob;
	}

	public void setIsfob(Boolean isfob) {
		this.isfob = isfob;
	}

	public Long getSizesetid_link() {
		return sizesetid_link;
	}

	public void setSizesetid_link(Long sizesetid_link) {
		this.sizesetid_link = sizesetid_link;
	}

	public Long getPcontractpriceid_link() {
		return pcontractpriceid_link;
	}

	public void setPcontractpriceid_link(Long pcontractpriceid_link) {
		this.pcontractpriceid_link = pcontractpriceid_link;
	}

	public Float getQuota() {
		return quota;
	}

	public void setQuota(Float quota) {
		this.quota = quota;
	}

	public Float getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}

	public Long getUnitid_link() {
		return unitid_link;
	}

	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}

	public Float getLost_ratio() {
		return lost_ratio;
	}

	public void setLost_ratio(Float lost_ratio) {
		this.lost_ratio = lost_ratio;
	}

	public Long getMaterialid_link() {
		return materialid_link;
	}

	public void setMaterialid_link(Long materialid_link) {
		this.materialid_link = materialid_link;
	}

	public Long getProviderid_link() {
		return providerid_link;
	}

	public void setProviderid_link(Long providerid_link) {
		this.providerid_link = providerid_link;
	}

}
