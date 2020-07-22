package vn.gpay.gsmart.core.reports;

public class ReportMonth {
	private Integer month;
	private Integer year;
	public ReportMonth(Integer month,Integer year){
		this.month = month;
		this.year = year;
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

}
