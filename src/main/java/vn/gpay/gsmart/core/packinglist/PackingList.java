package vn.gpay.gsmart.core.packinglist;

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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.sku.SKU;

@Table(name="invoice_pklist")
@Entity
public class PackingList implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_pklist_generator")
	@SequenceGenerator(name="invoice_pklist_generator", sequenceName = "invoice_pklist_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="invoiceid_link")
    private Long invoiceid_link;
	
	@Column(name ="invoicedid_link")
    private Long invoicedid_link;
	
	@Column(name ="skuid_link")
    private Long skuid_link;

	@Column(name ="colorid_link")
    private Long colorid_link;
	
	@Column(name ="lotnumber",length=100)
    private String lotnumber ;
	
	@Column(name ="packageid")
    private Integer packageid;
	
	@Column(name="ydsorigin")
	private Float ydsorigin;
	
	@Column(name="ydscheck")
	private Float ydscheck;
	
	@Column(name ="m3")
    private Float m3;
	
	@Column(name ="width")
    private Float width;
	
	@Column(name ="netweight")
    private Float netweight;
	
	@Column(name ="grossweight")
    private Float grossweight;
	
	@Column(name ="epc",length =50)
    private String epc;

	@Column(name ="rssi")
	private Integer rssi;	
	
	@Column(name ="encryptdatetime")
    private Date encryptdatetime;
	
	@Column(name="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;
	
	private String sizenumber;
	
	//mo rong
			public String getSkucode() {
				if(sku!=null) {
					return sku.getCode();
				}
				return "";
				
			}
			public String getSkuname() {
				if(sku!=null) {
					return sku.getName();
				}
				return "";
			}
			
			public String getHscode() {
				if(sku!=null) {
					return sku.getHscode();
				}
				return "";
				
			}
			
			public String getHsname() {
				if(sku!=null) {
					return sku.getHsname();
				}
				return "";
			}
			
			
//			public String getColorcode() {
//				if(color!=null) {
//					return color.getCode();
//				}
//				return "";
//				
//			}
//			public String getColorname() {
//				if(color!=null) {
//					return color.getName();
//				}
//				return "";
//				
//			}
//			public String getColorRGB() {
//				if(color!=null) {
//					return color.getRgbvalue();
//				}
//				return "";
//				
//			}
			
			@NotFound(action = NotFoundAction.IGNORE)
			@ManyToOne
		    @JoinColumn(name="skuid_link",updatable =false,insertable =false)
		    private SKU sku;
			
//			@NotFound(action = NotFoundAction.IGNORE)
//			@ManyToOne
//		    @JoinColumn(name="colorid_link",updatable =false,insertable =false)
//		    private Color color;

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
			public Long getInvoiceid_link() {
				return invoiceid_link;
			}
			public void setInvoiceid_link(Long invoiceid_link) {
				this.invoiceid_link = invoiceid_link;
			}
			public Long getInvoicedid_link() {
				return invoicedid_link;
			}
			public void setInvoicedid_link(Long invoicedid_link) {
				this.invoicedid_link = invoicedid_link;
			}
			public Long getSkuid_link() {
				return skuid_link;
			}
			public void setSkuid_link(Long skuid_link) {
				this.skuid_link = skuid_link;
			}
			public Long getColorid_link() {
				return colorid_link;
			}
			public void setColorid_link(Long colorid_link) {
				this.colorid_link = colorid_link;
			}
			public String getLotnumber() {
				return lotnumber;
			}
			public void setLotnumber(String lotnumber) {
				this.lotnumber = lotnumber;
			}
			public Integer getPackageid() {
				return packageid;
			}
			public void setPackageid(Integer packageid) {
				this.packageid = packageid;
			}
			public Float getYdsorigin() {
				return ydsorigin;
			}
			public void setYdsorigin(Float ydsorigin) {
				this.ydsorigin = ydsorigin;
			}
			public Float getYdscheck() {
				return ydscheck;
			}
			public void setYdscheck(Float ydscheck) {
				this.ydscheck = ydscheck;
			}
			public Float getWidth() {
				return width;
			}
			public void setWidth(Float width) {
				this.width = width;
			}
			public Float getNetweight() {
				return netweight;
			}
			public void setNetweight(Float netweight) {
				this.netweight = netweight;
			}
			public Float getGrossweight() {
				return grossweight;
			}
			public void setGrossweight(Float grossweight) {
				this.grossweight = grossweight;
			}
			public String getEpc() {
				return epc;
			}
			public void setEpc(String epc) {
				this.epc = epc;
			}
			public Integer getRssi() {
				return rssi;
			}
			public void setRssi(Integer rssi) {
				this.rssi = rssi;
			}
			public Date getEncryptdatetime() {
				return encryptdatetime;
			}
			public void setEncryptdatetime(Date encryptdatetime) {
				this.encryptdatetime = encryptdatetime;
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
//			public SKU getSku() {
//				return sku;
//			}
//			public void setSku(SKU sku) {
//				this.sku = sku;
//			}
//			public Color getColor() {
//				return color;
//			}
//			public void setColor(Color color) {
//				this.color = color;
//			}
			public String getSizenumber() {
				return sizenumber;
			}
			public void setSizenumber(String sizenumber) {
				this.sizenumber = sizenumber;
			}
			public Float getM3() {
				return m3;
			}
			public void setM3(Float m3) {
				this.m3 = m3;
			}
	
}
