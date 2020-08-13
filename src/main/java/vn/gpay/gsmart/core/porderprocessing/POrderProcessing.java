package vn.gpay.gsmart.core.porderprocessing;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;

@Table(name="porder_processing")
@DynamicUpdate(true) //Chi update cac thuoc tinh thay doi, cac thuoc tinh khac giu nguyen gia tri
@Entity
public class POrderProcessing implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link ;
	
	@Column(name ="processingdate")
    private Date processingdate ;
	
	@Column(name ="porderid_link")
    private Long porderid_link ;	

	@Column(name ="ordercode",length=50)
    private String ordercode ;

	@Column(name ="orderdate")
    private Date orderdate ;

	@Column(name ="season",length=100)
    private String season;		
	
	@Transient
    private Integer salarymonth;
	
	@Transient
    private Integer salaryyear;
	
	@Column(name ="collection",length=100)
    private String collection;
	
	@Column(name ="granttoorgid_link")
    private Long granttoorgid_link;	
	
	@Column(name ="totalorder")
    private Integer totalorder ;	
	
//Tổ cắt	
	@Column(name ="amountcutting")
    private Integer amountcutting ;
	@Transient
    private Integer amountcuttingold;	
	
	@Column(name ="amountcuttingsum")
    private Integer amountcuttingsum ;	
	
	@Column(name ="amountcuttingsumprev")
    private Integer amountcuttingsumprev ;	
	
	@Column(name ="amountnumbering")
    private Integer amountnumbering ;
	@Transient
    private Integer amountnumberingold;	
	
	@Column(name ="amountnumberingsum")
    private Integer amountnumberingsum ;	
	
	@Column(name ="amountnumberingsumprev")
    private Integer amountnumberingsumprev ;		

	@Column(name ="amountmex")
    private Integer amountmex ;
	@Transient
    private Integer amountmexold;	
	
	@Column(name ="amountmexsum")
    private Integer amountmexsum ;	
	
	@Column(name ="amountmexsumprev")
    private Integer amountmexsumprev ;		
	
	@Column(name ="amounttoline")
    private Integer amounttoline ;
	@Transient
    private Integer amounttolineold;	
	
	@Column(name ="amounttolinesum")
    private Integer amounttolinesum ;	
	
	@Column(name ="amounttolinesumprev")
    private Integer amounttolinesumprev ;		
	
