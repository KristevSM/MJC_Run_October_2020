package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<T> {

    Optional<T> find(Long id);
    Long save(T model);
    void update(T model);
    void delete(Long id);

    List<T> findAll(int from, int pageSize);
}
