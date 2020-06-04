package vn.gpay.gsmart.core.stockin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StockInDRepository extends JpaRepository<StockInD, Long>{

}
