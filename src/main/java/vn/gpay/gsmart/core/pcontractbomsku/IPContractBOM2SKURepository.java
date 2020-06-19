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
			+ "and c.skuid_link = :skuid_link "
			+ "and c.materialid_link = :materialid_link")
	public List<PContractBOM2SKU> getall_material_in_productBOMSKU(
			@Param ("productid_link")final  Long productid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("skuid_link")final  Long skuid_link,
			@Param ("materialid_link")final  Long materialid_link);
	
	@Query(value = "select c from PContractBOM2SKU c "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and c.materialid_link = :materialid_link")
	public List<PContractBOM2SKU> getall_bymaterial_in_productBOMSKU(
			@Param ("productid_link")final  Long productid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("materialid_link")final  Long materialid_link);
	
	@Query(value = "select c from PContractBOM2SKU c "
			+ "inner join SKU_Attribute_Value d on c.skuid_link = d.skuid_link "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and d.attributevalueid_link = :colorid_link "
			+ "and (c.materialid_link = :materialid_link or 0 = :materialid_link)")
	public List<PContractBOM2SKU> get_material_by_colorid_link(
			@Param ("productid_link")final  Long productid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("colorid_link")final  Long colorid_link,
			@Param ("materialid_link")final  Long materialid_link);
	
	@Query(value = "select d.attributevalueid_link "
			+ "from SKU_Attribute_Value d "
			+ "left join PContractBOMSKU c on c.skuid_link = d.skuid_link "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and d.attributevalueid_link != :colorid_link")
	public List<Long> getsize_by_colorid_link(
			@Param ("productid_link")final  Long productid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("colorid_link")final  Long colorid_link);
	
	@Query(value = "select c from PContractBOM2SKU c where c.skuid_link = :skuid_link")
	public List<PContractBOM2SKU> getMaterials_BySKUId(@Param ("skuid_link")final  Long skuid_link);
}
