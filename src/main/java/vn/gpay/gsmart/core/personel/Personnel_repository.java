package vn.gpay.gsmart.core.personel;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Personnel_repository extends JpaRepository<Personel, Long>, JpaSpecificationExecutor<Personel> {
	@Query(value = "select c from Personel c " + "where c.register_code is null ")
	public List<Personel> getByNotRegister();

	@Query("SELECT c FROM Personel c " + "where c.register_code = :register_code "
			+ "and c.orgrootid_link = :orgrootid_link")
	public List<Personel> getby_registercode(@Param("register_code") final String register_code,
			@Param("orgrootid_link") final Long orgrootid_link);

	@Query("SELECT c FROM Personel c " + "where (c.orgid_link = :orgid_link or orgmanagerid_link = :orgid_link)")
	public List<Personel> getbyOrg(@Param("orgid_link") final Long orgid_link);

	@Query("SELECT c FROM Personel c " + "inner join TimeSheetLunch b on c.id = b.personnelid_link "
			+ "where c.orgid_link = :orgid_link " + "and b.shifttypeid_link = :shifttypeid_link "
			+ "and b.workingdate = :workingdate " + "and b.isworking = true ")
	public List<Personel> getForPProcessingProductivity(@Param("orgid_link") final Long orgid_link,
			@Param("shifttypeid_link") final Integer shifttypeid_link, @Param("workingdate") final Date workingdate);

	@Query("SELECT c FROM Personel c " + "inner join Org a on c.orgid_link = a.id "
			+ "where a.parentid_link in :orgid_link " + "and c.orgrootid_link = :orgrootid_link "
			+ "and (:ishas_bikenumber = false or (bike_number is not null and bike_number != ''))")
	public List<Personel> getperson_and_bikenumber(@Param("orgid_link") final List<Long> orgid_link,
			@Param("ishas_bikenumber") final Boolean ishas_bikenumber,
			@Param("orgrootid_link") final Long orgrootid_link);

	@Query("SELECT c FROM Personel c " + "where bike_number = :bike_number")
	public List<Personel> getby_bikenumber(@Param("bike_number") final String bike_number);

	// tim personel theo ma
	@Query("select c from Personel c where c.code = :personnel_code")
	public Personel getPersonelBycode(@Param("personnel_code") final String personnel_code);

	// lấy danh sách nhân viên theo mã nhân viên, không chứa id truyền vào
	@Query(value = "select c from Personel c where c.code = :code " + "and c.id <> :id")
	public List<Personel> getPersonelByCode_Id_Personel(@Param("code") final String code, @Param("id") final Long id);
	
	//lấy danh sách nhân viên theo tổ 
	@Query(value = "select c from Personel c where c.orgid_link = :org_id ")
	public List<Personel> getPersonelByOrgid_link( @Param("org_id") final Long id);
}
