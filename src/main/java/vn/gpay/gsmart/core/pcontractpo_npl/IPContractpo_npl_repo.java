package vn.gpay.gsmart.core.pcontractpo_npl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPContractpo_npl_repo extends JpaRepository<PContractPO_NPL, Long>, JpaSpecificationExecutor<PContractPO_NPL> {
	@Query(value = "select c from PContractPO_NPL c "
			+ "where pcontract_poid_link = :pcontractpoid_link "
			+ "and npl_skuid_link = :material_skuid_link"
			)
	public List<PContractPO_NPL> getby_po_and_npl(
			@Param ("pcontractpoid_link")final Long pcontractpoid_link,
			@Param ("material_skuid_link")final Long material_skuid_link);
	
	@Query(value = "select c from PContractPO_NPL c "
			+ "where pcontractid_link = :pcontractid_link "
			+ "and npl_skuid_link = :material_skuid_link"
			)
	public List<PContractPO_NPL> getby_pcontract_and_npl(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("material_skuid_link")final Long material_skuid_link);
}
