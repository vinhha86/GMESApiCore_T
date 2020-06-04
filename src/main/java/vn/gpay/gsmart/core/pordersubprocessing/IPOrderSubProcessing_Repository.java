package vn.gpay.gsmart.core.pordersubprocessing;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPOrderSubProcessing_Repository extends JpaRepository<POrderSubProcessing, Long>{
	@Query(value = "Select a from POrderSubProcessing a where a.ordercode = :ordercode")
	public List<POrderSubProcessing>findByOrderCode(@Param ("ordercode")final String ordercode);
	
	@Query(value = "Select a from POrderSubProcessing a where a.ordercode = :ordercode and a.workingprocessid_link = :workingprocessid_link")
	public List<POrderSubProcessing>findByProcessID(@Param ("ordercode")final String ordercode, @Param ("workingprocessid_link")final Long workingprocessid_link);	
	
	@Query(value = "Select a from POrderSubProcessing a where a.porderid_link = :porderid_link and a.workingprocessid_link = :workingprocessid_link")
	public List<POrderSubProcessing>findByProcessID(@Param ("porderid_link")final Long porderid_link, @Param ("workingprocessid_link")final Long workingprocessid_link);	

	@Query(value = "Select a from POrderSubProcessing a where a.porderid_link = :porderid_link")
	public List<POrderSubProcessing>findByOrderID(@Param ("porderid_link")final Long porderid_link);	
}
