package com.epam.esm.repository;

import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String tagName);
    @Query(value = "SELECT tag.tag_id, name, COUNT(name) AS qty from orders\n" +
            "inner join users u on u.user_id = orders.user_id\n" +
            "inner join tag_has_gift_certificate on (orders.certificate_id=tag_has_gift_certificate.gift_certificate_id)\n" +
            "inner join tag on (tag.tag_id=tag_has_gift_certificate.tag_id) where orders.user_id = \n" +
            "(select orders.user_id from orders\n" +
            "group by orders.user_id\n" +
            "order by sum(orders.cost) desc\n" +
            "limit 1) \n" +
            "group by tag.name\n" +
            "order by qty desc\n " +
            "limit 1", nativeQuery = true)
    Optional<Tag> getUsersMostWidelyUsedTag();
}
