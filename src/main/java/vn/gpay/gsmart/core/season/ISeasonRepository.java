package vn.gpay.gsmart.core.season;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface ISeasonRepository extends JpaRepository<Season, Long>, JpaSpecificationExecutor<Season> {
	@Query(value = "select c from Season c where c.orgrootid_link = :orgrootid_link ")
	public List<Season> getall_byorgrootid(@Param ("orgrootid_link")final  Long orgrootid_link);
}
