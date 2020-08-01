package vn.gpay.gsmart.core.porder_sewingcost;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPOrderSewignCost_Repository extends JpaRepository<POrderSewingCost, Long>, JpaSpecificationExecutor<POrderSewingCost> {
	@Query(value = "select c from POrderSewingCost c"
			+ " where  c.porderid_link = :porderid_link and "
			+ "( workingprocessid_link = :workingprocessid_link or :workingprocessid_link is null)")
	public List<POrderSewingCost> getby_porder_and_workingprocess(
			@Param("porderid_link") final Long porderid_link,
			@Param("workingprocessid_link") final Long workingprocessid_link);
}
