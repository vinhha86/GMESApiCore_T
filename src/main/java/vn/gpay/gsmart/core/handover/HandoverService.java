package vn.gpay.gsmart.core.handover;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;

@Service
public class HandoverService  extends AbstractService<Handover> implements IHandoverService{

	@Autowired HandoverRepository repo;
	
	@Override
	protected JpaRepository<Handover, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<Handover> getByType(Long handovertypeid_link) {
		// TODO Auto-generated method stub
		return repo.getByType(handovertypeid_link);
	}
	
	@Override
	public List<Handover> getByType(Long handovertypeid_link, Integer status) {
		// TODO Auto-generated method stub
		return repo.getByType(handovertypeid_link, status);
	}

	@Override
	public List<Handover> getByHandoverCode(String handover_code) {
		// TODO Auto-generated method stub
		return repo.getByHandoverCode(handover_code);
	}

	@Override
	public List<Handover> getHandOverBySearch(
				Long handovertypeid_link,
				Date handover_datefrom,
				Date handover_dateto,
				Long orgid_from_link,
				Long orgid_to_link,
				Integer status
			) {

		Specification<Handover> specification = Specifications.<Handover>and()
				.eq(Objects.nonNull(handovertypeid_link), "handovertypeid_link", handovertypeid_link)
				.eq(Objects.nonNull(orgid_from_link), "orgid_from_link", orgid_from_link)
				.eq(Objects.nonNull(orgid_to_link), "orgid_to_link", orgid_to_link)
				.ge(Objects.nonNull(handover_datefrom),"handover_date",GPAYDateFormat.atStartOfDay(handover_datefrom))
                .le(Objects.nonNull(handover_dateto),"handover_date",GPAYDateFormat.atEndOfDay(handover_dateto))
                .eq(Objects.nonNull(status), "status", status)
//                .ne("porderstatus.id", -1)
//                .ne("porderstatus.id", -3)
//                .eq(Objects.nonNull(granttoorgid_link), "granttoorgid_link", granttoorgid_link)
				.build();
		
		return repo.findAll(specification);
	}

}
