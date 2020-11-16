package vn.gpay.gsmart.core.porderprocessing;

import java.util.Date;

public class POrderProcessingBinding {
	// Output bar chart
	private Long sumOutput;
	private Long sumError;
	private Long sumStocked;
	private Long parentid_link;
	private String name;
	
	// Stocked line chart
	private Date processingDate;
	private Long dataDHA;
	private Long dataNV;
	private Long dataBN1;
	private Long dataBN2;
	private Long dataBN3;
	
	public Long getSumOutput() {
		return sumOutput;
	}
	public void setSumOutput(Long sumOutput) {
		this.sumOutput = sumOutput;
	}
	public Long getSumError() {
		return sumError;
	}
	public void setSumError(Long sumError) {
		this.sumError = sumError;
	}
	public Long getSumStocked() {
		return sumStocked;
	}
	public void setSumStocked(Long sumStocked) {
		this.sumStocked = sumStocked;
	}
	public Long getParentid_link() {
		return parentid_link;
	}
	public void setParentid_link(Long parentid_link) {
		this.parentid_link = parentid_link;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	// 
	
	public Date getProcessingDate() {
		return processingDate;
	}
	public void setProcessingDate(Date processingDate) {
		this.processingDate = processingDate;
	}
	public Long getDataDHA() {
		return dataDHA;
	}
	public void setDataDHA(Long dataDHA) {
		this.dataDHA = dataDHA;
	}
	public Long getDataNV() {
		return dataNV;
	}
	public void setDataNV(Long dataNV) {
		this.dataNV = dataNV;
	}
	public Long getDataBN1() {
		return dataBN1;
	}
	public void setDataBN1(Long dataBN1) {
		this.dataBN1 = dataBN1;
	}
	public Long getDataBN2() {
		return dataBN2;
	}
	public void setDataBN2(Long dataBN2) {
		this.dataBN2 = dataBN2;
	}
	public Long getDataBN3() {
		return dataBN3;
	}
	public void setDataBN3(Long dataBN3) {
		this.dataBN3 = dataBN3;
	}
	
	
}
