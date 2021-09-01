package vn.gpay.gsmart.core.personnel_history;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPersonnel_His_Service extends Operations<Personnel_His> {
	List<Personnel_His> gethis_by_person(Long personnelid_link);
	Long getmaxid_bytype_andperson(Long personnelid_link, int type);
	Long getpreid_bytype_andperson(Long personnelid_link, int type);
	Personnel_His getprehis_bytype_andperson(Long personnelid_link, int type);
	List<Personnel_His> getHis_personByType_Id(Long personnelid_link, Integer type);
}
