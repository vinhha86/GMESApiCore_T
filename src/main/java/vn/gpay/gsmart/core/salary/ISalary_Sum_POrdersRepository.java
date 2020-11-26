package vn.gpay.gsmart.core.salary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface ISalary_Sum_POrdersRepository extends JpaRepository<Salary_Sum_POrders, Long>, JpaSpecificationExecutor<Salary_Sum_POrders> {
	@Query(value = "select c from Salary_Sum_POrders c"
			+ " where c.orgid_link = :orgid_link"
			+ " and c.year = :year and c.month = :month")
	public List<Salary_Sum_POrders> getall_byorg(@Param ("orgid_link")final  Long orgid_link,
			@Param ("year")final  Integer year,
			@Param ("month")final  Integer month);
	
	@Query(value = "select distinct c from Salary_Sum_POrders c"
			+ " inner join PorderProcessingNs d on c.porderid_link = d.porderid_link"
			+ " where d.personnelid_link = :personnelid_link"
			+ " and c.year = :year and c.month = :month")
	public List<Salary_Sum_POrders> getall_bypersonnel(@Param ("personnelid_link")final  Long personnelid_link,
			@Param ("year")final  Integer year,
			@Param ("month")final  Integer month);	
	
	@Query(value = "select c from Salary_Sum_POrders c"
			+ " where c.pordergrantid_link = :pordergrantid_link"
			+ " and c.year = :year"
			+ " and c.month = :month")
	public List<Salary_Sum_POrders> getby_key(@Param ("pordergrantid_link")final  Long pordergrantid_link,
			@Param ("year")final  Integer year,
			@Param ("month")final  Integer month);
}
