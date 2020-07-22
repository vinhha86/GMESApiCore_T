package vn.gpay.gsmart.core.reports;

public class CMP_Data {
	private Integer id;
	private Long orgid_link;
	private Long parentorgid_link;
	private String parentorgname;
	private String orgname;
	private Integer month;
	private Integer year;
	private Float cmpamount;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getOrgid_link() {
		return orgid_link;
	}
	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}
	public Long getParentorgid_link() {
		return parentorgid_link;
	}
	public void setParentorgid_link(Long parentorgid_link) {
		this.parentorgid_link = parentorgid_link;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Float getCmpamount() {
		return cmpamount;
	}
	public void setCmpamount(Float cmpamount) {
		this.cmpamount = cmpamount;
	}
	public String getParentorgname() {
		return parentorgname;
	}
	public void setParentorgname(String parentorgname) {
		this.parentorgname = parentorgname;
	}


}
