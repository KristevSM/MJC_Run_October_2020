package com.epam.esm.validator;

import com.epam.esm.dao.CertificateSearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Sergei Kristev
 */
@Component
public class CertificateSearchValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CertificateSearchQuery.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CertificateSearchQuery query = (CertificateSearchQuery) target;

        if (StringUtils.hasLength(query.getPartOfName()) && query.getPartOfName().length() > 40) {
            errors.rejectValue("partOfName", "partOfName.maxSize");
        }
        if (StringUtils.hasLength(query.getPartOfDescription()) && query.getPartOfDescription().length() > 250) {
            errors.rejectValue("partOfDescription", "partOfDescription.maxSize");
        }
        if (StringUtils.hasLength(query.getTagName()) && query.getTagName().length() > 40) {
            errors.rejectValue("tagName", "tagName.maxSize");
        }
        if (StringUtils.hasLength(query.getSortParameter()) && (!"name".equals(query.getSortParameter())
                && (!"create_date".equals(query.getSortParameter())))) {
            errors.rejectValue("sortParameter", "sortParameter.invalidValue");
        }
        if (StringUtils.hasLength(query.getSortOrder()) && (!"ASC".equals(query.getSortOrder())
                && (!"DESC".equals(query.getSortOrder())))) {
            errors.rejectValue("sortOrder", "sortOrder.invalidValue");
        }

    }
}
