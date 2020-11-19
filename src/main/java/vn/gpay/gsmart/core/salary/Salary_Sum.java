package vn.gpay.gsmart.core.salary;

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

import vn.gpay.gsmart.core.personel.Personel;

@Table(name="salary_sum")
@Entity
public class Salary_Sum implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salary_sum_generator")
	@SequenceGenerator(name="salary_sum_generator", sequenceName = "salary_sum_id_seq", allocationSize=1)
	private Long id;
	
	private Long personnelid_link;
	private Integer year;
	private Integer month;
	private Date fromdate;
	private Date todate;
	private Integer sumcolid_link;
	private Integer sumcoltypeid_link;
	private Float sumvalue;
	private Integer luongsp_sl;
	private Integer luongsp_tien;
	private Integer luongtg_sl;
	private Integer luongtg_tien;
	private Integer nghi_sl;
	private Integer nghi_tien;
	private Integer phucap_chucvu;
	private Integer phucap_khac;
	private Integer tongluong;
	private Integer ky1_tien;
	private Integer ky2_tien;
	private Integer giamtru_bhxh;
	private Integer giamtru_bhyt;
	private Integer giamtru_bhtn;
	private Integer giamtru_kpcd;
	private Integer giamtru_tong;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="sumcolid_link",insertable=false,updatable =false)
    private Salary_Sum_Col sumcol;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="sumcoltypeid_link",insertable=false,updatable =false)
    private Salary_Sum_Col_Type sumcoltype;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="personnelid_link",insertable=false,updatable =false)
    private Personel personnel;
	
	@Transient
	public String getSumcol_code() {
		if(sumcol != null) {
			return sumcol.getCode();
		}
		return "";
	}
	
	@Transient
	public String getSumcoltype_name() {
		if(sumcoltype != null) {
			return sumcoltype.getName();
		}
		return "";
	}
	
	@Transient
	public String getPersonel_fullname() {
		if(personnel != null) {
			return personnel.getFullname();
		}
		return "";
	}
	@Transient
	public String getPersonel_saltypecode() {
		if(personnel != null) {
			return personnel.getSaltype_code();
		}
		return "";
	}
	@Transient
	public String getPersonel_sallevelcode() {
		if(personnel != null) {
			return personnel.getSallevel_code();
		}
		return "";
	}	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPersonnelid_link() {
		return personnelid_link;
	}
	public void setPersonnelid_link(Long personnelid_link) {
		this.personnelid_link = personnelid_link;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Date getFromdate() {
		return fromdate;
	}
	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}
	public Date getTodate() {
		return todate;
	}
	public void setTodate(Date todate) {
		this.todate = todate;
	}
	public Integer getSumcolid_link() {
		return sumcolid_link;
	}
	public void setSumcolid_link(Integer sumcolid_link) {
		this.sumcolid_link = sumcolid_link;
	}
	public Integer getSumcoltypeid_link() {
		return sumcoltypeid_link;
	}
	public void setSumcoltypeid_link(Integer sumcoltypeid_link) {
		this.sumcoltypeid_link = sumcoltypeid_link;
	}
	public Float getSumvalue() {
		return sumvalue;
	}
	public void setSumvalue(Float sumvalue) {
		this.sumvalue = sumvalue;
	}

	public Integer getLuongsp_sl() {
		return luongsp_sl;
	}

	public void setLuongsp_sl(Integer luongsp_sl) {
		this.luongsp_sl = luongsp_sl;
	}

	public Integer getLuongsp_tien() {
		return luongsp_tien;
	}

	public void setLuongsp_tien(Integer luongsp_tien) {
		this.luongsp_tien = luongsp_tien;
	}

	public Integer getLuongtg_sl() {
		return luongtg_sl;
	}

	public void setLuongtg_sl(Integer luongtg_sl) {
		this.luongtg_sl = luongtg_sl;
	}

	public Integer getLuongtg_tien() {
		return luongtg_tien;
	}

	public void setLuongtg_tien(Integer luongtg_tien) {
		this.luongtg_tien = luongtg_tien;
	}

	public Integer getNghi_sl() {
		return nghi_sl;
	}

	public void setNghi_sl(Integer nghi_sl) {
		this.nghi_sl = nghi_sl;
	}

	public Integer getNghi_tien() {
		return nghi_tien;
	}

	public void setNghi_tien(Integer nghi_tien) {
		this.nghi_tien = nghi_tien;
	}

	public Integer getPhucap_chucvu() {
		return phucap_chucvu;
	}

	public void setPhucap_chucvu(Integer phucap_chucvu) {
		this.phucap_chucvu = phucap_chucvu;
	}

	public Integer getPhucap_khac() {
		return phucap_khac;
	}

	public void setPhucap_khac(Integer phucap_khac) {
		this.phucap_khac = phucap_khac;
	}

	public Integer getTongluong() {
		return tongluong;
	}

	public void setTongluong(Integer tongluong) {
		this.tongluong = tongluong;
	}

	public Integer getKy1_tien() {
		return ky1_tien;
	}

	public void setKy1_tien(Integer ky1_tien) {
		this.ky1_tien = ky1_tien;
	}

	public Integer getKy2_tien() {
		return ky2_tien;
	}

	public void setKy2_tien(Integer ky2_tien) {
		this.ky2_tien = ky2_tien;
	}

	public Integer getGiamtru_bhxh() {
		return giamtru_bhxh;
	}

	public void setGiamtru_bhxh(Integer giamtru_bhxh) {
		this.giamtru_bhxh = giamtru_bhxh;
	}

	public Integer getGiamtru_bhyt() {
		return giamtru_bhyt;
	}

	public void setGiamtru_bhyt(Integer giamtru_bhyt) {
		this.giamtru_bhyt = giamtru_bhyt;
	}

	public Integer getGiamtru_bhtn() {
		return giamtru_bhtn;
	}

	public void setGiamtru_bhtn(Integer giamtru_bhtn) {
		this.giamtru_bhtn = giamtru_bhtn;
	}

	public Integer getGiamtru_kpcd() {
		return giamtru_kpcd;
	}

	public void setGiamtru_kpcd(Integer giamtru_kpcd) {
		this.giamtru_kpcd = giamtru_kpcd;
	}

	public Integer getGiamtru_tong() {
		return giamtru_tong;
	}

	public void setGiamtru_tong(Integer giamtru_tong) {
		this.giamtru_tong = giamtru_tong;
	}

	public Salary_Sum_Col getSumcol() {
		return sumcol;
	}

	public void setSumcol(Salary_Sum_Col sumcol) {
		this.sumcol = sumcol;
	}

	public Salary_Sum_Col_Type getSumcoltype() {
		return sumcoltype;
	}

	public void setSumcoltype(Salary_Sum_Col_Type sumcoltype) {
		this.sumcoltype = sumcoltype;
	}

	public Personel getPersonnel() {
		return personnel;
	}

	public void setPersonnel(Personel personnel) {
		this.personnel = personnel;
	}
	
}
