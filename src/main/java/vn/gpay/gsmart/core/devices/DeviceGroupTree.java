package vn.gpay.gsmart.core.devices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeviceGroupTree implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private String code;
	
	private Long parentid_link;
	
	private boolean checked;
    
    private boolean expanded;
    
    private DeviceGroupTree parent;
    
    private List<DeviceGroupTree> children = new ArrayList<DeviceGroupTree>();

    /////
    
    public void addChild(DeviceGroupTree child) {
        if (!this.children.contains(child) && child != null)
            this.children.add(child);
    }
    
    public boolean getLeaf() {
    	if(this.children.size()>0) {
    		return false;
    	}else {
    		if (this.parentid_link == null || this.parentid_link == -1)
    			return false;
    		else
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
    
    ///// Getters, setters
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getParentid_link() {
		return parentid_link;
	}

	public void setParentid_link(Long parentid_link) {
		this.parentid_link = parentid_link;
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

	public DeviceGroupTree getParent() {
		return parent;
	}

	public void setParent(DeviceGroupTree parent) {
		this.parent = parent;
	}

	public List<DeviceGroupTree> getChildren() {
		return children;
	}

	public void setChildren(List<DeviceGroupTree> children) {
		this.children = children;
	}
}
