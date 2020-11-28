package com.epam.esm.service;

import com.epam.esm.dao.CertificateSearchQuery;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.TagValidator;
import org.hibernate.internal.build.AllowPrintStacktrace;
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

@Primary
@Service
public class GiftCertificateSpringData implements GiftCertificateService{

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final OrderRepository orderRepository;
    private final GiftCertificateValidator certificateValidator;
    private final TagValidator tagValidator;

    @Autowired
    public GiftCertificateSpringData(GiftCertificateRepository giftCertificateRepository, TagRepository tagRepository, OrderRepository orderRepository, GiftCertificateValidator certificateValidator, TagValidator tagValidator) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
        this.orderRepository = orderRepository;
        this.certificateValidator = certificateValidator;
        this.tagValidator = tagValidator;
    }


    //todo create method
    @Override
    public List<GiftCertificate> getCertificates(CertificateSearchQuery query, Long page, Long pageSize) {
        List<GiftCertificate> certificateList = new ArrayList<>();
//        try {
//            certificateList = giftCertificateRepository.getCertificates(query, page, pageSize);
//        } catch (GiftCertificateNotFoundException e) {
//            // If certificate not found - return empty list
//            return certificateList;
//        }
        return certificateList;
    }

    @Override
    public List<GiftCertificate> findCertificatesByTags(List<String> tagNames, Long page, Long pageSize) {
        return giftCertificateRepository.getGiftCertificatesByTagsNames(tagNames, tagNames.size());
    }

    @Override
    public GiftCertificate findCertificateById(Long id) {
        return giftCertificateRepository.findById(id).orElseThrow(() -> new GiftCertificateNotFoundException(MessageFormat
                .format("Gift certificate with id: {0} not found", id)));    }

    @Override
    public Long saveCertificate(GiftCertificate giftCertificate) {
        giftCertificate.setCreateDate(ZonedDateTime.now());
        giftCertificate.setLastUpdateDate(ZonedDateTime.now());
        if (giftCertificate.getTags() == null) {
            giftCertificate.setTags(new ArrayList<>());
        }
        BindingResult result = new BeanPropertyBindingResult(giftCertificate, "giftCertificate");
        certificateValidator.validate(giftCertificate, result);
        if (result.hasErrors()) {
            String brokenField = result.getFieldErrors().get(0).getField();
            String errorCode = result.getFieldErrors().get(0).getCode();
            throw new InvalidInputDataException(MessageFormat.format("Unexpected certificate''s field: {0}, error code: {1}",
                    brokenField, errorCode));
        }
        if (giftCertificateRepository.getCertificateByName(giftCertificate.getName()).isPresent()) {
            throw new IllegalArgumentException(MessageFormat.format("Certificate with name: {0} already exists", giftCertificate.getName()));
        }
        List<Tag> oldTags = giftCertificate.getTags();
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
        giftCertificate.setTags(new ArrayList<>());
        GiftCertificate newCertificate = giftCertificateRepository.save(giftCertificate);
        giftCertificate.setTags(newTags);
        giftCertificateRepository.save(giftCertificate);

        return newCertificate.getId();    }

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
    public void updateCertificate(GiftCertificate giftCertificate) {
        giftCertificate.setLastUpdateDate(ZonedDateTime.now());
        if (giftCertificate.getTags() == null) {
            giftCertificate.setTags(new ArrayList<>());
        }
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
    public GiftCertificate updateSingleCertificateField(Long id, String fieldName, String fieldValue) {
        return null;
    }
}
