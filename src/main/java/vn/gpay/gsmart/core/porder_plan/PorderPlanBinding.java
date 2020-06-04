package vn.gpay.gsmart.core.porder_plan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PorderPlanBinding {
	public  Date StartDate;
	public  Date EndDate;
	private Long Id; // Id tự sinh để build tree
	private Long id_origin;
	private Float PercentDone;
	public  String Name;
	private Long parentId;
	private Date orderdate;
	private Integer totalorder;
	private Boolean expanded;
	private Boolean leaf;
	private Integer plan_type;
	private Float totalpackage;
	private Float totalyds;
	private Long stockinid_link;
	private Integer status;
	private Long pcontractid_link;
	private Long productid_link;
	private String iconCls;
	private String pcontract_number;
	
	public String getPcontract_number() {
		return pcontract_number;
	}
	public void setPcontract_number(String pcontract_number) {
		this.pcontract_number = pcontract_number;
	}
	public Long getProductid_link() {
		return productid_link;
	}
	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}
	private Long porderid_link;
	public boolean Rollup;
	private List<PorderPlanBinding> children = new ArrayList<PorderPlanBinding>();
	
	public Long getId() {
		return Id;
	}
	public Float getPercentDone() {
		return PercentDone;
	}
	public String getName() {
		return Name;
	}
	public Long getParentId() {
		return parentId;
	}
	public Date getOrderdate() {
		return orderdate;
	}
	public Integer getTotalorder() {
		return totalorder;
	}
	public Boolean getExpanded() {
		return expanded;
	}
	public Boolean getLeaf() {
		return leaf;
	}
	public Integer getPlan_type() {
		return plan_type;
	}
	public Float getTotalpackage() {
		return totalpackage;
	}
	public Float getTotalyds() {
		return totalyds;
	}
	public Long getStockinid_link() {
		return stockinid_link;
	}
	public List<PorderPlanBinding> getChildren() {
		return children;
	}
	public void setId(Long id) {
		Id = id;
	}
	public void setPercentDone(Float percentDone) {
		PercentDone = percentDone;
	}
	public void setName(String name) {
		name = name;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}
	public void setTotalorder(Integer totalorder) {
		this.totalorder = totalorder;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}
	public void setPlan_type(Integer plan_type) {
		this.plan_type = plan_type;
	}
	public void setTotalpackage(Float totalpackage) {
		this.totalpackage = totalpackage;
	}
	public void setTotalyds(Float totalyds) {
		this.totalyds = totalyds;
	}
	public void setStockinid_link(Long stockinid_link) {
		this.stockinid_link = stockinid_link;
	}
	public void setChildren(List<PorderPlanBinding> children) {
		this.children = children;
	}
	public Long getId_origin() {
		return id_origin;
	}
	public void setId_origin(Long id_origin) {
		this.id_origin = id_origin;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getPorderid_link() {
		return porderid_link;
	}

	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public Long getPcontractid_link() {
		return pcontractid_link;
	}
	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	
}
