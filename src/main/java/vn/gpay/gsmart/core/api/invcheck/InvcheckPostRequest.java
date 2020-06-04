package vn.gpay.gsmart.core.api.invcheck;

import java.util.List;

import vn.gpay.gsmart.core.invcheck.InvcheckEpc;

public class InvcheckPostRequest {
	public String invcheckcode;
	public List<InvcheckEpc> data ;
}