//Sản xuất	
	@Column(name ="amountcut")
    private Integer amountcut ;
	@Transient
    private Integer amountcutold;	
	
	@Column(name ="amountcutsum")
    private Integer amountcutsum ;	
	
	@Column(name ="amountcutsumprev")
    private Integer amountcutsumprev ;	
	
	@Column(name ="amountinput")
    private Integer amountinput ;	
	@Transient
    private Integer amountinputold;	
	
	@Column(name ="amountinputsum")
    private Integer amountinputsum ;	
	
	@Column(name ="amountinputsumprev")
    private Integer amountinputsumprev ;	
	
	@Column(name ="amountoutput")
    private Integer amountoutput ;
	@Transient
    private Integer amountoutputold;		
	
	@Column(name ="amountoutputsum")
    private Integer amountoutputsum ;	
	
	@Column(name ="amountoutputsumprev")
    private Integer amountoutputsumprev ;	
	
	@Column(name ="amounterror")
    private Integer amounterror ;
	@Transient
    private Integer amounterrorold;		
	
	@Column(name ="amounterrorsum")
    private Integer amounterrorsum ;	
	
	@Column(name ="amounterrorsumprev")
    private Integer amounterrorsumprev ;	

	@Column(name ="amounttarget")
    private Integer amounttarget ;	
	@Transient
    private Integer amounttargetold;	
	
	@Column(name ="amounttargetprev")
    private Integer amounttargetprev ;		
	
	@Column(name ="amountkcsreg")
    private Integer amountkcsreg ;
	@Transient
    private Integer amountkcsregold;		
	
	@Column(name ="amountkcsregprev")
    private Integer amountkcsregprev ;		
	
	@Column(name ="amountkcs")
    private Integer amountkcs ;	
	@Transient
    private Integer amountkcsold;		
	
	@Column(name ="amountkcssum")
    private Integer amountkcssum ;	
	
	@Column(name ="amountkcssumprev")
    private Integer amountkcssumprev ;	

	@Column(name ="amountpackstocked")
    private Integer amountpackstocked ;	
	
	@Column(name ="amountpackstockedsum")
    private Integer amountpackstockedsum ;	
	
	@Column(name ="amountpackstockedsumprev")
    private Integer amountpackstockedsumprev ;		
	
	@Column(name ="amountpacked")
    private Integer amountpacked ;	
	@Transient
    private Integer amountpackedold;		
	
	@Column(name ="amountpackedsum")
    private Integer amountpackedsum ;	
	
	@Column(name ="amountpackedsumprev")
    private Integer amountpackedsumprev ;	
	
	@Column(name ="amountstocked")
    private Integer amountstocked ;	
	@Transient
    private Integer amountstockedold;		
	
	@Column(name ="amountstockedsum")
    private Integer amountstockedsum ;	
	
	@Column(name ="amountstockedsumprev")
    private Integer amountstockedsumprev ;	
	
	@Column(name ="totalstocked")
    private Integer totalstocked ;	
	
	@Column(name ="comment", length=100)
    private String comment ;	
	
	@Column(name ="status")
    private Integer status ;	

	@Transient
    private Integer priority;
	@Transient
    private Integer shortvalue ;	
	
	@Column(name ="usercreatedid_link")
    private Long usercreatedid_link ;	
	
	@Column(name ="timecreated")
    private Date timecreated ;
	
	private Long pordergrantid_link;

	@Transient
	private Integer iscuttt;
	@Transient
	private Boolean isstockouttocut;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="porderid_link",insertable=false,updatable =false)
    private POrder porder;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="pordergrantid_link",insertable=false,updatable =false)
    private POrderGrant porder_grant;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="granttoorgid_link",insertable=false,updatable =false)
    private Org orggrant;	
	
	@Transient
	public String getGranttoorgname() {
		if(orggrant != null) {
			return orggrant.getName();
		}
		return "";
	}
	
	@Transient
	public Integer getGrantamount() {
		if(porder_grant != null) {
			return porder_grant.getGrantamount();
		}
		return null;
	}
	
	@Transient
	public String getPOrdercode() {
		if(porder != null) {
			return porder.getOrdercode();
		}
		return "";
	}
	
	@Transient
	public Date getProductiondate() {
		if(porder != null) {
			return porder.getProductiondate();
		}
		return null;
	}
	@Transient
	public Date getSample_date() {
		if(porder != null) {
			return porder.getSample_date();
		}
		return null;
	}
	@Transient
	public Date getMaterial_date() {
		if(porder != null) {
			return porder.getMaterial_date();
		}
		return null;
	}
	@Transient
	public Date getCut_date() {
		if(porder != null) {
			return porder.getCut_date();
		}
		return null;
	}
	@Transient
	public Date getQc_date() {
		if(porder != null) {
			return porder.getQc_date();
		}
		return null;
	}
	@Transient
	public Date getPacking_date() {
		if(porder != null) {
			return porder.getPacking_date();
		}
		return null;
	}
	@Transient
	public Date getStockout_date() {
		if(porder != null) {
			return porder.getStockout_date();
		}
		return null;
	}
	@Transient
	public Integer getProductionyear() {
		if(porder != null) {
			return porder.getProductionyear();
		}
		return null;
	}
	
	@Transient
	public Integer getSalaryyear() {
		if(porder != null) {
			return porder.getSalaryyear();
		}
		return null;
	}
	
	@Transient
	public Integer getBalance_status() {
		if(porder != null) {
			return porder.getBalance_status();
		}
		return null;
	}
	
	@Transient
	public Date getBalance_date() {
		if(porder != null) {
			return porder.getBalance_date();
		}
		return null;
	}
	
	@Transient
	public Float getBalance_rate() {
		if(porder != null) {
			return porder.getBalance_rate();
		}
		return null;
	}
	
	@Transient
	public Date getGolivedate() {
		if(porder != null) {
			return porder.getGolivedate();
		}
		return null;
	}
	
	@Transient
	public String getGolivedesc() {
		if(porder != null) {
			return porder.getGolivedesc();
		}
		return null;
	}
	public Boolean getIsstockouttocut() {
		return isstockouttocut;
	}

	public void setIsstockouttocut(Boolean isstockouttocut) {
		this.isstockouttocut = isstockouttocut;
	}

	public Integer getIscuttt() {
		return iscuttt;
	}

	public void setIscuttt(Integer iscuttt) {
		this.iscuttt = iscuttt;
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

	public Long getGranttoorgid_link() {
		return granttoorgid_link;
	}

	public void setGranttoorgid_link(Long granttoorgid_link) {
		this.granttoorgid_link = granttoorgid_link;
	}

	public Integer getTotalorder() {
		return totalorder;
	}

	public void setTotalorder(Integer totalorder) {
		this.totalorder = totalorder;
	}

	public Integer getAmountcut() {
		return amountcut;
	}

	public void setAmountcut(Integer amountcut) {
		this.amountcut = amountcut;
		this.amountcutold = amountcut;
	}

	public Integer getAmountcutsum() {
		return amountcutsum;
	}

	public void setAmountcutsum(Integer amountcutsum) {
		this.amountcutsum = amountcutsum;
	}

	public Integer getAmountcutsumprev() {
		return amountcutsumprev;
	}

	public void setAmountcutsumprev(Integer amountcutsumprev) {
		this.amountcutsumprev = amountcutsumprev;
	}

	public Integer getAmountinput() {
		return amountinput;
	}

	public void setAmountinput(Integer amountinput) {
		this.amountinput = amountinput;
		this.amountinputold = amountinput;
	}

	public Integer getAmountinputsum() {
		return amountinputsum;
	}

	public void setAmountinputsum(Integer amountinputsum) {
		this.amountinputsum = amountinputsum;
	}

	public Integer getAmountinputsumprev() {
		return amountinputsumprev;
	}

	public void setAmountinputsumprev(Integer amountinputsumprev) {
		this.amountinputsumprev = amountinputsumprev;
	}

	public Integer getAmountoutput() {
		return amountoutput;
	}

	public void setAmountoutput(Integer amountoutput) {
		this.amountoutput = amountoutput;
		this.amountoutputold = amountoutput;
	}

	public Integer getAmountoutputsum() {
		return amountoutputsum;
	}

	public void setAmountoutputsum(Integer amountoutputsum) {
		this.amountoutputsum = amountoutputsum;
	}

	public Integer getAmountoutputsumprev() {
		return amountoutputsumprev;
	}

	public void setAmountoutputsumprev(Integer amountoutputsumprev) {
		this.amountoutputsumprev = amountoutputsumprev;
	}

	public Integer getAmounterror() {
		return amounterror;
	}

	public void setAmounterror(Integer amounterror) {
		this.amounterror = amounterror;
		this.amounterrorold = amounterror;
	}

	public Integer getAmounterrorsum() {
		return amounterrorsum;
	}

	public void setAmounterrorsum(Integer amounterrorsum) {
		this.amounterrorsum = amounterrorsum;
	}

	public Integer getAmounterrorsumprev() {
		return amounterrorsumprev;
	}

	public void setAmounterrorsumprev(Integer amounterrorsumprev) {
		this.amounterrorsumprev = amounterrorsumprev;
	}

	
	public Integer getAmountkcsreg() {
		return amountkcsreg;
	}

	public void setAmountkcsreg(Integer amountkcsreg) {
		this.amountkcsreg = amountkcsreg;
		this.amountkcsregold = amountkcsreg;
	}

	
	public Integer getAmounttarget() {
		return amounttarget;
	}

	public void setAmounttarget(Integer amounttarget) {
		this.amounttarget = amounttarget;
		this.amounttargetold = amounttarget;
	}

	public Integer getAmounttargetprev() {
		return amounttargetprev;
	}

	public void setAmounttargetprev(Integer amounttargetprev) {
		this.amounttargetprev = amounttargetprev;
	}

	public Integer getAmountkcs() {
		return amountkcs;
	}

	public void setAmountkcs(Integer amountkcs) {
		this.amountkcs = amountkcs;
		this.amountkcsold = amountkcs;
	}

	public Integer getAmountkcssum() {
		return amountkcssum;
	}

	public void setAmountkcssum(Integer amountkcssum) {
		this.amountkcssum = amountkcssum;
	}

	public Integer getAmountkcssumprev() {
		return amountkcssumprev;
	}

	public void setAmountkcssumprev(Integer amountkcssumprev) {
		this.amountkcssumprev = amountkcssumprev;
	}


	public Integer getAmountpackstocked() {
		return amountpackstocked;
	}

	public void setAmountpackstocked(Integer amountpackstocked) {
		this.amountpackstocked = amountpackstocked;
	}

	public Integer getAmountpackstockedsum() {
		return amountpackstockedsum;
	}

	public void setAmountpackstockedsum(Integer amountpackstockedsum) {
		this.amountpackstockedsum = amountpackstockedsum;
	}

	public Integer getAmountpackstockedsumprev() {
		return amountpackstockedsumprev;
	}

	public void setAmountpackstockedsumprev(Integer amountpackstockedsumprev) {
		this.amountpackstockedsumprev = amountpackstockedsumprev;
	}

	public Integer getAmountpacked() {
		return amountpacked;
	}

	public void setAmountpacked(Integer amountpacked) {
		this.amountpacked = amountpacked;
		this.amountpackedold = amountpacked;
	}

	public Integer getAmountpackedsum() {
		return amountpackedsum;
	}

	public void setAmountpackedsum(Integer amountpackedsum) {
		this.amountpackedsum = amountpackedsum;
	}

	public Integer getAmountpackedsumprev() {
		return amountpackedsumprev;
	}

	public void setAmountpackedsumprev(Integer amountpackedsumprev) {
		this.amountpackedsumprev = amountpackedsumprev;
	}

	public Integer getAmountstocked() {
		return amountstocked;
	}

	public void setAmountstocked(Integer amountstocked) {
		this.amountstocked = amountstocked;
		this.amountstockedold = amountstocked;
	}

	public Integer getAmountstockedsum() {
		return amountstockedsum;
	}

	public void setAmountstockedsum(Integer amountstockedsum) {
		this.amountstockedsum = amountstockedsum;
	}

	public Integer getAmountstockedsumprev() {
		return amountstockedsumprev;
	}

	public void setAmountstockedsumprev(Integer amountstockedsumprev) {
		this.amountstockedsumprev = amountstockedsumprev;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public Date getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}

	public Integer getTotalstocked() {
		return totalstocked;
	}

	public void setTotalstocked(Integer totalstocked) {
		this.totalstocked = totalstocked;
	}

	public Integer getShortvalue() {
		return shortvalue;
	}

	public void setShortvalue(Integer shortvalue) {
		this.shortvalue = shortvalue;
	}

	public Integer getAmountkcsregprev() {
		return amountkcsregprev;
	}

	public void setAmountkcsregprev(Integer amountkcsregprev) {
		this.amountkcsregprev = amountkcsregprev;
	}

	public Integer getAmountcutold() {
		return amountcutold;
	}

	public void setAmountcutold(Integer amountcutold) {
		this.amountcutold = amountcutold;
	}

	public Integer getAmountinputold() {
		return amountinputold;
	}

	public void setAmountinputold(Integer amountinputold) {
		this.amountinputold = amountinputold;
	}

	public Integer getAmountoutputold() {
		return amountoutputold;
	}

	public void setAmountoutputold(Integer amountoutputold) {
		this.amountoutputold = amountoutputold;
	}

	public Integer getAmounterrorold() {
		return amounterrorold;
	}

	public void setAmounterrorold(Integer amounterrorold) {
		this.amounterrorold = amounterrorold;
	}

	public Integer getAmounttargetold() {
		return amounttargetold;
	}

	public void setAmounttargetold(Integer amounttargetold) {
		this.amounttargetold = amounttargetold;
	}

	public Integer getAmountkcsregold() {
		return amountkcsregold;
	}

	public void setAmountkcsregold(Integer amountkcsregold) {
		this.amountkcsregold = amountkcsregold;
	}

	public Integer getAmountkcsold() {
		return amountkcsold;
	}

	public void setAmountkcsold(Integer amountkcsold) {
		this.amountkcsold = amountkcsold;
	}

	public Integer getAmountpackedold() {
		return amountpackedold;
	}

	public void setAmountpackedold(Integer amountpackedold) {
		this.amountpackedold = amountpackedold;
	}

	public Integer getAmountstockedold() {
		return amountstockedold;
	}

	public void setAmountstockedold(Integer amountstockedold) {
		this.amountstockedold = amountstockedold;
	}

	public Integer getSalarymonth() {
		return salarymonth;
	}

	public void setSalarymonth(Integer salarymonth) {
		this.salarymonth = salarymonth;
	}
	
	public void setSalaryyear(Integer salaryyear) {
		this.salaryyear = salaryyear;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getAmountcutting() {
		return amountcutting;
	}

	public void setAmountcutting(Integer amountcutting) {
		this.amountcutting = amountcutting;
		this.amountcuttingold = amountcutting;
	}

	public Integer getAmountcuttingold() {
		return amountcuttingold;
	}

	public void setAmountcuttingold(Integer amountcuttingold) {
		this.amountcuttingold = amountcuttingold;
	}

	public Integer getAmountcuttingsum() {
		return amountcuttingsum;
	}

	public void setAmountcuttingsum(Integer amountcuttingsum) {
		this.amountcuttingsum = amountcuttingsum;
	}

	public Integer getAmountcuttingsumprev() {
		return amountcuttingsumprev;
	}

	public void setAmountcuttingsumprev(Integer amountcuttingsumprev) {
		this.amountcuttingsumprev = amountcuttingsumprev;
	}

	public Integer getAmountnumbering() {
		return amountnumbering;
	}

	public void setAmountnumbering(Integer amountnumbering) {
		this.amountnumbering = amountnumbering;
		this.amountnumberingold = amountnumbering;
	}

	public Integer getAmountnumberingold() {
		return amountnumberingold;
	}

	public void setAmountnumberingold(Integer amountnumberingold) {
		this.amountnumberingold = amountnumberingold;
	}

	public Integer getAmountnumberingsum() {
		return amountnumberingsum;
	}

	public void setAmountnumberingsum(Integer amountnumberingsum) {
		this.amountnumberingsum = amountnumberingsum;
	}

	public Integer getAmountnumberingsumprev() {
		return amountnumberingsumprev;
	}

	public void setAmountnumberingsumprev(Integer amountnumberingsumprev) {
		this.amountnumberingsumprev = amountnumberingsumprev;
	}

	public Integer getAmountmex() {
		return amountmex;
	}

	public void setAmountmex(Integer amountmex) {
		this.amountmex = amountmex;
		this.amountmexold = amountmex;
	}

	public Integer getAmountmexold() {
		return amountmexold;
	}

	public void setAmountmexold(Integer amountmexold) {
		this.amountmexold = amountmexold;
	}

	public Integer getAmountmexsum() {
		return amountmexsum;
	}

	public void setAmountmexsum(Integer amountmexsum) {
		this.amountmexsum = amountmexsum;
	}

	public Integer getAmountmexsumprev() {
		return amountmexsumprev;
	}

	public void setAmountmexsumprev(Integer amountmexsumprev) {
		this.amountmexsumprev = amountmexsumprev;
	}

	public Integer getAmounttoline() {
		return amounttoline;
	}

	public void setAmounttoline(Integer amounttoline) {
		this.amounttoline = amounttoline;
		this.amounttolineold = amounttoline;
	}

	public Integer getAmounttolineold() {
		return amounttolineold;
	}

	public void setAmounttolineold(Integer amounttolineold) {
		this.amounttolineold = amounttolineold;
	}

	public Integer getAmounttolinesum() {
		return amounttolinesum;
	}

	public void setAmounttolinesum(Integer amounttolinesum) {
		this.amounttolinesum = amounttolinesum;
	}

	public Integer getAmounttolinesumprev() {
		return amounttolinesumprev;
	}

	public void setAmounttolinesumprev(Integer amounttolinesumprev) {
		this.amounttolinesumprev = amounttolinesumprev;
	}
	public Long getPordergrantid_link() {
		return pordergrantid_link;
	}
	public void setPordergrantid_link(Long pordergrantid_link) {
		this.pordergrantid_link = pordergrantid_link;
	}
	
	
}
