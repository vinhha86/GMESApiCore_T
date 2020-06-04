package vn.gpay.gsmart.core.porder_bom_sku;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPOrderBOMSKU_Repository extends JpaRepository<POrderBOMSKU, Long>, JpaSpecificationExecutor<POrderBOMSKU> {
	@Query("SELECT c FROM POrderBOMSKU c where c.porderid_link = :porderid_link")
	public List<POrderBOMSKU> getByPOrderID(@Param ("porderid_link")final Long porderid_link);
	
	@Query("SELECT c FROM POrderBOMSKU c where c.porderid_link = :porderid_link and materialid_link = :materialid_link")
	public List<POrderBOMSKU> getSKUByMaterial(@Param ("porderid_link")final Long porderid_link, @Param ("materialid_link")final Long materialid_link);

	@Query("SELECT c.productid_link, c.materialid_link, sum(c.amount) as amount FROM POrderBOMSKU c where c.porderid_link = :porderid_link group by c.productid_link, c.materialid_link")
	public List<Object[]> getByPOrderID_GroupByProduct(@Param ("porderid_link")final Long porderid_link);
	
	@Query("SELECT c.productcolor_name, c.materialid_link, sum(c.amount) as amount FROM POrderBOMSKU c where c.porderid_link = :porderid_link group by c.productcolor_name, c.materialid_link")
	public List<Object[]> getByPOrderID_GroupByColor(@Param ("porderid_link")final Long porderid_link);
}
