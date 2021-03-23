package vn.gpay.gsmart.core.cutplan;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.sku.SKU;

@Table(name="cutplan_row")
@Entity
public class CutPlan_Row implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cutplan_row_generator")
	@SequenceGenerator(name="cutplan_row_generator", sequenceName = "cutplan_row_id_seq", allocationSize=1)
	private Long id;
	private String code;
	private String name;
	private Integer la_vai;
	private Float dai_so_do;
	private Float sl_vai;
	private String kho;
	private Integer so_cay;
	private Date ngay;
	private Integer type;
	private Long material_skuid_link;
	private Long porderid_link;
	private Long createduserid_link;	
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne (optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name="material_skuid_link",insertable=false,updatable =false)
	private SKU sku;
	
	@Transient
	public String getMaSP() {
		if(sku != null) {
			if(sku.getCode() != null) {
				return sku.getCode();
			}
			return "sku.getCode() null";
		}
		return "sku null";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne //(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name="porderid_link",insertable=false,updatable =false)
	private POrder porder;
	
	@Transient
	public String getPordercode() {
		if(porder != null) {
			if(porder.getOrdercode() != null) {
				return porder.getOrdercode();
			}
			return "porder.getOrdercode() null";
		}
		return "porder null";
	}
	
	@Transient
	public Long getPorderId() {
		if(porder != null) {
			if(porder.getId() != null) {
				return porder.getId();
			}
			return null;
		}
		return null;
	}
	
	@Transient
	public Long getPorderPcontractId() {
		if(porder != null) {
			if(porder.getPcontractid_link() != null) {
				return porder.getPcontractid_link();
			}
			return null;
		}
		return null;
	}
	
	@Transient
	public Long getPorderProductId() {
		if(porder != null) {
			if(porder.getProductid_link() != null) {
				return porder.getProductid_link();
			}
			return null;
		}
		return null;
	}
	
	public Long getCreateduserid_link() {
		return createduserid_link;
	}
	public void setCreateduserid_link(Long createduserid_link) {
		this.createduserid_link = createduserid_link;
	}
	public Long getMaterial_skuid_link() {
		return material_skuid_link;
	}
	public void setMaterial_skuid_link(Long material_skuid_link) {
		this.material_skuid_link = material_skuid_link;
	}
	public Long getPorderid_link() {
		return porderid_link;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public Long getId() {
		return id;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public Integer getLa_vai() {
		return la_vai;
	}
	public Float getDai_so_do() {
		return dai_so_do;
	}
	public Float getSl_vai() {
		return sl_vai;
	}
	public String getKho() {
		return kho;
	}
	public Integer getSo_cay() {
		return so_cay;
	}
	public Date getNgay() {
		return ngay;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLa_vai(Integer la_vai) {
		this.la_vai = la_vai;
	}
	public void setDai_so_do(Float dai_so_do) {
		this.dai_so_do = dai_so_do;
	}
	public void setSl_vai(Float sl_vai) {
		this.sl_vai = sl_vai;
	}
	public void setKho(String kho) {
		this.kho = kho;
	}
	public void setSo_cay(Integer so_cay) {
		this.so_cay = so_cay;
	}
	public void setNgay(Date ngay) {
		this.ngay = ngay;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
