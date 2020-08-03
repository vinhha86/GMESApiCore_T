package vn.gpay.gsmart.core.devices;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DeviceGroupRepository extends JpaRepository<DeviceGroup, Long>{
	public List<DeviceGroup> findAllByOrderByIdAsc();
}
