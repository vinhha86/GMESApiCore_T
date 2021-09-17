package vn.gpay.gsmart.core.workingprocess;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IWorkingProcess_Repository extends JpaRepository<WorkingProcess, Long>{
	@Query(value = "Select a from WorkingProcess a where a.process_type=1")
	public List<WorkingProcess>findAll_SubProcess();
	@Query(value = "Select a from WorkingProcess a where a.process_type=0")
	public List<WorkingProcess>findAll_MainProcess();	
	
	@Query(value = "Select a from WorkingProcess a where a.process_type=1 and a.productid_link = :productid_link")
	public List<WorkingProcess>getby_product(
			@Param("productid_link") final Long productid_link
			);
	@Query(value = "Select a from WorkingProcess a "
			+ "where a.process_type=1 " 
			+ "and trim(lower(a.name)) = trim(lower(:name)) "
			+ "and a.productid_link = :productid_link "
			)
	public List<WorkingProcess>getByName_Product(
			@Param("name") final String name,
			@Param("productid_link") final Long productid_link
			);
	
	@Query(value = "Select a from WorkingProcess a "
			+ "where a.process_type=1 " 
			+ "and trim(lower(a.code)) = trim(lower(:code)) "
			+ "and a.productid_link = :productid_link "
			)
	public List<WorkingProcess>getByCode(
			@Param("code") final String code,
			@Param("productid_link") final Long productid_link
			);
	
	@Query(value = "Select a from WorkingProcess a "
			+ "where a.process_type=1 " 
			+ "and trim(lower(a.code)) = trim(lower(:code)) "
			+ "and a.productid_link = :productid_link "
			+ "and a.id != :id "
			)
	public List<WorkingProcess>getByCode_NotId(
			@Param("code") final String code,
			@Param("productid_link") final Long productid_link,
			@Param("id") final Long id
			);
}
