package vn.gpay.gsmart.core.Schedule;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Schedule_holiday {
	
	@JsonProperty("StartDate")
	private Date StartDate;
	
	@JsonProperty("EndDate")
	private Date EndDate;
	
	private String comment;
	
	public Date getStartDate() {
		return StartDate;
	}
	public Date getEndDate() {
		return EndDate;
	}
	public String getComment() {
		return comment;
	}
	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}
	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
