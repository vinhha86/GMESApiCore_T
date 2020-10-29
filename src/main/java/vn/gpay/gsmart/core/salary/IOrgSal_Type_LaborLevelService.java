package vn.gpay.gsmart.core.salary;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IOrgSal_Type_LaborLevelService extends Operations<OrgSal_Type_LaborLevel> {

	List<OrgSal_Type_LaborLevel> getall_bysaltype(Long saltypeid_link);

	List<OrgSal_Type_LaborLevel> getall_bysaltype_laborlevel(Long saltypeid_link, Long laborlevelid_link);

}
