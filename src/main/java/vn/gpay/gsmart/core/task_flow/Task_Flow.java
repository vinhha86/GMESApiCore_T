package vn.gpay.gsmart.core.task_flow;

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

import vn.gpay.gsmart.core.task_flow_status.Task_Flow_Status;

@Table(name="task_flow")
@Entity
public class Task_Flow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_flow_generator")
	@SequenceGenerator(name="task_flow_generator", sequenceName = "task_flow_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long taskid_link;
	private Long fromuserid_link;
	private Long touserid_link;
	private Date datecreated;
	private String description;
	private Integer flowdirection;
	private Integer taskstatusid_link;
	private Integer flowstatusid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="flowstatusid_link",insertable=false,updatable =false)
    private Task_Flow_Status status ;
	
	@Transient
	public String getTypeName() {
		if(status != null)
			return status.getName();
		return "";
	}
	
	public Integer getTaskstatusid_link() {
		return taskstatusid_link;
	}
	public void setTaskstatusid_link(Integer taskstatusid_link) {
		this.taskstatusid_link = taskstatusid_link;
	}
	public Integer getFlowstatusid_link() {
		return flowstatusid_link;
	}
	public void setFlowstatusid_link(Integer flowstatusid_link) {
		this.flowstatusid_link = flowstatusid_link;
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
	public Long getTaskid_link() {
		return taskid_link;
	}
	public void setTaskid_link(Long taskid_link) {
		this.taskid_link = taskid_link;
	}
	public Long getFromuserid_link() {
		return fromuserid_link;
	}
	public void setFromuserid_link(Long fromuserid_link) {
		this.fromuserid_link = fromuserid_link;
	}
	public Long getTouserid_link() {
		return touserid_link;
	}
	public void setTouserid_link(Long touserid_link) {
		this.touserid_link = touserid_link;
	}
	public Date getDatecreated() {
		return datecreated;
	}
	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getFlowdirection() {
		return flowdirection;
	}
	public void setFlowdirection(Integer flowdirection) {
		this.flowdirection = flowdirection;
	}
	
	
}
