package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateDaoJdbc;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.TagDaoJdbc;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

//    @Test
//    void shouldReturnAllCertificates() {
//
//        List<GiftCertificate> certificateList = mock(ArrayList.class);
//        Mockito.when(certificateList.size()).thenReturn(10);
//        when(giftCertificateDao.findAll()).thenReturn(certificateList);
//
//        assertEquals(10, giftCertificateService.findAllCertificates().size());
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).findAll();
//    }

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

//    @Test
//    void shouldUpdateCertificate() {
//        GiftCertificate certificate = mock(GiftCertificate.class);
//        List<Tag> tags = new ArrayList<>();
//        Tag tag1 = new Tag();
//        Tag tag2 = new Tag();
//        tag1.setId(1L);
//        tag2.setId(2L);
//        certificate.setTags(tags);
//        certificate.setId(1L);
//        when(certificate.getTags()).thenReturn(tags);
//        System.out.println(certificate.getTags());
//        giftCertificateService.updateCertificate(certificate);
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).update(certificate);
//        Mockito.verify(tagDao, Mockito.times(2)).assignNewTagToCertificate(anyLong(), anyLong());
//
//    }
//
//    @Test
//    void shouldDeleteCertificate() {
//
//        GiftCertificate certificate = mock(GiftCertificate.class);
//        when(giftCertificateDao.find(1L)).thenReturn(Optional.ofNullable(certificate));
//        giftCertificateService.deleteCertificate(1L);
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).find(1L);
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).delete(1L);
//
//    }
//
//    @Test
//    void shouldThrowExceptionDeleteCertificate() {
//
//        when(giftCertificateDao.find(1L)).thenThrow(GiftCertificateNotFoundException.class);
//        assertThrows(GiftCertificateNotFoundException.class, () -> {
//            giftCertificateService.deleteCertificate(1L);
//        });
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).find(1L);
//        Mockito.verify(giftCertificateDao, Mockito.times(0)).delete(1L);
//    }

//    @Test
//    void shouldFindCertificatesByTagName() {
//
//        List<GiftCertificate> certificateList = mock(ArrayList.class);
//        Mockito.when(certificateList.size()).thenReturn(5);
//        when(giftCertificateDao.getCertificatesByTagName(matches("tag 2"))).thenReturn(certificateList);
//        assertEquals(5, giftCertificateService.getCertificatesByTagName("tag 2").size());
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).getCertificatesByTagName("tag 2");
//    }
//
//    @Test
//    void shouldThrowExceptionWhenCertificateByTagNameNotFound() {
//
//        assertThrows(GiftCertificateNotFoundException.class, () -> {
//            giftCertificateService.getCertificatesByTagName("tag 2");
//        });
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).getCertificatesByTagName("tag 2");
//    }
//
//    @Test
//    void shouldFindCertificatesByPartOfName() {
//
//        List<GiftCertificate> certificateList = mock(ArrayList.class);
//        Mockito.when(certificateList.size()).thenReturn(5);
//        when(giftCertificateDao.getCertificatesByPartOfName(matches("name 2"))).thenReturn(certificateList);
//
//        assertEquals(5, giftCertificateService.getCertificatesByPartOfName("name 2").size());
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).getCertificatesByPartOfName("name 2");
//    }
//
//    @Test
//    void shouldThrowExceptionWhenCertificateByPartOfNameNotFound() {
//
//        assertThrows(GiftCertificateNotFoundException.class, () -> {
//            giftCertificateService.getCertificatesByPartOfName("name 2");
//        });
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).getCertificatesByPartOfName("name 2");
//    }
//
//    @Test
//    void shouldFindCertificatesByPartOfDescription() {
//
//        List<GiftCertificate> certificateList = mock(ArrayList.class);
//        Mockito.when(certificateList.size()).thenReturn(5);
//        when(giftCertificateDao.getCertificatesByPartOfDescription(matches("some description"))).thenReturn(certificateList);
//
//        assertEquals(5, giftCertificateService.getCertificatesByPartOfDescription("some description").size());
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).getCertificatesByPartOfDescription("some description");
//    }
//
//    @Test
//    void shouldThrowExceptionWhenCertificateByPrtOfDescriptionNotFound() {
//
//        assertThrows(GiftCertificateNotFoundException.class, () -> {
//            giftCertificateService.getCertificatesByPartOfDescription("description");
//        });
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).getCertificatesByPartOfDescription("description");
//    }
}