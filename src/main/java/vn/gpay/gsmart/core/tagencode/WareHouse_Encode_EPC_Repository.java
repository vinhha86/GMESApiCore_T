package vn.gpay.gsmart.core.tagencode;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface WareHouse_Encode_EPC_Repository extends JpaRepository<WareHouse_Encode_EPC, Long>,JpaSpecificationExecutor<WareHouse_Encode_EPC>{

//	@Query(value="select a from Menu a inner join UserMenu b on a.id = b.menuid where b.userid=:userid ")
//	public List<SaleBill> findByUserid(@Param ("userid")final long userid);
	
	@Query(value="select a from WareHouse_Encode_EPC a where  "
			+ "a.orgrootid_link =:orgrootid_link "
			+ "and a.deviceid_link =:deviceid_link "
			+ "and DATEDIFF('day', a.timecreate, :today) = 0")
	public List<WareHouse_Encode_EPC> encode_getbydevice(
			@Param ("orgrootid_link")final Long orgrootid_link,
			@Param ("deviceid_link")final Long deviceid_link ,
			@Param ("today")final Date today);
	
	@Query(value="select a from WareHouse_Encode_EPC a where "
			+ " a.orgrootid_link =:orgrootid_link and "
			+ "a.epc =:OldEPC and "
			+ "(a.warehouse_encodeid_link = :warehouse_encodeid_link or "
			+ ":warehouse_encodeid_link = 0)")
	public List<WareHouse_Encode_EPC> getTagEncode_byOldEPC(
			@Param ("orgrootid_link")final Long orgrootid_link,
			@Param ("OldEPC")final String OldEPC,
			@Param ("warehouse_encodeid_link")final Long warehouse_encodeid_link);
	
	@Query(value="select a from WareHouse_Encode_EPC a where a.orgrootid_link =:orgrootid_link and a.epc = :epc")
	public List<WareHouse_Encode_EPC> encode_getbyepc(@Param ("orgrootid_link")final Long orgrootid_link, @Param ("epc")final String epc);
	
	@Query(value="select a from WareHouse_Encode_EPC a where a.warehouse_encodeid_link = :warehouse_encodeid_link")
	public List<WareHouse_Encode_EPC> encode_getbyencodeid(@Param ("warehouse_encodeid_link")final Long warehouse_encodeid_link);

	@Modifying
	@Query(value = "delete from WareHouse_Encode_EPC c where orgrootid_link =:orgrootid_link and epc=:epc")
	public void deleteByEpc(@Param ("epc")final String epc,@Param ("orgrootid_link")final long orgrootid_link);	
}
