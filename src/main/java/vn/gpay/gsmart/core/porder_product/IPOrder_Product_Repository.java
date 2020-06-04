package vn.gpay.gsmart.core.porder_product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPOrder_Product_Repository extends JpaRepository<POrder_Product, Long>, JpaSpecificationExecutor<POrder_Product> {

}
