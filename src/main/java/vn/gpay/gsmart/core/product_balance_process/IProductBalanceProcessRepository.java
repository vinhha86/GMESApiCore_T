package vn.gpay.gsmart.core.product_balance_process;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IProductBalanceProcessRepository extends JpaRepository<ProductBalanceProcess, Long>, JpaSpecificationExecutor<ProductBalanceProcess>{
	@Query(value = "select c.productsewingcostid_link from ProductBalanceProcess c "
			+ "inner join ProductBalance b on c.productbalanceid_link = b.id "
			+ " where  b.productid_link = :productid_link ")
	public List<Long> getProductBalanceProcessIdByProduct(
			@Param("productid_link") final Long productid_link);
	
	@Query(value = "select c from ProductBalanceProcess c "
			+ " where c.productsewingcostid_link = :productsewingcostid_link ")
	public List<ProductBalanceProcess> getByProductSewingcost(
			@Param("productsewingcostid_link") final Long productsewingcostid_link);

}
