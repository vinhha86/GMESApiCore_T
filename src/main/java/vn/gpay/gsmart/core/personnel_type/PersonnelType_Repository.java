package vn.gpay.gsmart.core.personnel_type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PersonnelType_Repository extends JpaRepository<PersonnelType, Long>,JpaSpecificationExecutor<PersonnelType>{

}
