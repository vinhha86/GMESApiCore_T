package vn.gpay.gsmart.core.stockout_order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Stockout_order_pkl_repository extends JpaRepository<Stockout_order_pkl, Long>  {

}
