package vn.gpay.gsmart.core.stockin_type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IStockinTypeRepository extends JpaRepository<StockinType, Long> {

}
