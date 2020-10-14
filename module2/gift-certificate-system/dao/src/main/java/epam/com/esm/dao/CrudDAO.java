package epam.com.esm.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<T> {

    Optional<T> find(Long id);
    void save(T model);
    void update(T model);
    void delete(Long id);

    List<T> findAll();
}
