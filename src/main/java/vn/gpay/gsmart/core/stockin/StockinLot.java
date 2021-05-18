package vn.gpay.gsmart.core.stockin;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.sku.SKU;

@Table(name="stockin_lot")
@Entity
public class StockinLot implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockin_lot_generator")
	@SequenceGenerator(name="stockin_lot_generator", sequenceName = "stockin_lot_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name = "orgrootid_link")
	private Long orgrootid_link;
	
	@Column(name = "stockindid_link")
	private Long stockindid_link;
	
	@Column(name = "stockinid_link")
	private Long stockinid_link;

	@Column(name = "materialid_link")
	private Long materialid_link;
	
	@Column(name = "lot_number",length=50)
    private String lot_number;
	
	@Column(name = "totalpackage")
	private Integer totalpackage;
	
	@Column(name = "totalpackagecheck")
	private Integer totalpackagecheck;
	
	@Column(name = "totalpackagepklist")
	private Integer totalpackagepklist;
	
	@Column(name = "space",length=200)
    private String space;

	@Column(name = "totalyds")
    private Float totalyds;
	
	@Column(name = "totalmet")
    private Float totalmet;
	
	@Column(name = "totalydscheck")
    private Float totalydscheck;
	
	@Column(name = "totalmetcheck")
    private Float totalmetcheck;
	
	@Column(name = "grossweight")
    private Float grossweight;
	
	@Column(name = "grossweight_check")
    private Float grossweight_check;
	
	@Column(name = "status")
    private Integer status;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	//@BatchSize(size=10)
	@JoinColumn( name="stockinlotid_link", referencedColumnName="id")
	private List<StockinLotSpace>  stockin_lot_space  = new ArrayList<StockinLotSpace>();
	
	@Transient
	public String getStockinLotSpace() {
		String result = "";
		if(stockin_lot_space != null) {
			for(StockinLotSpace stockinLotSpace : stockin_lot_space) {
				String spaceepcid_link = stockinLotSpace.getSpaceepcid_link() == null ? "" : stockinLotSpace.getSpaceepcid_link();
				Integer totalpackage = stockinLotSpace.getTotalpackage() == null ? 0 : stockinLotSpace.getTotalpackage();
				if(result.equals("")) {
					result+=spaceepcid_link + "(" + totalpackage + ")";
				}else {
					result+="; " + spaceepcid_link + "(" + totalpackage + ")";
				}
			}
		}
		return result;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="materialid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@Transient
	public String getSkucode() {
		if(sku != null)
			return sku.getCode().equals(null) ? "" : sku.getCode();
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

	public Long getStockinid_link() {
		return stockinid_link;
	}

	public void setStockinid_link(Long stockinid_link) {
		this.stockinid_link = stockinid_link;
	}

	public Long getMaterialid_link() {
		return materialid_link;
	}

	public void setMaterialid_link(Long materialid_link) {
		this.materialid_link = materialid_link;
	}

	public String getLot_number() {
		return lot_number;
	}

	public void setLot_number(String lot_number) {
		this.lot_number = lot_number;
	}

	public Integer getTotalpackage() {
		return totalpackage;
	}

	public void setTotalpackage(Integer totalpakage) {
		this.totalpackage = totalpakage;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public Integer getTotalpackagecheck() {
		return totalpackagecheck;
	}

	public void setTotalpackagecheck(Integer totalpakagecheck) {
		this.totalpackagecheck = totalpakagecheck;
	}

	public Float getTotalyds() {
		return totalyds;
	}

	public void setTotalyds(Float totalyds) {
		this.totalyds = totalyds;
	}

	public Float getTotalmet() {
		return totalmet;
	}

	public void setTotalmet(Float totalmet) {
		this.totalmet = totalmet;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Float getTotalydscheck() {
		return totalydscheck;
	}

	public void setTotalydscheck(Float totalydscheck) {
		this.totalydscheck = totalydscheck;
	}

	public Float getTotalmetcheck() {
		return totalmetcheck;
	}

	public void setTotalmetcheck(Float totalmetcheck) {
		this.totalmetcheck = totalmetcheck;
	}

	public Float getGrossweight() {
		return grossweight;
	}

	public void setGrossweight(Float grossweight) {
		this.grossweight = grossweight;
	}

	public Float getGrossweight_check() {
		return grossweight_check;
	}

	public void setGrossweight_check(Float grossweight_check) {
		this.grossweight_check = grossweight_check;
	}

	public Integer getTotalpackagepklist() {
		return totalpackagepklist;
	}

	public void setTotalpackagepklist(Integer totalpackagepklist) {
		this.totalpackagepklist = totalpackagepklist;
	}

	public Long getStockindid_link() {
		return stockindid_link;
	}

	public void setStockindid_link(Long stockindid_link) {
		this.stockindid_link = stockindid_link;
	}

	public List<StockinLotSpace> getStockin_lot_space() {
		return stockin_lot_space;
	}

	public void setStockin_lot_space(List<StockinLotSpace> stockin_lot_space) {
		this.stockin_lot_space = stockin_lot_space;
	}
	
}
