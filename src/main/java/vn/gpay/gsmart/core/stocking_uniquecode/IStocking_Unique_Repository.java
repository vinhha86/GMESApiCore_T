package vn.gpay.gsmart.core.stocking_uniquecode;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
@Transactional
public interface IStocking_Unique_Repository extends JpaRepository<Stocking_UniqueCode, Long> {
	@Query(value = "select c from Stocking_UniqueCode c "
			+ "where stocking_type =:stocking_type")
	public List<Stocking_UniqueCode> getby_type(
			@Param ("stocking_type")final Integer stocking_type);
}
