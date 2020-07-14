package vn.gpay.gsmart.core.Schedule;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Schedule_plan {
	@JsonProperty("Id")
	private long Id;
	
	private long id_origin;
	private String code;
	private long parentid_origin;
	private long orgtypeid_link;
	private Integer type;

	@JsonProperty("Name")
	private String Name;
	
	@JsonProperty("iconCls")
	private String iconCls;
	
	private Boolean expanded;
	private Boolean leaf;
	private List<Schedule_plan> children = new ArrayList<Schedule_plan>();
	
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
	public long getId_origin() {
		return id_origin;
	}
	public void setId_origin(long id_origin) {
		this.id_origin = id_origin;
	}
	public String getCode() {
		return code;
	}
	public long getParentid_origin() {
		return parentid_origin;
	}
	public long getOrgtypeid_link() {
		return orgtypeid_link;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setParentid_origin(long parentid_origin) {
		this.parentid_origin = parentid_origin;
	}
	public void setOrgtypeid_link(long orgtypeid_link) {
		this.orgtypeid_link = orgtypeid_link;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
