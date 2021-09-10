package vn.gpay.gsmart.core.category;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ILaborLevelService extends Operations<LaborLevel>{
	public List<LaborLevel> findAllByOrderByIdAsc();
	List<LaborLevel> findByName(String name);
}
