package vn.gpay.gsmart.core.task_document;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="task_document")
@Entity
public class Task_Document implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_document_generator")
	@SequenceGenerator(name="task_document_generator", sequenceName = "task_document_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long taskid_link;
	private String filename;
	private String description;
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
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
