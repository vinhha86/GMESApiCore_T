package vn.gpay.gsmart.core.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface GpayUserRepository extends JpaRepository<GpayUser, Long>,JpaSpecificationExecutor<GpayUser> {

	GpayUser findByUsername(String username);

	GpayUser findByEmail(String email);
	
	GpayUser findById(long id);

}