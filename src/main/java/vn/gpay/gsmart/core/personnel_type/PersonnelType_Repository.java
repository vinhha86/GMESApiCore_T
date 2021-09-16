package vn.gpay.gsmart.core.personnel_type;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
@Transactional
public interface PersonnelType_Repository extends JpaRepository<PersonnelType, Long>,JpaSpecificationExecutor<PersonnelType>{
	
	@Query(value = "select c from PersonnelType c where trim(lower(replace(c.name,' ',''))) = trim(lower(replace(:name, ' ','')))  ")
	public List<PersonnelType> getByName(@Param ("name")final String name);
}
