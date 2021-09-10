package vn.gpay.gsmart.core.porder_balance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPOrderBalanceRepository  extends JpaRepository<POrderBalance, Long>, JpaSpecificationExecutor<POrderBalance> {
	@Query("SELECT c FROM POrderBalance c where c.porderid_link = :porderid_link "
			+ "order by c.sortvalue ")
	public List<POrderBalance> getByPorder(@Param ("porderid_link")final Long porderid_link);
	
	@Query("SELECT c FROM POrderBalance c "
			+ "inner join POrderBalanceProcess b on c.id = b.porderbalanceid_link "
			+ "where c.porderid_link = :porderid_link "
			+ "and b.pordersewingcostid_link = :pordersewingcostid_link "
			+ "order by c.sortvalue ")
	public List<POrderBalance> getByPOrderAndPOrderSewingCost(
			@Param ("porderid_link")final Long porderid_link,
			@Param ("pordersewingcostid_link")final Long pordersewingcostid_link
			);
	
	@Query("SELECT c FROM POrderBalance c "
			+ "where lower(c.balance_name) = lower(:balance_name) "
			+ "and c.porderid_link = :porderid_link "
			+ "order by c.sortvalue ")
	public List<POrderBalance> getByBalanceName_POrder(
			@Param ("balance_name")final String balance_name, 
			@Param ("porderid_link")final Long porderid_link);
}
