package vn.gpay.gsmart.core.stockout_order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Stockout_order_d_repository extends JpaRepository<Stockout_order_d, Long>{
	@Query(value = "select c from Stockout_order_d c where stockoutorderid_link = :stockoutorderid_likn ")
	public List<Stockout_order_d> getby_stockout_order(
			@Param ("stockoutorderid_likn")final  long stockoutorderid_likn);
}
