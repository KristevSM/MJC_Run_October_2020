package com.epam.esm.service;

import com.epam.esm.dao.CertificateSearchQuery;
import com.epam.esm.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {

    List<GiftCertificate> getCertificates(CertificateSearchQuery query, Long page, Long pageSize);
    List<GiftCertificate> findCertificatesByTags(List<String> tagNames, Long page, Long pageSize);
    GiftCertificate findCertificateById(Long id);
    Long saveCertificate(GiftCertificate giftCertificate);
    void updateCertificate(GiftCertificate giftCertificate);
    void deleteCertificate(Long id);
    GiftCertificate updateSingleCertificateField(Long id, String fieldName,String fieldValue);
}
