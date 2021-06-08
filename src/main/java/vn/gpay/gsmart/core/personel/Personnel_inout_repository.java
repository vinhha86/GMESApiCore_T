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
public interface Personnel_inout_repository extends JpaRepository<Personnel_inout, Long>,JpaSpecificationExecutor<Personnel_inout> {
	@Query(value = "select c from Personnel_inout c "
			+ "where personnelid_link = :personnelid_link "
			+ "and ((timein >= :date_from and timein <= :date_to) or (timeout >= :date_from and timeout <= :date_to))"
			)
	public List<Personnel_inout> getby_person(
			@Param ("personnelid_link")final Long personnelid_link,
			@Param ("date_from")final Date date_from,
			@Param ("date_to")final Date date_to
			);
	
	@Query(value = "select c from Personnel_inout c "
			+ "where (timein >= :date_from and timein <= :date_to) or (timeout >= :date_from and timeout <= :date_to)"
			)
	public List<Personnel_inout> GetPersonInOut(
			@Param ("date_from")final Date date_from,
			@Param ("date_to")final Date date_to
			);
}
