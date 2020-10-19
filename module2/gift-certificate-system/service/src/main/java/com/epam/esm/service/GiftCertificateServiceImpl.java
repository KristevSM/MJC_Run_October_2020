package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
    }

    @Override
    public List<GiftCertificate> findAllCertificates() {
        return giftCertificateDao.findAll();
    }

    @Override
    public GiftCertificate findCertificateById(Long id) {
        return giftCertificateDao.find(id).orElseThrow(GiftCertificateNotFoundException::new);
    }

    @Override
    public Long saveCertificate(GiftCertificate giftCertificate) {
        return giftCertificateDao.save(giftCertificate);
    }

    @Override
    public void updateCertificate(GiftCertificate giftCertificate) {
        List<Tag> tags = giftCertificate.getTags();

        tags.forEach(tag -> tagDao.assignNewTagToCertificate(tag.getId(), giftCertificate.getId()));
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        giftCertificateDao.update(giftCertificate);
    }

    @Override
    public void deleteCertificate(Long id) {
        Optional<GiftCertificate> certificate = giftCertificateDao.find(id);
        if (certificate.isPresent()) {
            GiftCertificate giftCertificate = certificate.get();
            giftCertificate.getTags().forEach(tag -> tagDao.removeTagAndCertificate(tag.getId(), id));
        } else {
            throw new GiftCertificateNotFoundException("Gift certificate with id: " + id + " not found");
        }
    }

    @Override
    public List<GiftCertificate> getCertificatesByTagName(String tagName) {
        return giftCertificateDao.getCertificatesByTagName(tagName);
    }

    @Override
    public List<GiftCertificate> getCertificatesByPartOfName(String partOfName) {
        return giftCertificateDao.getCertificatesByPartOfName(partOfName);
    }

    @Override
    public List<GiftCertificate> getCertificatesByPartOfDescription(String partOfDescription) {
        return giftCertificateDao.getCertificatesByPartOfDescription(partOfDescription);
    }

    @Override
    public void addTagToCertificate(Long certificateId, Long tagId) {
        tagDao.addNewTagAndToCertificate(tagId, certificateId);
    }

    @Override
    public void removeTagFromCertificate(Long certificateId, Long tagId) {
        giftCertificateDao.removeTagFromCertificate(certificateId, tagId);
    }

    @Override
    public void sortCertificateByParameters(String... parameters) {

    }
}
