package vn.gpay.gsmart.core.pcontractbomsku;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
@Transactional
public interface IPContractBOM2SKURepository extends JpaRepository<PContractBOM2SKU, Long>, JpaSpecificationExecutor<PContractBOM2SKU> {
	@Query(value = "select c from PContractBOM2SKU c "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and c.product_skuid_link = :skuid_link "
			+ "and c.material_skuid_link = :materialid_link")
	public List<PContractBOM2SKU> getall_material_in_productBOMSKU(
			@Param ("productid_link")final  Long productid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("skuid_link")final  Long skuid_link,
			@Param ("materialid_link")final  Long materialid_link);
	
	@Query(value = "select c from PContractBOM2SKU c "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and c.material_skuid_link = :materialid_link")
	public List<PContractBOM2SKU> getall_bymaterial_in_productBOMSKU(
			@Param ("productid_link")final  Long productid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("materialid_link")final  Long materialid_link);
	
	@Query(value = "select c from PContractBOM2SKU c "
			+ "inner join SKU_Attribute_Value d on c.product_skuid_link = d.skuid_link "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and (d.attributevalueid_link = :colorid_link or :colorid_link = 0) "
			+ "and (c.material_skuid_link = :materialid_link or 0 = :materialid_link)")
	public List<PContractBOM2SKU> get_material_by_colorid_link(
			@Param ("productid_link")final  Long productid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("colorid_link")final  long colorid_link,
			@Param ("materialid_link")final  long materialid_link);
	
	@Query(value = "select d.attributevalueid_link "
			+ "from SKU_Attribute_Value d "
			+ "left join PContractBOM2SKU c on c.product_skuid_link = d.skuid_link "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and d.attributevalueid_link != :colorid_link")
	public List<Long> getsize_by_colorid_link(
			@Param ("productid_link")final  Long productid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("colorid_link")final  Long colorid_link);
	
	@Query(value = "select c from PContractBOM2SKU c where c.product_skuid_link = :skuid_link")
	public List<PContractBOM2SKU> getMaterials_BySKUId(@Param ("skuid_link")final  Long skuid_link);
	
	@Query(value = "select c from PContractBOM2SKU c where "
			+ "c.pcontractid_link = :pcontractid_link and "
			+ "c.product_skuid_link = :skuid_link")
	public List<PContractBOM2SKU> getBOM_By_PContractSKU(
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("skuid_link")final  Long skuid_link);
	
	@Query(value = "select c from PContractBOM2SKU c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and c.pcontractid_link = :pcontractid_link")
	public List<PContractBOM2SKU> getall_bypcontract(
			@Param ("orgrootid_link")final  Long orgrootid_link, 
			@Param ("pcontractid_link")final  Long pcontractid_link);
	
	@Query(value = "select c from PContractBOM2SKU c "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link")
	public List<PContractBOM2SKU> getall_bypcontract_andproduct(
			@Param ("productid_link")final  Long productid_link, 
			@Param ("pcontractid_link")final  Long pcontractid_link);
	
	//Lay danh sach cac san pham co su dung nguyen phu lieu
	@Query(value = "select distinct c.productid_link from PContractBOM2SKU c "
			+ "where c.pcontractid_link = :pcontractid_link and c.material_skuid_link = :material_skuid_link")
	public List<Object[]> getProductlist_ByMaterial(
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("material_skuid_link")final  Long material_skuid_link);
	
	//Lay danh sach cac nguyen phu lieu cua 1 Contract
	@Query(value = "select distinct a.material_skuid_link, b.code from PContractBOM2SKU a inner join SKU b on b.id = a.material_skuid_link "
			+ "where a.pcontractid_link = :pcontractid_link "
			+ "order by b.code")
	public List<Object[]> getMateriallist_ByContract(
			@Param ("pcontractid_link")final  Long pcontractid_link);
}
