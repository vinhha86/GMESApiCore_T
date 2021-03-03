package vn.gpay.gsmart.core.productattributevalue;

public class ProductAttributeValueBinding {
	
	private  String attributeValueName;
	private String list_attributevalueid;
	private  String attributeName;
	private Long attributeid_link;
	private Boolean is_select;
	private Integer sortvalue;
	
	
	public String getAttributeValueName() {
		return attributeValueName;
	}
	public void setAttributeValueName(String attributeValueName) {
		this.attributeValueName = attributeValueName;
	}
	public String getList_attributevalueid() {
		return list_attributevalueid;
	}
	public void setList_attributevalueid(String list_attributevalueid) {
		this.list_attributevalueid = list_attributevalueid;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public Long getAttributeid_link() {
		return attributeid_link;
	}
	public void setAttributeid_link(Long attributeid_link) {
		this.attributeid_link = attributeid_link;
	}
	public Boolean getIs_select() {
		return is_select;
	}
	public void setIs_select(Boolean is_select) {
		this.is_select = is_select;
	}
	public Integer getSortvalue() {
		return sortvalue;
	}
	public void setSortvalue(Integer sortvalue) {
		this.sortvalue = sortvalue;
	}
	
}
