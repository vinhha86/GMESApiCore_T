package vn.gpay.gsmart.core.porder_balance_process;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPOrderBalanceProcessRepository extends JpaRepository<POrderBalanceProcess, Long>, JpaSpecificationExecutor<POrderBalanceProcess>{
	@Query(value = "select c.pordersewingcostid_link from POrderBalanceProcess c "
			+ "inner join POrderBalance b on c.porderbalanceid_link = b.id "
			+ " where  b.porderid_link = :porderid_link ")
	public List<Long> getPOrderBalanceProcessIdByPorder(
			@Param("porderid_link") final Long porderid_link);
	
	@Query(value = "select c from POrderBalanceProcess c "
			+ " where c.pordersewingcostid_link = :pordersewingcostid_link ")
	public List<POrderBalanceProcess> getByPorderSewingcost(
			@Param("pordersewingcostid_link") final Long pordersewingcostid_link);
}
