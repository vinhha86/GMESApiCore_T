package vn.gpay.gsmart.core.stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.stock.Stockspace;

@Table(name="Stockrow")
@Entity
public class Stockrow implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;
	
	@Column(name ="orgid_link")
    private Long orgid_link;
	
	@Column(name ="code",length=50)
    private String code;
	
	@Column(name ="name",length=100)
    private String name;
	
	@Column(name ="name_en",length=100)
    private String name_en;
	
	//@Column(name ="rowid_link")
   // private Long rowid_link;
	
	@Column(name ="status")
    private Integer status;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	@JoinColumn( name="rowid_link", referencedColumnName="id",updatable=false,insertable=false)
	private List<Stockspace>  stockspaces  = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgid_link() {
		return orgid_link;
	}

	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName_en() {
		return name_en;
	}

	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<Stockspace> getStockspaces() {
		return stockspaces;
	}

	public void setStockspaces(List<Stockspace> stockspaces) {
		this.stockspaces = stockspaces;
	}	
}
