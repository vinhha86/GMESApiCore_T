package vn.gpay.gsmart.core.pcontractbomcolor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPContractBOMColorRepository extends JpaRepository<PContractBOMColor, Long>, JpaSpecificationExecutor<PContractBOMColor> {
	@Query(value = "select c from PContractBOMColor c "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and c.colorid_link = :colorid_link "
			+ "and ( c.materialid_link = :materialid_link or 0 = :materialid_link)")
	public List<PContractBOMColor> getall_material_in_productBOMColor(
			@Param ("productid_link")final  long productid_link,
			@Param ("pcontractid_link")final  long pcontractid_link,
			@Param ("colorid_link")final  long colorid_link,
			@Param ("materialid_link")final  long materialid_link);
	
	@Query(value = "select c from PContractBOMColor c "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and c.materialid_link = :materialid_link")
	public List<PContractBOMColor> getcolor_bymaterial_in_productBOMColor(
			@Param ("productid_link")final  Long productid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("materialid_link")final  Long materialid_link);
	
	@Query(value = "select c from PContractBOMColor c "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link ")
	public List<PContractBOMColor> getall_in_productBOMColor(
			@Param ("productid_link")final  long productid_link,
			@Param ("pcontractid_link")final  long pcontractid_link);
	
	@Query(value = "select c from PContractBOMColor c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and c.pcontractid_link = :pcontractid_link")
	public List<PContractBOMColor> getall_bypcontract(
			@Param ("orgrootid_link")final  Long orgrootid_link, 
			@Param ("pcontractid_link")final  Long pcontractid_link);
}
