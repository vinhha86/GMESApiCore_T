package vn.gpay.gsmart.core.pcontractproductdocument;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IPContractProducDocumentService extends Operations<PContractProductDocument> {
	public List<PContractProductDocument> getlist_byproduct(long orgrootid_link, long pcontractid_link,long productid_link);

	List<PContractProductDocument> getlist_bycontract(long orgrootid_link, long pcontractid_link);
}
