package vn.gpay.gsmart.core.salary;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IOrgSal_Type_LaborLevelRepository extends JpaRepository<OrgSal_Type_LaborLevel, Long>, JpaSpecificationExecutor<OrgSal_Type_LaborLevel> {

}
