package vn.gpay.gsmart.core.salary;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IOrgSal_Type_LevelService extends Operations<OrgSal_Type_Level> {
	public List<OrgSal_Type_Level> getall_byorgrootid(long orgrootid_link);

	List<OrgSal_Type_Level> getall_byorg_and_type(Long orgid_link, Integer typeid_link);

	List<OrgSal_Type_Level> getall_bylaborlevel(Long orgid_link, Integer typeid_link, Long laborlevelid_link);

	OrgSal_Type_Level get_bysaltype_and_level(Long saltypeid_link, Long sallevelid_link);

	List<OrgSal_Type_Level> get_bysaltype(Long saltypeid_link);
}
