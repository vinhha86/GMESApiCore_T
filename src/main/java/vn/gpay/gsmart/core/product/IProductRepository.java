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
			+ "and TRIM(LOWER(buyercode)) = TRIM(LOWER(:code)) "
			+ "and id != :productid_link "
			+ "and producttypeid_link = :producttypeid_link")
	public List<Product> get_byorgid_link_and_code(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("productid_link")final  Long productid_link,
			@Param ("code")final  String code,
			@Param ("producttypeid_link")final  int producttypeid_link);
	
	@Query(value = "select c from Product c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and c.status = 1 "
			+ "and (:code is not null or :code is null) "
			+ "and (:name is not null or :name is null) "
			+ "and producttypeid_link >= :producttypeid_link_from "
			+ "and producttypeid_link <= :producttypeid_link_to")
	public List<Product> get_product_by_type(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("name")final  String name,
			@Param ("code")final  String code,
			@Param ("producttypeid_link_from")final  int producttypeid_link_from,
			@Param ("producttypeid_link_to")final  int producttypeid_link_to);
	
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
	
//	@Query(value = "select c from Product c")
//	public List<Product> findAllIgnoreCase(@Nullable Specification<Product> spec);
	
	@Query(value = "select c from Product c where c.buyercode = :buyercode")
	public List<Product> getProductByExactBuyercode(@Param ("buyercode")final  String buyercode);
	
	@Query(value = "select c from Product c where LOWER(c.buyercode) like LOWER(:buyercode)")
	public List<Product> getProductByLikeBuyercode(@Param ("buyercode")final  String buyercode);
	
	@Query(value = "select c from Product c "
			+ "inner join SKU b on c.id = b.productid_link "
			+ "inner join SKU_Attribute_Value a on a.skuid_link = c.id "
			+ "where LOWER(c.buyercode) = LOWER(:code) "
			+ "and c.orgrootid_link = :orgrootid_link "
			+ "and c.status = 1 "
			+ "and c.description = :description "
			+ "and c.producttypeid_link = :type "
			+ "and a.attributeid_link = :attributeid_link "
			+ "and a.attributevalueid_link = :attributevalueid_link "
			+ "group by c")
	public List<Product> getby_code_type_description_and_value(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("code")final  String code,
			@Param ("description")final  String description,
			@Param ("attributevalueid_link")final  Long attributevalueid_link,
			@Param ("attributeid_link")final  Long attributeid_link,
			@Param ("type")final  int type);

}
