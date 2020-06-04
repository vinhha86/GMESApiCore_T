package vn.gpay.gsmart.core.porder;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

//import ivy.admin.factory.response.IvyERPOrderBySaleDataList;

@Entity
public class POrderFilter implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Transient
    private Long orgrootid_link ;
	
	@Transient
    private Date processingdate ;
	
	@Transient
    private Long porderid_link ;	

	@Transient
    private Long pcontractid_link ;
	
	@Transient
    private String contractcode ;
	
	@Transient
    private String ordercode ;

	@Transient
    private Date orderdate ;

	@Transient
    private String season;		
	
	@Transient
    private Date balance_date;	
	
	@Transient
    private Float balance_rate;	
	
	@Transient
    private Integer balance_status;
	
	@Transient
    private Date productiondate;
	
	@Transient
    private Integer productionyear;
	
	@Transient
    private Integer salaryyear;
	
	@Transient
    private Integer salarymonth;	
	
	@Transient
    private String collection;
	
	@Transient
    private Long granttoorgid_link;	
	
	@Transient
    private String granttoorgname ;	
	
	@Transient
    private Long granttolineid_link;
	
	@Transient
    private String granttolinename ;	
	
	@Transient
    private Date golivedate;
	
	@Transient
    private String golivedesc ;
	
	@Transient
    private Integer totalorder ;	
	
	@Transient
    private Integer amountcutsum ;	
	
	@Transient
    private Integer amountinputsum ;	
	
	@Transient
    private Integer amountoutputsum ;	
	
	@Transient
    private Integer amounterrorsum ;	

	@Transient
    private Integer amountkcssum ;	
	
	@Transient
    private Integer amountpackstockedsum ;	
	
	@Transient
    private Integer amountpackedsum ;	

	@Transient
    private Integer amountstockedsum ;	
	
	@Transient
    private Integer totalstocked ;	
	
	@Transient
    private String comment ;	
	
	@Transient
    private Integer status ;	

	@Transient
    private Integer shortvalue ;	
	
	@Transient
    private Long usercreatedid_link ;	
	
	@Transient
    private Date timecreated ;

	@Transient
	private Integer iscuttt;
	
	@Transient
    private String mausac;	
	
	@Transient
    private String soluongsaledat;		
	
	@Transient
	private Date material_date;
	@Transient
	private Date sample_date;
	@Transient
	private Date cut_date;
	@Transient
	private Date packing_date;
	@Transient
	private Date qc_date;
	@Transient
	private Date stockout_date;
	
	
