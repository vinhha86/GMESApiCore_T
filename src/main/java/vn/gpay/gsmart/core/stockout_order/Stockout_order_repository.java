package vn.gpay.gsmart.core.stockout_order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Stockout_order_repository extends JpaRepository<Stockout_order, Long>, JpaSpecificationExecutor<Stockout_order> {
	@Query(value = "select c from Stockout_order c where porderid_link = :porderid_link ")
	public List<Stockout_order> getby_porder(
			@Param ("porderid_link")final  long porderid_link);
}
