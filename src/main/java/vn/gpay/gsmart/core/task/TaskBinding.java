package vn.gpay.gsmart.core.task;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import vn.gpay.gsmart.core.task_checklist.SubTask;
import vn.gpay.gsmart.core.task_flow.Comment;

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
	@JsonProperty("Description")
	private String Description;
	@JsonProperty("UserInChargeName")
	private String UserInChargeName;
	@JsonProperty("SubTasks")
	private List<SubTask> SubTasks = new ArrayList<SubTask>();
	@JsonProperty("Comments")
	private List<Comment> Comments = new ArrayList<Comment>();
	private Long tasktypeid_link;
	private String cls_task;
	private Long orgid_link;
	
	public Long getOrgid_link() {
		return orgid_link;
	}
	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}
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
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getUserInChargeName() {
		return UserInChargeName;
	}
	public void setUserInChargeName(String userInChargeName) {
		UserInChargeName = userInChargeName;
	}
	public List<Comment> getComments() {
		return Comments;
	}
	public void setComments(List<Comment> comments) {
		Comments = comments;
	}
	public Long getTasktypeid_link() {
		return tasktypeid_link;
	}
	public void setTasktypeid_link(Long tasktypeid_link) {
		this.tasktypeid_link = tasktypeid_link;
	}
	public String getCls_task() {
		return cls_task;
	}
	public void setCls_task(String cls_task) {
		this.cls_task = cls_task;
	}
	
	
	
}
