package vn.gpay.gsmart.core.personnel_notmap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Personnel_notmap_repository extends JpaRepository<Personnel_notmap, Long>,JpaSpecificationExecutor<Personnel_notmap> {

}
