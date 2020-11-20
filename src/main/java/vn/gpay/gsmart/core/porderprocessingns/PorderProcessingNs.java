package vn.gpay.gsmart.core.porderprocessingns;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="porder_processing_ns")
@Entity
public class PorderProcessingNs implements Serializable {
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porder_processing_ns_generator")
	@SequenceGenerator(name="porder_processing_ns_generator", sequenceName = "porder_processing_ns_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Date processingdate;
	private Integer partitionkey;
	private Integer shifttypeid_link;
	private Long porderid_link;
	private Long pordergrantid_link;
	private Long personnelid_link;
	private Long porderbalanceid_link;
	private Long pordersewingcostid_link;
	private Integer amount_complete;
	private Integer amount_money;
	private Integer amount_timespent;
	private Long userupdateid_link;
	private Date date_updated;
	private Long userapproveid_link;
	private Date date_approved;
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
	public Integer getPartitionkey() {
		return partitionkey;
	}
	public void setPartitionkey(Integer partitionkey) {
		this.partitionkey = partitionkey;
	}
	public Integer getShifttypeid_link() {
		return shifttypeid_link;
	}
	public void setShifttypeid_link(Integer shifttypeid_link) {
		this.shifttypeid_link = shifttypeid_link;
	}
	public Long getPorderid_link() {
		return porderid_link;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public Long getPordergrantid_link() {
		return pordergrantid_link;
	}
	public void setPordergrantid_link(Long pordergrantid_link) {
		this.pordergrantid_link = pordergrantid_link;
	}
	public Long getPersonnelid_link() {
		return personnelid_link;
	}
	public void setPersonnelid_link(Long personnelid_link) {
		this.personnelid_link = personnelid_link;
	}
	public Long getPorderbalanceid_link() {
		return porderbalanceid_link;
	}
	public void setPorderbalanceid_link(Long porderbalanceid_link) {
		this.porderbalanceid_link = porderbalanceid_link;
	}
	public Long getPordersewingcostid_link() {
		return pordersewingcostid_link;
	}
	public void setPordersewingcostid_link(Long pordersewingcostid_link) {
		this.pordersewingcostid_link = pordersewingcostid_link;
	}
	public Integer getAmount_complete() {
		return amount_complete;
	}
	public void setAmount_complete(Integer amount_complete) {
		this.amount_complete = amount_complete;
	}
	public Integer getAmount_money() {
		return amount_money;
	}
	public void setAmount_money(Integer amount_money) {
		this.amount_money = amount_money;
	}
	public Long getUserupdateid_link() {
		return userupdateid_link;
	}
	public void setUserupdateid_link(Long userupdateid_link) {
		this.userupdateid_link = userupdateid_link;
	}
	public Date getDate_updated() {
		return date_updated;
	}
	public void setDate_updated(Date date_updated) {
		this.date_updated = date_updated;
	}
	public Long getUserapproveid_link() {
		return userapproveid_link;
	}
	public void setUserapproveid_link(Long userapproveid_link) {
		this.userapproveid_link = userapproveid_link;
	}
	public Date getDate_approved() {
		return date_approved;
	}
	public void setDate_approved(Date date_approved) {
		this.date_approved = date_approved;
	}
	public Integer getAmount_timespent() {
		return amount_timespent;
	}
	public void setAmount_timespent(Integer amount_timespent) {
		this.amount_timespent = amount_timespent;
	}
	
}
