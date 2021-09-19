package vn.gpay.gsmart.core.personel;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPersonnel_Service extends Operations<Personel> {
	List<Personel> getby_orgmanager(Long orgmanagerid_link, long orgrootid_link);

	List<Personel> getby_org(Long orgid_link, long orgrootid_link);

	List<Personel> getByNotRegister();

	List<Personel> getPerson_by_register_code(Long orgrootid_link, String register_code);

	public List<Personel> getForPProcessingProductivity(Long orgid_link, Integer shifttypeid_link, Date workingdate);

	List<Personel> getby_orgs(List<Long> orgid_link, long orgrootid_link, boolean ishas_bikenumber);

	List<Personel> getby_bikenumber(String bike_number);

	// lay personel theo ma
	public Personel getPersonelBycode(String personnel_code);

	// lấy danh sách nhân viên theo mã nhân viên,không chứa id truyền vào
	public List<Personel> getPersonelByCode_Id_Personel(String code, Long id);

	// lấy danh sách nhân viên theo tổ, loại nhân viên
	public List<Personel> getPersonelByOrgid_link(Long org_id, Long personnel_typeid_link);

	// lấy danh sách nhân viên theo đơn vị, loại nhân viên
	public List<Personel> getPersonelByOrgid_link_PersonelType(Long orgmanagerid_link, Long personnel_typeid_link);

	public int GetSizePersonnelByGrant(long pordergrantid_link, Long personnel_typeid_link);

	//lay person theo ten
	public Personel getPersonelByname(String personnel_name);
	
	
}
