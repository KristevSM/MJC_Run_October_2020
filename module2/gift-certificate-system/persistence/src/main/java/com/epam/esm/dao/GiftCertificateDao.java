package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao extends CrudDAO<GiftCertificate> {

    List<GiftCertificate> getCertificates(CertificateSearchQuery query, Long page, Long pageSize);
    List<GiftCertificate> findCertificatesByTags(List<String> tagNames, Long page, Long pageSize);
    Optional<GiftCertificate> getCertificateByName(String name);
}
