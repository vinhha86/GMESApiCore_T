package vn.gpay.gsmart.core.pcontracttype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PContractTypeRepository extends JpaRepository<PContractType, Long>,JpaSpecificationExecutor<PContractType>{

}
