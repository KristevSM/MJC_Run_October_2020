package com.epam.esm.service;

import com.epam.esm.dao.CertificateSearchQuery;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author Sergei Kristev
 *
 * Service for managing GiftCertificate objects.
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;

    /**
     * Constructor accepts GiftCertificateDao and TagDao objects.
     *
     * @param giftCertificateDao    GiftCertificateDao instance.
     * @param tagDao                TagDao instance.
     */
    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
    }

    /**
     * Gets certificate by id.
     *
     * @param id   GiftCertificate id.
     * @return GiftCertificate instance else throws GiftCertificateNotFoundException.
     * @throws GiftCertificateNotFoundException Gift certificate with id: {0} not found.
     */
    @Override
    public GiftCertificate findCertificateById(Long id) {
        return giftCertificateDao.find(id).orElseThrow(()-> new GiftCertificateNotFoundException(MessageFormat
                .format("Gift certificate with id: {0} not found", id)));
    }

    /**
     * Saves new certificate.
     *
     * @param giftCertificate   GiftCertificate instance.
     * @return GiftCertificate's id.
     */
    @Override
    public Long saveCertificate(GiftCertificate giftCertificate) {
        return giftCertificateDao.save(giftCertificate);
    }

    /**
     * Updates certificate.
     *
     * First, gets list of certificate's tags. Subsequently, assigns new tag to certificate through <i>tagDao</i>.
     * After that updates certificate through <i>giftCertificateDao</i>
     *
     * @param giftCertificate GiftCertificate instance.
     */
    @Override
    public void updateCertificate(GiftCertificate giftCertificate) {
        List<Tag> tags = giftCertificate.getTags();
        tags.forEach(tag -> tagDao.assignNewTagToCertificate(tag.getId(), giftCertificate.getId()));
        giftCertificate.setLastUpdateDate(ZonedDateTime.now());
        giftCertificateDao.update(giftCertificate);
    }

    /**
     * Deletes certificate.
     *
     * First, finds a certificate by ID. Subsequently, if the certificate record is exists, method removes all tags
     * from passed certificate through <i>tagDao</i>, else throw TagNotFoundException. After that deletes certificate
     * through <i>giftCertificateDao</i>
     *
     * @param id GiftCertificate id.
     * @throws TagNotFoundException Gift certificate with id: {0} not found.
     */
    @Override
    public void deleteCertificate(Long id) {
        Optional<GiftCertificate> certificate = giftCertificateDao.find(id);
        if (certificate.isPresent()) {
            GiftCertificate giftCertificate = certificate.get();
            giftCertificate.getTags().forEach(tag -> tagDao.removeTagAndCertificate(tag.getId(), id));
            giftCertificateDao.delete(id);
        } else {
            throw new GiftCertificateNotFoundException(MessageFormat.format("Gift certificate with id: {0} not found", id));
        }
    }

    /**
     * Searches gift certificates.
     *
     * First, finds certificates throw using CertificateSearchQuery. If list of certificates is empty,
     * throws GiftCertificateNotFoundException, else certificate's list added with tags and returned.
     *
     * @param query CertificateSearchQuery
     * @return GiftCertificates list.
     */
    @Override
    public List<GiftCertificate> getCertificates(CertificateSearchQuery query) {
        List<GiftCertificate> certificateList = giftCertificateDao.getCertificates(query);
        if (certificateList.isEmpty()) {
            throw new GiftCertificateNotFoundException("Gift certificate was not found");
        } else if (query.hasTagName()) {
            List<GiftCertificate> listWithTags = new ArrayList<>();
            certificateList.forEach(certificate -> listWithTags.add(findCertificateById(certificate.getId())));
            return listWithTags;
        }
        return certificateList;
    }

    /**
     * Assigns new tag to certificate.
     *
     * First, finds a certificate by id. Subsequently, if the certificate record is exists, method finds tag by id and
     * assigns passed tag to certificate.
     *
     * @param certificateId       GiftCertificate id.
     * @param tagId               Tag id.
     * @throws TagNotFoundException Tag with id: {0} was not found
     * @throws GiftCertificateNotFoundException Gift certificate with id: {0} was not found.
     */
    @Override
    public void addTagToCertificate(Long certificateId, Long tagId) {
        Optional<GiftCertificate> certificate = giftCertificateDao.find(certificateId);
        Optional<Tag> tag = tagDao.find(tagId);
        if (!certificate.isPresent()) {
            throw new GiftCertificateNotFoundException(MessageFormat.format("Gift certificate with id: {0} was not found", certificateId));
        } else if (!tag.isPresent()) {
            throw new TagNotFoundException(MessageFormat.format("Tag with id: {0} was not found", tagId));
        } else {
            tagDao.addNewTagAndToCertificate(tagId, certificateId);
        }
    }

    /**
     * Removes tag.
     *
     * Removes tag from certificate.
     *
     * @param certificateId       GiftCertificate id.
     * @param tagId               Tag id.
     */
    @Override
    public void removeTagFromCertificate(Long certificateId, Long tagId) {
        giftCertificateDao.removeTagFromCertificate(certificateId, tagId);
    }

}
