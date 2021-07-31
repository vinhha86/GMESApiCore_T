package vn.gpay.gsmart.core.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface GpayUserRepository extends JpaRepository<GpayUser, Long>,JpaSpecificationExecutor<GpayUser> {

	GpayUser findByUsername(String username);

	GpayUser findByEmail(String email);
	
	GpayUser findById(long id);
	
	//lấy danh sách user theo mã nhân viên, không chứa id truyền vào
	@Query(value="select c from GpayUser c where c.personnel_code = :personnel_code "
			+ "and c.id <> :id")
	public List<GpayUser> getUserBycode_Personel(
			@Param ("personnel_code") final String personnel_code,
			@Param ("id") final Long id);
}