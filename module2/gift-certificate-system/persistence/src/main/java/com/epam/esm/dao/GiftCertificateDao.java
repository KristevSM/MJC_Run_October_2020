package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao extends CrudDAO<GiftCertificate> {

    List<GiftCertificate> getCertificates(CertificateSearchQuery query, int from, int pageSize);
}
