package com.epam.esm.validator;

import com.epam.esm.model.GiftCertificate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class GiftCertificateValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return GiftCertificate.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        GiftCertificate giftCertificate = (GiftCertificate) target;
        if (giftCertificate.getName() == null || giftCertificate.getName().length() > 40
                || giftCertificate.getName().length() < 1 ) {
            errors.rejectValue("name", "giftCertificateName.size");
        }
        if (giftCertificate.getDescription() == null || giftCertificate.getDescription().length() > 250
                || giftCertificate.getDescription().length() < 1 ) {
            errors.rejectValue("description", "giftCertificateDescription.size");
        }
        if (giftCertificate.getPrice() == null
                || giftCertificate.getPrice().doubleValue() <= 0) {
            errors.rejectValue("price", "giftCertificatePrice.negative");
        }
        if (giftCertificate.getCreateDate() == null || giftCertificate.getLastUpdateDate() == null
                || giftCertificate.getLastUpdateDate().isBefore(giftCertificate.getCreateDate())) {
            errors.rejectValue("lastUpdateDate", "giftCertificateLastUpdateDate.beforeCreating");
        }
        if (giftCertificate.getDuration() <= 0) {
            errors.rejectValue("duration", "giftCertificateDuration.negative");
        }
//        if (giftCertificate.getTags() == null) {
//            errors.rejectValue("tags", "giftCertificateTags.isNotSet");
//        }
    }
}
