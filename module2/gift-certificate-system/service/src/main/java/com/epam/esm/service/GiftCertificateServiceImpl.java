package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
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
        List<GiftCertificate> certificateList = giftCertificateDao.findAll();
        if (certificateList.isEmpty()) {
            throw new GiftCertificateNotFoundException("Gift certificates were not found");
        } else {
            return certificateList;
        }
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
        giftCertificate.setLastUpdateDate(ZonedDateTime.now());
        giftCertificateDao.update(giftCertificate);
    }

    @Override
    public void deleteCertificate(Long id) {
        Optional<GiftCertificate> certificate = giftCertificateDao.find(id);
        if (certificate.isPresent()) {
            GiftCertificate giftCertificate = certificate.get();
            giftCertificate.getTags().forEach(tag -> tagDao.removeTagAndCertificate(tag.getId(), id));
        } else {
            throw new GiftCertificateNotFoundException(MessageFormat.format("Gift certificate with id: {0} not found", id));
        }
    }

    @Override
    public List<GiftCertificate> getCertificatesByTagName(String tagName) {
        List<GiftCertificate> certificateList = giftCertificateDao.getCertificatesByTagName(tagName);
        if (certificateList.isEmpty()) {
            throw new GiftCertificateNotFoundException(MessageFormat.format("Gift certificate with tag name: {0} was not found", tagName));
        } else {
            return certificateList;
        }
    }

    @Override
    public List<GiftCertificate> getCertificatesByPartOfName(String partOfName) {
        List<GiftCertificate> certificateList = giftCertificateDao.getCertificatesByPartOfName(partOfName);
        if (certificateList.isEmpty()) {
            throw new GiftCertificateNotFoundException(MessageFormat.format("Gift certificate containing the name: {0} was not found", partOfName));
        } else {
            return certificateList;
        }
    }

    @Override
    public List<GiftCertificate> getCertificatesByPartOfDescription(String partOfDescription) {
        List<GiftCertificate> certificateList = giftCertificateDao.getCertificatesByPartOfDescription(partOfDescription);
        if (certificateList.isEmpty()) {
            throw new GiftCertificateNotFoundException(MessageFormat.format("Gift certificate containing the description: {0} was not found", partOfDescription));
        } else {
            return certificateList;
        }
    }

    @Override
    public void addTagToCertificate(Long certificateId, Long tagId) {
        Optional<GiftCertificate> certificate = giftCertificateDao.find(certificateId);
        Optional<Tag> tag = tagDao.find(tagId);
        if (!certificate.isPresent()) {
            throw new GiftCertificateNotFoundException(MessageFormat.format("Gift certificate with id: {0} was not found", certificateId));
        } else if (!tag.isPresent()) {
            throw new TagNotFoundException(MessageFormat.format("Tag with id: {0} was not found", tagId));
        } else {
            tagDao.addNewTagAndToCertificate(tagId, certificateId);
        }
    }

    @Override
    public void removeTagFromCertificate(Long certificateId, Long tagId) {
            giftCertificateDao.removeTagFromCertificate(certificateId, tagId);
    }

    @Override
    public void sortCertificateByParameters(String... parameters) {

    }
}
