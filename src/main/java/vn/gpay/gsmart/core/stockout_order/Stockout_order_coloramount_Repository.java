package vn.gpay.gsmart.core.stockout_order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Stockout_order_coloramount_Repository extends JpaRepository<Stockout_order_coloramount, Long> {
	@Query(value = "select c from Stockout_order_coloramount c "
			+ "where stockoutorderid_link = :stockoutorderid_link "
			+ "order by c.amount desc")
	public List<Stockout_order_coloramount> getby_stockout_order(
			@Param ("stockoutorderid_link")final  long stockoutorderid_link);
	
	@Query(value = "select c from Stockout_order_coloramount c "
			+ "where stockoutorderid_link = :stockoutorderid_link "
			+ "and skuid_link = :skuid_link")
	public List<Stockout_order_coloramount> getby_stockout_order_and_sku(
			@Param ("stockoutorderid_link")final  long stockoutorderid_link,
			@Param ("skuid_link")final  long skuid_link);
}
