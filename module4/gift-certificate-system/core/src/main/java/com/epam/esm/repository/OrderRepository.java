package com.epam.esm.repository;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId, PageRequest pageRequest);
}
