package com.epam.esm.converter;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.model.GiftCertificate;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class GiftCertificateConverter {

    private final TagConverter tagConverter;


    public GiftCertificateDTO convertCertificateDTOFromCertificate(GiftCertificate certificate) {
        GiftCertificateDTO giftCertificateDTO = GiftCertificateDTO.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .description(certificate.getDescription())
                .createDate(certificate.getCreateDate())
                .lastUpdateDate(certificate.getLastUpdateDate())
                .duration(certificate.getDuration())
                .price(certificate.getPrice())
                .build();
        if (certificate.getTags() != null) {
            giftCertificateDTO.setTags(tagConverter.convertTagDTOsFromTags(certificate.getTags()));
        }
        return giftCertificateDTO;
    }

    public GiftCertificate convertCertificateFromCertificateDTO(GiftCertificateDTO certificateDTO) {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .id(certificateDTO.getId())
                .name(certificateDTO.getName())
                .description(certificateDTO.getDescription())
                .createDate(certificateDTO.getCreateDate())
                .lastUpdateDate(certificateDTO.getLastUpdateDate())
                .duration(certificateDTO.getDuration())
                .price(certificateDTO.getPrice())
                .build();
        if (certificateDTO.getTags() != null) {
            giftCertificate.setTags(tagConverter.convertTagsFromTagDTOs(certificateDTO.getTags()));
        }
        return giftCertificate;
    }
}
