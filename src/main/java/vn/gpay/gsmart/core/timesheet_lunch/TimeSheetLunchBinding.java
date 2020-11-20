package vn.gpay.gsmart.core.timesheet_lunch;

import java.util.Date;

public class TimeSheetLunchBinding {
	// a.id, a.code, a.fullname, b.workingdate, b.shifttypeid_link, b.isworking, b.islunch
	private Long personnelid_link;
	private String personnelCode;
	private String personnelFullname;
	private Date workingdate;
	private boolean isWorkingShift1;
	private boolean isWorkingShift2;
	private boolean isWorkingShift3;
	private boolean isLunchShift1;
	private boolean isLunchShift2;
	private boolean isLunchShift3;
	
	public Long getPersonnelid_link() {
		return personnelid_link;
	}
	public void setPersonnelid_link(Long personnelid_link) {
		this.personnelid_link = personnelid_link;
	}
	public String getPersonnelCode() {
		return personnelCode;
	}
	public void setPersonnelCode(String personnelCode) {
		this.personnelCode = personnelCode;
	}
	public String getPersonnelFullname() {
		return personnelFullname;
	}
	public void setPersonnelFullname(String personnelFullname) {
		this.personnelFullname = personnelFullname;
	}
	public Date getWorkingdate() {
		return workingdate;
	}
	public void setWorkingdate(Date workingdate) {
		this.workingdate = workingdate;
	}
	public boolean isWorkingShift1() {
		return isWorkingShift1;
	}
	public void setWorkingShift1(boolean isWorkingShift1) {
		this.isWorkingShift1 = isWorkingShift1;
	}
	public boolean isWorkingShift2() {
		return isWorkingShift2;
	}
	public void setWorkingShift2(boolean isWorkingShift2) {
		this.isWorkingShift2 = isWorkingShift2;
	}
	public boolean isWorkingShift3() {
		return isWorkingShift3;
	}
	public void setWorkingShift3(boolean isWorkingShift3) {
		this.isWorkingShift3 = isWorkingShift3;
	}
	public boolean isLunchShift1() {
		return isLunchShift1;
	}
	public void setLunchShift1(boolean isLunchShift1) {
		this.isLunchShift1 = isLunchShift1;
	}
	public boolean isLunchShift2() {
		return isLunchShift2;
	}
	public void setLunchShift2(boolean isLunchShift2) {
		this.isLunchShift2 = isLunchShift2;
	}
	public boolean isLunchShift3() {
		return isLunchShift3;
	}
	public void setLunchShift3(boolean isLunchShift3) {
		this.isLunchShift3 = isLunchShift3;
	}
}
