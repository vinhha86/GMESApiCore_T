package vn.gpay.gsmart.core.approle;

public class AppFunctionBinding {
	private Long id;
	private String refid_view;
	private String refid_item;
	private String name;
	private String name_en;
	private String icon;
	private boolean checked;
	private boolean readonly;
	public Long getId() {
		return id;
	}
	public String getRefid_view() {
		return refid_view;
	}
	public String getRefid_item() {
		return refid_item;
	}
	public String getName() {
		return name;
	}
	public String getName_en() {
		return name_en;
	}
	public String getIcon() {
		return icon;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setRefid_view(String refid_view) {
		this.refid_view = refid_view;
	}
	public void setRefid_item(String refid_item) {
		this.refid_item = refid_item;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean isReadonly() {
		return readonly;
	}
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
	
	
}
