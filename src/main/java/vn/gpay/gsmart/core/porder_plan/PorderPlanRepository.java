package vn.gpay.gsmart.core.porder_plan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PorderPlanRepository extends JpaRepository<Porder_Plan, Long>, JpaSpecificationExecutor<Porder_Plan> {
	@Query(value = "select c from Porder_Plan c "
			+ "where porderid_link = :porderid_link "
			+ "and plan_type= :plan_type "
			+ "order by c.id")
	public List<Porder_Plan> getby_porder(
			@Param ("porderid_link")final  Long porderid_link,
			@Param ("plan_type")final  int plan_type);
}
