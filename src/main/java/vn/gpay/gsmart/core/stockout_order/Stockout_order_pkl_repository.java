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
}
