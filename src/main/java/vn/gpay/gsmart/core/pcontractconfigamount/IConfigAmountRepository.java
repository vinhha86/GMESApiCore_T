package vn.gpay.gsmart.core.pcontractconfigamount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IConfigAmountRepository extends JpaRepository<ConfigAmount, Long>, JpaSpecificationExecutor<ConfigAmount> {

}
