package com.epam.esm.service;

import com.epam.esm.dao.CertificateSearchQuery;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;

import java.util.List;

public interface GiftCertificateService {

    List<GiftCertificate> getCertificates(CertificateSearchQuery query, int fromCertificate, int pageSize);
    List<GiftCertificate> findCertificatesByTags(List<String> tagNames, int fromCertificate, int pageSize);
    GiftCertificate findCertificateById(Long id);
    Long saveCertificate(GiftCertificate giftCertificate);
    void updateCertificate(GiftCertificate giftCertificate);
    void deleteCertificate(Long id);
}
