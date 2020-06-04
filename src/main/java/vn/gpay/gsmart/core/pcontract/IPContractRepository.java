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
}
