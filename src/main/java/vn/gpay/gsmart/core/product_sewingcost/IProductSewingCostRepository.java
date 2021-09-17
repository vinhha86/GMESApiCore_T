package vn.gpay.gsmart.core.product_sewingcost;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IProductSewingCostRepository extends JpaRepository<ProductSewingCost, Long>, JpaSpecificationExecutor<ProductSewingCost> {
	@Query(value = "select c from ProductSewingCost c"
			+ " where  c.productid_link = :productid_link and "
			+ "( workingprocessid_link = :workingprocessid_link or :workingprocessid_link is null)")
	public List<ProductSewingCost> getby_product_and_workingprocess(
			@Param("productid_link") final Long productid_link,
			@Param("workingprocessid_link") final Long workingprocessid_link);
	
	@Query(value = "select c from ProductSewingCost c "
			+ "where workingprocessid_link = :workingprocessid_link")
	public List<ProductSewingCost> getby_workingprocess(
			@Param("workingprocessid_link") final Long workingprocessid_link);
	
	@Query(value = "select c from ProductSewingCost c"
			+ " where  c.productid_link = :productid_link and "
			+ "c.id not in :listProductBalanceProcessId ")
	public List<ProductSewingCost> getByProductUnused(
			@Param("productid_link") final Long productid_link,
			@Param("listProductBalanceProcessId") final List<Long> listProductBalanceProcessId);
	
	@Query(value = "select c from ProductSewingCost c"
			+ " where  c.productid_link = :productid_link ")
	public List<ProductSewingCost> getByProductUnused(
			@Param("productid_link") final Long productid_link);
}
