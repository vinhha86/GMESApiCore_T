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
public interface TagEncodeRepository extends JpaRepository<TagEncode, Long>,JpaSpecificationExecutor<TagEncode>{

//	@Query(value="select a from Menu a inner join UserMenu b on a.id = b.menuid where b.userid=:userid ")
//	public List<SaleBill> findByUserid(@Param ("userid")final long userid);
	
	@Query(value="select a from TagEncode a where  a.orgid_link =:orgid_link and a.deviceid_link =:deviceid_link and DATEDIFF('day', a.timecreate, :today) = 0")
	public List<TagEncode> encode_getbydevice(@Param ("orgid_link")final Long orgid_link,@Param ("deviceid_link")final Long deviceid_link ,@Param ("today")final Date today);
	
	
	@Modifying
	@Query(value = "delete from TagEncode c where orgid_link =:orgid_link and epc=:epc")
	public void deleteByEpc(@Param ("epc")final String epc,@Param ("orgid_link")final long orgid_link);	
}
