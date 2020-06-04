package vn.gpay.gsmart.core.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface ColorRepository extends JpaRepository<Color, Long>{

	@Query(value = "select c from Color c")
	public List<Color> findAll();
}
