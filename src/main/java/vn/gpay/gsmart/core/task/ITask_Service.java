package vn.gpay.gsmart.core.task;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ITask_Service extends Operations<Task> {
	public List<Task> getby_user(Long userid_link);
}
