package vn.gpay.gsmart.core.personel;

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

import vn.gpay.gsmart.core.category.LaborLevel;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.salary.OrgSal_Level;
import vn.gpay.gsmart.core.salary.OrgSal_Type;

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
	private Long orgmanagerid_link;
	private String register_code;
	private String image_name;
	private Long positionid_link;
	private Long levelid_link;
	private Long saltypeid_link;
	private Long sallevelid_link;
	private String bike_number;
	private Boolean isbike;
	
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="levelid_link",insertable=false,updatable =false)
    private LaborLevel laborLevel;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="saltypeid_link",insertable=false,updatable =false)
    private OrgSal_Type saltype;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="sallevelid_link",insertable=false,updatable =false)
    private OrgSal_Level sallevel;

	@Transient
	public String getSaltype_code() {
		if(saltype!=null) {
			return saltype.getCode();
		}
		return "";
	}
	@Transient
	public String getSallevel_code() {
		if(sallevel!=null) {
			return sallevel.getCode();
		}
		return "";
	}
	
	@Transient
	public String getLaborlevel_name() {
		if(laborLevel!=null) {
			return laborLevel.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgid_link",insertable=false,updatable =false)
    private Org org;
	
	
	@Transient
	public String getOrgname() {
		if(org!=null) {
			return org.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgmanagerid_link",insertable=false,updatable =false)
    private Org orgManage;
	
	
	@Transient
	public String getOrgManageName() {
		if(orgManage!=null) {
			return orgManage.getName();
		}
		return "";
	}
	
	@Transient
	public String getOrgGrantName() {
		return orgManage.getCode() + " - "+ org.getCode();
	}
	
	
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
	public Long getOrgmanagerid_link() {
		return orgmanagerid_link;
	}
	public void setOrgmanagerid_link(Long orgmanagerid_link) {
		this.orgmanagerid_link = orgmanagerid_link;
	}
	public String getRegister_code() {
		return register_code;
	}
	public void setRegister_code(String register_code) {
		this.register_code = register_code;
	}



	public String getImage_name() {
		return image_name;
	}



	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}



	public Long getPositionid_link() {
		return positionid_link;
	}



	public Long getLevelid_link() {
		return levelid_link;
	}



	public void setPositionid_link(Long positionid_link) {
		this.positionid_link = positionid_link;
	}



	public void setLevelid_link(Long levelid_link) {
		this.levelid_link = levelid_link;
	}



	public Long getSaltypeid_link() {
		return saltypeid_link;
	}



	public void setSaltypeid_link(Long saltypeid_link) {
		this.saltypeid_link = saltypeid_link;
	}



	public Long getSallevelid_link() {
		return sallevelid_link;
	}



	public void setSallevelid_link(Long sallevelid_link) {
		this.sallevelid_link = sallevelid_link;
	}
	public String getBike_number() {
		return bike_number;
	}
	public void setBike_number(String bike_number) {
		this.bike_number = bike_number;
	}
	public Boolean getIsbike() {
		return isbike;
	}
	public void setIsbike(Boolean isbike) {
		this.isbike = isbike;
	}
	
	
}
