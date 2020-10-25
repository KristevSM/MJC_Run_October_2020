package com.epam.esm.service;

import com.epam.esm.dao.*;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GiftCertificateServiceImplTest {

    private GiftCertificateService giftCertificateService;

    @Mock
    private GiftCertificateDao giftCertificateDao;

    @Mock
    private TagDao tagDao;

    @BeforeEach
    void setUp() {
        this.giftCertificateDao = mock(GiftCertificateDaoJdbc.class);
        this.tagDao = mock(TagDaoJdbc.class);
        this.giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao, tagDao);

    }

    @Test
    void shouldReturnAllCertificates() {

        List<GiftCertificate> certificateList = mock(ArrayList.class);
        Mockito.when(certificateList.size()).thenReturn(10);
        CertificateSearchQuery query = new CertificateSearchQuery();
        when(giftCertificateDao.getCertificates(query)).thenReturn(certificateList);
        assertEquals(10, giftCertificateService.getCertificates(query).size());
        Mockito.verify(giftCertificateDao, Mockito.times(1)).getCertificates(query);
    }

    @Test
    void shouldFindCertificateById() {

        GiftCertificate certificate = mock(GiftCertificate.class);
        when(giftCertificateDao.find(1L)).thenReturn(Optional.of(certificate));

        assertEquals(certificate, giftCertificateService.findCertificateById(1L));
        Mockito.verify(giftCertificateDao, Mockito.times(1)).find(1L);
    }

    @Test
    void shouldThrowExceptionWhenCertificateByIdNotFound() {

        GiftCertificate certificate = mock(GiftCertificate.class);
        when(giftCertificateDao.find(2L)).thenReturn(Optional.of(certificate));

        assertThrows(GiftCertificateNotFoundException.class, () -> {
            giftCertificateService.findCertificateById(1L);
        });
        Mockito.verify(giftCertificateDao, Mockito.times(1)).find(1L);
    }

    @Test
    void shouldSaveNewCertificate() {

        GiftCertificate certificate = mock(GiftCertificate.class);

        when(giftCertificateDao.save(certificate)).thenReturn(1L);
        Long expectedId = 1L;
        assertEquals(expectedId, giftCertificateService.saveCertificate(certificate));
        Mockito.verify(giftCertificateDao, Mockito.times(1)).save(certificate);
    }

    @Test
    void shouldUpdateCertificate() {
        GiftCertificate certificate = new GiftCertificate();
        List<Tag> tags = new ArrayList<>();
        Tag tag1 = new Tag();
        Tag tag2 = new Tag();
        tag1.setId(1L);
        tag2.setId(2L);
        certificate.setTags(tags);
        certificate.setId(1L);
        System.out.println(certificate.getTags());
        giftCertificateService.updateCertificate(certificate);
        Mockito.verify(giftCertificateDao, Mockito.times(1)).update(certificate);
//        Mockito.verify(tagDao, Mockito.times(1)).assignNewTagToCertificate(certificate.getId(), tag1.getId());

    }

    @Test
    void shouldDeleteCertificate() {

        GiftCertificate certificate = mock(GiftCertificate.class);
        when(giftCertificateDao.find(1L)).thenReturn(Optional.ofNullable(certificate));
        giftCertificateService.deleteCertificate(1L);
        Mockito.verify(giftCertificateDao, Mockito.times(1)).find(1L);
        Mockito.verify(giftCertificateDao, Mockito.times(1)).delete(1L);

    }

    @Test
    void shouldThrowExceptionDeleteCertificate() {

        when(giftCertificateDao.find(1L)).thenThrow(GiftCertificateNotFoundException.class);
        assertThrows(GiftCertificateNotFoundException.class, () -> {
            giftCertificateService.deleteCertificate(1L);
        });
        Mockito.verify(giftCertificateDao, Mockito.times(1)).find(1L);
        Mockito.verify(giftCertificateDao, Mockito.times(0)).delete(1L);
    }

    @Test
    void shouldFindCertificatesByTagName() {

        List<GiftCertificate> certificateList = mock(ArrayList.class);
        Mockito.when(certificateList.size()).thenReturn(5);
        CertificateSearchQuery query = new CertificateSearchQuery();
        query.setTagName("tag 2");
        when(giftCertificateDao.getCertificates(query)).thenReturn(certificateList);
        assertEquals(5, giftCertificateDao.getCertificates(query).size());
        Mockito.verify(giftCertificateDao, Mockito.times(1)).getCertificates(query);
    }

    @Test
    void shouldThrowExceptionWhenCertificateByTagNameNotFound() {
        CertificateSearchQuery query = new CertificateSearchQuery();
        assertThrows(GiftCertificateNotFoundException.class, () -> {
            giftCertificateService.getCertificates(query);
        });
        Mockito.verify(giftCertificateDao, Mockito.times(1)).getCertificates(query);
    }

    @Test
    void shouldRemoveTagFromCertificate() {

        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(1L);
        Tag tag = new Tag();
        tag.setId(1L);
        giftCertificateService.removeTagFromCertificate(certificate.getId(), tag.getId());
        Mockito.verify(giftCertificateDao, Mockito.times(1))
                .removeTagFromCertificate(certificate.getId(), tag.getId());

    }
}