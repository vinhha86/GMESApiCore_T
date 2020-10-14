package vn.gpay.gsmart.core.handover;

import java.util.List;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Handover_AutoID_Repository extends JpaRepository<Handover_AutoID, Long>, JpaSpecificationExecutor<Handover> {
	@Lock(LockModeType.OPTIMISTIC)
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
	@Query("SELECT c FROM Handover_AutoID c where prefix = :prefix")
	public List<Handover_AutoID> fetchLastID(@Param ("prefix")final String prefix);
}
