package vn.gpay.gsmart.core.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface PortRepository extends JpaRepository<Port, Long>{

}
