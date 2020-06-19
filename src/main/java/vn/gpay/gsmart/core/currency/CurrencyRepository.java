package vn.gpay.gsmart.core.currency;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CurrencyRepository extends JpaRepository<Currency, Long>,JpaSpecificationExecutor<Currency>{
	@Query(value = "select c from Currency c order by c.code asc")
	public List<Currency> getAllCurrency();
}
