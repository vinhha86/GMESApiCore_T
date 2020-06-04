package vn.gpay.gsmart.core.invcheck;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vn.gpay.gsmart.core.stockout.StockOut;

@Repository
@Transactional
public interface InvcheckRepository extends JpaRepository<Invcheck, Long>,JpaSpecificationExecutor<Invcheck>{

	//@EntityGraph(attributePaths = { "epcs" },type=EntityGraphType.FETCH)
//	@Query(value = "select c from Invcheck c where orgid_link=:orgid_link order by invcheckdatetime desc")
	
	@Query(value = "select c from Invcheck c where orgid_link =:orgid_link "
			+ " and ( coalesce(:orgcheckid_link, null) is null  or (( coalesce(:orgcheckid_link, null) is not null ) and c.orgcheckid_link=:orgcheckid_link ) )"
			+ " and (  c.invcheckcode like '%'||:textsearch ||'%')"
			+ "     and   ( (coalesce(:invdateto_from, null) is null and coalesce(:invdateto_to, null) is null) "
	        + "             or ((coalesce(c.invcheckdatetime, null) is not null) "
	        + "                 and ( "
	        + "                        ((coalesce(:invdateto_from, null) is null and coalesce(:invdateto_to, null) is not null) and c.invcheckdatetime <= :invdateto_to) "
	        + "                     or ((coalesce(:invdateto_to, null) is null and coalesce(:invdateto_from, null) is not null) and c.invcheckdatetime >= :invdateto_from)"
	        + "                     or (c.invcheckdatetime between :invdateto_from and :invdateto_to)"
	        + "                 )"
	        + "             )"
	        + "       ) order by invcheckdatetime desc"
			)
	public List<Invcheck> invcheck_list(@Param ("orgid_link")final Long orgid_link ,@Param ("textsearch")final String textsearch,@Param ("orgcheckid_link")final String orgfrom_code,@Param ("invdateto_from")final Date invdateto_from,@Param ("invdateto_to")final Date invdateto_to);

	@Query(value = "select a from Invcheck a  where a.orgcheckid_link  = :orgcheckid_link  and a.status =0")
	public List<Invcheck> invcheck_getactive(@Param ("orgcheckid_link")final Long orgcheckid_link );
	
	@Query(value = "select a from Invcheck a  where a.invcheckcode  = :invcheckcode")
	public List<Invcheck> invcheck_getbycode(@Param ("invcheckcode")final String invcheckcode );	
	
	@Modifying
	@Query(value = "update Invcheck set status =1 where id  = :invcheckid")
	public void invcheck_deactive(@Param ("invcheckid")final Long invcheckid);
}
