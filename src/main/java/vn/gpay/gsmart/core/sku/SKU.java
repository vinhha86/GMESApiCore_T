package vn.gpay.gsmart.core.sku;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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

import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.utils.AtributeFixValues;

@Table(name="sku")
@Entity
public class SKU implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sku_generator")
	@SequenceGenerator(name="sku_generator", sequenceName = "sku_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	
	private String code;
	
	private String partnercode;
	
	private String barcode;
	
	private Integer skutypeid_link;
	
	private String name;
	
	private String name_en;
	
	private Integer categoryid_link;
	
	private Integer bossid_link;
	
	private Integer providerid_link;
	
	private Integer fabricid_link;
	
	private Integer packingtype;
	
//	private Integer colorid_link;
	
	private Long unitid_link;
	
	private String imgurl1;
	
	private String imgurl2;
	
	private String imgurl3;
	
	private String hscode;
	
	private String hsname;
	
	private Float saleprice;
	
	private BigDecimal discountpercent;
	
	private BigDecimal vatpercent;
	
	private Long productid_link;
	
//	@NotFound(action = NotFoundAction.IGNORE)
//	@OneToMany
//    @JoinColumn(name="productid_link",insertable=false,updatable =false)
//    private List<ProductAttributeValue> listproductvalue = new ArrayList<ProductAttributeValue>();
    
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private List<SKU_Attribute_Value> listSKUvalue = new ArrayList<SKU_Attribute_Value>();
    
    @Transient
    public boolean getIs_default() {
    	for (SKU_Attribute_Value sku_Attribute_Value : listSKUvalue) {
			if(sku_Attribute_Value.getIsdefaultvalue()) {
				return true;
			}			
		}
    	return false;
    }
    
    @Transient
    public int getSort_size() {
    	int sort = 0;
    	for (SKU_Attribute_Value sku_Attribute_Value : listSKUvalue) {
			if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_SIZE || 
					sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_SIZEWIDTH) {
				sort = sku_Attribute_Value.getSort_Size();
				break;
			}			
		}
    	return sort;
    }
    
    @Transient
    public String getMauSanPham() {
    	String name ="";
    	for (SKU_Attribute_Value sku_Attribute_Value : listSKUvalue) {
			if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_COLOR) {
				name = sku_Attribute_Value.getAttributeValueName();
				break;
			}
		}
    	return name;
    }
    
    @Transient
	public Long getColorid_link() {
		long ID_Mau =0;
		for (SKU_Attribute_Value sku_Attribute_Value : listSKUvalue) {
			if(sku_Attribute_Value.getAttributeid_link() == 4) {
				ID_Mau = sku_Attribute_Value.getAttributevalueid_link();
				break;
			}
			else if(sku_Attribute_Value.getAttributeid_link() == 36) {
				ID_Mau = sku_Attribute_Value.getAttributevalueid_link();
				break;
			}
		}
		return ID_Mau;
	}
    
    @Transient
    public String getColor_name() {
    	String name ="";
    	for (SKU_Attribute_Value sku_Attribute_Value : listSKUvalue) {
			if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_COLOR) {
				name = sku_Attribute_Value.getAttributeValueName();
				break;
			} 
		}
    	return name;
    }
    @Transient
    public String getSize_name() {
    	String name ="";
    	for (SKU_Attribute_Value sku_Attribute_Value : listSKUvalue) {
			if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_SIZE) {
				name = sku_Attribute_Value.getAttributeValueName();
				break;
			}
			else if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_SIZEWIDTH) {
				name = sku_Attribute_Value.getAttributeValueName();
				break;
			}	
			else if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_TEX) {
				name = sku_Attribute_Value.getAttributeValueName();
				break;
			}
		}
    	return name;
    }      
    @Transient
    public String getFabric_name() {
    	String name ="";
    	for (SKU_Attribute_Value sku_Attribute_Value : listSKUvalue) {
			if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_FABRIC) {
				name = sku_Attribute_Value.getAttributeValueName();
				break;
			} 
		}
    	return name;
    }    
    @Transient
    public Long getColor_id() {
    	Long name = null;
    	for (SKU_Attribute_Value sku_Attribute_Value : listSKUvalue) {
			if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_COLOR) {
				name = sku_Attribute_Value.getAttributevalueid_link();
				break;
			}
		}
    	return name;
    }
    
    @Transient
    public Long getSize_id() {
    	Long name = null;
    	for (SKU_Attribute_Value sku_Attribute_Value : listSKUvalue) {
			if(sku_Attribute_Value.getAttributeid_link() == 30) {
				name = sku_Attribute_Value.getAttributevalueid_link();
				break;
			}
		}
    	return name;
    }
    
    @Transient
    public String getCoSanPham() {
//    	String name ="";
//    	for (SKU_Attribute_Value sku_Attribute_Value : listSKUvalue) {
//			if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_SIZE) {
//				name = sku_Attribute_Value.getAttributeValueName();
//				break;
//			}
//			else if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_SIZEWIDTH) {
//				name = sku_Attribute_Value.getAttributeValueName();
//				break;
//			}
//			else if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_TEX) {
//				name = sku_Attribute_Value.getAttributeValueName();
//				break;
//			}
//		}
//    	return name;
    	if(product!=null) {
    		return product.getCoKho();
    	}
    	return "";
    }
  
	@Transient
    public String getThanhPhanVai() {
    	if(product!=null)
    		return product.getThanhPhanVai();
    	return "";
    }
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productid_link",insertable=false,updatable =false)
    private Product product;

	@Transient
	public String getProduct_code() {
		if(product != null) {
			return product.getBuyercode();
		}
		return null;
	}
	
	@Transient
	public String getProduct_name() {
		if(product!=null) {
//			if(product.getBuyername() != null)
				return product.getBuyername();
		}
		return "";
	}
	
	@Transient
	public Integer getProducttypeid_link() {
		if(product != null) {
			return product.getProducttypeid_link();
		}
		return null;
	}
	@Transient
	public String getProducttype_name() {
		if(product != null) {
			return product.getProducttype_name();
		}
		return null;
	}
	
	@Transient
	public String getDescription() {
		if(product != null) {
			return product.getDescription();
		}
		return null;
	}
	
	@Transient
	public Float unitPrice;
	
	
	public Float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Float unitPrice) {
		this.unitPrice = unitPrice;
	}

	//    @NotFound(action = NotFoundAction.IGNORE)
