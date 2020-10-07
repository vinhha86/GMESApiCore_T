package vn.gpay.gsmart.core.stockout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StockOutDRepository extends JpaRepository<StockOutD, Long>{

}
