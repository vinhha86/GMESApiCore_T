package vn.gpay.gsmart.core.tasktype_checklist;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="tasktype_checklist")
@Entity
public class TaskType_CheckList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasktype_checklist_generator")
	@SequenceGenerator(name="tasktype_checklist_generator", sequenceName = "tasktype_checklist_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long tasktypeid_link;
	private String name;
	public Long getId() {
		return id;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public Long getTasktypeid_link() {
		return tasktypeid_link;
	}
	public String getName() {
		return name;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public void setTasktypeid_link(Long tasktypeid_link) {
		this.tasktypeid_link = tasktypeid_link;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
