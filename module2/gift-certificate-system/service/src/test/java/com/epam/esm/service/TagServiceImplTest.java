package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.TagDaoJdbc;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:test-db.xml"})
class TagServiceImplTest {

    private GiftCertificateService certificateService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private GiftCertificateDao giftCertificateDao;

    @BeforeEach
    void setUp() {
        certificateService = new GiftCertificateServiceImpl(giftCertificateDao, tagDao);
        tagDao = new TagDaoJdbc(dataSource);
    }

    @Test
    void shouldFindAllTags() {
        List<Tag> tagList = tagDao.findAll();
    }

    @Test
    void findTagById() {
    }

    @Test
    void saveTag() {
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