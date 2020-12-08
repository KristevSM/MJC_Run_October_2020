package com.epam.esm.service;

import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.*;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    @Override
    public Page<GiftCertificateDTO> getCertificates(CertificateSearchQuery query, int page, int pageSize) {
        GiftCertificateSpecification specification = new GiftCertificateSpecification();
        Sort.Direction direction = Sort.Direction.ASC;
        String sortProperty = "id";

        if (query.hasPartOfName()) {
            specification.add(new SearchCriteria("name", query.getPartOfName(), SearchOperation.MATCH));
        }
        if (query.hasPartOfDescription()) {
            specification.add(new SearchCriteria("description", query.getPartOfDescription(), SearchOperation.MATCH));
        }
//        if (query.hasTagName()) {
//            specification.add(new SearchCriteria("tags.name", query.getPartOfName(), SearchOperation.EQUAL));
//        }
        if (query.hasSortParameter()) {
            sortProperty = query.getSortParameter();
        }
        if (query.hasSortOrder()) {
            if (query.getSortOrder().equals("DESC")) {
                direction = Sort.Direction.DESC;
            }
        }
        return giftCertificateRepository.findAll(specification,
                PageRequest.of(page, pageSize, Sort.by(direction, sortProperty)))
                .map(certificateConverter::convertFromEntity);
    }

    @Override
    public Page<GiftCertificateDTO> findCertificatesByTags(List<String> tagNames, int page, int pageSize) {
        return giftCertificateRepository.getGiftCertificatesByTagsNames(tagNames, tagNames.size(), PageRequest.of(page, pageSize))
                .map(certificateConverter::convertFromEntity);
    }

    @Override
    public GiftCertificateDTO findCertificateById(Long id) {
        GiftCertificate certificate = giftCertificateRepository.findById(id).orElseThrow(() -> new GiftCertificateNotFoundException(MessageFormat
                .format("Gift certificate with id: {0} not found", id)));
        return certificateConverter.convertFromEntity(certificate);
    }

    @Override
    public GiftCertificateDTO saveCertificate(GiftCertificateDTO giftCertificateDTO) {
        giftCertificateDTO.setCreateDate(ZonedDateTime.now());
        giftCertificateDTO.setLastUpdateDate(ZonedDateTime.now());
        if (giftCertificateDTO.getTags() == null) {
            giftCertificateDTO.setTags(new ArrayList<>());
        }
        GiftCertificate certificate = certificateConverter.convertFromDTO(giftCertificateDTO);
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
        newCertificate = giftCertificateRepository.save(certificate);

        return certificateConverter.convertFromEntity(newCertificate);
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
    public GiftCertificateDTO updateCertificate(GiftCertificateDTO giftCertificateDTO) {
        giftCertificateDTO.setLastUpdateDate(ZonedDateTime.now());
        if (giftCertificateDTO.getTags() == null) {
            giftCertificateDTO.setTags(new ArrayList<>());
        }
        GiftCertificate giftCertificate = certificateConverter.convertFromDTO(giftCertificateDTO);
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
            GiftCertificate updatedCertificate = giftCertificateRepository.save(giftCertificate);
            return certificateConverter.convertFromEntity(updatedCertificate);
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
        GiftCertificate certificate = giftCertificateRepository.findById(id).orElseThrow(() -> new GiftCertificateNotFoundException(MessageFormat
                .format("Gift certificate with id: {0} not found", id)));
        try {
            switch (fieldName) {
                case "name":
                    certificate.setName(fieldValue);
                    break;
                case "description":
                    certificate.setDescription(fieldValue);
                    break;
                case "price":
                    certificate.setPrice(BigDecimal.valueOf(Double.parseDouble(fieldValue)));
                    break;
                case "duration":
                    certificate.setDuration(Integer.parseInt(fieldValue));
                    break;
                default:
                    throw new InvalidInputDataException(MessageFormat.format("Unknown field: {0}", fieldName));
            }
        } catch (NumberFormatException e) {
            throw new InvalidInputDataException(MessageFormat
                    .format("Unsupported value type: {0} for field: {1}", fieldValue, fieldName));
        }
        BindingResult result = new BeanPropertyBindingResult(certificate, "giftCertificate");
        certificateValidator.validate(certificate, result);
        if (result.hasErrors()) {
            String brokenField = result.getFieldErrors().get(0).getField();
            String errorCode = result.getFieldErrors().get(0).getCode();
            throw new InvalidInputDataException(MessageFormat.format("Unexpected certificate''s field: {0}, error code: {1}",
                    brokenField, errorCode));
        }
        certificate.setLastUpdateDate(ZonedDateTime.now());
        giftCertificateRepository.save(certificate);
        return certificateConverter.convertFromEntity(certificate);
    }
}
