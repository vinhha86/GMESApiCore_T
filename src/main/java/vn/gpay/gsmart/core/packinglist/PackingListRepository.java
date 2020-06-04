package vn.gpay.gsmart.core.packinglist;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface PackingListRepository extends JpaRepository<PackingList, Long>{

	@Query(value = "select c from PackingList c where stockindid_link =:code order by lotnumber,packageid")
	public List<PackingList> findByStockinDID(@Param ("code")final long stockincode);
	
	@Query(value = "select a from PackingList a "
			+ "inner join InvoiceD b on a.invoicedid_link = b.id "
			+ "where b.invoiceid_link =:invoiceid_link")
	public List<PackingList> inv_getbyid(@Param ("invoiceid_link")final  long invoiceid_link);
	
	@Query(value = "select a from PackingList a "
			+ "where invoicedid_link =:invoicedid_link")
	public List<PackingList> getpkl_byinvoiced(@Param ("invoicedid_link")final  long invoicedid_link);
	
	@Query(value = "select a from PackingList a "
			+ "where invoicedid_link =:invoicedid_link and lotnumber = :lotnumber")
	public List<PackingList> getpackageid_bylotnumber(
			@Param ("invoicedid_link")final  long invoicedid_link,
			@Param ("lotnumber")final  String lotnumber);
}
