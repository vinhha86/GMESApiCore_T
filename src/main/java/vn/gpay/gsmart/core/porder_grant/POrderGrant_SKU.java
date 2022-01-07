package vn.gpay.gsmart.core.porder_grant;
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

import com.fasterxml.jackson.annotation.JsonProperty;

import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.sku.SKU;


@Table(name="porder_grant_sku")
@Entity
public class POrderGrant_SKU implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porder_grant_sku_generator")
	@SequenceGenerator(name="porder_grant_sku_generator", sequenceName = "porder_grant_sku_id_seq", allocationSize=1)
	protected Long id;
    private Long orgrootid_link;
    
    @JsonProperty("pordergrantid_link")
    private Long pordergrantid_link;
    
    @JsonProperty("skuid_link")
    private Long skuid_link;
    
    @JsonProperty("grantamount")
    private Integer grantamount;
    
    @JsonProperty("pcontract_poid_link")
    private Long pcontract_poid_link;
    
    @Transient
    public Integer amount_break;
    @Transient
    public String ma_SanPham;
    @Transient
    public String ten_SanPham;
    @Transient
    public String ma_SKU;
    @Transient
    public String mau_SanPham;
    @Transient
    public String co_SanPham;
    
    @NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="pcontract_poid_link",insertable=false,updatable =false)
    private PContract_PO pcontractPo;
    
    @NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="pordergrantid_link",insertable=false,updatable =false)
    private POrderGrant porder_grant;
    
    @Transient
    public Long getPorderid_link() {
    	if(porder_grant != null) {
        	return porder_grant.getPorderid_link();
    	}
    	return (long) 0;
    }
    @Transient
	public String getPcontractPo_PoBuyer() {
		if(pcontractPo!=null) {
			if(pcontractPo.getPo_buyer() != null) {
				return pcontractPo.getPo_buyer();
			}
		}
		return "";
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private SKU sku;

	@Transient
	public int getSort_size() {
		if(sku!=null)
			return sku.getSort_size();
		return 0;
	}
	
	@Transient
	public String getSku_product_code() {
		if(sku != null) {
			return sku.getProduct_code();
		}
		return "";
	}
	
	@Transient
	public Long getProductid_link() {
		if(sku != null) {
			return sku.getProductid_link();
		}
		return (long)0;
	}
	
	@Transient
	public Long getColorid_link() {
		if(sku != null) {
			return sku.getColorid_link();
		}
		return (long)0;
	}
	
	@Transient
	public Long getSize_id() {
		if(sku != null) {
			return sku.getSize_id();
		}
		return (long)0;
	}
	
	@Transient
	public String getProduct_name() {
		if(sku != null) {
			return sku.getProduct_name();
		}
		return "";
	}
	
	@Transient
	@JsonProperty("skucode")
	public String getSkucode() {
		if(sku!=null) {
			return sku.getCode();
		}
		return "";
	}
	@Transient
	public String getSkuname() {
		if(sku!=null) {
			return sku.getName();
		}
		return "";
	}
	@Transient
	public String getMauSanPham() {
		if(sku!=null) {
			return sku.getMauSanPham();
		}
		return "";
	}
	@Transient
	public String getCoSanPham() {
		if(sku!=null) {
			return sku.getCoSanPham();
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

	public Long getPordergrantid_link() {
		return pordergrantid_link;
	}

	public void setPordergrantid_link(Long pordergrantid_link) {
		this.pordergrantid_link = pordergrantid_link;
	}

	public Long getSkuid_link() {
		return skuid_link;
	}

	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}

	public Integer getGrantamount() {
		return grantamount;
	}

	public void setGrantamount(Integer grantamount) {
		this.grantamount = grantamount;
	}

	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}

	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}
	public Integer getAmount_break() {
		return amount_break;
	}
	public void setAmount_break(Integer amount_break) {
		this.amount_break = amount_break;
	}
	public String getMa_SanPham() {
		return ma_SanPham;
	}
	public void setMa_SanPham(String ma_SanPham) {
		this.ma_SanPham = ma_SanPham;
	}
	public String getTen_SanPham() {
		return ten_SanPham;
	}
	public void setTen_SanPham(String ten_SanPham) {
		this.ten_SanPham = ten_SanPham;
	}
	public String getMa_SKU() {
		return ma_SKU;
	}
	public void setMa_SKU(String ma_SKU) {
		this.ma_SKU = ma_SKU;
	}
	public String getMau_SanPham() {
		return mau_SanPham;
	}
	public void setMau_SanPham(String mau_SanPham) {
		this.mau_SanPham = mau_SanPham;
	}
	public String getCo_SanPham() {
		return co_SanPham;
	}
	public void setCo_SanPham(String co_SanPham) {
		this.co_SanPham = co_SanPham;
	}
	
}
