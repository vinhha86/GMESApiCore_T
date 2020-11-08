package vn.gpay.gsmart.core.deviceout_type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IDeviceOutTypeRepository extends JpaRepository<DeviceOutType, Long>{

}
