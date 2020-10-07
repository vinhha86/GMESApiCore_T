package vn.gpay.gsmart.core.tagencode;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WareHouse_Encode_EPC_History_Repository extends JpaRepository<WareHouse_Encode_EPC_History, Long>,JpaSpecificationExecutor<WareHouse_Encode_EPC_History> {
	@Query(value="select a from WareHouse_Encode_EPC_History a where "
			+ " a.epc =:epc and orgencodeid_link = :orgencodeid_link")
	public List<WareHouse_Encode_EPC_History> getEpc(
			@Param ("epc")final String epc,
			@Param ("orgencodeid_link")final Long orgencodeid_link);
}
