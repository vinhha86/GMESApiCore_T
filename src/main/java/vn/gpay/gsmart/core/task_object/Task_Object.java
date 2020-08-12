package vn.gpay.gsmart.core.task_object;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="task_object")
@Entity
public class Task_Object implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_object_generator")
	@SequenceGenerator(name="task_object_generator", sequenceName = "task_object_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long taskid_link;
	private Long taskobjecttypeid_link;
	private Long objectid_link;
	public Long getId() {
		return id;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public Long getTaskid_link() {
		return taskid_link;
	}
	public Long getTaskobjecttypeid_link() {
		return taskobjecttypeid_link;
	}
	public Long getObjectid_link() {
		return objectid_link;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public void setTaskid_link(Long taskid_link) {
		this.taskid_link = taskid_link;
	}
	public void setTaskobjecttypeid_link(Long taskobjecttypeid_link) {
		this.taskobjecttypeid_link = taskobjecttypeid_link;
	}
	public void setObjectid_link(Long objectid_link) {
		this.objectid_link = objectid_link;
	}
	
	
}
