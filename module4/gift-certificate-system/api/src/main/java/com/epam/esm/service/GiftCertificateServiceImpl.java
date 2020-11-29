package com.epam.esm.service;

import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.CertificateSearchQuery;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final OrderRepository orderRepository;
    private final GiftCertificateValidator certificateValidator;
    private final GiftCertificateConverter certificateConverter;
    private final TagValidator tagValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, TagRepository tagRepository, OrderRepository orderRepository, GiftCertificateValidator certificateValidator, GiftCertificateConverter certificateConverter, TagValidator tagValidator) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
        this.orderRepository = orderRepository;
        this.certificateValidator = certificateValidator;
        this.certificateConverter = certificateConverter;
        this.tagValidator = tagValidator;
    }

    //todo create method
    @Override
    public List<GiftCertificateDTO> getCertificates(CertificateSearchQuery query, Long page, Long pageSize) {
        List<GiftCertificateDTO> certificateDTOS = new ArrayList<>();
//        try {
//            certificateList = giftCertificateRepository.getCertificates(query, page, pageSize);
//        } catch (GiftCertificateNotFoundException e) {
//            // If certificate not found - return empty list
//            return certificateList;
//        }
        return certificateDTOS;
    }

    @Override
    public List<GiftCertificateDTO> findCertificatesByTags(List<String> tagNames, Long page, Long pageSize) {
        List<GiftCertificate> certificates = giftCertificateRepository.getGiftCertificatesByTagsNames(tagNames, tagNames.size());
        return certificates.stream()
                .map(certificateConverter::convertCertificateDTOFromCertificate)
                .collect(Collectors.toList());
    }

    @Override
    public GiftCertificateDTO findCertificateById(Long id) {
        GiftCertificate certificate = giftCertificateRepository.findById(id).orElseThrow(() -> new GiftCertificateNotFoundException(MessageFormat
                .format("Gift certificate with id: {0} not found", id)));
        return certificateConverter.convertCertificateDTOFromCertificate(certificate);
    }

    @Override
    public Long saveCertificate(GiftCertificateDTO giftCertificateDTO) {
        giftCertificateDTO.setCreateDate(ZonedDateTime.now());
        giftCertificateDTO.setLastUpdateDate(ZonedDateTime.now());
        if (giftCertificateDTO.getTags() == null) {
            giftCertificateDTO.setTags(new ArrayList<>());
        }
        GiftCertificate certificate = certificateConverter.convertCertificateFromCertificateDTO(giftCertificateDTO);
        BindingResult result = new BeanPropertyBindingResult(certificate, "giftCertificate");
        certificateValidator.validate(certificate, result);
        if (result.hasErrors()) {
            String brokenField = result.getFieldErrors().get(0).getField();
            String errorCode = result.getFieldErrors().get(0).getCode();
            throw new InvalidInputDataException(MessageFormat.format("Unexpected certificate''s field: {0}, error code: {1}",
                    brokenField, errorCode));
        }
        if (giftCertificateRepository.getCertificateByName(certificate.getName()).isPresent()) {
            throw new IllegalArgumentException(MessageFormat.format("Certificate with name: {0} already exists", certificate.getName()));
        }
        List<Tag> oldTags = certificate.getTags();
        List<Tag> newTags = new ArrayList<>();
        for (Tag tag : oldTags) {
            BindingResult resultTagValidation = new BeanPropertyBindingResult(tag, "tag");
            tagValidator.validate(tag, result);
            Optional<Tag> currentTag = tagRepository.findByName(tag.getName());
            if (currentTag.isPresent()) {
                tag.setId(currentTag.get().getId());
            } else checkTag(result, tag, resultTagValidation);
            newTags.add(tag);
        }
        certificate.setTags(new ArrayList<>());
        GiftCertificate newCertificate = giftCertificateRepository.save(certificate);
        certificate.setTags(newTags);
        giftCertificateRepository.save(certificate);

        return newCertificate.getId();
    }

    private void checkTag(BindingResult result, Tag tag, BindingResult resultTagValidation) {
        if (resultTagValidation.hasErrors()) {
            String brokenField = result.getFieldErrors().get(0).getField();
            String errorCode = result.getFieldErrors().get(0).getCode();
            throw new InvalidInputDataException(MessageFormat.format("Unexpected tag''s field: {0}, error code: {1}",
                    brokenField, errorCode));
        } else {
            Tag newTag = tagRepository.save(tag);
            tag.setId(newTag.getId());
        }
    }

    @Override
    public void updateCertificate(GiftCertificateDTO giftCertificateDTO) {
        giftCertificateDTO.setLastUpdateDate(ZonedDateTime.now());
        if (giftCertificateDTO.getTags() == null) {
            giftCertificateDTO.setTags(new ArrayList<>());
        }
        GiftCertificate giftCertificate = certificateConverter.convertCertificateFromCertificateDTO(giftCertificateDTO);
        BindingResult result = new BeanPropertyBindingResult(giftCertificate, "giftCertificate");
        certificateValidator.validate(giftCertificate, result);
        if (result.hasErrors()) {
            String brokenField = result.getFieldErrors().get(0).getField();
            String errorCode = result.getFieldErrors().get(0).getCode();
            throw new InvalidInputDataException(MessageFormat.format("Unexpected certificate''s field: {0}, error code: {1}",
                    brokenField, errorCode));
        } else {
            List<Tag> oldTags = giftCertificate.getTags();
            List<Tag> newTags = new ArrayList<>();
            for (Tag tag : oldTags) {
                BindingResult resultTagValidation = new BeanPropertyBindingResult(tag, "tag");
                tagValidator.validate(tag, result);
                Optional<Tag> currentTag = tagRepository.findByName(tag.getName());
                if (currentTag.isPresent()) {
                    tag.setId(currentTag.get().getId());
                } else {
                    checkTag(result, tag, resultTagValidation);
                }
                newTags.add(tag);
            }
            giftCertificate.setTags(newTags);
            giftCertificate.setLastUpdateDate(ZonedDateTime.now());
            giftCertificateRepository.save(giftCertificate);
        }
    }

    @Override
    public void deleteCertificate(Long id) {
        Optional<GiftCertificate> certificate = giftCertificateRepository.findById(id);
        if (certificate.isPresent()) {
            List<Order> orders = certificate.get().getOrders();
            for (Order order : orders) {
                order.setGiftCertificate(null);
                orderRepository.save(order);
            }
            giftCertificateRepository.delete(certificate.get());
        } else {
            throw new GiftCertificateNotFoundException(MessageFormat.format("Gift certificate with id: {0} not found", id));
        }
    }

    @Override
    public GiftCertificateDTO updateSingleCertificateField(Long id, String fieldName, String fieldValue) {
        return null;
    }
}
