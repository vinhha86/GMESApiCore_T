package vn.gpay.gsmart.core.cutplan_processing;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="cutplan_processing_d")
@Entity
public class CutplanProcessingD implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cutplan_processing_d_generator")
	@SequenceGenerator(name="cutplan_processing_d_generator", sequenceName = "cutplan_processing_d_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name = "orgrootid_link")
	private Long orgrootid_link;
	
	@Column(name = "cutplan_processingid_link")
	private Long cutplan_processingid_link;
	
	@Column(name = "warehouseid_link")
	private Long warehouseid_link;
	
	@Column(name = "epc")
	private String epc;
	
	@Column(name = "barcode")
	private String barcode;
	
	@Column(name = "lotnumber")
	private String lotnumber;
	
	@Column(name = "packageid")
	private Integer packageid;
	
	@Column(name = "met")
	private Float met;
	
	@Column(name = "la_vai")
	private Integer la_vai;
	
	@Column(name = "tieu_hao")
	private Float tieu_hao;
	
	@Column(name = "con_lai")
	private Float con_lai;
	
	@Column(name = "ps")
	private Float ps;
	
	@Column(name = "timecreated")
	private Date timecreated;

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

	public Long getCutplan_processingid_link() {
		return cutplan_processingid_link;
	}

	public void setCutplan_processingid_link(Long cutplan_processingid_link) {
		this.cutplan_processingid_link = cutplan_processingid_link;
	}

	public Long getWarehouseid_link() {
		return warehouseid_link;
	}

	public void setWarehouseid_link(Long warehouseid_link) {
		this.warehouseid_link = warehouseid_link;
	}

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getLotnumber() {
		return lotnumber;
	}

	public void setLotnumber(String lotnumber) {
		this.lotnumber = lotnumber;
	}

	public Integer getPackageid() {
		return packageid;
	}

	public void setPackageid(Integer packageid) {
		this.packageid = packageid;
	}

	public Float getMet() {
		return met;
	}

	public void setMet(Float met) {
		this.met = met;
	}

	public Integer getLa_vai() {
		return la_vai;
	}

	public void setLa_vai(Integer la_vai) {
		this.la_vai = la_vai;
	}

	public Float getTieu_hao() {
		return tieu_hao;
	}

	public void setTieu_hao(Float tieu_hao) {
		this.tieu_hao = tieu_hao;
	}

	public Float getCon_lai() {
		return con_lai;
	}

	public void setCon_lai(Float con_lai) {
		this.con_lai = con_lai;
	}

	public Float getPs() {
		return ps;
	}

	public void setPs(Float ps) {
		this.ps = ps;
	}

	public Date getTimecreated() {
		return timecreated;
	}

	public void setTimecreated(Date timecreated) {
		this.timecreated = timecreated;
	}
	
	
}
