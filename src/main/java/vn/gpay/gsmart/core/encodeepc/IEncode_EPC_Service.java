package vn.gpay.gsmart.core.encodeepc;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IEncode_EPC_Service extends Operations<Encode_EPC> {
	List<Encode_EPC> get_epc_by_encodeid_link(long encodeid_link);
}
