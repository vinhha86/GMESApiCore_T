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
	
	@Query(value = "select c from POrderSewingCost c "
			+ "where workingprocessid_link = :workingprocessid_link")
	public List<POrderSewingCost> getby_workingprocess(
			@Param("workingprocessid_link") final Long workingprocessid_link);
	
	@Query(value = "select c from POrderSewingCost c"
			+ " where  c.porderid_link = :porderid_link and "
			+ "c.id not in :listPorderBalanceProcessId ")
	public List<POrderSewingCost> getByPorderUnused(
			@Param("porderid_link") final Long porderid_link,
			@Param("listPorderBalanceProcessId") final List<Long> listPorderBalanceProcessId);
	
	@Query(value = "select c from POrderSewingCost c"
			+ " where  c.porderid_link = :porderid_link ")
	public List<POrderSewingCost> getByPorderUnused(
			@Param("porderid_link") final Long porderid_link);
	
	@Query(value = "select a from POrderSewingCost a "
			+ "inner join POrderBalanceProcess b on a.id = b.pordersewingcostid_link "
			+ "inner join POrderBalance c on b.porderbalanceid_link = c.id "
			+ "inner join POrderGrantBalance d on d.porderbalanceid_link = c.id "
			+ "where d.personnelid_link = :personnelid_link "
			)
	public List<POrderSewingCost> getForPProcessProductivity(
			@Param("personnelid_link") final Long personnelid_link);
}