//	@ManyToOne
//    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
//    private Unit unit;	
	@Transient
	public String getUnit_name() {
		if(product != null)
			return product.getUnitName();
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSkutypeid_link() {
		return skutypeid_link;
	}

	public void setSkutypeid_link(Integer skutypeid_link) {
		this.skutypeid_link = skutypeid_link;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName_en() {
		return name_en;
	}

	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	public Integer getCategoryid_link() {
		return categoryid_link;
	}

	public void setCategoryid_link(Integer categoryid_link) {
		this.categoryid_link = categoryid_link;
	}

	public Integer getBossid_link() {
		return bossid_link;
	}

	public void setBossid_link(Integer bossid_link) {
		this.bossid_link = bossid_link;
	}

	public Integer getProviderid_link() {
		return providerid_link;
	}

	public void setProviderid_link(Integer providerid_link) {
		this.providerid_link = providerid_link;
	}

	public Integer getFabricid_link() {
		return fabricid_link;
	}

	public void setFabricid_link(Integer fabricid_link) {
		this.fabricid_link = fabricid_link;
	}

	public Integer getPackingtype() {
		return packingtype;
	}

	public void setPackingtype(Integer packingtype) {
		this.packingtype = packingtype;
	}

//	public Integer getColorid_link() {
//		return colorid_link;
//	}
//
//	public void setColorid_link(Integer colorid_link) {
//		this.colorid_link = colorid_link;
//	}

	public Long getUnitid_link() {
		return unitid_link != null ? unitid_link :  product.getUnitid_link();
	}

	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}

	public String getImgurl1() {
		return imgurl1;
	}

	public void setImgurl1(String imgurl1) {
		this.imgurl1 = imgurl1;
	}

	public String getImgurl2() {
		return imgurl2;
	}

	public void setImgurl2(String imgurl2) {
		this.imgurl2 = imgurl2;
	}

	public String getImgurl3() {
		return imgurl3;
	}

	public void setImgurl3(String imgurl3) {
		this.imgurl3 = imgurl3;
	}

	public String getHscode() {
		return hscode;
	}

	public void setHscode(String hscode) {
		this.hscode = hscode;
	}

	public String getHsname() {
		return hsname;
	}

	public void setHsname(String hsname) {
		this.hsname = hsname;
	}

	public Float getSaleprice() {
		return saleprice;
	}

	public void setSaleprice(Float saleprice) {
		this.saleprice = saleprice;
	}

	public BigDecimal getDiscountpercent() {
		return discountpercent;
	}

	public void setDiscountpercent(BigDecimal discountpercent) {
		this.discountpercent = discountpercent;
	}

	public BigDecimal getVatpercent() {
		return vatpercent;
	}

	public void setVatpercent(BigDecimal vatpercent) {
		this.vatpercent = vatpercent;
	}

	public Long getProductid_link() {
		return productid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}

	public void setListSKUvalue(List<SKU_Attribute_Value> listSKUvalue) {
		this.listSKUvalue = listSKUvalue;
	}

	public String getPartnercode() {
		return partnercode;
	}

	public void setPartnercode(String partnercode) {
		this.partnercode = partnercode;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}
