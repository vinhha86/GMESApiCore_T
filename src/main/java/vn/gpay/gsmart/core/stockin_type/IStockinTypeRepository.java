package vn.gpay.gsmart.core.stockin_type;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IStockinTypeRepository extends JpaRepository<StockinType, Long>, JpaSpecificationExecutor<StockinType> {
	@Query(value = "select a from StockinType a "
			+ "where (a.id >= :typeFrom or :typeFrom is null) "
			+ "and (a.id <= :typeTo or :typeTo is null) "
			+ "order by a.id")
	public List<StockinType> findType(
			@Param ("typeFrom")final Long typeFrom,
			@Param ("typeTo")final Long typeTo
			);
}
