package vn.gpay.gsmart.core.cutplan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CutPlan_Repository extends JpaRepository<CutPlan_Size, Long>, JpaSpecificationExecutor<CutPlan_Size>{
	@Query(value = "select c from CutPlan c "
			+ " where c.skuid_link = :skuid_link " 
			+ "and c.porderid_link = :porderid_link "
			+ "and c.orgrootid_link = :orgrootid_link")
	public List<CutPlan_Size> getby_sku_and_porder(
			@Param("skuid_link") final Long skuid_link,
			@Param("porderid_link") final Long porderid_link,
			@Param("orgrootid_link") final Long orgrootid_link);
}
