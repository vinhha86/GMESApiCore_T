package vn.gpay.gsmart.core.pcontract_po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PContract_PO_Gantt {
	private long id;
	private long id_origin;
	private String code;
	private long orgtypeid_link ;
	private long parentid_origin;
	private String parentname;
	private Integer totalpackage;
	private String mahang;
	
	public String getMahang() {
		return mahang;
	}
	public void setMahang(String mahang) {
		this.mahang = mahang;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@JsonProperty("Name")
	private String Name;
	
	@JsonProperty("StartDate")
	private Date StartDate;
	
	@JsonProperty("EndDate")
	private Date EndDate;
	
	private Boolean expanded;
	private Boolean leaf;
	private String iconCls;
	
	@JsonProperty("Cls")
	private String Cls;	
	
	private Long parentId;
	
	@JsonProperty("Rollup")
	private boolean Rollup;
	private List<PContract_PO_Gantt> children = new ArrayList<PContract_PO_Gantt>();
	
	public String getClss() {
		return Cls;
	}
	public void setClss(String cls) {
		Cls = cls;
	}
	
	public Date getEndDate() {
		return EndDate;
	}
	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}
	public boolean isRollup() {
		return Rollup;
	}
	public void setRollup(boolean rollup) {
		Rollup = rollup;
	}
	public List<PContract_PO_Gantt> getChildren() {
		return children;
	}
	public void setChildren(List<PContract_PO_Gantt> children) {
		this.children = children;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Boolean getExpanded() {
		return expanded;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
	public Boolean getLeaf() {
		return leaf;
	}
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getId_origin() {
		return id_origin;
	}
	public void setId_origin(long id_origin) {
		this.id_origin = id_origin;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Date getStartDate() {
		return StartDate;
	}
	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}
	public long getOrgtypeid_link() {
		return orgtypeid_link;
	}
	public long getParentid_origin() {
		return parentid_origin;
	}
	public void setOrgtypeid_link(long orgtypeid_link) {
		this.orgtypeid_link = orgtypeid_link;
	}
	public void setParentid_origin(long parentid_origin) {
		this.parentid_origin = parentid_origin;
	}
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	public String getParentname() {
		return parentname;
	}
	public void setParentname(String parentname) {
		this.parentname = parentname;
	}
	
	
}
