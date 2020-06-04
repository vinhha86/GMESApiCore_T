package vn.gpay.gsmart.core.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class ColorServiceImpl extends AbstractService<Color> implements IColorService{

	@Autowired
	ColorRepository repositoty;

	@Override
	protected JpaRepository<Color, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

}
