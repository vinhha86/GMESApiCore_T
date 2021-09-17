package vn.gpay.gsmart.core.porder_workingprocess;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPOrderWorkingProcess_Repository extends JpaRepository<POrderWorkingProcess, Long>{
	@Query(value = "Select a from POrderWorkingProcess a where a.process_type=1")
	public List<POrderWorkingProcess>findAll_SubProcess();
	@Query(value = "Select a from POrderWorkingProcess a where a.process_type=0")
	public List<POrderWorkingProcess>findAll_MainProcess();	
	
	@Query(value = "Select a from POrderWorkingProcess a where a.process_type=1 and a.productid_link = :productid_link")
	public List<POrderWorkingProcess>getby_product(
			@Param("productid_link") final Long productid_link
			);
	@Query(value = "Select a from POrderWorkingProcess a "
			+ "where a.process_type=1 " 
			+ "and trim(lower(a.name)) = trim(lower(:name)) "
			+ "and a.productid_link = :productid_link "
			)
	public List<POrderWorkingProcess>getByName_Product(
			@Param("name") final String name,
			@Param("productid_link") final Long productid_link
			);
	
	@Query(value = "Select a from POrderWorkingProcess a "
			+ "where a.process_type = 1 " 
			+ "and a.porderid_link = :porderid_link "
			)
	public List<POrderWorkingProcess>getby_porder(
			@Param("porderid_link") final Long porderid_link
			);
	
	@Query(value = "Select a from POrderWorkingProcess a "
			+ "where a.process_type=1 " 
			+ "and trim(lower(a.code)) = trim(lower(:code)) "
			+ "and a.productid_link = :productid_link "
			+ "and a.porderid_link = :porderid_link "
			)
	public List<POrderWorkingProcess>getByCode(
			@Param("code") final String code,
			@Param("productid_link") final Long productid_link,
			@Param("porderid_link") final Long porderid_link
			);
	
	@Query(value = "Select a from POrderWorkingProcess a "
			+ "where a.process_type=1 " 
			+ "and trim(lower(a.code)) = trim(lower(:code)) "
			+ "and a.productid_link = :productid_link "
			+ "and a.porderid_link = :porderid_link "
			+ "and a.id != :id "
			)
	public List<POrderWorkingProcess>getByCode_NotId(
			@Param("code") final String code,
			@Param("productid_link") final Long productid_link,
			@Param("porderid_link") final Long porderid_link,
			@Param("id") final Long id
			);
}
