package vn.gpay.gsmart.core.invoice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import vn.gpay.gsmart.core.org.Org;

@Table(name="invoice")
@Entity
public class Invoice implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_generator")
	@SequenceGenerator(name="invoice_generator", sequenceName = "invoice_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="invoicenumber",length=50)
    private String invoicenumber;
	
	@Column(name ="invoicedate")
    private Date invoicedate;
	
	@Column(name ="org_prodviderid_link")
    private Long org_prodviderid_link;
	
	@Column(name ="orgid_to_link")
    private Long orgid_to_link;
	
	@Column(name ="org_portfromid_link")
    private Long org_portfromid_link;
	
	@Column(name ="org_porttoid_link")
    private Long org_porttoid_link;
	
	@Column(name ="shipdatefrom")
    private Date shipdatefrom;
	
	@Column(name ="shipdateto")
    private Date shipdateto;
		
	@Column(name ="custom_declaration",length=50)
    private String custom_declaration;
	
	private Date declaration_date;	
	
	@Column(name="shippersson")
	private String shippersson;
	
	@Column(name ="extrainfo",length=200)
    private String extrainfo;
	
	@Column(name="status")
	private Integer status;
	
	@Column(name="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;
		
	@Transient
	public String getOrgfrom_name() {
		if(orgfrom!=null) {
			return orgfrom.getName();
		}
		return "";
	}
	@Transient
	public String getOrgfrom_code() {
		if(orgfrom !=null) {
			return orgfrom.getCode();
		}
		return "";
	}
	
	@Transient
	public String getOrgto_name() {
		if(orgto!=null) {
			return orgto.getName();
		}
		return "";
	}
	@Transient
	public String getOrgto_code() {
		if(orgto !=null) {
			return orgto.getCode();
		}
		return "";
	}
	
	@Transient
	public String getPortfrom_name() {
		if(portfrom!=null) {
			return portfrom.getName();
		}
		return "";
	}
	@Transient
	public String getPortfrom_code() {
		if(portfrom !=null) {
			return portfrom.getCode();
		}
		return "";
	}
	
	@Transient
	public String getPortto_name() {
		if(portto!=null) {
			return portto.getName();
		}
		return "";
	}
	@Transient
	public String getPortto_code() {
		if(portto !=null) {
			return portto.getCode();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name="org_prodviderid_link",insertable=false,updatable =false)
    private Org orgfrom ;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name="orgid_to_link",insertable=false,updatable =false)
    private Org orgto ;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name="org_portfromid_link",insertable=false,updatable =false)
    private Org portfrom ;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name="org_porttoid_link",insertable=false,updatable =false)
    private Org portto ;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	@JoinColumn( name="invoiceid_link", referencedColumnName="id")
	private List<InvoiceD>  invoice_d  = new ArrayList<InvoiceD>();

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInvoicenumber() {
		return invoicenumber;
	}
	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}
	public Date getInvoicedate() {
		return invoicedate;
	}
	public void setInvoicedate(Date invoicedate) {
		this.invoicedate = invoicedate;
	}
	public Long getOrgid_to_link() {
		return orgid_to_link;
	}
	public void setOrgid_to_link(Long orgid_to_link) {
		this.orgid_to_link = orgid_to_link;
	}
	public Date getShipdatefrom() {
		return shipdatefrom;
	}
	public void setShipdatefrom(Date shipdatefrom) {
		this.shipdatefrom = shipdatefrom;
	}
	public Date getShipdateto() {
		return shipdateto;
	}
	public void setShipdateto(Date shipdateto) {
		this.shipdateto = shipdateto;
	}
	public Integer getTotalpackage() {
		int totalpackage = 0;
		for(InvoiceD _invoiced : invoice_d) {
			totalpackage += _invoiced.getTotalpackage() == null ? 0 : _invoiced.getTotalpackage();
		}
		return totalpackage;
	}
	public Float getTotalm3() {
		float totalm3 = 0;
		for(InvoiceD _invoiced : invoice_d) {
			totalm3 += _invoiced.getM3() == null ? 0 : _invoiced.getM3();
		}
		return totalm3;
	}
	public Float getTotalnetweight() {
		float totalnetweight = 0;
		for(InvoiceD _invoiced : invoice_d) {
			totalnetweight += _invoiced.getNetweight() == null ? 0 : _invoiced.getNetweight();
		}
		return totalnetweight;
	}
	public Float getTotalgrossweight() {
		float totalgrossweight = 0;
		for(InvoiceD _invoiced : invoice_d) {
			totalgrossweight += _invoiced.getGrossweight() == null ? 0 : _invoiced.getGrossweight();
		}
		return totalgrossweight;
	}
	public String getShippersson() {
		return shippersson;
	}
	public void setShippersson(String shippersson) {
		this.shippersson = shippersson;
	}
	public String getExtrainfo() {
		return extrainfo;
	}
	public void setExtrainfo(String extrainfo) {
		this.extrainfo = extrainfo;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public Org getOrgfrom() {
		return orgfrom;
	}
	public void setOrgfrom(Org orgfrom) {
		this.orgfrom = orgfrom;
	}
	public Org getOrgto() {
		return orgto;
	}
	public void setOrgto(Org orgto) {
		this.orgto = orgto;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public Long getOrg_prodviderid_link() {
		return org_prodviderid_link;
	}
	public void setOrg_prodviderid_link(Long org_prodviderid_link) {
		this.org_prodviderid_link = org_prodviderid_link;
	}
	public String getCustom_declaration() {
		return custom_declaration;
	}
	public void setCustom_declaration(String custom_declaration) {
		this.custom_declaration = custom_declaration;
	}
	public Date getDeclaration_date() {
		return declaration_date;
	}
	public void setDeclaration_date(Date declaration_date) {
		this.declaration_date = declaration_date;
	}
	public Long getOrg_portfromid_link() {
		return org_portfromid_link;
	}
	public List<InvoiceD> getInvoice_d() {
		return invoice_d;
	}
	public void setOrg_portfromid_link(Long org_portfromid_link) {
		this.org_portfromid_link = org_portfromid_link;
	}
	public void setInvoice_d(List<InvoiceD> invoice_d) {
		this.invoice_d = invoice_d;
	}
	public Long getOrg_porttoid_link() {
		return org_porttoid_link;
	}
	public void setOrg_porttoid_link(Long org_porttoid_link) {
		this.org_porttoid_link = org_porttoid_link;
	}
	
}
