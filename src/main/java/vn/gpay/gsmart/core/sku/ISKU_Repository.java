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
	@Query(value = "select a from SKU a " + "inner join SKU_Attribute_Value b on a.id = b.skuid_link "
			+ "where a.productid_link = :productid_link " + "group by a " + "having count(a.id) > 1 "
			+ "order by a.id DESC")
	public List<SKU> getlist_byproduct(@Param("productid_link") final Long productid_link);

	@Query(value = "select c from SKU c where UPPER(c.code) = UPPER(:code) and orgrootid_link = :orgrootid_link")
	public List<SKU> getSKU_byCode(@Param("code") final String code,
			@Param("orgrootid_link") final long orgrootid_link);

	@Query(value = "select a from SKU a inner join Product b on a.productid_link = b.id and b.producttypeid_link >19 and b.producttypeid_link <30")
	public List<SKU> getSKU_MainMaterial(Specification<SKU> specification);

	@Query(value = "select a from SKU a inner join Product b on a.productid_link = b.id and b.producttypeid_link = :producttypeid_link")
	public List<SKU> getSKU_ByType(@Param("producttypeid_link") final Integer producttypeid_link);

	@Query(value = "select a from SKU a inner join Product b on a.code = :code and a.productid_link = b.id and b.producttypeid_link = :producttypeid_link")
	public List<SKU> getSKU_ByTypeAndCode(@Param("code") final String code,
			@Param("producttypeid_link") final Integer producttypeid_link);

	@Query(value = "select a from SKU a inner join Product b " + "on b.id = a.productid_link "
			+ "where a.skutypeid_link = :skutypeid_link " + "and lower(a.code) like lower(concat('%',:code,'%')) ")
	public List<SKU> getProductSKU_ByCode(@Param("skutypeid_link") final Integer skutypeid_link,
			@Param("code") final String code);

	@Query(value = "select a from SKU a inner join Product b " + "on b.id = a.productid_link "
			+ "where a.skutypeid_link = :skutypeid_link "
//			+ "and lower(a.barcode) like lower(concat('%',:barcode,'%')) "
			+ "and a.barcode=:barcode ")
	public List<SKU> getProductSKU_ByBarCode(@Param("skutypeid_link") final Integer skutypeid_link,
			@Param("barcode") final String barcode);

	@Query(value = "select distinct a from SKU a " + "inner join POrderGrant_SKU b on a.id = b.skuid_link "
			+ "inner join POrderGrant c on c.id = b.pordergrantid_link "
			+ "inner join POrder d on d.id = c.porderid_link " + "where d.id = :porderid_link ")
	public List<SKU> getSKUforHandOver(@Param("porderid_link") final Long porderid_link);

	@Query(value = "select c from SKU c " + "inner join PContractProductBom2 a on c.id = a.materialid_link "
			+ "inner join Product b on b.id = c.productid_link " + "where a.pcontractid_link = :pcontractid_link "
			+ "and b.producttypeid_link >= :producttypeid_link_from "
			+ "and b.producttypeid_link < :producttypeid_link_to " + "group by c")
	public List<SKU> getbytype_and_pcontract(@Param("producttypeid_link_from") final Integer producttypeid_link_from,
			@Param("producttypeid_link_to") final Integer producttypeid_link_to,
			@Param("pcontractid_link") final Long pcontractid_link);

	@Query(value = "select c from SKU c " + "inner join PContractProductBom2 a on c.id = a.materialid_link "
			+ "inner join Product b on b.id = c.productid_link " + "where a.pcontractid_link = :pcontractid_link "
			+ "and b.producttypeid_link >= :producttypeid_link_from "
			+ "and b.producttypeid_link < :producttypeid_link_to " + "and a.productid_link =:productid_link "
			+ "group by c")
	public List<SKU> getbytype_and_pcontract_product(
			@Param("producttypeid_link_from") final Integer producttypeid_link_from,
			@Param("producttypeid_link_to") final Integer producttypeid_link_to,
			@Param("pcontractid_link") final Long pcontractid_link, @Param("productid_link") final Long productid_link);

	@Query(value = "select a from SKU a " + "where lower(a.code) like lower(concat('%',:code,'%')) "
			+ "and a.skutypeid_link >= 20 and a.skutypeid_link < 30 ")
	public List<SKU> getSkuByCode(@Param("code") final String code);

	@Query(value = "select a from SKU a " + "where lower(a.code) like lower(concat('%',:code,'%')) "
			+ "and (a.skutypeid_link >= :typeFrom or :typeFrom is null) "
			+ "and (a.skutypeid_link < :typeTo or :typeTo is null) ")
	public List<SKU> getSkuByCodeAndType(@Param("code") final String code, @Param("typeFrom") final Integer typeFrom,
			@Param("typeTo") final Integer typeTo);
}
