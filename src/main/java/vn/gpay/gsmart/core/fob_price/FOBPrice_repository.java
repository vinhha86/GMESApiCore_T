package vn.gpay.gsmart.core.fob_price;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FOBPrice_repository extends JpaRepository<FOBPrice, Long>,JpaSpecificationExecutor<FOBPrice> {
	@Query(value = "select c from FOBPrice c where c.orgrootid_link =:orgrootid_link")
	public List<FOBPrice> getbyorgroot(
			@Param ("orgrootid_link")final  Long orgrootid_link);
	
	@Query(value = "select c from FOBPrice c where c.isdefault = true and c.id != 1 order by c.id")
	public List<FOBPrice> getAllDefault();
	
	@Query(value = "select c from FOBPrice c where lower(c.name) =lower(:name)")
	public List<FOBPrice> getByName(
			@Param ("name")final  String name);
	
	@Query(value = "select c from FOBPrice c where lower(c.name) =lower(:name) "
			+ "and c.id != :id "
			)
	public List<FOBPrice> getByName_other(
			@Param ("name")final  String name,
			@Param ("id")final  Long id
			);
}
