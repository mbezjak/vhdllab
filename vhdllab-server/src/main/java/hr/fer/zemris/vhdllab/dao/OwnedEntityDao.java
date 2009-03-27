package hr.fer.zemris.vhdllab.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface OwnedEntityDao<T> extends EntityDao<T> {

    List<T> findByUser(String userId);

}
