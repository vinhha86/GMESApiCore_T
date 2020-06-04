package vn.gpay.gsmart.core.customer;


import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ICustomerService extends Operations<Customer>{

	public List<Customer> findbycode(String customercode);
}
