package vn.gpay.gsmart.core.sizeset;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ISizeSetRepository extends JpaRepository<SizeSet, Long>, JpaSpecificationExecutor<SizeSet> {
	@Query(value = "select c from SizeSet c where c.orgrootid_link = :orgrootid_link order by c.sortvalue asc")
	public List<SizeSet> getall_byorgrootid(@Param ("orgrootid_link")final  Long orgrootid_link);
	
	@Query(value = "select max(sortvalue) from SizeSet c")	
	public int getMaxSortValue();
}
