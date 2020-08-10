package vn.gpay.gsmart.core.task_flow;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
	private Integer statusid_link;
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
	public Integer getStatusid_link() {
		return statusid_link;
	}
	public void setStatusid_link(Integer statusid_link) {
		this.statusid_link = statusid_link;
	}
	
	
}
