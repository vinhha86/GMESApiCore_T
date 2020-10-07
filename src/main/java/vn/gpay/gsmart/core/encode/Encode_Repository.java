package vn.gpay.gsmart.core.encode;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface Encode_Repository extends JpaRepository<Encode, Long>, JpaSpecificationExecutor<Encode> {
	@Query(value = "select c from Encode c "
			+ "where c.porderid_link= :porderid_link "
			+ "and c.skuid_link = :skuid_link "
			+ "and c.orgrootid_link = :orgrootid_link")
	public List<Encode> get_Encode_by_porder_and_sku(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("porderid_link")final  Long porderid_link,
			@Param ("skuid_link")final  Long skuid_link);
	
	@Query(value = "select c from Encode c "
			+ "where c.orgrootid_link= :orgrootid_link "
			+ "and c.deviceid_link = :deviceid_link "
			+ "and c.status = 0")
	public List<Encode> get_Encode_using_device(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("deviceid_link")final  Long deviceid_link);
}
