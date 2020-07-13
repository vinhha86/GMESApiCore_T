package vn.gpay.gsmart.core.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractService<T extends Serializable> implements Operations<T>{

	@Override
    @Transactional(readOnly = true)
    public T findOne(final long id) {
        return getRepository().findById(id).get();
    }

	// read - all

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return new ArrayList<T>(getRepository().findAll());
    }

    @Override
    public Page<T> findPaginated(final int page, final int size) {
        return getRepository().findAll(PageRequest.of(page, size));
    }

    // write

    @Override
    public T create(final T entity) {
        return getRepository().save(entity);
    }
    
   // save
    @Override
    public T save(final T entity) {
        return getRepository().save(entity);
    }
    
    @Override
    public T saveAndFlush(final T entity) {
        return getRepository().saveAndFlush(entity);
    }
    
    @Override
    public T update(final T entity) {
        return getRepository().save(entity);
    }

    @Override
    public void delete(final T entity) {
    	getRepository().delete(entity);
    }

    @Override
    public void deleteById(final long entityId) {
    	getRepository().deleteById(entityId);
    }

    protected abstract JpaRepository<T, Long> getRepository();
}
