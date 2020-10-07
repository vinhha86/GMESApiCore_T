package vn.gpay.gsmart.core.vat_type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IVatType_Repository extends JpaRepository<VatType, Long> {

}
