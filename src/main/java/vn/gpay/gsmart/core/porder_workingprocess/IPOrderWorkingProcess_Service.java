package vn.gpay.gsmart.core.porder_workingprocess;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrderWorkingProcess_Service extends Operations<POrderWorkingProcess>{

	List<POrderWorkingProcess> findAll_SubProcess();

	List<POrderWorkingProcess> findAll_MainProcess();
	
	List<POrderWorkingProcess> getby_product(Long productid_link);
	
	List<POrderWorkingProcess> getByName_Product(String name, Long productid_link);
	
	List<POrderWorkingProcess>getby_porder(Long porderid_link);
	
	List<POrderWorkingProcess>getByCode(String code, Long productid_link, Long porderid_link);
	
	List<POrderWorkingProcess>getByCode_NotId(String code, Long productid_link, Long porderid_link, Long id);

}
