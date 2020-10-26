package vn.gpay.gsmart.core.salary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IOrgSal_Com_PositionRepository extends JpaRepository<OrgSal_Com_Position, Long>, JpaSpecificationExecutor<OrgSal_Com_Position> {

}
