package vn.gpay.gsmart.core.documentguide;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DocumentGuide_Repository extends JpaRepository<DocumentGuide, Long>,JpaSpecificationExecutor<DocumentGuide> {
	@Query(value = "select c from DocumentGuide c where c.doctype = :doctype" )
	public List<DocumentGuide> loadByType(
			@Param ("doctype")final Integer doctype
			);
}
