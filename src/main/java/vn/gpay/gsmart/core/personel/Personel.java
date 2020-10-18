package vn.gpay.gsmart.core.personel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="personnel")
@Entity
public class Personel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personnel_generator")
	@SequenceGenerator(name="personnel_generator", sequenceName = "personnel_id_seq", allocationSize=1)
	protected Long id;
	private String code; //Ma NV
	private Long orgrootid_link;
	private Long orgid_link; //don vi, phong ban truc thuoc
	private String fullname; // ho ten
	private Long personnel_typeid_link; //Loai nhan vien
	private Integer gender; // Gioi tinh
	private String tel; // Dien thoai
	private Long countryid_link; // Quoc tinh
	private Long provinceid_link; // tinh, thanh pho
	private Long districtid_link; // Quan huyen
	private Long communeid_link; // Xa Phuong
	private String address; // Dia chi
	private String idnumber; // CMT
	private Date birthdate; // Ngay sinh
	private Integer status; // Trang tHai
	private String email; // Email
	
	
	public Long getId() {
		return id;
	}
	public String getCode() {
		return code;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public Long getOrgid_link() {
		return orgid_link;
	}
	public String getFullname() {
		return fullname;
	}
	public Long getPersonnel_typeid_link() {
		return personnel_typeid_link;
	}
	public Integer getGender() {
		return gender;
	}
	public String getTel() {
		return tel;
	}
	public Long getCountryid_link() {
		return countryid_link;
	}
	public Long getProvinceid_link() {
		return provinceid_link;
	}
	public Long getDistrictid_link() {
		return districtid_link;
	}
	public Long getCommuneid_link() {
		return communeid_link;
	}
	public String getAddress() {
		return address;
	}
	public String getIdnumber() {
		return idnumber;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public Integer getStatus() {
		return status;
	}
	public String getEmail() {
		return email;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public void setPersonnel_typeid_link(Long personnel_typeid_link) {
		this.personnel_typeid_link = personnel_typeid_link;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public void setCountryid_link(Long countryid_link) {
		this.countryid_link = countryid_link;
	}
	public void setProvinceid_link(Long provinceid_link) {
		this.provinceid_link = provinceid_link;
	}
	public void setDistrictid_link(Long districtid_link) {
		this.districtid_link = districtid_link;
	}
	public void setCommuneid_link(Long communeid_link) {
		this.communeid_link = communeid_link;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
