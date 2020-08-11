package vn.gpay.gsmart.core.task_checklist;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubTask {

	@JsonProperty("Id")
	private Long Id;

	@JsonProperty("Name")
	private String Name;

	@JsonProperty("Done")
	private Boolean Done;

	@JsonProperty("TaskId")
	private Long TaskId;

	public Long getId() {
		return Id;
	}

	public String getName() {
		return Name;
	}

	public Boolean getDone() {
		return Done;
	}

	public Long getTaskId() {
		return TaskId;
	}

	public void setId(Long id) {
		Id = id;
	}

	public void setName(String name) {
		Name = name;
	}

	public void setDone(Boolean done) {
		Done = done;
	}

	public void setTaskId(Long taskId) {
		TaskId = taskId;
	}
	
	
}
