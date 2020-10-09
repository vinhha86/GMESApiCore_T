package vn.gpay.gsmart.core.handover;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IHandoverService extends Operations<Handover>{
	public List<Handover> getByType(Long handovertypeid_link);
}
