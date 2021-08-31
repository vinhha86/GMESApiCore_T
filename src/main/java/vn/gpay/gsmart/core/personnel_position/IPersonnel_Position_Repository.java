package vn.gpay.gsmart.core.personnel_position;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPersonnel_Position_Repository extends JpaRepository<Personnel_Position, Long>,JpaSpecificationExecutor<Personnel_Position>{
	@Query(value = "select c from Personnel_Position c ")
	public List<Personnel_Position> getPersonnel_Position();
	//lay chuc vu theo code
	@Query(value = "select c from Personnel_Position c where "
			+ " trim(lower(replace(c.name,' ',''))) = trim(lower(replace(:name, ' ',''))) "
			+ "and trim(lower(replace(c.code,' ',''))) = trim(lower(replace(:code, ' ','')))")
	public Personnel_Position getByName_Code(
			@Param ("name")final String name,
			@Param ("code")final String code);
}
