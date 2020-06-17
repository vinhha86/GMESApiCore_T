package vn.gpay.gsmart.core.sku;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ISKU_Repository extends JpaRepository<SKU, Long>, JpaSpecificationExecutor<SKU> {
	@Query(value = "select c from SKU c where c.productid_link = :productid_link  order by c.id DESC")
	public List<SKU> getlist_byproduct(@Param ("productid_link")final  Long productid_link);
	
	@Query(value = "select c from SKU c where UPPER(c.code) = UPPER(:code) and orgrootid_link = :orgrootid_link")
	public List<SKU> getSKU_byCode(
			@Param ("code")final  String code,
			@Param ("orgrootid_link")final  long orgrootid_link);
	
	@Query(value = "select a from SKU a inner join Product b on a.productid_link = b.id and b.producttypeid_link >19 and b.producttypeid_link <30")
	public List<SKU>getSKU_MainMaterial(Specification<SKU> specification);
	
	@Query(value = "select a from SKU a inner join Product b on a.productid_link = b.id and b.producttypeid_link = :producttypeid_link")
	public List<SKU>getSKU_ByType(@Param ("producttypeid_link")final Integer producttypeid_link);

	@Query(value = "select a from SKU a inner join Product b on a.code = :code and a.productid_link = b.id and b.producttypeid_link = :producttypeid_link")
	public List<SKU>getSKU_ByTypeAndCode(@Param ("code")final  String code, @Param ("producttypeid_link")final Integer producttypeid_link);
}
