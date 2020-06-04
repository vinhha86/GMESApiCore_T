package vn.gpay.gsmart.core.stock;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IStockrowService extends Operations<Stockrow>{

	public List<Stockrow> findStockrowByOrgID(long id);
}
