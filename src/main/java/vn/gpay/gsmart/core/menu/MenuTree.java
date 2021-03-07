package vn.gpay.gsmart.core.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MenuTree  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String text;
	
	private String text_vi;
	
	private String text_en;
	
	private String iconCls;
	
	private String rowCls;
	
	private String routeId;	
	
	private String viewType;
	
	private String urlc;
	
	private String title_vi;
	
	private String title_en;
	
	private String title_new_vi;
	
	private String title_new_en;
	
	private String title_edit_vi;
	
	private String title_edit_en;
	
	private String xtype_edit;
	
	private String xtype_new;
	
	private String xtype_edit_detail;
	
	private Integer type;
	
	private boolean checked;
    
    private boolean expanded;
    
    private String parent_id;
    
    private String parent_name;
    
    private Integer index;
	
	private MenuTree parent;
	 
    private List<MenuTree> children =new ArrayList<MenuTree>();
    
    public void setChildren(List<MenuTree> children) {
        this.children = children;
    }
    public void addChild(MenuTree child) {
        if (!this.children.contains(child) && child != null)
            this.children.add(child);
    }
    
    public boolean getLeaf() {
    	if(this.children.size()>0) {
    		return false;
    	}else {
    		return true;
    	}
    }
    public boolean getSelectable() {
    	if(this.children.size()>0) {
    		return false;
    	}else {
    		return true;
    	}
    }
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getText_vi() {
		return text_vi;
	}
	public void setText_vi(String text_vi) {
		this.text_vi = text_vi;
	}
	public String getText_en() {
		return text_en;
	}
	public void setText_en(String text_en) {
		this.text_en = text_en;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getRowCls() {
		return rowCls;
	}
	public void setRowCls(String rowCls) {
		this.rowCls = rowCls;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getViewType() {
		return viewType;
	}
	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
	public String getUrlc() {
		return urlc;
	}
	public void setUrlc(String urlc) {
		this.urlc = urlc;
	}
	public String getTitle_vi() {
		return title_vi;
	}
	public void setTitle_vi(String title_vi) {
		this.title_vi = title_vi;
	}
	public String getTitle_en() {
		return title_en;
	}
	public void setTitle_en(String title_en) {
		this.title_en = title_en;
	}
	public String getTitle_new_vi() {
		return title_new_vi;
	}
	public void setTitle_new_vi(String title_new_vi) {
		this.title_new_vi = title_new_vi;
	}
	public String getTitle_new_en() {
		return title_new_en;
	}
	public void setTitle_new_en(String title_new_en) {
		this.title_new_en = title_new_en;
	}
	public String getTitle_edit_vi() {
		return title_edit_vi;
	}
	public void setTitle_edit_vi(String title_edit_vi) {
		this.title_edit_vi = title_edit_vi;
	}
	public String getTitle_edit_en() {
		return title_edit_en;
	}
	public void setTitle_edit_en(String title_edit_en) {
		this.title_edit_en = title_edit_en;
	}
	public String getXtype_edit() {
		return xtype_edit;
	}
	public void setXtype_edit(String xtype_edit) {
		this.xtype_edit = xtype_edit;
	}
	public String getXtype_new() {
		return xtype_new;
	}
	public void setXtype_new(String xtype_new) {
		this.xtype_new = xtype_new;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getXtype_edit_detail() {
		return xtype_edit_detail;
	}
	public void setXtype_edit_detail(String xtype_edit_detail) {
		this.xtype_edit_detail = xtype_edit_detail;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public MenuTree getParent() {
		return parent;
	}
	public void setParent(MenuTree parent) {
		this.parent = parent;
	}
	public List<MenuTree> getChildren() {
		return children;
	}
	public String getParent_name() {
		return parent_name;
	}
	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}
	
	
}
