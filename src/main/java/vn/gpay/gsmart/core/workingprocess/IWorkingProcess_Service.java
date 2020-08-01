package vn.gpay.gsmart.core.workingprocess;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IWorkingProcess_Service extends Operations<WorkingProcess>{

	List<WorkingProcess> findAll_SubProcess();

	List<WorkingProcess> findAll_MainProcess();
	
	List<WorkingProcess> getby_product(Long productid_link);

}
