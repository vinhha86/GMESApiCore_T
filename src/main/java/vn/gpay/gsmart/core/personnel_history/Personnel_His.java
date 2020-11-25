package vn.gpay.gsmart.core.personnel_history;

import java.io.Serializable;
import java.util.Date;

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

import vn.gpay.gsmart.core.category.LaborLevel;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.position.Position;
import vn.gpay.gsmart.core.salary.OrgSal_Level;
import vn.gpay.gsmart.core.salary.OrgSal_Type;

@Table(name="personnel_history")
@Entity
public class Personnel_His implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personnel_history_generator")
	@SequenceGenerator(name="personnel_history_generator", sequenceName = "personnel_history_id_seq", allocationSize=1)
	protected Long id;
	private Long positionid_link;
	private Long levelid_link;
	private Long orgid_link;
	private String decision_number;
	private Date decision_date;
	private Integer type;
	private Long personnelid_link;
	private Long saltypeid_link;
	private Long sallevelid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgid_link",insertable=false,updatable =false)
    private Org org;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="positionid_link",insertable=false,updatable =false)
    private Position pos;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="levelid_link",insertable=false,updatable =false)
    private LaborLevel level;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="saltypeid_link",insertable=false,updatable =false)
    private OrgSal_Type orgsaltype;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="sallevelid_link",insertable=false,updatable =false)
    private OrgSal_Level orgsallevel;
	
	@Transient
	public String getSaltype_name() {
		if(orgsaltype!=null)
			return orgsaltype.getName();
		return "";
	}
	
	@Transient
	public String getSallevel_name() {
		if(orgsallevel!=null)
			return orgsallevel.getName();
		return "";
	}
	
	@Transient
	public String getPosition_name() {
		if(pos!=null)
			return pos.getName();
		return "";
	}
	
	@Transient
	public String getOrg_name() {
		if( org!=null)
			return  org.getName();
		return "";
	}
	
	@Transient
	public String getLevel_name() {
		if(level!=null)
			return level.getName();
		return "";
	}
	
	public Long getId() {
		return id;
	}
	public Long getPositionid_link() {
		return positionid_link;
	}
	public Long getLevelid_link() {
		return levelid_link;
	}
	public Long getOrgid_link() {
		return orgid_link;
	}
	public String getDecision_number() {
		return decision_number;
	}
	public Date getDecision_date() {
		return decision_date;
	}
	public Integer getType() {
		return type;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setPositionid_link(Long positionid_link) {
		this.positionid_link = positionid_link;
	}
	public void setLevelid_link(Long levelid_link) {
		this.levelid_link = levelid_link;
	}
	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}
	public void setDecision_number(String decision_number) {
		this.decision_number = decision_number;
	}
	public void setDecision_date(Date decision_date) {
		this.decision_date = decision_date;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getPersonnelid_link() {
		return personnelid_link;
	}
	public void setPersonnelid_link(Long personnelid_link) {
		this.personnelid_link = personnelid_link;
	}

	public Long getSaltypeid_link() {
		return saltypeid_link;
	}

	public void setSaltypeid_link(Long saltypeid_link) {
		this.saltypeid_link = saltypeid_link;
	}

	public Long getSallevelid_link() {
		return sallevelid_link;
	}

	public void setSallevelid_link(Long sallevelid_link) {
		this.sallevelid_link = sallevelid_link;
	}
}
