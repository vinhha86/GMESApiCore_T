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
			+ "where personnelid_link_out = :personnelid_link "
			+ "and ngay = :ngay"
			)
	public List<Personnel_inout> getby_person(
			@Param ("personnelid_link")final Long personnelid_link,
			@Param ("ngay")final Date ngay
			);
	
	@Query(value = "select c from Personnel_inout c "
			+ "where ngay = :ngay"
			)
	public List<Personnel_inout> GetPersonInOut(
			@Param ("ngay")final Date ngay
			);
}
