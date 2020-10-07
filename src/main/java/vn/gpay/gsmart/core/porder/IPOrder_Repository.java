package vn.gpay.gsmart.core.porder;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPOrder_Repository extends JpaRepository<POrder, Long>, JpaSpecificationExecutor<POrder> {
	@Query("SELECT c FROM POrder c where c.pcontractid_link = :pcontractid_link")
	public List<POrder> getByContract(@Param ("pcontractid_link")final Long pcontractid_link);
	
	@Query("SELECT c FROM POrder c where c.id = :id")
	public List<POrder> getById(@Param ("id")final Long id);

	@Query("SELECT c FROM POrder c where c.pcontractid_link = :pcontractid_link and c.productid_link = :productid_link")
	public List<POrder> getByContractAndProduct(@Param ("pcontractid_link")final Long pcontractid_link, @Param ("productid_link")final Long productid_link);

	@Query("SELECT c FROM POrder c where c.pcontract_poid_link = :pcontract_poid_link and c.productid_link = :productid_link")
	public List<POrder> getByPOAndProduct(@Param ("pcontract_poid_link")final Long pcontract_poid_link, @Param ("productid_link")final Long productid_link);

	@Query("SELECT c FROM POrder c where c.pcontractid_link = :pcontractid_link and c.pcontract_poid_link = :pcontract_poid_link")
	public List<POrder> getByContractAndPO(@Param ("pcontractid_link")final Long pcontractid_link, @Param ("pcontract_poid_link")final Long pcontract_poid_link);

	@Query("SELECT c FROM POrder c where c.pcontractid_link = :pcontractid_link and c.pcontract_poid_link = :pcontract_poid_link and c.status >= 0")
	public List<POrder> getByContractAndPO_Granted(@Param ("pcontractid_link")final Long pcontractid_link, @Param ("pcontract_poid_link")final Long pcontract_poid_link);

	@Query("SELECT c FROM POrder c where c.porderreqid_link = :porderreqid_link and c.pcontract_poid_link = :pcontract_poid_link")
	public List<POrder> getByPOrder_Req(@Param ("pcontract_poid_link")final Long pcontract_poid_link, @Param ("porderreqid_link")final Long porderreqid_link);

	@Query("SELECT c FROM POrder c where c.status = :status")
	public List<POrder> getByStatus(@Param ("status")final Integer status);
	
	@Query("SELECT c FROM POrder c where c.salarymonth is null")
	public List<POrder> getAll_SalaryUngranted(@Nullable Specification<POrder> spec, Sort sort);
	
	@Query(value = "select MAX(a.priority) from POrder a where a.status = 2")
	public Integer getMaxPriority();
	
	@Query(value = "select c from POrder c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and ordercode = :ordercode "
			+ "and status <> -1")
	public List<POrder> get_by_code(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("ordercode")final  String ordercode);
}
