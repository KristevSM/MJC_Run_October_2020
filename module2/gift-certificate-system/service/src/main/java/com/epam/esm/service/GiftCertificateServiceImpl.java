package com.epam.esm.service;

import com.epam.esm.dao.CertificateSearchQuery;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * @author Sergei Kristev
 * <p>
 * Service for managing GiftCertificate objects.
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final OrderDao orderDao;
    private final GiftCertificateValidator certificateValidator;
    private final TagValidator tagValidator;

    /**
     * Constructor accepts GiftCertificateDao and TagDao objects.
     *
     * @param giftCertificateDao   GiftCertificateDao instance.
     * @param tagDao               TagDao instance.
     * @param certificateValidator GiftCertificateValidator instance.
     * @param tagValidator         TagValidator instance.
     */
    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao, OrderDao orderDao,
                                      GiftCertificateValidator certificateValidator, TagValidator tagValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.orderDao = orderDao;
        this.certificateValidator = certificateValidator;
        this.tagValidator = tagValidator;
    }


    /**
     * Gets certificate by id.
     *
     * @param id GiftCertificate id.
     * @return GiftCertificate instance else throws GiftCertificateNotFoundException.
     * @throws GiftCertificateNotFoundException Gift certificate with id: {0} not found.
     */
    @Override
    public GiftCertificate findCertificateById(Long id) {
        return giftCertificateDao.find(id).orElseThrow(() -> new GiftCertificateNotFoundException(MessageFormat
                .format("Gift certificate with id: {0} not found", id)));
    }

    /**
     * Saves new certificate.
     *
     * @param giftCertificate GiftCertificate instance.
     * @return GiftCertificate's id.
     */
    @Transactional
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
        if (giftCertificateDao.getCertificateByName(giftCertificate.getName()).isPresent()) {
            throw new IllegalArgumentException(MessageFormat.format("Certificate with name: {0} already exists", giftCertificate.getName()));
        }
        List<Tag> oldTags = giftCertificate.getTags();
        List<Tag> newTags = new ArrayList<>();
        for (Tag tag : oldTags) {
            BindingResult resultTagValidation = new BeanPropertyBindingResult(tag, "tag");
            tagValidator.validate(tag, result);
            Optional<Tag> currentTag = tagDao.findByTagName(tag.getName());
            if (currentTag.isPresent()) {
                tag.setId(currentTag.get().getId());
            } else checkTag(result, tag, resultTagValidation);
            newTags.add(tag);
        }
        giftCertificate.setTags(new ArrayList<>());
        Long certificateId = giftCertificateDao.save(giftCertificate);
        giftCertificate.setTags(newTags);
        giftCertificateDao.update(giftCertificate);

        return certificateId;
    }

    /**
     * Updates certificate.
     * <p>
     * First, gets list of certificate's tags. After that the certificate object is being validated.
     * If there are invalid fields, it is returned <i>InvalidInputDataException</i> After that updates
     * certificate through <i>giftCertificateDao</i>
     *
     * @param giftCertificate GiftCertificate instance.
     * @throws InvalidInputDataException Unexpected certificate''s field: {0}, error code: {1}
     */
    @Transactional
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
                Optional<Tag> currentTag = tagDao.findByTagName(tag.getName());
                if (currentTag.isPresent()) {
                    tag.setId(currentTag.get().getId());
                } else {
                    checkTag(result, tag, resultTagValidation);
                }
                newTags.add(tag);
            }
            giftCertificate.setTags(newTags);
            giftCertificate.setLastUpdateDate(ZonedDateTime.now());
            giftCertificateDao.update(giftCertificate);
        }
    }

    private void checkTag(BindingResult result, Tag tag, BindingResult resultTagValidation) {
        if (resultTagValidation.hasErrors()) {
            String brokenField = result.getFieldErrors().get(0).getField();
            String errorCode = result.getFieldErrors().get(0).getCode();
            throw new InvalidInputDataException(MessageFormat.format("Unexpected tag''s field: {0}, error code: {1}",
                    brokenField, errorCode));
        } else {
            Long id = tagDao.save(tag);
            tag.setId(id);
        }
    }

    /**
     * Deletes certificate.
     * <p>
     * First, finds a certificate by ID. Subsequently, if the certificate record is exists, method updates
     * all order's tags: set field certificate_id to null. After that deletes certificate
     * through <i>giftCertificateDao</i>
     *
     * @param id GiftCertificate id.
     * @throws GiftCertificateNotFoundException Gift certificate with id: {0} not found.
     */
    @Transactional
    @Override
    public void deleteCertificate(Long id) {
        Optional<GiftCertificate> certificate = giftCertificateDao.find(id);
        if (certificate.isPresent()) {
            List<Order> orders = certificate.get().getOrders();
            for (Order order : orders) {
                order.setGiftCertificate(null);
                orderDao.update(order);
            }
            giftCertificateDao.delete(id);
        } else {
            throw new GiftCertificateNotFoundException(MessageFormat.format("Gift certificate with id: {0} not found", id));
        }
    }

    /**
     * Searches gift certificates.
     * <p>
     * First, finds certificates throw using CertificateSearchQuery. If list of certificates is empty,
     * returns empty list, else certificate's list added with tags and returned.
     *
     * @param query CertificateSearchQuery
     * @return GiftCertificates list.
     */
    @Transactional
    @Override
    public List<GiftCertificate> getCertificates(CertificateSearchQuery query, Long page, Long pageSize) {
        List<GiftCertificate> certificateList = new ArrayList<>();
        try {
            certificateList = giftCertificateDao.getCertificates(query, page, pageSize);
        } catch (GiftCertificateNotFoundException e) {
            // If certificate not found - return empty list
            return certificateList;
        }
        return certificateList;
    }

    /**
     * Searches gift certificates by several tags.
     * <p>
     * Searches gift certificates by several tags.
     *
     * @param tagNames List of tag's names
     * @param page     Index of first page instance.
     * @param pageSize Count pages in response.
     * @return GiftCertificates list.
     */
    @Override
    public List<GiftCertificate> findCertificatesByTags(List<String> tagNames, Long page, Long pageSize) {
        return giftCertificateDao.findCertificatesByTags(tagNames, page, pageSize);
    }

    /**
     * Updates certificate's single field.
     * <p>
     * First, finds a certificate by ID. Subsequently, if the certificate record is exists, method check
     * in switch case fieldName and set fieldValue.
     *
     * @param id GiftCertificate id.
     * @param fieldName Field's name.
     * @param fieldValue Field's value.
     * @throws InvalidInputDataException Unknown field: {0}
     * @throws NumberFormatException Unsupported value type: {0} for field: {1}
     */
    @Transactional
    @Override
    public GiftCertificate updateSingleCertificateField(Long id, String fieldName, String fieldValue) {
        GiftCertificate certificate = giftCertificateDao.find(id).orElseThrow(() -> new GiftCertificateNotFoundException(MessageFormat
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
        giftCertificateDao.update(certificate);
        return certificate;
    }
}
