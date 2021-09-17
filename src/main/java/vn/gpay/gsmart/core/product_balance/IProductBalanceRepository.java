package vn.gpay.gsmart.core.product_balance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IProductBalanceRepository  extends JpaRepository<ProductBalance, Long>, JpaSpecificationExecutor<ProductBalance> {
	@Query("SELECT c FROM ProductBalance c where c.productid_link = :productid_link "
			+ "order by c.sortvalue ")
	public List<ProductBalance> getByProduct(@Param ("productid_link")final Long productid_link);
	
	@Query("SELECT c FROM ProductBalance c "
			+ "inner join ProductBalanceProcess b on c.id = b.productbalanceid_link "
			+ "where c.productid_link = :productid_link "
			+ "and b.productsewingcostid_link = :productsewingcostid_link "
			+ "order by c.sortvalue ")
	public List<ProductBalance> getByProductAndProductSewingCost(
			@Param ("productid_link")final Long productid_link,
			@Param ("productsewingcostid_link")final Long productsewingcostid_link
			);
	
	@Query("SELECT c FROM ProductBalance c "
			+ "where lower(c.balance_name) = lower(:balance_name) "
			+ "and c.productid_link = :productid_link "
			+ "order by c.sortvalue ")
	public List<ProductBalance> getByBalanceName_Product(
			@Param ("balance_name")final String balance_name, 
			@Param ("productid_link")final Long productid_link);

}
