package vn.gpay.gsmart.core.org;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrgTree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String code;
	
	private Integer orgtypeid_link;
	
	private String name;
	
	private Integer countryid_link;
	
	private String city;
	
	private String address;
	
	private Double gpslat;
	
	private Double gpslong;
	
	private Integer mainbizid_link;
	
	private Integer timezone;
	
	private String contactperson;
	
	private String email;
	
	private Integer langid_link;
	
	private Integer status;
	
	private Long parentid_link;
	
	private String phone;
	
	private Long orgrootid_link;
	
	private Long colorid_link;
	
	private BigDecimal linecost;
	
	private boolean checked;
    
    private boolean expanded;
	
	private OrgTree parent;
	
	private List<OrgTree> children = new ArrayList<OrgTree>();
	
	/////
	
	public void addChild(OrgTree child) {
        if (!this.children.contains(child) && child != null)
            this.children.add(child);
    }
    
    public boolean getLeaf() {
    	if(this.children.size()>0) {
    		return false;
    	}else {
    		return true;
    	}
    }
    public boolean getSelectable() {
    	if(this.children.size()>0) {
    		return false;
    	}else {
    		return true;
    	}
    }
	
	///// Getters, setters

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

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public OrgTree getParent() {
		return parent;
	}

	public void setParent(OrgTree parent) {
		this.parent = parent;
	}

	public List<OrgTree> getChildren() {
		return children;
	}

	public void setChildren(List<OrgTree> children) {
		this.children = children;
	}
	
	
	
	
}
