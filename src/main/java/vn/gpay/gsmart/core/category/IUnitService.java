package vn.gpay.gsmart.core.category;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IUnitService extends Operations<Unit> {
	List<Unit> getbyName(String name);
}
