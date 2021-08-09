package vn.gpay.gsmart.core.dictionary_type;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vn.gpay.gsmart.core.devices_type.Devices_Type;
import vn.gpay.gsmart.core.personnel_type.PersonnelType;

@Repository
@Transactional
public interface IDictionaryType_Repository  extends JpaRepository<DictionaryType, Long>,JpaSpecificationExecutor<DictionaryType>{
	@Query(value = "select c from DictionaryType c " )
	public List<DictionaryType> loadDictionaryType();
}
