package vn.gpay.gsmart.core.cutplan;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
	private Float la_vai;
	private Float dai_so_do;
	private Float sl_vai;
	private String kho;
	private String so_cay;
	private Date ngay;
	private Integer type;
	private Long product_skuid_link;
	private Integer amount;
	
	
	
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Long getProduct_skuid_link() {
		return product_skuid_link;
	}
	public void setProduct_skuid_link(Long product_skuid_link) {
		this.product_skuid_link = product_skuid_link;
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
	public Float getLa_vai() {
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
	public String getSo_cay() {
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
	public void setLa_vai(Float la_vai) {
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
	public void setSo_cay(String so_cay) {
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
