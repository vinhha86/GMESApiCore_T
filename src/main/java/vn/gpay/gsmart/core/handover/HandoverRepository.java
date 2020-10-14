package vn.gpay.gsmart.core.handover;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface HandoverRepository  extends JpaRepository<Handover, Long>,JpaSpecificationExecutor<Handover>{
	@Query(value = "select c from Handover c where c.handovertypeid_link = :handovertypeid_link ")
	public List<Handover> getByType(@Param ("handovertypeid_link")final Long handovertypeid_link);
	
	@Query(value = "select c from Handover c where c.handovertypeid_link = :handovertypeid_link "
			+"and c.status = :status ")
	public List<Handover> getByType(
			@Param ("handovertypeid_link")final Long handovertypeid_link, 
			@Param ("status")final Integer status);
	
	@Query(value = "select c from Handover c where c.handover_code = :handover_code ")
	public List<Handover> getByHandoverCode(@Param ("handover_code")final String handover_code);
}
