package vn.gpay.gsmart.core.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductTree implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	
	private String text;
	private Long parent_id;
	private String code;
	private byte[] imgproduct;
	private String iconCls;
	
	public String getIconCls() {
		return "no-icon";
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public byte[] getImgproduct() {
		return imgproduct;
	}
	public void setImgproduct(byte[] imgproduct) {
		this.imgproduct = imgproduct;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	private List<ProductTree> children =new ArrayList<ProductTree>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Long getParent_id() {
		return parent_id;
	}
	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}
	public List<ProductTree> getChildren() {
		return children;
	}
	public void setChildren(List<ProductTree> children) {
		this.children = children;
	}
}
