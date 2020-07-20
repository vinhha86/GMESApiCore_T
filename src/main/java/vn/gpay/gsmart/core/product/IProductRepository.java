package vn.gpay.gsmart.core.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
	@Query(value = "select c from Product c where c.orgrootid_link = :orgrootid_link "
			+ "and producttypeid_link = :producttypeid_link and c.status = 1")
	public List<Product> getall_product_byorgrootid_link(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("producttypeid_link")final  int producttypeid_link);
	
	@Query(value = "select c from Product c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and c.status = 1 "
			+ "and code = :code "
			+ "and id != :productid_link "
			+ "and producttypeid_link = :producttypeid_link")
	public List<Product> get_byorgid_link_and_code(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("productid_link")final  Long productid_link,
			@Param ("code")final  String code,
			@Param ("producttypeid_link")final  int producttypeid_link);
	
	@Query(value = "select c from Product c where c.producttypeid_link >9 and c.producttypeid_link<20 and c.status = 1")
	public Page<Product> getall_products(@Nullable Specification<Product> spec, Pageable pageable);
	
	@Query(value = "select c from Product c where c.producttypeid_link >19 and c.producttypeid_link<30 and c.status = 1")
	public Page<Product> getall_mainmaterials(@Nullable Specification<Product> spec, Pageable pageable);
	
	@Query(value = "select c from Product c where c.producttypeid_link >29 and c.producttypeid_link<40 and c.status = 1")
	public Page<Product> getall_sewingtrim(@Nullable Specification<Product> spec, Pageable pageable);
	
	@Query(value = "select c from Product c where c.producttypeid_link >39 and c.producttypeid_link<50 and c.status = 1")
	public Page<Product> getall_packingtrim(@Nullable Specification<Product> spec, Pageable pageable);

	@Query(value = "select c from Product c where c.producttypeid_link >19 and c.producttypeid_link<50 and c.status = 1")
	public List<Product> getall_materials(@Nullable Specification<Product> spec);
	
	@Query(value = "select c from Product c "
			+ "inner join ProductPairing d on c.id = d.productid_link "
			+ "where d.productpairid_link = :productid_link")
	public List<Product> getby_pairid(
			@Param ("productid_link")final  Long productid_link);
	
	@Query(value = "select c.id from Product c "
			+ "left join PContractProduct d on c.id = d.productid_link "
			+ "where d.id is null "
			+ "group by c.id")
	public List<Long> getproduct_notinbuyer();

}
