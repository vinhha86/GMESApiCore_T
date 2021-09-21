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
import vn.gpay.gsmart.core.personnel_position.Personnel_Position;
import vn.gpay.gsmart.core.salary.OrgSal_Level;
import vn.gpay.gsmart.core.salary.OrgSal_Type;
import vn.gpay.gsmart.core.timesheet_shift_type.TimesheetShiftType;

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
	private Date date_startworking; //ngày vào công ty
	private Date date_endworking; // ngày nghỉ việc
	private String reason; // lý do nghỉ việc
	private Date date_probation_contract; //ngày kí hợp đồng thử việc
	private Date date_limit_contract; //ngày kí hợp đồng có thời hạn
	private Date date_unlimit_contract; // ngày kí hợp đồng không có thời hạn
	private Date date_insurance; //ngày đóng bảo hiểm
	private String village; //Thôn. xóm
	private Date dateof_idnumber;//ngày cấp chứng minh thư
	private String place_idnumber; //nơi cấp chứng minh thư
	private String healthinfo;// loại sức khỏe
	private String insurance_number;//số sổ bảo hiểm
	private Long timesheet_absence_type_id_link; 
	private String account_number;
	private String household_number;
	
	
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
		if(orgManage!=null && org != null) {
			return orgManage.getCode() + " - "+ org.getCode();
		}
		return "";
	}
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="timesheet_absence_type_id_link",insertable=false,updatable =false)
    private TimesheetShiftType timesheetshifttype;
	
	
	@Transient
	public String getShiftName() {
		if(timesheetshifttype!=null) {
			return timesheetshifttype.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="positionid_link",insertable=false,updatable =false)
    private Personnel_Position personnel_position;

	@Transient
	public String getPosition() {
		if(personnel_position!=null) {
			return personnel_position.getName();
		}
		return "";
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
	public Date getDate_startworking() {
		return date_startworking;
	}
	public void setDate_startworking(Date date_startworking) {
		this.date_startworking = date_startworking;
	}
	public Date getDate_endworking() {
		return date_endworking;
	}
	public void setDate_endworking(Date date_endworking) {
		this.date_endworking = date_endworking;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getDate_probation_contract() {
		return date_probation_contract;
	}
	public void setDate_probation_contract(Date date_probation_contract) {
		this.date_probation_contract = date_probation_contract;
	}
	public Date getDate_limit_contract() {
		return date_limit_contract;
	}
	public void setDate_limit_contract(Date date_limit_contract) {
		this.date_limit_contract = date_limit_contract;
	}
	public Date getDate_unlimit_contract() {
		return date_unlimit_contract;
	}
	public void setDate_unlimit_contract(Date date_unlimit_contract) {
		this.date_unlimit_contract = date_unlimit_contract;
	}
	public Date getDate_insurance() {
		return date_insurance;
	}
	public void setDate_insurance(Date date_insurance) {
		this.date_insurance = date_insurance;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public Date getDateof_idnumber() {
		return dateof_idnumber;
	}
	public void setDateof_idnumber(Date dateof_idnumber) {
		this.dateof_idnumber = dateof_idnumber;
	}
	public String getHealthinfo() {
		return healthinfo;
	}
	public void setHealthinfo(String healthinfo) {
		this.healthinfo = healthinfo;
	}
	public String getInsurance_number() {
		return insurance_number;
	}
	public void setInsurance_number(String insurance_number) {
		this.insurance_number = insurance_number;
	}
	public String getPlace_idnumber() {
		return place_idnumber;
	}
	public void setPlace_idnumber(String place_idnumber) {
		this.place_idnumber = place_idnumber;
	}
	public Long getTimesheet_absence_type_id_link() {
		return timesheet_absence_type_id_link;
	}
	public void setTimesheet_absence_type_id_link(Long timesheet_absence_type_id_link) {
		this.timesheet_absence_type_id_link = timesheet_absence_type_id_link;
	}

	public String getHousehold_number() {
		return household_number;
	}
	public void setHousehold_number(String household_number) {
		this.household_number = household_number;
	}
	public String getAccount_number() {
		return account_number;
	}
	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}
	
	
}
