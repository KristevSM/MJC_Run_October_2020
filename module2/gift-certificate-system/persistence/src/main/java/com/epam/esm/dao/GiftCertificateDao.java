package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao extends CrudDAO<GiftCertificate> {

    List<GiftCertificate> getCertificatesByTagName(String tagName);
    List<GiftCertificate> getCertificatesByPartOfName(String partOfName);
    List<GiftCertificate> getCertificatesByPartOfDescription(String partOfDescription);
}
