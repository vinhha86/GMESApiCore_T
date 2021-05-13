package vn.gpay.gsmart.core.porders_poline;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface POrder_POLine_Repo extends JpaRepository<POrder_POLine, Long>,JpaSpecificationExecutor<POrder_POLine>{
	@Query(value = "select c.porderid_link from POrder_POLine c "
			+ " where pcontract_poid_link = :pcontract_poid_link")
	public List<Long> get_porderid_by_line(
			@Param("pcontract_poid_link") final Long pcontract_poid_link);
}
