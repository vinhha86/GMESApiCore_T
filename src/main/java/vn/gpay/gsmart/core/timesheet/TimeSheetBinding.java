package vn.gpay.gsmart.core.timesheet;

import java.util.Date;

public class TimeSheetBinding {
	private Long registerCodeCount;
	private Date registerDate;
	
	public Long getRegisterCodeCount() {
		return registerCodeCount;
	}
	public void setRegisterCodeCount(Long registerCodeCount) {
		this.registerCodeCount = registerCodeCount;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
}
