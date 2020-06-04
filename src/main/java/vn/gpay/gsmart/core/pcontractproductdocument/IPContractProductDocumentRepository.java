package vn.gpay.gsmart.core.pcontractproductdocument;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPContractProductDocumentRepository extends JpaRepository<PContractProductDocument, Long>, JpaSpecificationExecutor<PContractProductDocument> {
	@Query(value = "select c from PContractProductDocument c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and c.productid_link = :productid_link ")
	public List<PContractProductDocument> getall_byproduct(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("productid_link")final  Long productid_link);
}