//	@Transient
//	private IvyERPOrderBySaleDataList saleorder;
//
//	public Long getId() {
//		return id;
//	}
//
//	public IvyERPOrderBySaleDataList getSaleorder() {
//		return saleorder;
//	}
//
//	public void setSaleorder(IvyERPOrderBySaleDataList saleorder) {
//		this.saleorder = saleorder;
//	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public Date getProcessingdate() {
		return processingdate;
	}

	public void setProcessingdate(Date processingdate) {
		this.processingdate = processingdate;
	}

	public Long getPorderid_link() {
		return porderid_link;
	}

	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}

	public String getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public Date getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public Date getBalance_date() {
		return balance_date;
	}

	public void setBalance_date(Date balance_date) {
		this.balance_date = balance_date;
	}

	public Float getBalance_rate() {
		return balance_rate;
	}

	public void setBalance_rate(Float balance_rate) {
		this.balance_rate = balance_rate;
	}

	public Integer getBalance_status() {
		return balance_status;
	}

	public void setBalance_status(Integer balance_status) {
		this.balance_status = balance_status;
	}

	public Date getProductiondate() {
		return productiondate;
	}

	public void setProductiondate(Date productiondate) {
		this.productiondate = productiondate;
	}

	public Integer getProductionyear() {
		return productionyear;
	}

	public void setProductionyear(Integer productionyear) {
		this.productionyear = productionyear;
	}

	public Integer getSalaryyear() {
		return salaryyear;
	}

	public void setSalaryyear(Integer salaryyear) {
		this.salaryyear = salaryyear;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public Long getGranttoorgid_link() {
		return granttoorgid_link;
	}

	public void setGranttoorgid_link(Long granttoorgid_link) {
		this.granttoorgid_link = granttoorgid_link;
	}

	public String getGranttoorgname() {
		return granttoorgname;
	}

	public void setGranttoorgname(String granttoorgname) {
		this.granttoorgname = granttoorgname;
	}

	public Date getGolivedate() {
		return golivedate;
	}

	public void setGolivedate(Date golivedate) {
		this.golivedate = golivedate;
	}

	public String getGolivedesc() {
		return golivedesc;
	}

	public void setGolivedesc(String golivedesc) {
		this.golivedesc = golivedesc;
	}

	public Integer getTotalorder() {
		return totalorder;
	}

	public void setTotalorder(Integer totalorder) {
		this.totalorder = totalorder;
	}

	public Integer getAmountcutsum() {
		return amountcutsum;
	}

	public void setAmountcutsum(Integer amountcutsum) {
		this.amountcutsum = amountcutsum;
	}

	public Integer getAmountinputsum() {
		return amountinputsum;
	}

	public void setAmountinputsum(Integer amountinputsum) {
		this.amountinputsum = amountinputsum;
	}

	public Integer getAmountoutputsum() {
		return amountoutputsum;
	}

	public void setAmountoutputsum(Integer amountoutputsum) {
		this.amountoutputsum = amountoutputsum;
	}

	public Integer getAmounterrorsum() {
		return amounterrorsum;
	}

	public void setAmounterrorsum(Integer amounterrorsum) {
		this.amounterrorsum = amounterrorsum;
	}

	public Integer getAmountkcssum() {
		return amountkcssum;
	}

	public void setAmountkcssum(Integer amountkcssum) {
		this.amountkcssum = amountkcssum;
	}

	public Integer getAmountpackstockedsum() {
		return amountpackstockedsum;
	}

	public void setAmountpackstockedsum(Integer amountpackstockedsum) {
		this.amountpackstockedsum = amountpackstockedsum;
	}

	public Integer getAmountpackedsum() {
		return amountpackedsum;
	}

	public void setAmountpackedsum(Integer amountpackedsum) {
		this.amountpackedsum = amountpackedsum;
	}

	public Integer getAmountstockedsum() {
		return amountstockedsum;
	}

	public void setAmountstockedsum(Integer amountstockedsum) {
		this.amountstockedsum = amountstockedsum;
	}

	public Integer getTotalstocked() {
		return totalstocked;
	}

	public void setTotalstocked(Integer totalstocked) {
		this.totalstocked = totalstocked;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getShortvalue() {
		return shortvalue;
	}

	public void setShortvalue(Integer shortvalue) {
		this.shortvalue = shortvalue;
	}

	public Long getUsercreatedid_link() {
		return usercreatedid_link;
	}

	public void setUsercreatedid_link(Long usercreatedid_link) {
		this.usercreatedid_link = usercreatedid_link;
	}

	public Date getTimecreated() {
		return timecreated;
	}

	public void setTimecreated(Date timecreated) {
		this.timecreated = timecreated;
	}

	public Integer getIscuttt() {
		return iscuttt;
	}

	public void setIscuttt(Integer iscuttt) {
		this.iscuttt = iscuttt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getSalarymonth() {
		return salarymonth;
	}

	public void setSalarymonth(Integer salarymonth) {
		this.salarymonth = salarymonth;
	}

	public String getMausac() {
		return mausac;
	}

	public void setMausac(String mausac) {
		this.mausac = mausac;
	}

	public String getSoluongsaledat() {
		return soluongsaledat;
	}

	public void setSoluongsaledat(String soluongsaledat) {
		this.soluongsaledat = soluongsaledat;
	}
	
	public Object clone() throws CloneNotSupportedException 
	{ 
		POrderFilter t = (POrderFilter)super.clone();
		return t;
	}

	public String getContractcode() {
		return contractcode;
	}

	public void setContractcode(String contractcode) {
		this.contractcode = contractcode;
	}

	public Long getId() {
		return id;
	}

	public Long getGranttolineid_link() {
		return granttolineid_link;
	}

	public void setGranttolineid_link(Long granttolineid_link) {
		this.granttolineid_link = granttolineid_link;
	}

	public String getGranttolinename() {
		return granttolinename;
	}

	public void setGranttolinename(String granttolinename) {
		this.granttolinename = granttolinename;
	}

	public Date getMaterial_date() {
		return material_date;
	}

	public void setMaterial_date(Date material_date) {
		this.material_date = material_date;
	}

	public Date getSample_date() {
		return sample_date;
	}

	public void setSample_date(Date sample_date) {
		this.sample_date = sample_date;
	}

	public Date getCut_date() {
		return cut_date;
	}

	public void setCut_date(Date cut_date) {
		this.cut_date = cut_date;
	}

	public Date getPacking_date() {
		return packing_date;
	}

	public void setPacking_date(Date packing_date) {
		this.packing_date = packing_date;
	}

	public Date getQc_date() {
		return qc_date;
	}

	public void setQc_date(Date qc_date) {
		this.qc_date = qc_date;
	}

	public Date getStockout_date() {
		return stockout_date;
	}

	public void setStockout_date(Date stockout_date) {
		this.stockout_date = stockout_date;
	}

	public Long getPcontractid_link() {
		return pcontractid_link;
	}

	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
	
}
