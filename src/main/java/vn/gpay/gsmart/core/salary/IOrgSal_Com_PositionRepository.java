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
public interface IOrgSal_Com_PositionRepository extends JpaRepository<OrgSal_Com_Position, Long>, JpaSpecificationExecutor<OrgSal_Com_Position> {
	@Query(value = "select c from OrgSal_Com_Position c where c.salcomid_link = :salcomid_link ")
	public List<OrgSal_Com_Position> getall_bysalcom(@Param ("salcomid_link")final  Long salcomid_link);
	
	@Query(value = "select c from OrgSal_Com_Position c where c.salcomid_link = :salcomid_link and c.positionid_link = :positionid_link")
	public List<OrgSal_Com_Position> getall_bysalcom_position(@Param ("salcomid_link")final  Long salcomid_link, @Param ("positionid_link")final  Long positionid_link);

}
