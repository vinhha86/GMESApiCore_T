package vn.gpay.gsmart.core.salebill;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.category.Color;
import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.sku.SKU;

@Table(name="salebill_epc")
@Entity
public class SaleBillEpc implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salebill_epc_generator")
	@SequenceGenerator(name="salebill_epc_generator", sequenceName = "salebill_epc_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name = "orgid_link")
	private Long orgid_link;
	
	@Column(name = "billid_link")
	private Long billid_link;
	
	@Column(name = "skuid_link")
	private Long skuid_link;
	
	@Column(name = "epc")
	private String epc;
	
	@Column(name = "oldepc")
	private String oldepc;
	
	@Column(name = "colorid_link")
	private Long colorid_link;
	
	@Column(name ="unitid_link")
    private Integer unitid_link;
	
	@Column(name = "unitprice")
	private Float unitprice;
	
	@Column(name = "amount")
	private Long amount;
	
	@Column(name = "vatpercent")
	private Float vatpercent;
	
	@Column(name = "totalvat")
	private Float totalvat;
	
	@Column(name = "discountpercent")
	private Float discountpercent;
	
	@Column(name = "discount")
	private Float discount;
	
	@Column(name = "totalamount")
	private Float totalamount;
	
	@Column(name = "totalsum")
	private Float totalsum;
	
	@Column(name = "rssi")
	private Long rssi;
	
	@Column(name="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="colorid_link",insertable=false,updatable =false)
    private Color color;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
    private Unit unit;
	
	@Transient
	public String getSkucode() {
		if(sku!=null) {
			return sku.getCode();
		}
		return "";
	}
	@Transient
	public String getSkuname() {
		if(sku!=null) {
			return sku.getName();
		}
		return "";
	}
	@Transient
	public String getHscode() {
		if(sku!=null) {
			return sku.getHscode();
		}
		return "";
	}
	@Transient
	public String getHsname() {
		if(sku!=null) {
			return sku.getHsname();
		}
		return "";
	}
	
	@Transient
	public String getUnitname() {
		if(unit!=null) {
			return unit.getName();
		}
		return "";
	}
	@Transient
	public String getColorcode() {
		if(color!=null) {
			return color.getCode();
		}
		return "";
	}
	@Transient
	public String getColorname() {
		if(color!=null) {
			return color.getName();
		}
		return "";
	}
	@Transient
	public String getColorRGB() {
		if(color!=null) {
			return color.getRgbvalue();
		}
		return "";
	}
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
	public Long getBillid_link() {
		return billid_link;
	}
	public void setBillid_link(Long billid_link) {
		this.billid_link = billid_link;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public String getOldepc() {
		return oldepc;
	}
	public void setOldepc(String oldepc) {
		this.oldepc = oldepc;
	}
	public Long getColorid_link() {
		return colorid_link;
	}
	public void setColorid_link(Long colorid_link) {
		this.colorid_link = colorid_link;
	}
	public Integer getUnitid_link() {
		return unitid_link;
	}
	public void setUnitid_link(Integer unitid_link) {
		this.unitid_link = unitid_link;
	}
	public Float getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Float getVatpercent() {
		return vatpercent;
	}
	public void setVatpercent(Float vatpercent) {
		this.vatpercent = vatpercent;
	}
	public Float getTotalvat() {
		return totalvat;
	}
	public void setTotalvat(Float totalvat) {
		this.totalvat = totalvat;
	}
	public Float getDiscountpercent() {
		return discountpercent;
	}
	public void setDiscountpercent(Float discountpercent) {
		this.discountpercent = discountpercent;
	}
	public Float getDiscount() {
		return discount;
	}
	public void setDiscount(Float discount) {
		this.discount = discount;
	}
	public Float getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(Float totalamount) {
		this.totalamount = totalamount;
	}
	public Float getTotalsum() {
		return totalsum;
	}
	public void setTotalsum(Float totalsum) {
		this.totalsum = totalsum;
	}
	public Long getRssi() {
		return rssi;
	}
	public void setRssi(Long rssi) {
		this.rssi = rssi;
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
	public Long getLastuserupdateid_link() {
		return lastuserupdateid_link;
	}
	public void setLastuserupdateid_link(Long lastuserupdateid_link) {
		this.lastuserupdateid_link = lastuserupdateid_link;
	}
	public Date getLasttimeupdate() {
		return lasttimeupdate;
	}
	public void setLasttimeupdate(Date lasttimeupdate) {
		this.lasttimeupdate = lasttimeupdate;
	}
	public SKU getSku() {
		return sku;
	}
	public void setSku(SKU sku) {
		this.sku = sku;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	
}
