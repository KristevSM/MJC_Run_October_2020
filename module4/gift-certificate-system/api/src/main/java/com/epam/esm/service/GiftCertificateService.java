package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.repository.CertificateSearchQuery;
import com.epam.esm.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {

    List<GiftCertificateDTO> getCertificates(CertificateSearchQuery query, Long page, Long pageSize);
    List<GiftCertificateDTO> findCertificatesByTags(List<String> tagNames, Long page, Long pageSize);
    GiftCertificateDTO findCertificateById(Long id);
    Long saveCertificate(GiftCertificateDTO giftCertificateDTO);
    void updateCertificate(GiftCertificateDTO giftCertificateDTO);
    void deleteCertificate(Long id);
    GiftCertificateDTO updateSingleCertificateField(Long id, String fieldName,String fieldValue);
}
