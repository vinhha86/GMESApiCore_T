package vn.gpay.gsmart.core.stockout;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StockOutPklistRepository extends JpaRepository<StockOutPklist, Long>{

	@Query(value = "select a from StockOutPklist a "
			+ "inner join StockOutD b on a.stockoutdid_link = b.id "
			+ "where b.stockoutid_link =:stockoutid_link")
	List<StockOutPklist> inv_getbyid(@Param ("stockoutid_link")final long stockoutid_link) ;
}
