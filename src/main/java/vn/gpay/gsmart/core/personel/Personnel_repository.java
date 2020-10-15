package vn.gpay.gsmart.core.personel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Personnel_repository extends JpaRepository<Personel, Long>,JpaSpecificationExecutor<Personel> {

}