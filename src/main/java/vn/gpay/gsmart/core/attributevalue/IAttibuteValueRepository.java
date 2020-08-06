package vn.gpay.gsmart.core.attributevalue;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IAttibuteValueRepository extends JpaRepository<Attributevalue, Long>, JpaSpecificationExecutor<Attributevalue> {
	@Query(value = "select c from Attributevalue c "
			+ "where c.attributeid_link =:attributeid_link "
			+ "order by sortvalue")
	public List<Attributevalue> getlist_ByidAttribute(@Param ("attributeid_link")final long attributeid_link);
	
	@Query(value = "select max(sortvalue) from Attributevalue c "
			+ "where c.attributeid_link =:attributeid_link ")	
	public int getMaxSortValue(@Param ("attributeid_link")final long attributeid_link);
}
