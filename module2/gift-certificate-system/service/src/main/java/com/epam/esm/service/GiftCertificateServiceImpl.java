package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Optional<GiftCertificate> findCertificateById(Long id) {
        return giftCertificateDao.find(id);
    }

    @Override
    public Long saveCertificate(GiftCertificate giftCertificate) {
        //todo if tag == null -> assign default tag
        return giftCertificateDao.save(giftCertificate);
    }

    @Override
    public void updateCertificate(GiftCertificate giftCertificate) {

    }

    @Override
    public void deleteCertificate(Long id) {

    }

    @Override
    public List<GiftCertificate> getCertificatesByTagName(String tagName) {
        return null;
    }

    @Override
    public List<GiftCertificate> getCertificatesByPartOfName(String partOfName) {
        return null;
    }

    @Override
    public List<GiftCertificate> getCertificatesByPartOfDescription(String partOfDescription) {
        return null;
    }

    @Override
    public void addTagToCertificate(Long certificateId, Long tagId) {

    }

    @Override
    public void removeTagFromCertificate(Long certificateId, Long tagId) {

    }

    @Override
    public void sortCertificateByParameters(String... parameters) {

    }
}
