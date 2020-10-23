package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao extends CrudDAO<GiftCertificate> {

    List<GiftCertificate> getCertificates(CertificateSearchQuery query);
    void addTagToCertificate(Long certificateId, Long tagId);
    void removeTagFromCertificate(Long certificateId, Long tagId);
}
