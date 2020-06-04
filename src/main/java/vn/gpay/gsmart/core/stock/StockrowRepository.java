package vn.gpay.gsmart.core.stock;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface StockrowRepository extends JpaRepository<Stockrow, Long>{

	@Query(value = "select c from Stockrow c where orgid_link =:orgid")
	public List<Stockrow> findStockrowByOrgID(@Param ("orgid")final long orgid);
}
