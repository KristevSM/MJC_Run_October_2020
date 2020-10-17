package com.epam.esm.validator;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TagValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Tag.class.equals(clazz);

    }

    @Override
    public void validate(Object target, Errors errors) {

        Tag tag = (Tag) target;
        if(StringUtils.hasLength(tag.getName())
                && tag.getName().length() > 40) {
            errors.rejectValue("name", "tag.maxSize");
        }
    }
}
