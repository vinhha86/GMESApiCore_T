package vn.gpay.gsmart.core.cutplan_processing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CutplanProcessingRepository extends JpaRepository<CutplanProcessing, Long>,JpaSpecificationExecutor<CutplanProcessing>{
	@Query(value = "select c from CutplanProcessing c where cutplanrowid_link =:cutplanrowid_link")
	public List<CutplanProcessing> getby_cutplanrow(
			@Param ("cutplanrowid_link")final Long cutplanrowid_link);
	
	@Query(value = "SELECT sum(a.amountcut), a.processingdate "
			+ "from CutplanProcessing a "
			+ "inner join CutPlan_Row b on a.cutplanrowid_link = b.id "
			+ "where b.porderid_link = :porderid_link "
			+ "and a.material_skuid_link = :skuid_link "
			+ "group by a.processingdate "
			+ "order by a.processingdate "
			)
	public List<Object[]> getForChart_TienDoCat(
			@Param ("porderid_link")final Long porderid_link,
			@Param ("skuid_link")final Long skuid_link
			);
	
	@Query(value = "SELECT a.amount, a.product_skuid_link, a.cutplanrowid_link, "
			+ "b.material_skuid_link, c.processingdate, d.la_vai "
			+ "from CutPlan_Size a "
			+ "inner join CutPlan_Row b on a.cutplanrowid_link = b.id "
			+ "inner join CutplanProcessing c on c.cutplanrowid_link = b.id "
			+ "inner join CutplanProcessingD d on c.id = d.cutplan_processingid_link "
			
			+ "where b.porderid_link = :porderid_link "
			+ "and a.product_skuid_link = :product_skuid_link "
			+ "and b.material_skuid_link = :material_skuid_link "
			+ "and b.type = 0 "
			)
	public List<Object[]> getSlCat_by_product_material_porder(
			@Param ("product_skuid_link")final Long product_skuid_link,
			@Param ("material_skuid_link")final Long material_skuid_link,
			@Param ("porderid_link")final Long porderid_link
			);
}
