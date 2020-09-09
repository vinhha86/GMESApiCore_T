package vn.gpay.gsmart.core.pcontract;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
@Transactional
public interface IPContractRepository extends JpaRepository<PContract, Long>, JpaSpecificationExecutor<PContract> {
	@Query(value = "select c from PContract c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and c.status = 1 "
			+ "and contractcode = :contractcode "
			+ "and id != :pcontractid_link")
	public List<PContract> get_byorgrootid_link_and_contractcode(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link,@Param ("contractcode")final  String contractcode);
	
	@Query(value = "select c from PContract c "
			+ "inner join PContract_PO b on b.pcontractid_link = c.id "
			+ "inner join Product a on b.productid_link = a.id "
			+ "where b.po_buyer like :po_buyer "
			+ "and a.buyercode like :buyercode ")
	public List<PContract> getBySearchIgnoreCase(@Param ("po_buyer")final String po_buyer,
			@Param ("buyercode")final String buyercode);
	
	
}
