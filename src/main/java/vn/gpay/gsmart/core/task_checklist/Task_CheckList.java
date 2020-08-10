package vn.gpay.gsmart.core.task_checklist;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="task_checklist")
@Entity
public class Task_CheckList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_checklist_generator")
	@SequenceGenerator(name="task_checklist_generator", sequenceName = "task_checklist_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long taskid_link;
	private String description;
	private Long usercreatedid_link;
	private Date datecreated;
	private Date duedate;
	private Date datefinished;
	private Long userfinishedid_link;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getUsercreatedid_link() {
		return usercreatedid_link;
	}
	public void setUsercreatedid_link(Long usercreatedid_link) {
		this.usercreatedid_link = usercreatedid_link;
	}
	public Date getDatecreated() {
		return datecreated;
	}
	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}
	public Date getDuedate() {
		return duedate;
	}
	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}
	public Date getDatefinished() {
		return datefinished;
	}
	public void setDatefinished(Date datefinished) {
		this.datefinished = datefinished;
	}
	public Long getUserfinishedid_link() {
		return userfinishedid_link;
	}
	public void setUserfinishedid_link(Long userfinishedid_link) {
		this.userfinishedid_link = userfinishedid_link;
	}
	
	
}
