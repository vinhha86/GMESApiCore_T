package vn.gpay.gsmart.core.approle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AppRole_Repository extends JpaRepository<AppRole, Long>, JpaSpecificationExecutor<AppRole> {

}
