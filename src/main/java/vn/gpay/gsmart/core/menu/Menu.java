package vn.gpay.gsmart.core.menu;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Table(name="app_menu")
@Entity
public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	protected String id;

	@Column(name = "xtype")
	private String xtype;
	
	@Column(name = "text_vi")
	private String text_vi;
	
	@Column(name = "text_en")
	private String text_en;
	
	@Column(name = "icon")
	private String icon;
	
	@Column(name = "create")
	private String create;
	
	@Column(name="title_list_en")
	private String title_list_en;
	
	@Column(name="title_list_vi")
	private String title_list_vi;
	
	@Column(name = "txtcreate_vi")
	private String txtcreate_vi;
	
	@Column(name = "txtcreate_en")
	private String txtcreate_en;
	
	@Column(name = "edit")
	private String edit;
	
	@Column(name = "txtedit_vi")
	private String txtedit_vi;
	
	@Column(name = "txtedit_en")
	private String txtedit_en;
	
	@Column(name = "urlc")
	private String urlc;
	
	@Column(name = "index")
	private Integer index;
	
	@Column(name = "type")
	private Integer type;
	
	@Column(name = "parent_id")
	private String parent_id;
	
	@Transient
	public boolean checked;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXtype() {
		return xtype;
	}

	public void setXtype(String xtype) {
		this.xtype = xtype;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCreate() {
		return create;
	}

	public void setCreate(String create) {
		this.create = create;
	}

	public String getTitle_list_en() {
		return title_list_en;
	}

	public void setTitle_list_en(String title_list_en) {
		this.title_list_en = title_list_en;
	}

	public String getTitle_list_vi() {
		return title_list_vi;
	}

	public void setTitle_list_vi(String title_list_vi) {
		this.title_list_vi = title_list_vi;
	}

	public String getTxtcreate_vi() {
		return txtcreate_vi;
	}

	public void setTxtcreate_vi(String txtcreate_vi) {
		this.txtcreate_vi = txtcreate_vi;
	}

	public String getTxtcreate_en() {
		return txtcreate_en;
	}

	public void setTxtcreate_en(String txtcreate_en) {
		this.txtcreate_en = txtcreate_en;
	}

	public String getEdit() {
		return edit;
	}

	public void setEdit(String edit) {
		this.edit = edit;
	}

	public String getTxtedit_vi() {
		return txtedit_vi;
	}

	public void setTxtedit_vi(String txtedit_vi) {
		this.txtedit_vi = txtedit_vi;
	}

	public String getTxtedit_en() {
		return txtedit_en;
	}

	public void setTxtedit_en(String txtedit_en) {
		this.txtedit_en = txtedit_en;
	}

	public String getUrlc() {
		return urlc;
	}

	public void setUrlc(String urlc) {
		this.urlc = urlc;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}
