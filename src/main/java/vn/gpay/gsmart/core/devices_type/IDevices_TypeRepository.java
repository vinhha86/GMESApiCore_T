package vn.gpay.gsmart.core.devices_type;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IDevices_TypeRepository
		extends JpaRepository<Devices_Type, Long>, JpaSpecificationExecutor<Devices_Type> {
	@Query(value = "select c from Devices_Type c " )
	public List<Devices_Type> loadDivicesType();
	
	@Query(value = "select c from Devices_Type c " 
			+ "where trim(lower(c.name)) = trim(lower(:name))"
			)
	public List<Devices_Type> loadDevicesTypeByName(
			@Param ("name")final String name
			);
	@Query(value = "select c from Devices_Type c " 
			+ "where trim(lower(c.code)) = trim(lower(:code))"
			)
	public List<Devices_Type> loadDevicesTypeByCode(
			@Param ("code")final String code
			);
}
