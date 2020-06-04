package vn.gpay.gsmart.core.stockingunique;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface StockingUniqueRepository extends JpaRepository<StockingUniqueCode, Long> {
	@Query(value = "select c from StockingUniqueCode c "
			+ "where stocking_type =:stocking_type")
	public List<StockingUniqueCode> getby_type(
			@Param ("stocking_type")final Integer stocking_type);
}
