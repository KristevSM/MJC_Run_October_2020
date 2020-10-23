package com.epam.esm.service;

import com.epam.esm.dao.CertificateSearchQuery;
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
import java.util.*;

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
            giftCertificateDao.delete(id);
        } else {
            throw new GiftCertificateNotFoundException(MessageFormat.format("Gift certificate with id: {0} not found", id));
        }
    }

    @Override
    public List<GiftCertificate> getCertificates(CertificateSearchQuery query) {
        List<GiftCertificate> certificateList = giftCertificateDao.getCertificates(query);
        if (certificateList.isEmpty()) {
            throw new GiftCertificateNotFoundException("Gift certificate was not found");
        } else if (query.hasTagName()) {
            List<GiftCertificate> listWithTags = new ArrayList<>();
            certificateList.forEach(certificate -> listWithTags.add(findCertificateById(certificate.getId())));
            return listWithTags;
        }
        return certificateList;
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

}
