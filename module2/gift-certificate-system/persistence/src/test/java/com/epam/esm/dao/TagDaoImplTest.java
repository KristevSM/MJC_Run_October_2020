package com.epam.esm.dao;

import com.epam.esm.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TagDaoImplTest {

    private TagDaoImpl tagDao;

    @BeforeEach
    void setUp() {
        tagDao = new TagDaoImpl();
    }

        @Test
    void shouldFindAllTags() {
        List<Tag> tags = tagDao.findAll(1L, 20L);
        assertEquals(12L, tags.size());
        System.out.println(tags);

    }

    @Test
    void shouldFindTagById() {
        Optional<Tag> tag1 = tagDao.find(1L);
        Optional<Tag> tag2 = tagDao.find(12L);
        assertTrue(tag1.isPresent());
        assertTrue(tag2.isPresent());

    }

    @Test
    void shouldSaveAndDeleteTag() {
        Tag tag = Tag.builder()
                .name("New tag")
                .build();

        Long id = tagDao.save(tag);
        assertTrue(tagDao.find(id).isPresent());

        tagDao.delete(id);
        assertFalse(tagDao.find(id).isPresent());
    }

    @Test
    void shouldUpdateTag() {
        Optional<Tag> tag = tagDao.find(1L);
        Tag tagFromDao = tag.get();

        assertNotEquals("Updated name", tagFromDao.getName());
        tagFromDao.setName("Updated name");

        tagDao.update(tagFromDao);

        Optional<Tag> updatedTag = tagDao.find(1L);
        assertEquals("Updated name", updatedTag.get().getName());
    }

    @Test
    void shouldFindTagName() {
        Optional<Tag> tag = tagDao.findByTagName("Apple");
        assertTrue(tag.isPresent());

    }
}