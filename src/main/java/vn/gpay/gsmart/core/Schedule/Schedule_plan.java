package vn.gpay.gsmart.core.Schedule;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Schedule_plan {
	@JsonProperty("Id")
	private long Id;

	@JsonProperty("Name")
	private String Name;
	
	@JsonProperty("iconCls")
	private String iconCls;
	
	private Boolean expanded;
	private Boolean leaf;
	private List<Schedule_plan> children;
	
	public long getId() {
		return Id;
	}
	public String getName() {
		return Name;
	}
	public String getIconCls() {
		return iconCls;
	}
	public Boolean getExpanded() {
		return expanded;
	}
	public Boolean getLeaf() {
		return leaf;
	}
	public List<Schedule_plan> getChildren() {
		return children;
	}
	public void setId(long id) {
		Id = id;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}
	public void setChildren(List<Schedule_plan> children) {
		this.children = children;
	}
	
	
}
