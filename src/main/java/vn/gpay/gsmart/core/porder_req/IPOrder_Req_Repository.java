package vn.gpay.gsmart.core.porder_req;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPOrder_Req_Repository extends JpaRepository<POrder_Req, Long>, JpaSpecificationExecutor<POrder_Req> {
	@Query("SELECT c FROM POrder_Req c where c.pcontractid_link = :pcontractid_link")
	public List<POrder_Req> getByContract(@Param ("pcontractid_link")final Long pcontractid_link);
	
	@Query("SELECT c FROM POrder_Req c where c.pcontractid_link = :pcontractid_link and c.productid_link = :productid_link")
	public List<POrder_Req> getByContractAndProduct(@Param ("pcontractid_link")final Long pcontractid_link, @Param ("productid_link")final Long productid_link);

	@Query("SELECT c FROM POrder_Req c where c.pcontractid_link = :pcontractid_link and c.pcontract_poid_link = :pcontract_poid_link")
	public List<POrder_Req> getByContractAndPO(@Param ("pcontractid_link")final Long pcontractid_link, @Param ("pcontract_poid_link")final Long pcontract_poid_link);

	@Query("SELECT c FROM POrder_Req c where c.pcontract_poid_link = :pcontract_poid_link")
	public List<POrder_Req> getByPO( @Param ("pcontract_poid_link")final Long pcontract_poid_link);
	
	@Query("SELECT c FROM POrder_Req c where c.pcontract_poid_link = :pcontract_poid_link and is_calculate = 'False'")
	public List<POrder_Req> getByPO_calculate( @Param ("pcontract_poid_link")final Long pcontract_poid_link);

	@Query("SELECT c FROM POrder_Req c where c.status = :status")
	public List<POrder_Req> getByStatus(@Param ("status")final Integer status);
	
}
