package com.epam.esm.repository;

import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String tagName);
//    Optional<Tag> getUsersMostWidelyUsedTag();
}
