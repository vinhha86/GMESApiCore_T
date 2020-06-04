package vn.gpay.gsmart.core.actionlog;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IActionLogs_Service extends Operations<ActionLogs>{

	List<ActionLogs> findUserByUser(String userid_link);

}
