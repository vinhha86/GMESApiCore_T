package vn.gpay.gsmart.core.dictionary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IDictionary_Repository extends JpaRepository<Dictionary, Long>,JpaSpecificationExecutor<Dictionary>{
	@Query(value = "select c from Dictionary c " )
	public List<Dictionary> loadDictionary();
}
