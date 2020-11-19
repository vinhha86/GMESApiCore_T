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
public interface ISalary_SumRepository extends JpaRepository<Salary_Sum, Long>, JpaSpecificationExecutor<Salary_Sum> {
	@Query(value = "select c from Salary_Sum c inner join Personel d on c.personnelid_link = d.id "
			+ "where d.orgid_link = :orgid_link")
	public List<Salary_Sum> getall_byorg(@Param ("orgid_link")final  Long orgid_link);
	
	@Query(value = "select c from Salary_Sum c"
			+ " where c.personnelid_link = :personnelid_link"
			+ " and c.year = :year"
			+ " and c.month = :month")
	public List<Salary_Sum> getby_key(@Param ("personnelid_link")final  Long personnelid_link,
			@Param ("year")final  Integer year,
			@Param ("month")final  Integer month);
}
