package vn.gpay.gsmart.core.task;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import vn.gpay.gsmart.core.task_checklist.SubTask;

public class TaskBinding {

	@JsonProperty("Id")
	private Long Id;
	@JsonProperty("Name")
	private String Name;
	@JsonProperty("State")
	private String State;
	@JsonProperty("ResourceId")
	private Long ResourceId;
	@JsonProperty("PercentDone")
	private Integer PercentDone;
	@JsonProperty("Duration")
	private Integer Duration;
	@JsonProperty("SubTasks")
	private List<SubTask> SubTasks = new ArrayList<SubTask>();
	public Long getId() {
		return Id;
	}
	public String getName() {
		return Name;
	}
	public String getState() {
		return State;
	}
	public Long getResourceId() {
		return ResourceId;
	}
	public Integer getPercentDone() {
		return PercentDone;
	}
	public Integer getDuration() {
		return Duration;
	}
	public List<SubTask> getSubTasks() {
		return SubTasks;
	}
	public void setId(Long id) {
		Id = id;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setState(String state) {
		State = state;
	}
	public void setResourceId(Long resourceId) {
		ResourceId = resourceId;
	}
	public void setPercentDone(Integer percentDone) {
		PercentDone = percentDone;
	}
	public void setDuration(Integer duration) {
		Duration = duration;
	}
	public void setSubTasks(List<SubTask> subTasks) {
		SubTasks = subTasks;
	}
	
	
	
}
