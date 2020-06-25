package vn.gpay.gsmart.core.org;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
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

import vn.gpay.gsmart.core.category.Color;


@Table(name="org")
@Entity
public class Org implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "org_generator")
	@SequenceGenerator(name="org_generator", sequenceName = "org_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="code",length =100)
    private String code;
	
	@Column(name ="orgtypeid_link")
    private Integer orgtypeid_link;
	
	@Column(name = "name",length=200)
    private String name;
	
	@Column(name = "countryid_link",nullable = false)
    private Integer countryid_link;
	
	@Column(name = "city",length=100)
    private String city;
	
	@Column(name = "address",length=200)
    private String address;
	
	@Column(name ="gpslat",nullable = false)
    private Double gpslat;
	
	@Column(name ="gpslong",nullable = false)
    private Double gpslong;
	
	@Column(name ="mainbizid_link")
    private Integer mainbizid_link;
	
	@Column(name ="timezone",nullable = false)
    private Integer timezone;
	
	@Column(name = "contactperson",length=100)
    private String contactperson;
	
	@Column(name = "email",length=100)
    private String email;
	
	@Column(name ="langid_link",nullable = false)
    private Integer langid_link;
	
	@Column(name ="status")
    private Integer status;
	
	@Column(name ="parentid_link",nullable = false)
    private Long parentid_link;
	
	@Column(name = "phone",length=100)
    private String phone;
	
	@Column(name ="orgrootid_link",nullable = false)
    private Long orgrootid_link;
	
	@Column(name ="colorid_link")
    private Long colorid_link;
	
	@Column(name ="linecost")
    private BigDecimal linecost;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="colorid_link",insertable=false,updatable =false)
    private Color color;
	
	@Transient
	public boolean checked;
	
	@Transient
	public String getCls() {
		if(color!=null) {
			return color.getCode();
		}
		return "";
	}
	
	@Transient
	public String getClsName() {
		if(color!=null) {
			return color.getName();
		}
		return "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getOrgtypeid_link() {
		return orgtypeid_link;
	}

	public void setOrgtypeid_link(Integer orgtypeid_link) {
		this.orgtypeid_link = orgtypeid_link;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCountryid_link() {
		return countryid_link;
	}

	public void setCountryid_link(Integer countryid_link) {
		this.countryid_link = countryid_link;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getGpslat() {
		return gpslat;
	}

	public void setGpslat(Double gpslat) {
		this.gpslat = gpslat;
	}

	public Double getGpslong() {
		return gpslong;
	}

	public void setGpslong(Double gpslong) {
		this.gpslong = gpslong;
	}

	public Integer getMainbizid_link() {
		return mainbizid_link;
	}

	public void setMainbizid_link(Integer mainbizid_link) {
		this.mainbizid_link = mainbizid_link;
	}

	public Integer getTimezone() {
		return timezone;
	}

	public void setTimezone(Integer timezone) {
		this.timezone = timezone;
	}

	public String getContactperson() {
		return contactperson;
	}

	public void setContactperson(String contactperson) {
		this.contactperson = contactperson;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getLangid_link() {
		return langid_link;
	}

	public void setLangid_link(Integer langid_link) {
		this.langid_link = langid_link;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getParentid_link() {
		return parentid_link;
	}

	public void setParentid_link(Long parentid_link) {
		this.parentid_link = parentid_link;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	
	public Long getColorid_link() {
		return colorid_link;
	}

	public void setColorid_link(Long colorid_link) {
		this.colorid_link = colorid_link;
	}

	public BigDecimal getLinecost() {
		return linecost;
	}

	public void setLinecost(BigDecimal linecost) {
		this.linecost = linecost;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
