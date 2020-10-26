package com.epam.esm.service;

import com.epam.esm.dao.CertificateSearchQuery;
import com.epam.esm.model.GiftCertificate;
import java.util.List;

public interface GiftCertificateService {

    List<GiftCertificate> getCertificates(CertificateSearchQuery query);
    GiftCertificate findCertificateById(Long id);
    Long saveCertificate(GiftCertificate giftCertificate);
    void updateCertificate(GiftCertificate giftCertificate);
    void deleteCertificate(Long id);

    void addTagToCertificate(Long certificateId, Long tagId);
    void removeTagFromCertificate(Long certificateId, Long tagId);
    void patchTags(GiftCertificate oldCertificate, GiftCertificate certificatePatched);
}
