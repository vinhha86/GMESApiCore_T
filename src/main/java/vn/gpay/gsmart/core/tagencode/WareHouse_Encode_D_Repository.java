package vn.gpay.gsmart.core.tagencode;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface WareHouse_Encode_D_Repository extends JpaRepository<WareHouse_Encode_D, Long>,JpaSpecificationExecutor<WareHouse_Encode_D> {
	@Query(value = "select a from WareHouse_Encode_D a "
			+ "where a.warehouse_encodeid_link =:warehouse_encodeid_link "
			+ "and a.skucode = :skucode")
	List<WareHouse_Encode_D> get_byskucode(
			@Param ("warehouse_encodeid_link")final long warehouse_encodeid_link,
			@Param ("skucode")final String skucode) ;
	
	@Query(value = "select a from WareHouse_Encode_D a "
			+ "where a.warehouse_encodeid_link =:warehouse_encodeid_link "
			+ "and a.skuid_link = :skuid_link")
	List<WareHouse_Encode_D> get_byskuid(
			@Param ("warehouse_encodeid_link")final long warehouse_encodeid_link,
			@Param ("skuid_link")final Long skuid_link) ;
	
	@Query(value="select a from WareHouse_Encode_D a where a.warehouse_encodeid_link = :warehouse_encodeid_link")
	public List<WareHouse_Encode_D> encode_getbyencodeid(@Param ("warehouse_encodeid_link")final Long warehouse_encodeid_link);

}
