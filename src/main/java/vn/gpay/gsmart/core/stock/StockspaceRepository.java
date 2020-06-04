package vn.gpay.gsmart.core.stock;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface StockspaceRepository extends JpaRepository<Stockspace, Long>{
	
	@Query(value = "select c from Stockspace c where orgid_link =:orgid and spaceepc =:spaceepc")
	public List<Stockspace> findStockspaceByEpc(@Param ("orgid")final long orgid, @Param ("spaceepc")final String spaceepc);
}
