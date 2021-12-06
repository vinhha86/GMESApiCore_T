package vn.gpay.gsmart.core.stockout_order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Stockout_order_pkl_repository extends JpaRepository<Stockout_order_pkl, Long>  {
	@Query(value = "select c from Stockout_order_pkl c where stockoutorderdid_link = :stockoutorderdid_link ")
	public List<Stockout_order_pkl> getby_detail(
			@Param ("stockoutorderdid_link")final  long stockoutorderdid_link);
	
	@Query(value = "select distinct c from Stockout_order_pkl c "
			+ "inner join Stockout_order_d d on d.id = c.stockoutorderdid_link "
			+ "inner join Stockout_order e on e.id = d.stockoutorderid_link "
			+ "where e.id = :stockoutorderid_link "
			+ "and c.epc = :epc "
			)
	public List<Stockout_order_pkl> getByEpc_stockout_order(
			@Param ("stockoutorderid_link")final Long stockoutorderid_link,
			@Param ("epc")final String epc
			);
}
