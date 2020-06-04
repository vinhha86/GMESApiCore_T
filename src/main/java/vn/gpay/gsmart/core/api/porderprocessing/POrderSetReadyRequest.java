package vn.gpay.gsmart.core.api.porderprocessing;

import java.util.List;

import vn.gpay.gsmart.core.porder.POrderSetReady;

public class POrderSetReadyRequest{
	public Long granttoorgid_link;
	public List<POrderSetReady> data;
}
