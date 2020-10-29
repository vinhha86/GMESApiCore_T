package vn.gpay.gsmart.core.salary;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IOrgSal_LevelService extends Operations<OrgSal_Level> {

	List<OrgSal_Level> getall_byorgrootid(long orgrootid_link);

}
