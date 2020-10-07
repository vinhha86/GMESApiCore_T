package vn.gpay.gsmart.core.tagencode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WareHouse_Encode_Repository extends JpaRepository<WareHouse_Encode, Long>,JpaSpecificationExecutor<WareHouse_Encode> {

}
