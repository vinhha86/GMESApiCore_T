package vn.gpay.gsmart.core.customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long>{

	@Query(value = "select c from Customer c where code =:customercode")
	public List<Customer> findbycode(@Param ("customercode")final String customercode);
}
