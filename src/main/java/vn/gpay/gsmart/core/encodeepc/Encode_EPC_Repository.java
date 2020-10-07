package vn.gpay.gsmart.core.encodeepc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Encode_EPC_Repository  extends JpaRepository<Encode_EPC, Long>, JpaSpecificationExecutor<Encode_EPC> {
	@Query(value = "select c from Encode_EPC c "
			+ "where c.encodeid_link= :encodeid_link")
	public List<Encode_EPC> get_epc_by_encodeid_link(
			@Param ("encodeid_link")final  Long encodeid_link);
}
