package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateDaoJdbc;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.TagDaoJdbc;
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

    private GiftCertificateService giftCertificateService;

    @BeforeEach
    void setUp() {
        tagDao = new TagDaoJdbc(dataSource);
        tagService = new TagServiceImpl(tagDao);
        giftCertificateDao = new GiftCertificateDaoJdbc(dataSource);
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao, tagDao);

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
    void shouldUpdateTag() {
        Tag actualTag = tagService.findTagById(3L);
        String previousName = actualTag.getName();
        System.out.println(previousName);
        String testName = "Tag 3 updated";
        actualTag.setName(testName);
        tagService.updateTag(actualTag);
        actualTag = tagService.findTagById(3L);
        assertEquals(testName, actualTag.getName());
        actualTag.setName(previousName);
        tagService.updateTag(actualTag);
        actualTag = tagService.findTagById(3L);
        assertEquals(previousName, actualTag.getName());
    }

    @Test
    void shouldAssignTag() {
        Tag tag = tagService.findTagById(3L);
        GiftCertificate certificate = giftCertificateService.findCertificateById(3L);
        System.out.println(certificate.getTags());
        Long tagId = tag.getId();
        Long certificateId = certificate.getId();
        tagService.assignTag(tagId, certificateId);
        certificate = giftCertificateService.findCertificateById(certificateId);

        assertTrue(certificate.getTags()
                .stream()
                .anyMatch(t -> t.getName().equals(tag.getName())));

        System.out.println(certificate.getTags());
        giftCertificateService.removeTagFromCertificate(certificateId, tagId);
        certificate = giftCertificateService.findCertificateById(certificateId);
        System.out.println(certificate.getTags());

        assertFalse(certificate.getTags()
                .stream()
                .anyMatch(t -> t.getName().equals(tag.getName())));
    }

    @Test
    void shouldAssignDefaultTag() {

        GiftCertificate certificate = giftCertificateService.findCertificateById(3L);
        System.out.println(certificate.getTags());
        Long certificateId = certificate.getId();
        tagService.assignDefaultTag("Main", certificateId);
        certificate = giftCertificateService.findCertificateById(certificateId);

        assertTrue(certificate.getTags()
                .stream()
                .anyMatch(t -> t.getName().equals("Main")));

        System.out.println(certificate.getTags());

        Tag defaultTag = tagService.findTagByTagName("Main");
        giftCertificateService.removeTagFromCertificate(certificateId, defaultTag.getId());
        certificate = giftCertificateService.findCertificateById(certificateId);
        System.out.println(certificate.getTags());

        assertFalse(certificate.getTags()
                .stream()
                .anyMatch(t -> t.getName().equals(defaultTag.getName())));
    }
}