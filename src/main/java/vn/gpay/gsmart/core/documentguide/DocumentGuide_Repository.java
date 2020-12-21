package vn.gpay.gsmart.core.documentguide;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DocumentGuide_Repository extends JpaRepository<DocumentGuide, Long>,JpaSpecificationExecutor<DocumentGuide> {

}
