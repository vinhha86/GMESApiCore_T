package vn.gpay.gsmart.core.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.task_checklist.Task_CheckList;
import vn.gpay.gsmart.core.task_flow.Task_Flow;

@Table(name="task")
@Entity
public class Task implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
	@SequenceGenerator(name="task_generator", sequenceName = "task_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private String name;
	private String description;
	private Long usercreatedid_link;
	private Date datecreated;
	private Integer duration;
	private Date duedate;
	private Integer percentdone;
	private Integer statusid_link;
	private Long tasktypeid_link;
	private Long userinchargeid_link; 
	private Date datefinished;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="taskid_link",insertable=false,updatable =false)
    private List<Task_CheckList> subTasks = new ArrayList<Task_CheckList>();
    
    @NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="taskid_link",insertable=false,updatable =false)
    private List<Task_Flow> comments = new ArrayList<Task_Flow>();
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Date getDuedate() {
		return duedate;
	}
	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}
	public Integer getPercentdone() {
		return percentdone;
	}
	public void setPercentdone(Integer percentdone) {
		this.percentdone = percentdone;
	}
	public Integer getStatusid_link() {
		return statusid_link;
	}
	public void setStatusid_link(Integer statusid_link) {
		this.statusid_link = statusid_link;
	}
	public Long getTasktypeid_link() {
		return tasktypeid_link;
	}
	public void setTasktypeid_link(Long tasktypeid_link) {
		this.tasktypeid_link = tasktypeid_link;
	}
	public Long getUserinchargeid_link() {
		return userinchargeid_link;
	}
	public void setUserinchargeid_link(Long userinchargeid_link) {
		this.userinchargeid_link = userinchargeid_link;
	}
	public Date getDatefinished() {
		return datefinished;
	}
	public void setDatefinished(Date datefinished) {
		this.datefinished = datefinished;
	}
	public List<Task_CheckList> getSubTasks() {
		return subTasks;
	}
	public List<Task_Flow> getComments() {
		return comments;
	}
	public void setComments(List<Task_Flow> comments) {
		this.comments = comments;
	}
	public void setSubTasks(List<Task_CheckList> subTasks) {
		this.subTasks = subTasks;
	}
	
	
}
