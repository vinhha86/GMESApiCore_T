package vn.gpay.gsmart.core.pcontract_po_shipping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import vn.gpay.gsmart.core.category.Port;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;

@Table(name="pcontract_po_shipping")
@Entity
public class PContract_PO_Shipping implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_po_shipping_generator")
	@SequenceGenerator(name="pcontract_po_shipping_generator", sequenceName = "pcontract_po_shipping_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long pcontract_poid_link;
	private Long productid_link;
	private String code;
	private Date shipdate;
	private Integer shipamount;
	private String shipnotice;
	private Long portfromid_link;
	private Long porttoid_link;
	private String packingnotice;
	@Transient
	private String packingnoticecode;
	private Long usercreatedid_link;
	private Date timecreate;
	
	public String getPackingnoticecode() {
		return packingnoticecode;
	}
	public void setPackingnoticecode(String packingnoticecode) {
		this.packingnoticecode = packingnoticecode;
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="pcontract_po_shippingid_link",insertable=false,updatable =false)
    private List<PContract_PO_Shipping_D> shipping_d = new ArrayList<PContract_PO_Shipping_D>();

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="portfromid_link",insertable=false,updatable =false)
    private Port port_from;
	
	@Transient
	public String getPortfromname() {
		if(port_from != null) {
			return port_from.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="porttoid_link",insertable=false,updatable =false)
    private Port port_to;
	
	@Transient
	public String getPorttoname() {
		if(port_to != null) {
			return port_to.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="pcontract_poid_link",insertable=false,updatable =false)
    private PContract_PO po;
	
	@Transient
	public Long getPcontractid_link() {
		if(po != null) {
			return po.getPcontractid_link();
		}
		return null;
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

	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}

	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}

	public Long getProductid_link() {
		return productid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getShipdate() {
		return shipdate;
	}

	public void setShipdate(Date shipdate) {
		this.shipdate = shipdate;
	}

	public Integer getShipamount() {
		return shipamount;
	}

	public void setShipamount(Integer shipamount) {
		this.shipamount = shipamount;
	}

	public String getShipnotice() {
		return shipnotice;
	}

	public void setShipnotice(String shipnotice) {
		this.shipnotice = shipnotice;
	}

	public Long getPortfromid_link() {
		return portfromid_link;
	}

	public void setPortfromid_link(Long portfromid_link) {
		this.portfromid_link = portfromid_link;
	}

	public Long getPorttoid_link() {
		return porttoid_link;
	}

	public void setPorttoid_link(Long porttoid_link) {
		this.porttoid_link = porttoid_link;
	}

	public String getPackingnotice() {
		return packingnotice;
	}

	public void setPackingnotice(String packingnotice) {
		this.packingnotice = packingnotice;
	}

	public Long getUsercreatedid_link() {
		return usercreatedid_link;
	}

	public void setUsercreatedid_link(Long usercreatedid_link) {
		this.usercreatedid_link = usercreatedid_link;
	}

	public Date getTimecreate() {
		return timecreate;
	}

	public void setTimecreate(Date timecreate) {
		this.timecreate = timecreate;
	}

	public List<PContract_PO_Shipping_D> getShipping_d() {
		return shipping_d;
	}

	public void setShipping_d(List<PContract_PO_Shipping_D> shipping_d) {
		this.shipping_d = shipping_d;
	}

}
