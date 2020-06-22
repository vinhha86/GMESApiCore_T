package vn.gpay.gsmart.core.markettype;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IMarketType_Repository extends JpaRepository<MarketType, Long>, JpaSpecificationExecutor<MarketType> {
	@Query(value = "select c from MarketType c "
			+ "where c.status = :status")
	public List<MarketType> getby_status(@Param ("status")final  int status);
}
