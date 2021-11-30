package vn.gpay.gsmart.core.timesheet_lunch;

public class TimeSheetLunch_Binding {
	private Long personnelid_link;
	private Integer lunchShift;
	private Boolean isCheck;
	
	public Long getPersonnelid_link() {
		return personnelid_link;
	}
	public void setPersonnelid_link(Long personnelid_link) {
		this.personnelid_link = personnelid_link;
	}
	public Integer getLunchShift() {
		return lunchShift;
	}
	public void setLunchShift(Integer lunchShift) {
		this.lunchShift = lunchShift;
	}
	public Boolean getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(Boolean isCheck) {
		this.isCheck = isCheck;
	} 
	
	
	// mobile
	private Long orgId;
	private String orgCode;
	private String orgName;
	private Integer orgType;
	private Integer sumCa1;
	private Integer sumCa2;
	private Integer sumCa3;
	private Integer sumCa4;
	private Integer sumCa5;
	
	//
	
	private String caName;
	private Integer soDangKy;
	private Integer soThem;
	private Integer soTong;
	private Long timesheet_shift_type_id_link;
	private Long timesheet_shift_type_org_id_link;
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Integer getOrgType() {
		return orgType;
	}
	public void setOrgType(Integer orgType) {
		this.orgType = orgType;
	}
	public Integer getSumCa1() {
		return sumCa1;
	}
	public void setSumCa1(Integer sumCa1) {
		this.sumCa1 = sumCa1;
	}
	public Integer getSumCa2() {
		return sumCa2;
	}
	public void setSumCa2(Integer sumCa2) {
		this.sumCa2 = sumCa2;
	}
	public Integer getSumCa3() {
		return sumCa3;
	}
	public void setSumCa3(Integer sumCa3) {
		this.sumCa3 = sumCa3;
	}
	public Integer getSumCa4() {
		return sumCa4;
	}
	public void setSumCa4(Integer sumCa4) {
		this.sumCa4 = sumCa4;
	}
	public Integer getSumCa5() {
		return sumCa5;
	}
	public void setSumCa5(Integer sumCa5) {
		this.sumCa5 = sumCa5;
	}
	//
	public String getCaName() {
		return caName;
	}
	public void setCaName(String caName) {
		this.caName = caName;
	}
	public Integer getSoDangKy() {
		return soDangKy;
	}
	public void setSoDangKy(Integer soDangKy) {
		this.soDangKy = soDangKy;
	}
	public Integer getSoThem() {
		return soThem;
	}
	public void setSoThem(Integer soThem) {
		this.soThem = soThem;
	}
	public Integer getSoTong() {
		return soTong;
	}
	public void setSoTong(Integer soTong) {
		this.soTong = soTong;
	}
	public Long getTimesheet_shift_type_id_link() {
		return timesheet_shift_type_id_link;
	}
	public void setTimesheet_shift_type_id_link(Long timesheet_shift_type_id_link) {
		this.timesheet_shift_type_id_link = timesheet_shift_type_id_link;
	}
	public Long getTimesheet_shift_type_org_id_link() {
		return timesheet_shift_type_org_id_link;
	}
	public void setTimesheet_shift_type_org_id_link(Long timesheet_shift_type_org_id_link) {
		this.timesheet_shift_type_org_id_link = timesheet_shift_type_org_id_link;
	}
	
}
