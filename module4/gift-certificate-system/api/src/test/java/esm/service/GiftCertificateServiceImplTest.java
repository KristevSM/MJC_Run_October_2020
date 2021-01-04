package esm.service;

import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GiftCertificateServiceImplTest {

//    private GiftCertificateService giftCertificateService;
//    private GiftCertificateValidator giftCertificateValidator;
//    private TagValidator tagValidator;
//    private GiftCertificateDao giftCertificateDao;
//    private TagDao tagDao;
//    private OrderDao orderDao;
//
//    @BeforeEach
//    void setUp() {
//        this.giftCertificateDao = mock(GiftCertificateDaoImpl.class);
//        this.tagDao = mock(TagDaoImpl.class);
//        this.orderDao = mock(OrderDaoImpl.class);
//        this.giftCertificateValidator = mock(GiftCertificateValidator.class);
//        this.tagValidator = mock(TagValidator.class);
//        this.giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao, tagDao, orderDao,
//                giftCertificateValidator, tagValidator);
//
//    }
//
//    @Test
//    void shouldReturnAllCertificates() {
//
//        List<GiftCertificate> certificateList = mock(ArrayList.class);
//        Mockito.when(certificateList.size()).thenReturn(10);
//        CertificateSearchQuery query = new CertificateSearchQuery();
//        when(giftCertificateDao.getCertificates(query, 1L, 20L)).thenReturn(certificateList);
//        assertEquals(10, giftCertificateService.getCertificates(query, 1L, 20L).size());
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).getCertificates(query, 1L, 20L);
//    }
//
//    @Test
//    void shouldFindCertificateById() {
//
//        GiftCertificate certificate = mock(GiftCertificate.class);
//        when(giftCertificateDao.find(1L)).thenReturn(Optional.of(certificate));
//
//        assertEquals(certificate, giftCertificateService.findCertificateById(1L));
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).find(1L);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenCertificateByIdNotFound() {
//
//        GiftCertificate certificate = mock(GiftCertificate.class);
//        when(giftCertificateDao.find(2L)).thenReturn(Optional.of(certificate));
//
//        assertThrows(GiftCertificateNotFoundException.class, () -> {
//            giftCertificateService.findCertificateById(1L);
//        });
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).find(1L);
//    }
//
//    @Test
//    void shouldSaveNewCertificate() {
//
//        GiftCertificate certificate = mock(GiftCertificate.class);
//
//        when(giftCertificateDao.save(certificate)).thenReturn(1L);
//        Long expectedId = 1L;
//        assertEquals(expectedId, giftCertificateService.saveCertificate(certificate));
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).save(certificate);
//    }
//
//    @Test
//    void shouldUpdateCertificate() {
//        GiftCertificate certificate = new GiftCertificate();
//        List<Tag> tags = new ArrayList<>();
//        Tag tag1 = new Tag();
//        Tag tag2 = new Tag();
//        tag1.setId(1L);
//        tag2.setId(2L);
//        certificate.setTags(tags);
//        certificate.setId(1L);
//        System.out.println(certificate.getTags());
//        giftCertificateService.updateCertificate(certificate);
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).update(certificate);
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
//
//    @Test
//    void shouldFindCertificatesByTagName() {
//
//        List<GiftCertificate> certificateList = mock(ArrayList.class);
//        Mockito.when(certificateList.size()).thenReturn(5);
//        CertificateSearchQuery query = new CertificateSearchQuery();
//        query.setTagName("tag 2");
//        when(giftCertificateDao.getCertificates(query, 1L, 20L)).thenReturn(certificateList);
//        assertEquals(5, giftCertificateDao.getCertificates(query, 1L, 20L).size());
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).getCertificates(query, 1L, 20L);
//    }
//
//    @Test
//    void shouldFindCertificatesByTagNames() {
//
//        List<GiftCertificate> certificateList = mock(ArrayList.class);
//        Mockito.when(certificateList.size()).thenReturn(2);
//        List<String> tagNames = new ArrayList<>();
//        tagNames.add("Tag1");
//        tagNames.add("Tag2");
//        when(giftCertificateDao.findCertificatesByTags(tagNames, 1L, 20L)).thenReturn(certificateList);
//        assertEquals(2, giftCertificateDao.findCertificatesByTags(tagNames, 1L, 20L).size());
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).findCertificatesByTags(tagNames, 1L, 20L);
//    }
//
//    @Test
//    void shouldReturnEmptyListWhenCertificateByTagNameNotFound() {
//        CertificateSearchQuery query = new CertificateSearchQuery();
//        List<GiftCertificate> certificates = giftCertificateService.getCertificates(query, 1L, 20L);
//        assertTrue(certificates.isEmpty());
//        Mockito.verify(giftCertificateDao, Mockito.times(1)).getCertificates(query, 1L, 20L);
//    }
}