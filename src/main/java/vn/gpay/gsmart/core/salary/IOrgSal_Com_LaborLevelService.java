package vn.gpay.gsmart.core.salary;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IOrgSal_Com_LaborLevelService extends Operations<OrgSal_Com_LaborLevel> {

	List<OrgSal_Com_LaborLevel> getall_bysalcom_laborlevel(Long salcomid_link, Long laborlevelid_link);

	List<OrgSal_Com_LaborLevel> getall_bysalcom(Long salcomid_link);

}
