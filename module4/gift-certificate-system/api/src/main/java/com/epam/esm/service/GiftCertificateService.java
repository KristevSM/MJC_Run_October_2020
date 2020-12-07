package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.repository.CertificateSearchQuery;
import com.epam.esm.model.GiftCertificate;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GiftCertificateService {

    Page<GiftCertificateDTO> getCertificates(CertificateSearchQuery query, int page, int pageSize);
    Page<GiftCertificateDTO> findCertificatesByTags(List<String> tagNames, int page, int pageSize);
    GiftCertificateDTO findCertificateById(Long id);
    Long saveCertificate(GiftCertificateDTO giftCertificateDTO);
    void updateCertificate(GiftCertificateDTO giftCertificateDTO);
    void deleteCertificate(Long id);
    GiftCertificateDTO updateSingleCertificateField(Long id, String fieldName,String fieldValue);
}
