package vn.gpay.gsmart.core.stocking_uniquecode;

import vn.gpay.gsmart.core.base.Operations;

public interface IStocking_UniqueCode_Service extends Operations<Stocking_UniqueCode> {
	public Stocking_UniqueCode getby_type(Integer type);
}
