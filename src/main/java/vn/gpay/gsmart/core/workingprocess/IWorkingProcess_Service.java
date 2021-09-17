package vn.gpay.gsmart.core.workingprocess;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IWorkingProcess_Service extends Operations<WorkingProcess>{

	List<WorkingProcess> findAll_SubProcess();

	List<WorkingProcess> findAll_MainProcess();
	
	List<WorkingProcess> getby_product(Long productid_link);
	
	List<WorkingProcess> getByName_Product(String name, Long productid_link);

	List<WorkingProcess>getByCode(String code, Long productid_link);
	
	List<WorkingProcess>getByCode_NotId(String code, Long productid_link, Long id);
}
