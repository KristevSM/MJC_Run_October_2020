package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.TagDaoJdbc;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:test-db.xml"})
class TagServiceImplTest {

    private TagService tagService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private GiftCertificateDao giftCertificateDao;

    @BeforeEach
    void setUp() {
        tagService = new TagServiceImpl(tagDao);
        tagDao = new TagDaoJdbc(dataSource);
    }

    @Test
    void shouldFindAllTags() {

        List<Tag> tags = tagService.findAllTags();
        int expectedListSize = 3;
        System.out.println(tags);
        assertEquals(expectedListSize, tags.size());
    }

    @Test
    void shouldFindTagById() {
        Long expectedId = 1L;
        Tag tag = tagService.findTagById(expectedId);
        assertEquals(expectedId, tag.getId());
        assertThrows(TagNotFoundException.class, () -> {
            tagService.findTagById(12L);
        });
    }

    @Test
    void shouldSaveAndRemoveTag() {
        Tag tag = Tag.builder()
                .name("Tag 5")
                .build();

        Long tagId = tagService.saveTag(tag);
        Tag actualTag = tagService.findTagById(tagId);
        assertEquals(tagId, actualTag.getId());
        tagService.deleteTag(tagId);
        assertThrows(TagNotFoundException.class, () -> {
            tagService.findTagById(5L);
        });
    }

    @Test
    void updateTag() {
    }

    @Test
    void assignTag() {
    }

    @Test
    void deleteTag() {
    }

    @Test
    void assignDefaultTag() {
    }
}