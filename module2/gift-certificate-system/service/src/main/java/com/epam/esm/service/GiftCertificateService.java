package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import java.util.List;

public interface GiftCertificateService {

    List<GiftCertificate> findAllCertificates();
    GiftCertificate findCertificateById(Long id);
    Long saveCertificate(GiftCertificate giftCertificate);
    void updateCertificate(GiftCertificate giftCertificate);
    void deleteCertificate(Long id);

    List<GiftCertificate> getCertificatesByTagName(String tagName);
    List<GiftCertificate> getCertificatesByPartOfName(String partOfName);
    List<GiftCertificate> getCertificatesByPartOfDescription(String partOfDescription);
    void addTagToCertificate(Long certificateId, Long tagId);
    void removeTagFromCertificate(Long certificateId, Long tagId);

    List<GiftCertificate> sortCertificateByParameters(String sortParameter, String direction, List<GiftCertificate> certificates);
}
