package vn.gpay.gsmart.core.tasktype;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="tasktype")
@Entity
public class TaskType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasktype_generator")
	@SequenceGenerator(name="tasktype_generator", sequenceName = "tasktype_id_seq", allocationSize=1)
	private Integer id;
	private String name;
	private String description;
	private Integer duration;
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	
}
