package vn.gpay.gsmart.core.task_flow;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Comment {
	@JsonProperty("Date")
	private Date Date;
	
	@JsonProperty("TaskId")
	private Long TaskId;
	
	@JsonProperty("UserId")
	private Long UserId;
	
	@JsonProperty("Text")
	private String Text;
	
	private String typename;
		
	public Date getDate() {
		return this.Date;
	}

	public Long getTaskId() {
		return TaskId;
	}

	public Long getUserId() {
		return UserId;
	}

	public String getText() {
		return Text;
	}

	public void setDate(Date date) {
		this.Date = date;
	}

	public void setTaskId(Long taskId) {
		TaskId = taskId;
	}

	public void setUserId(Long userId) {
		UserId = userId;
	}

	public void setText(String text) {
		Text = text;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}
	
	
}
