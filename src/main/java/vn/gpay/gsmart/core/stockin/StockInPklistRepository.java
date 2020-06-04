package vn.gpay.gsmart.core.stockin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StockInPklistRepository extends JpaRepository<StockInPklist, Long>{

	@Query(value = "select a from StockInPklist a inner join StockInD b on a.stockindid_link = b.id where b.stockinid_link =:stockinid_link")
	List<StockInPklist> inv_getbyid(@Param ("stockinid_link")final long stockinid_link) ;
}
