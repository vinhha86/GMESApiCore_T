package vn.gpay.gsmart.core.pcontract;

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
public interface PContract_AutoID_Repository extends JpaRepository<PContract_AutoID, Long>, JpaSpecificationExecutor<PContract> {
	@Lock(LockModeType.OPTIMISTIC)
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
	@Query("SELECT c FROM PContract_AutoID c where prefix = :prefix")
	public List<PContract_AutoID> fetchLastID(@Param ("prefix")final String prefix);
}
