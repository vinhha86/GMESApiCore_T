package vn.gpay.gsmart.core.porder_bom_color;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PorderBomColor_Repository extends JpaRepository<PorderBomColor, Long>, JpaSpecificationExecutor<PorderBomColor> {
	@Query("SELECT c FROM PorderBomColor c where c.porderid_link = :porderid_link")
	public List<PorderBomColor> getby_porder(
			@Param ("porderid_link")final Long porderid_link);
	
	@Query("SELECT c FROM PorderBomColor c where c.porderid_link = :porderid_link and c.colorid_link = :colorid_link")
	public List<PorderBomColor> getby_porder_and_color(
			@Param ("porderid_link")final Long porderid_link,
			@Param ("colorid_link")final Long colorid_link);
	
	@Query("SELECT c FROM PorderBomColor c where c.porderid_link = :porderid_link "
			+ "and c.colorid_link = :colorid_link "
			+ "and c.materialid_link = :materialid_link")
	public List<PorderBomColor> getby_porder_and_color_and_material(
			@Param ("porderid_link")final Long porderid_link,
			@Param ("colorid_link")final Long colorid_link,
			@Param ("materialid_link")final Long materialid_link);
	
	@Query("SELECT c FROM PorderBomColor c where c.pcontractid_link = :pcontractid_link and productid_link = :productid_link "
			+ "and c.colorid_link = :colorid_link "
			+ "and c.materialid_link = :materialid_link")
	public List<PorderBomColor> getby_pcontract_product_and_color_and_material(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("productid_link")final Long productid_link,
			@Param ("colorid_link")final Long colorid_link,
			@Param ("materialid_link")final Long materialid_link);
	
	@Query("SELECT c FROM PorderBomColor c where c.porderid_link = :porderid_link and c.materialid_link = :materialid_link")
	public List<PorderBomColor> getby_porder_and_material(
			@Param ("porderid_link")final Long porderid_link,
			@Param ("materialid_link")final Long materialid_link);
}
