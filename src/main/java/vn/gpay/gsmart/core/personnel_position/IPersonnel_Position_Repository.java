package vn.gpay.gsmart.core.personnel_position;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPersonnel_Position_Repository extends JpaRepository<Personnel_Position, Long>,JpaSpecificationExecutor<Personnel_Position>{
	@Query(value = "select c from Personnel_Position c ")
	public List<Personnel_Position> getPersonnel_Position();
	
}
