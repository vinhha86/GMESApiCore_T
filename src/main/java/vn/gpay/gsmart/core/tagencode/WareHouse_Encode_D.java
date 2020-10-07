package vn.gpay.gsmart.core.tagencode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.sku.SKU;

@Table(name="warehouse_encode_d")
@Entity
public class WareHouse_Encode_D implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_encode_d_generator")
	@SequenceGenerator(name="warehouse_encode_d_generator", sequenceName = "warehouse_encode_d_id_seq", allocationSize=1)
	protected Long id;
	private Long orgrootid_link;
	private Long warehouse_encodeid_link;
	private String skucode;
	private Long skuid_link;
	private Long usercreateid_link;
	private Date timecreate;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL )
	@JoinColumn( name="warehouse_encodedid_link", referencedColumnName="id")
	private List<WareHouse_Encode_EPC>  warehouse_encode_epc  = new ArrayList<WareHouse_Encode_EPC>();
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@Transient
	public String getSkuname() {
		if(sku!=null) {
			return sku.getName();
		}
		return "";
	}
	@Transient
	public String getColor_name() {
		if(sku!=null) {
			return sku.getColor_name();
		}
		return "";
	}
	@Transient
	public String getSize_name() {
		if(sku!=null) {
			return sku.getSize_name();
		}
		return "";
	}
	public void setSku(SKU sku) {
		this.sku = sku;
	}
	@Transient
	public String getProduct_code() {
		if(sku!=null) {
			return sku.getProduct_code();
		}
		return "";
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public Long getWarehouse_encodeid_link() {
		return warehouse_encodeid_link;
	}
	public void setWarehouse_encodeid_link(Long warehouse_encodeid_link) {
		this.warehouse_encodeid_link = warehouse_encodeid_link;
	}
	public String getSkucode() {
		return skucode;
	}
	public void setSkucode(String skucode) {
		this.skucode = skucode;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public Integer getTotalencode() {
		return warehouse_encode_epc.size() < 1 ? 1: warehouse_encode_epc.size();
	}
	public Long getUsercreateid_link() {
		return usercreateid_link;
	}
	public void setUsercreateid_link(Long usercreateid_link) {
		this.usercreateid_link = usercreateid_link;
	}
	public Date getTimecreate() {
		return timecreate;
	}
	public void setTimecreate(Date timecreate) {
		this.timecreate = timecreate;
	}
	public List<WareHouse_Encode_EPC> getWarehouse_encode_epc() {
		return warehouse_encode_epc;
	}
	public void setWarehouse_encode_epc(List<WareHouse_Encode_EPC> warehouse_encode_epc) {
		this.warehouse_encode_epc = warehouse_encode_epc;
	}
}
