package vn.gpay.gsmart.core.org;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface OrgTypeRepository extends JpaRepository<OrgType, Long>{

}
